package com.team127.atom.service.impl;

import com.team127.atom.mapper.IssueMapper;
import com.team127.atom.mapper.ReplyMapper;
import com.team127.atom.mapper.CommentMapper;
import com.team127.atom.model.Comment;
import com.team127.atom.model.Issue;
import com.team127.atom.model.Reply;
import com.team127.atom.service.ForumService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ForumServiceImpl implements ForumService {
    @Autowired
    IssueMapper issueMapper;

    @Autowired
    ReplyMapper replyMapper;

    @Autowired
    CommentMapper commentMapper;
    @Resource
    RedisTemplate<String, Object> redisTemplate;


    @Override
    public List<Issue> getIssues(String id) {
        return issueMapper.selectByUserId(id);

    }

    @Override
    public List<Issue> getPopularIssue() {
        return issueMapper.selectByStars();
    }

    @Override
    public List<Issue> getRecentIssue() {
        return issueMapper.selectByTime();
    }

    @Override
    public List<Issue> getIssueByContent(String content) {
        return issueMapper.selectByContent(content);
    }

    @Override
    public Issue getIssueById(String id) {
        Issue res;
        res = issueMapper.selectByPrimaryKey(id);
//        String key = "Issue" + id;
//        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
//            res = issueMapper.selectByPrimaryKey(id);
//            redisTemplate.opsForValue().set(key, res, 1, TimeUnit.HOURS);
//        } else {
//            res = (Issue) redisTemplate.opsForValue().get(key);
//        }
        return res;
    }

    @Override
    public boolean getUserIssueLike(String user, String issue){
        String key = "User" + user + "likeIssue" + issue;
        boolean res;
        res = (issueMapper.selectUserLikeIssue(user, issue) != 0);
//        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))){
//            res = (issueMapper.selectUserLikeIssue(user, issue) != 0);
//            redisTemplate.opsForValue().set(key, res, 1, TimeUnit.HOURS);
//        }else {
//            res = (boolean)redisTemplate.opsForValue().get(key);
//        }
        return res;
    }

    @Override
    public boolean getUserIssueStar(String user, String issue) {
        String key = "User" + user + "starIssue" + issue;
        boolean res;
        res = (issueMapper.selectUserStarIssue(user, issue) != 0);
//        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))){
//            res = (issueMapper.selectUserLikeIssue(user, issue) != 0);
//            redisTemplate.opsForValue().set(key, res, 1, TimeUnit.HOURS);
//        }else {
//            res = (boolean)redisTemplate.opsForValue().get(key);
//        }
        return res;
    }

    @Override
    public boolean addIssue(Issue issue) {
        return issueMapper.insert(issue) == 1;
    }

    @Override
    public boolean removeIssue(String id) {
        int signal = issueMapper.deleteByPrimaryKey(id);
        redisTemplate.delete("Issue"+id);
        return signal == 1;
    }

    @Override
    public boolean updateIssueInfo(Issue issue) {
        int signal = issueMapper.updateByPrimaryKeySelective(issue);
//        redisTemplate.delete("Issue"+issue.getId());
        return signal == 1;
    }

    @Override
    public boolean addReply(Reply reply) {
        return replyMapper.insert(reply)==1;
    }

    @Override
    public boolean removeReply(String id) {
        int signal = replyMapper.deleteByPrimaryKey(id);
        redisTemplate.delete("Reply"+id);
        return signal == 1;
    }

    @Override
    public boolean updateReply(Reply reply) {
        int signal = replyMapper.updateByPrimaryKeySelective(reply);
//        redisTemplate.delete("Reply"+reply.getId());
        return signal == 1;
    }

    @Override
    public List<Reply> getUserReply(String userId) {
        return replyMapper.selectReplyByUser(userId);
    }

    @Override
    public List<Reply> getIssueReply(String issueId) {
        return replyMapper.selectReplyByIssue(issueId);
    }

    @Override
    public Reply getReply(String issueId, Long createdAt) {
        return  replyMapper.selectReply(issueId, createdAt);
    }

    @Override
    public List<Reply> getPopularIssueReply(String issueId) {
        return replyMapper.selectPopularReplyByIssue(issueId);
    }

    @Override
    public Reply getReplyById(String replyId) {
        return replyMapper.selectByPrimaryKey(replyId);
    }

    @Override
    public boolean getUserReplyLike(String user, String reply) {
        return false;
    }

    @Override
    public boolean getUserReplyStar(String user, String reply) {
        String key = "User" + user + "starReply" + reply;
        boolean res;
        res = (issueMapper.selectUserStarReply(user, reply) != 0);
//        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))){
//            res = (issueMapper.selectUserLikeIssue(user, issue) != 0);
//            redisTemplate.opsForValue().set(key, res, 1, TimeUnit.HOURS);
//        }else {
//            res = (boolean)redisTemplate.opsForValue().get(key);
//        }
        return res;
    }

    @Override
    public boolean starIssue(String userId, String issueId) {
        String key = "User" + userId + "Issue" + issueId;
//        redisTemplate.opsForValue().set(key, true, 1, TimeUnit.HOURS);
        return issueMapper.starIssue(userId, issueId) == 1;
    }

    @Override
    public boolean unStarIssue(String userId, String issueId) {
        String key = "User" + userId + "Issue" + issueId;
//        redisTemplate.opsForValue().set(key, false, 1, TimeUnit.HOURS);
        return issueMapper.unStarIssue(userId, issueId) == 1;
    }

    @Override
    public boolean likeIssue(String userId, String issueId) {
        return issueMapper.likeIssue(userId, issueId) == 1;
    }

    @Override
    public boolean dislikeIssue(String userId, String issueId) {
        return issueMapper.dislikeIssue(userId, issueId) == 1;
    }

    @Override
    public List<Issue> getIssueDraft(String userId) {
        return issueMapper.selectDraft(userId);
    }

    @Override
    public int modifyReply(String replyId, String content) {
        return replyMapper.modifyReply(replyId, content);
    }

    @Override
    public boolean starReply(String userId, String replyId) {
        return (replyMapper.starReply(userId, replyId) == 1);
    }

    @Override
    public boolean unStarReply(String userId, String replyId) {
        return (replyMapper.unStarReply(userId, replyId) == 1);
    }

    @Override
    public boolean addComment(Comment comment) {
        return commentMapper.insert(comment)==1;
    }

    @Override
    public List<Comment> getReplyComment(String replyId) {
        return commentMapper.selectCommentByReply(replyId);
    }

    @Override
    public int removeComment(String commentId) {
        return commentMapper.deleteByPrimaryKey(commentId);
    }

    @Override
    public int modifyComment(String commentId, String content) {
        return commentMapper.modifyComment(commentId, content);

    }

    @Override
    public List<Comment> getUserComment(String userId) {
        return commentMapper.selectCommentByUser(userId);
    }

    @Override
    public List<Comment> getPopularReplyComment(String replyId) {
        return commentMapper.selectPopularCommentByReply(replyId);
    }

    @Override
    public Comment getCommentById(String commentId) {
        return commentMapper.selectByPrimaryKey(commentId);
    }

    @Override
    public int updateComment(Comment comment) {
        return commentMapper.updateByPrimaryKeySelective(comment);
    }

    @Override
    public boolean starComment(String userId, String commentId) {
        return (commentMapper.starComment(userId,commentId) == 1);
    }

    @Override
    public boolean unStarComment(String userId, String commentId) {
        return (commentMapper.unStarComment(userId, commentId) == 1);
    }

    @Override
    public boolean getUserCommentStar(String userId, String commentId) {
        String key = "User" + userId + "starComment" + commentId;
        boolean res;
        res = (issueMapper.selectUserStarComment(userId, commentId) != 0);

//        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))){
//            res = (issueMapper.selectUserLikeIssue(user, issue) != 0);
//            redisTemplate.opsForValue().set(key, res, 1, TimeUnit.HOURS);
//        }else {
//            res = (boolean)redisTemplate.opsForValue().get(key);
//        }
       return res;
    }

    @Override
    public Issue getOneIssueDraft(String id) {
        return issueMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateDraft(String issue, String title, String content) {
        return issueMapper.updateDraft(issue, title, content);
    }

    @Override
    public int postDraft(String issue) {
        return issueMapper.postDraft(issue);
    }
}
