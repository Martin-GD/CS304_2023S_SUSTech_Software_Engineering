package com.team127.atom.web.controller;

import com.team127.atom.model.*;
import com.team127.atom.service.ForumService;
import com.team127.atom.service.NoteService;
import com.team127.atom.service.UserService;
import com.team127.atom.util.HashGenerator;
import com.team127.atom.util.JWTUtil;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;

import com.team127.atom.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.team127.atom.util.LCS;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@Slf4j
public class IssueController {
    private double weight = 0.8;

    private double weight1 = 0.4;
    @Autowired
    ForumService forumService;

    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

//    LCS lcs;

    @PostMapping("/createIssue")
    public Response createIssue(@RequestParam("user") String userId,
                                @RequestParam("title") String title,
                                @RequestParam("content") String content) {
        //TODO:
        Issue issue = new Issue();
        issue.setId(HashGenerator.hash(userId, title));
        issue.setTitle(title);
        issue.setOpener(userId);
        issue.setContent(content);
        issue.setIsDraft(2);
        issue.setLove(0);
        issue.setStar(0);
        Long time = System.currentTimeMillis();
        issue.setCreatedAt(time);
        boolean isSuccess = forumService.addIssue(issue);
        if (isSuccess){
            return Response.success(issue);
        }
        else {
            return Response.error(10001, "未能成功发布问题");
        }
    }

    @PostMapping("/uploadIssueImage")
    public Response uploadIssueImage(@RequestParam("user") String userId,
                                    @RequestParam("issue") String issueId,
                                    @RequestParam("imageName") String imageName,
                                    @RequestParam("image") MultipartFile image) {
        User user = userService.getUserById(userId);
        Note note = noteService.getNoteById(issueId);
//        if (!JWTUtil.verify(token, user.getId(), user.getPassword()) ||
//                !note.getOwner().equals(user.getId())) {
//            return Response.error(1004, "Token verification failed");
//        }
        NoteImage storeImage = new NoteImage();
        storeImage.setImageId(issueId + imageName);
        try {
            storeImage.setImage(image.getBytes());
            int res = noteService.storeImage(storeImage);
            if (res == 1) {
                return Response.error(400, "The picture name has exist");
            }
        } catch (Exception e) {
            return Response.error(400, "Upload image failed");
        }
        return Response.success(
                "http://10.26.11.88:8080/getNoteImage/" + issueId + "/" + imageName + ".png");
    }

    @GetMapping(value = "/getIssueImage/{issueId}/{imageName}.png")
    @ResponseBody
    public void getIssueImage(@PathVariable("issueId") String issueId,
                             @PathVariable("imageName") String imageName,
                             HttpServletResponse response) {
        NoteImage image = new NoteImage();
        image.setImageId(issueId + imageName);
        byte[] res = noteService.getImage(image.getImageId());
        System.out.println("get issue Image");
        response.setContentType("image/png");
        response.setStatus(200);
        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return Response.success(Base64.getEncoder().encodeToString(res));
    }

    @PostMapping("/modifyIssue")
    @CrossOrigin
    public Response modifyIssue(@RequestParam("issue") String issueId,
            @RequestParam("title") String title,
            @RequestParam("content") String content) {
        Issue issue = forumService.getIssueById(issueId);
        issue.setTitle(title);
        issue.setContent(content);
        forumService.updateIssueInfo(issue);
        return Response.success(null);
    }

    @PostMapping("/deleteIssue")
    @CrossOrigin
    public Response deleteIssue(@RequestParam("issue") String issueId) {
        forumService.removeIssue(issueId);
        return Response.success(null);
    }

    @GetMapping("/getIssueList")
    @CrossOrigin
        public Response getIssueList(@RequestParam("user") String userId) {
        //TODO:
        List<Issue> issues = forumService.getIssues(userId);
        return getIssueDetail(issues, userId);
    }

    @GetMapping("/getPopularIssue")
    public Response getPopularIssue(@RequestParam("user") String userId) {
        List<Issue> list = forumService.getPopularIssue();
        //TODO:

        return getIssueDetail(list, userId);
    }

