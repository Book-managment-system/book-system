'use client';
import React from "react";
import CartItem from "./CartItem";
import { CartBookPrice } from "../../lib/mockCartData";

interface CartListProps {
  items: CartBookPrice[];
  onRemove: (isbn: string) => void;
}

const CartList: React.FC<CartListProps> = ({ items, onRemove }) => {
  if (items.length === 0) {
    return <p>Your cart is empty.</p>;
  }

  return (
    <div className="border rounded shadow p-2">
      {items.map((item) => (
        <CartItem key={item.isbn} item={item} onRemove={onRemove} />
      ))}
    </div>
  );
};

export default CartList;
