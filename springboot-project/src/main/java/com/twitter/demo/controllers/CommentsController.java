package com.twitter.demo.controllers;

import com.twitter.demo.entities.dto.CreateCommentDto;
import com.twitter.demo.entities.dto.CreatePostDto;
import com.twitter.demo.services.CommentService;
import com.twitter.demo.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentsController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<Void> createComment(@RequestBody CreateCommentDto request){
        commentService.createComment(request);
        return ResponseEntity.noContent().build();
    }

}