    @GetMapping("/getRecentIssue")
    @CrossOrigin
    public Response getRecentIssue(@RequestParam("user") String userId) {
        //TODO:
        List<Issue> list = forumService.getRecentIssue();
        //TODO:
        //推荐搜索
        return getIssueDetail(list, userId);
    }

    @GetMapping("/getChangeRecentIssue")
    @CrossOrigin
    public Response getChangeRecentIssue(@RequestParam("Issue") String issueId,
                                         @RequestParam("createdAt") Long createdAt,
                                         @RequestParam("user") String user) {
        //TODO:
        Issue issue = forumService.getIssueById(issueId);
        issue.replyNum = forumService.getIssueReply(issue.getId()).size();
        issue.setIsLike(forumService.getUserIssueLike(user,issue.getId()));
        issue.setIsStar(forumService.getUserIssueStar(user,issue.getId()));
        issue.setFollow(userService.isLike(user, issue.getOpener()));
        if (forumService.getReply(issueId, createdAt) == null){
            issue.setReply(null);
            return Response.success(issue);
        }
        else {
            System.out.println(forumService.getReply(issueId, createdAt));
            issue.setReply(forumService.getReply(issueId, createdAt));
            System.out.println(issue.getReply());
            issue.getReply().setCommentList(forumService.getReplyComment(issue.getReply().getId()));
            System.out.println(issue.getReply());
            getReplyDetail(issue, user);
        }
        System.out.println(issue);
        return Response.success(issue);
    }

    @GetMapping("/getIssueByContent")
    @CrossOrigin
    public Response getIssueByContent(@RequestParam("content") String content,
                                      @RequestParam("user") String userId) {
        //TODO:
        List<Issue> list = forumService.getIssueByContent(content);
        LCS.Lcs(content, list);
        getIssueDetail(list, userId);

        System.out.println(list);
        return getPoints(list, userId);
    }

