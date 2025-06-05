package com.twitter.demo.services;

import com.twitter.demo.entities.Post;
import com.twitter.demo.entities.User;
import com.twitter.demo.entities.dto.CreatePostDto;
import com.twitter.demo.entities.dto.UserPostsDto;
import com.twitter.demo.repositories.PostRepository;
import com.twitter.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public void createPost(CreatePostDto request){
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }

        Post post = new Post();
        post.setAuthor(optionalUser.get());
        post.setMessage(request.getContent());
        post.setComments(new ArrayList<>());
        postRepository.save(post);
    }

    public void deleteUserPost(UUID userId, UUID postId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new RuntimeException("Post not found");
        }

        Post post = optionalPost.get();
        if (!post.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("This post does not belong to the user");
        }
        postRepository.delete(post);
    }

    public List<UserPostsDto> getUserPosts(UUID userId){
        List<UserPostsDto> response = new ArrayList<>();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        List<Post> userPost = postRepository.findAllByAuthor(optionalUser.get());
        userPost.forEach(post -> {
            response.add(new UserPostsDto(post.getId(), post.getMessage()));
        });

        return response;
    }

    public void likePost(UUID userId, UUID postId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()){
            throw new RuntimeException("Post not found");
        }
        optionalPost.get().getLikes().add(optionalUser.get());
        postRepository.save(optionalPost.get());
    }

    public List<Post> getLikedPosts(UUID userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return optionalUser.get().getLikes();
    }

}
