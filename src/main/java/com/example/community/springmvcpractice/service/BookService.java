package com.example.community.springmvcpractice.service;

import com.example.community.springmvcpractice.BookDto;
import com.example.community.springmvcpractice.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public BookDto createBook(BookDto bookDto) {
        return bookRepository.save(bookDto);
    }
}
