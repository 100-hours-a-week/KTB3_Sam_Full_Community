package com.example.community.springmvcpractice;

public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String description;
    private String isbn;

    public BookDto() {}

    public BookDto(Long id, String title, String author, String description, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
    }

    public BookDto(String title, String author, String description, String isbn) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
