package com.twitter.demo.services;

import com.twitter.demo.entities.Comments;
import com.twitter.demo.entities.Post;
import com.twitter.demo.entities.User;
import com.twitter.demo.entities.dto.CreateCommentDto;
import com.twitter.demo.repositories.CommentsRepository;
import com.twitter.demo.repositories.PostRepository;
import com.twitter.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
public class CommentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    public void createComment(CreateCommentDto request){
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }

        Optional<Post> optionalPost = postRepository.findById(request.getPostId());
        if (optionalPost.isEmpty()){
            throw new RuntimeException("Post not found");
        }
        Comments comment = new Comments();
        comment.setAuthor(optionalUser.get());
        comment.setPost(optionalPost.get());
        comment.setMessage(request.getContent());
        commentsRepository.save(comment);
    }
}
