package com.team127.atom.service;

import com.team127.atom.model.Comment;
import com.team127.atom.model.Issue;
import com.team127.atom.model.Reply;

import java.util.List;

public interface ForumService {

    List<Issue> getIssues(String id);

    List<Issue> getPopularIssue();

    List<Issue> getRecentIssue();

    List<Issue> getIssueByContent(String content);

    Issue getIssueById(String id);

    boolean getUserIssueLike(String user, String issue);

    boolean getUserIssueStar(String user, String issue);

    boolean addIssue(Issue issue);

    boolean removeIssue(String id);

    boolean updateIssueInfo(Issue issue);

    boolean addReply(Reply reply);

    boolean removeReply(String string);

    boolean updateReply(Reply reply);

    List<Reply> getUserReply(String userId);

    List<Reply> getIssueReply(String issueId);

    Reply getReply(String issueId, Long createdAt);

    List<Reply> getPopularIssueReply(String issueId);

    Reply getReplyById(String replyId);

    boolean getUserReplyLike(String user, String reply);

    boolean getUserReplyStar(String user, String reply);

    boolean starIssue(String userId, String issueId);

    boolean unStarIssue(String userId, String issueId);

    boolean likeIssue(String userId, String issueId);

    boolean dislikeIssue(String userId, String issueId);

    List<Issue> getIssueDraft(String userId);

    int modifyReply(String replyId, String content);

    boolean starReply(String userId, String replyId);

    boolean unStarReply(String userId, String replyId);

    boolean addComment(Comment comment);

    List<Comment> getReplyComment(String replyId);


    int removeComment(String commentId);

    int modifyComment(String commentId, String content);

    List<Comment> getUserComment(String userId);

    List<Comment> getPopularReplyComment(String replyId);

    Comment getCommentById(String commentId);

    int updateComment(Comment comment);

    boolean starComment(String userId, String commentId);

    boolean unStarComment(String userId, String commentId);

    boolean getUserCommentStar(String userId, String commentId);

    Issue getOneIssueDraft(String id);

    int updateDraft(String issue, String title, String content);

    int postDraft(String issue);
}
