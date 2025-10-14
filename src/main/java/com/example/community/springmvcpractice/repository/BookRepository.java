package com.example.community.springmvcpractice.repository;

import com.example.community.springmvcpractice.BookDto;
import org.springframework.stereotype.Repository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BookRepository {
    private Map<Long, BookDto> bookDB;
    private long sequence;

    public BookRepository() {
        this.bookDB = new LinkedHashMap<>();
        this.sequence = 0L;

        this.save(new BookDto("Clean Code","Robert C. Martin","소프트웨어 장인 정신을 담은 책입니다.","9780132350884"));
        this.save(new BookDto("객체지향의 사실과 오해","조영호", "객체지향의 본질을 쉽게 설명합니다.","9791186710770"));
        this.save(new BookDto("Effective Java", "Joshua Bloch", "자바 개발자를 위한 베스트 프랙티스 모음집입니다.","9780134685991"));
    }

    public BookDto save(BookDto book) {
        if(book.getId() == null) {
            this.sequence++;
            book.setId(sequence);
        }
        bookDB.put(book.getId(), book);
        return book;
    }

    public Optional<BookDto> findById(Long id) {
        return Optional.ofNullable(bookDB.get(id));
    }

    public List<BookDto> findAll() {
        return this.bookDB.keySet().stream()
                .map(bookDB::get)
                .collect(Collectors.toList());
    }
}
