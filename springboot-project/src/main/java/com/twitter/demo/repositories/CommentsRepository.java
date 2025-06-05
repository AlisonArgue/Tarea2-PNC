package com.twitter.demo.repositories;

import com.twitter.demo.entities.Comments;
import com.twitter.demo.entities.Post;
import com.twitter.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, UUID> {

    public List<Comments> findAllByAuthor(User author);
    public Optional<Comments> findById(UUID id);
}
