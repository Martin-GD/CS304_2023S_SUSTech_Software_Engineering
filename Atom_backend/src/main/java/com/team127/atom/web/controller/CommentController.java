package com.team127.atom.web.controller;

import com.team127.atom.model.*;
import com.team127.atom.service.ForumService;
import com.team127.atom.util.HashGenerator;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class CommentController {
    @Autowired
    ForumService forumService;

    @PostMapping("/addComment")
    public Response createComment(@RequestParam("user") String userId,@RequestParam("reply") String reply,
                                @RequestParam("content") String content) {
        //TODO:
        Comment comment = new Comment();
        comment.setId(HashGenerator.hash(userId, reply, content));
        comment.setAuthor(userId);
        comment.setReply(reply);
        comment.setContent(content);
        Long time = System.currentTimeMillis();
        comment.setCreatedAt(time);
        forumService.addComment(comment);
        return Response.success(comment);
    }

    @PostMapping("/deleteComment")
    public Response deleteComment(@RequestParam("comment") String commentId) {
        //TODO:
        int i = forumService.removeComment(commentId);
        return Response.success(i);
    }

    @PostMapping("/modifyComment")
    public Response modifyComment(@RequestParam("comment") String commentId,
                                @RequestParam("content") String content
    ) {
        //TODO:
        int i = forumService.modifyComment(commentId, content);
        return Response.success(i);
    }

    @GetMapping("/getCommentListUser")
    public Response getCommentListUser(@RequestParam("user") String userId) {
        //TODO:
        List<Comment> commentList = forumService.getUserComment(userId);

        return Response.success(commentList);
    }

    @GetMapping("/getComment List") //就是最近的comment
    public Response getCommentListForum(@RequestParam("reply") String replyId) {
        //TODO:
        List<Comment> replyList = forumService.getReplyComment(replyId);

        return Response.success(replyList);
    }

    @GetMapping("/getPopularComment")
    public Response getPopularComment(@RequestParam("reply") String replyId) {
        //TODO:
        List<Comment> replyList = forumService.getPopularReplyComment(replyId);
        return Response.success(replyList);
    }

//    @GetMapping("/getRecentReply")
//    public Response getRecentReply(@RequestParam("title") String title,
//                                @RequestParam("content") String content) {
//        //TODO:
//        return Response.success(null);
//    }

    @PostMapping("/starComment")
    public Response starComment(@RequestParam("user") String userId,@RequestParam("comment") String commentId) {
        //TODO:
        Comment comment = forumService.getCommentById(commentId);
        Integer old = comment.getStar();
        old +=1;
        comment.setStar(old);
        forumService.updateComment(comment);
        boolean res = forumService.starComment(userId,commentId);
        if (res) {
            return Response.success(comment);
        } else {
            return Response.error(0, "Fail to star the comment.");
        }
    }

    @PostMapping("/unstarComment")
    public Response unstarComment(@RequestParam("user") String userId,
                                @RequestParam("comment") String commentId) {
        //TODO:
        Comment comment = forumService.getCommentById(commentId);
        Integer old = comment.getStar();
        old -=1;
        comment.setStar(old);
        forumService.updateComment(comment);
        boolean res = forumService.unStarComment(userId,commentId);
        if (res) {
            return Response.success(comment);
        } else {
            return Response.error(0, "Fail to unstar the comment.");
        }
    }
}
