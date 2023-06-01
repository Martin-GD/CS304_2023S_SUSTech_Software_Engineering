package com.team127.atom.web.controller;

import com.team127.atom.model.Reply;
import com.team127.atom.service.ForumService;
import com.team127.atom.service.UserService;
import com.team127.atom.util.HashGenerator;
import lombok.extern.slf4j.Slf4j;
import com.team127.atom.model.Response;
import com.team127.atom.model.Note;
import com.team127.atom.model.NoteContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
public class ReplyController {
    @Autowired
    ForumService forumService;

    @PostMapping("/addReply")
    public Response createReply(@RequestParam("user") String userId,@RequestParam("issue") String issue,
                                @RequestParam("content") String comment) {
        //TODO:
        Reply reply = new Reply();
        reply.setId(HashGenerator.hash(userId, issue, comment));
        reply.setAuthor(userId);
        reply.setIssue(issue);
        reply.setComment(comment);
        Long time = System.currentTimeMillis();
        reply.setCreatedAt(time);
        forumService.addReply(reply);
        return Response.success(reply);
    }

    @PostMapping("/deleteReply")
    public Response deleteReply(@RequestParam("reply") String replyId) {
        //TODO:
        forumService.removeReply(replyId);
        return Response.success(null);
    }

    @PostMapping("/modifyReply")
    public Response modifyReply(@RequestParam("reply") String replyId,
                                @RequestParam("content") String comment
                                ) {
        //TODO:
        forumService.modifyReply(replyId, comment);
        return Response.success(comment);
    }

    @GetMapping("/getReplyListUser")
    public Response getReplyList(@RequestParam("user") String userId) {
        //TODO:
        List<Reply> replyList = forumService.getUserReply(userId);

        return Response.success(replyList);
    }

    @GetMapping("/getReplyListForum") //就是最近的reply
    public Response getReplyListForum(@RequestParam("issue") String issueId) {
        //TODO:
        List<Reply> replyList = forumService.getIssueReply(issueId);

        return Response.success(replyList);
    }

    @GetMapping("/getPopularReply")
    public Response getPopularReply(@RequestParam("issue") String issueId) {
        //TODO:
        List<Reply> replyList = forumService.getPopularIssueReply(issueId);
        return Response.success(replyList);
    }

//    @GetMapping("/getRecentReply")
//    public Response getRecentReply(@RequestParam("title") String title,
//                                @RequestParam("content") String content) {
//        //TODO:
//        return Response.success(null);
//    }

    @PostMapping("/starReply")
    public Response starReply(@RequestParam("user") String userId,@RequestParam("reply") String replyId) {
        //TODO:
        Reply reply = forumService.getReplyById(replyId);
        Integer old = reply.getStar();
        old +=1;
        reply.setStar(old);
        forumService.updateReply(reply);
        boolean res = forumService.starReply(userId, replyId);
        if (res) {
            return Response.success(reply);
        } else {
            return Response.error(0, "Fail to star the reply.");
        }
    }

    @PostMapping("/unstarReply")
    public Response unstarReply(@RequestParam("user") String userId,@RequestParam("reply") String replyId) {
        //TODO:
        Reply reply = forumService.getReplyById(replyId);
        Integer old = reply.getStar();
        old -=1;
        reply.setStar(old);
        forumService.updateReply(reply);
        boolean res = forumService.unStarReply(userId, replyId);
        if (res) {
            return Response.success(reply);
        } else {
            return Response.error(0, "Fail to unstar the reply.");
        }
    }


}
