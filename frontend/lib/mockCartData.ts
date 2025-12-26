export interface CartBookPrice {
  isbn: string;
  title: string;
  unitPrice: number;
  quantity: number;
  totalPrice: number;
}

// Mock cart items
export const mockCart: CartBookPrice[] = [
  {
    isbn: "9780132350884",
    title: "Clean Code",
    unitPrice: 30,
    quantity: 1,
    totalPrice: 30
  },
  {
    isbn: "9780321356680",
    title: "Effective Java",
    unitPrice: 45,
    quantity: 2,
    totalPrice: 90
  }
];
