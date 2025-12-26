"use server";

import { responseErrorToString } from "@/lib/error";

export const fetchTopCustomers = async (
    token: string
) => {
    
    const response = await fetch(
        `http://localhost:8080/v1/api/user/reports/TopBookCustomers`,
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        }
    );

    console.log("HTTP status:", response.status);

    return response.json();
};

export const fetchPreviousMonthSales = async (
    token: string
) => {
    
    const response = await fetch(
        `http://localhost:8080/v1/api/user/reports/totalsales`,
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        }
    );

    console.log("HTTP status:", response.status);

    return response.json();
};

export const fetchDailySales = async (
    date: string,
    token: string
) => {
    
    const response = await fetch(
        `http://localhost:8080/v1/api/user/reports/t?date=${date}`,
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        }
    );

    console.log("HTTsP status:", response.status);

    return response.json();
};

export const fetchBookReport = async (
    isbn: string,
    token: string
) => {
    console.log(isbn)
    
    const response = await fetch(
        `http://localhost:8080/v1/api/user/reports/orderd/${isbn?isbn:-1}`,
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        }
    );

    console.log("HTTP status:", response.status);

    return response.json();
};

export const fetchTopBooks = async (
    token: string
) => {
    
    const response = await fetch(
        `http://localhost:8080/v1/api/user/reports/TopBooksales`,
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        }
    );

    console.log("HTTP status:", response.status);

    return response.json();
};