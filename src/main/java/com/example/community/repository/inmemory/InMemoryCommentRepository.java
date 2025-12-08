package com.example.community.repository.inmemory;

import com.example.community.entity.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCommentRepository extends InMemoryBoardLinkedRepository<Comment> {

}
