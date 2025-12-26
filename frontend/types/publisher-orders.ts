export enum PublisherOrderStatus {
    Pending = "Pending",
    Confirmed = "Confirmed",
}

export interface PublisherOrder {
    orderId: number;
    isbn: string;
    bookTitle: string;
    quantity: number;
    orderDate: string;
    status: PublisherOrderStatus;
}

