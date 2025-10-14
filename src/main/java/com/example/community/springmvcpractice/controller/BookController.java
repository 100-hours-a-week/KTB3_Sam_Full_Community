package com.example.community.springmvcpractice.controller;

import com.example.community.springmvcpractice.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books/list";
    }

    @GetMapping("/{id}")
    public String getBook(Model model, @PathVariable Long id){
        model.addAttribute("book", bookService.getBookById(id));
        return "books/detail";
    }
}
