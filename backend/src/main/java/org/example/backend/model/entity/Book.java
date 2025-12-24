package org.example.backend.model.entity;

import org.example.backend.model.enums.CategoryType;

public class Book {
    private String isbn;
    private String title;
    private int publisherId;
    private Integer publicationYear; // nullable
    private double sellingPrice;
    private CategoryType category;
    private int numberOfBooks;
    private int threshold;

    public Book(String isbn, String title, int publisherId, Integer publicationYear, double sellingPrice, CategoryType category, int numberOfBooks, int threshold) {
        this.isbn = isbn;
        this.title = title;
        this.publisherId = publisherId;
        this.publicationYear = publicationYear;
        this.sellingPrice = sellingPrice;
        this.category = category;
        this.numberOfBooks = numberOfBooks;
        this.threshold = threshold;
    }

    public Book() {
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public CategoryType getCategory() {
        return category;
    }

    public void setCategory(CategoryType category) {
        this.category = category;
    }

    public int getNumberOfBooks() {
        return numberOfBooks;
    }

    public void setNumberOfBooks(int numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
