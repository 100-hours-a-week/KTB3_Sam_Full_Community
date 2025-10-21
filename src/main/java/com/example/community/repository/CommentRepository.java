package com.example.community.repository;

import com.example.community.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class CommentRepository {
    private Map<Long, Comment> commentDB;
    private Map<Long, List<Long>> boardIndexMap;
    private long sequence = 0L;

    CommentRepository() {
        this.commentDB = new LinkedHashMap<>();
        this.boardIndexMap = new ConcurrentHashMap<>();
    }

    public Comment save(Comment comment) {
        if(comment.getId() == null) {
            comment.setId(++sequence);
            if(!boardIndexMap.containsKey(comment.getBoardId())) {
                List<Long> commentIds = new ArrayList<>();
                commentIds.add(comment.getId());
                boardIndexMap.put(comment.getBoardId(), commentIds);
            } else {
                boardIndexMap.get(comment.getBoardId()).add(comment.getId());
            }
        }
        commentDB.put(comment.getId(), comment);
        return comment;
    }

    public List<Comment> findAll() {
        return new ArrayList<>(commentDB.values());
    }

    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(commentDB.get(id));
    }

    public void deleteById(Long id) {
        Comment comment = commentDB.remove(id);
        boardIndexMap.get(comment.getBoardId()).remove(id);
    }

    public void deleteByBoardIdWithIndex(Long boardId) {
        List<Long> deleteCommentIds = boardIndexMap.remove(boardId);
        deleteCommentIds.forEach(commentDB::remove);
    }

    public void deleteByBoardIdWithoutIndex(Long boardId) {
        commentDB.values().removeIf(comment -> comment.getBoardId().equals(boardId));
    }

    public List<Comment> findAllByBoardId(Long boardId) {
        List<Long> foundCommentIds = boardIndexMap.get(boardId);
        return foundCommentIds.stream()
                .map(commentDB::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Comment> findAllByBoardIds(List<Long> boardIds) {
        return boardIds.stream()
                .flatMap(boardId -> {
                    List<Long> ids = boardIndexMap.get(boardId);
                    return ids.stream().map(commentDB::get).filter(Objects::nonNull);
                })
                .collect(Collectors.toList());
    }

    public List<Comment> findPageByBoardId(Long boardId, int page, int size) {
        List<Long> foundCommentIds = boardIndexMap.get(boardId);

        return foundCommentIds.stream()
                .skip((long) (page-1) * size)
                .limit(size)
                .map(commentDB::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public int count() {
        return commentDB.size();
    }
}
