package org.example.backend.model.dto;

import org.example.backend.model.entity.Book;

public class BookSalesHistory {
    private Book book;
    private int quantity;
    private int totalprofit;


    public int getTotalprofit() {
        return totalprofit;
    }
    public void setTotalprofit(int totalprofit) {
        this.totalprofit = totalprofit;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
}
