package org.example.backend.model.dto;

public class TotalSales {
    int totalSales = 0;
    int totalOrders = 0;
    String month;
    public TotalSales(int totalSales, int totalOrders, String month){
        this.totalSales = totalSales;
        this.totalOrders = totalOrders;
        this.month = month;
    }
    public int getTotalSales(){
        return totalSales;
    }
    public int getTotalOrders(){
        return totalOrders;
    }
    public String getMonth(){
        return month;
    }
    public void setTotalSales(int totalSales){
        this.totalSales = totalSales;
    }
    public void setTotalOrders(int totalOrders){
        this.totalOrders = totalOrders;
    }
    public void setMonth(String month){
        this.month = month;
    }
}