    //TODO:
    private Response getPoints(List<Issue> list, String userId){
        for(Issue issue:list){
            if(issue.isFollow()){
                issue.points += weight * issue.replyNum;
            }
            else {
                issue.points += weight1 * issue.replyNum;
            }
        }
        Collections.sort(list,new Comparator<Issue>() {
            @Override
            public int compare(Issue o1, Issue o2) {
                if(o1.points < o2.points){
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });
        System.out.println(list);
        return Response.success(list);
    }

    private void getIssueDetail(Issue issue, String userId) {
        getDetail(issue, userId);
    }

    private void getDetail(Issue issue, String userId) {
        issue.setIsLike(forumService.getUserIssueLike(userId,issue.getId()));
        issue.setIsStar(forumService.getUserIssueStar(userId,issue.getId()));
        issue.setFollow(userService.isLike(userId, issue.getOpener()));
        if(!forumService.getIssueReply(issue.getId()).isEmpty()) {
            issue.setReply(forumService.getIssueReply(issue.getId()).get(0));
            issue.replyNum = forumService.getIssueReply(issue.getId()).size();
            getReplyDetail(issue, userId);
        }
    }

    private void getReplyDetail(Issue issue, String userId) {
        issue.getReply().setIsStar(forumService.getUserReplyStar(userId, issue.getReply().getId()));
        issue.getReply().setCommentList(forumService.getReplyComment(issue.getReply().getId()));
        issue.getReply().setFollow(userService.isLike(userId, issue.getReply().getAuthor()));
        issue.getReply().setName(userService.getUserById(issue.getReply().getAuthor()).getName());
        for (Comment comment: issue.getReply().getCommentList()){
            comment.setIsStar(forumService.getUserCommentStar(userId, comment.getId()));
            comment.setFollow(userService.isLike(userId, comment.getAuthor()));
            comment.setName(userService.getUserById(comment.getAuthor()).getName());
        }
    }

    private Response getIssueDetail(List<Issue> list, String userId) {
        for(Issue issue: list){
            getDetail(issue, userId);
        }
        return Response.success(list);
    }

    @PostMapping("/starIssue")
    @CrossOrigin
    public Response starIssue(@RequestParam("Issue") String issueId,
            @RequestParam("token") String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (JWTUtil.verify(token, user.getId(), user.getPassword()) == false) {
            return Response.error(1004, "Token verification failed");
        }
        Issue issue = forumService.getIssueById(issueId);
        int old = issue.getStar();
        old += 1;
        issue.setStar(old);
        System.out.println(issue);
        forumService.updateIssueInfo(issue);
        boolean res = forumService.starIssue(user.getId(), issueId);
        if (res) {
            return Response.success(issue);
        } else {
            return Response.error(0, "Fail to star the issue.");
        }
    }

    @PostMapping("/unstarIssue")
    @CrossOrigin
    public Response unstarIssue(@RequestParam("Issue") String issueId,
            @RequestParam("token") String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (JWTUtil.verify(token, user.getId(), user.getPassword()) == false) {
            return Response.error(1004, "Token verification failed");
        }
        Issue issue = forumService.getIssueById(issueId);
        int old = issue.getStar();
        old -= 1;
        issue.setStar(old);
        System.out.println(issue);
        forumService.updateIssueInfo(issue);
        boolean res = forumService.unStarIssue(user.getId(), issueId);
        if (res) {
            return Response.success(issue);
        } else {
            return Response.error(0, "Fail to unStar the issue.");
        }
    }

    @PostMapping("/likeIssue")
    @CrossOrigin
    public Response likeIssue(@RequestParam("Issue") String issueId,@RequestParam("user") String userId) {
        //TODO:
        Issue issue = forumService.getIssueById(issueId);
        int old = issue.getLove();
        old += 1;
        issue.setLove(old);
        System.out.println(issue);
        forumService.updateIssueInfo(issue);
        boolean res = forumService.likeIssue(userId, issueId);
        if (res) {
            return Response.success(issue);
        } else {
            return Response.error(0, "Fail to like the issue.");
        }
    }

    @PostMapping("/dislikeIssue")
    @CrossOrigin
    public Response dislikeIssue(@RequestParam("Issue") String issueId,@RequestParam("user") String userId) {
        //TODO:
        Issue issue = forumService.getIssueById(issueId);
        int old = issue.getLove();
        old -= 1;
        issue.setLove(old);
        forumService.updateIssueInfo(issue);
        boolean res = forumService.dislikeIssue(userId, issueId);
        if (res) {
            return Response.success(issue);
        } else {
            return Response.error(0, "Fail to dislike the issue.");
        }
    }

    @PostMapping("/getDraftList")
    @CrossOrigin
    public Response getDraft(@RequestParam("user") String userId) {
        //TODO:
        List<Issue> issue = forumService.getIssueDraft(userId);
        System.out.println(issue);

        return Response.success(issue);
    }

    @PostMapping("/getDraft")
    @CrossOrigin
    public Response getOneDraft(@RequestParam("issue") String Id) {
        //TODO:
        Issue issue = forumService.getOneIssueDraft(Id);
        System.out.println(issue);

        return Response.success(issue);
    }

    @PostMapping("/initDraft")
    @CrossOrigin
    public Response initDraft(@RequestParam("user") String userId,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content) {
        //TODO:
        Issue issue = new Issue();
        issue.setId(HashGenerator.hash(userId, title));
        issue.setOpener(userId);
        issue.setContent(content);
        issue.setTitle(title);
        issue.setIsDraft(0);
        Long time = System.currentTimeMillis();
        issue.setCreatedAt(time);
        forumService.addIssue(issue);
        return Response.success(null);
    }

    @PostMapping("/modifyDraft")
    public Response saveDraft(@RequestParam("issue") String issue,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content) {
        //TODO:
        boolean t = forumService.updateDraft(issue, title, content) == 1;
        if (t){
            return Response.success("成功修改");
        }
        else {
            return Response.error(1001, "删除失败");
        }
    }

    @PostMapping("/postDraft")
    public Response postDraft(@RequestParam("issue") String issue){
        boolean f =forumService.postDraft(issue) == 1;
        if (f){
            return Response.success("上传成功");
        }
        else {
            return Response.error(1001, "上传失败");
        }
    }

    @PostMapping("/deleteDraft")
    @CrossOrigin
    public Response deleteDraft(@RequestParam("issue") String issueId) {
        //TODO:
        forumService.removeIssue(issueId);
        return Response.success(null);
    }

}
