'use client';
import React from "react";
import { CartBookPrice } from "../../lib/mockCartData";

interface CartItemProps {
  item: CartBookPrice;
  onRemove: (isbn: string) => void;
}

const CartItem: React.FC<CartItemProps> = ({ item, onRemove }) => {
  return (
    <div className="flex justify-between items-center p-2 border-b">
      <div>
        <h3 className="font-bold">{item.title}</h3>
        <p className="text-sm text-gray-600">ISBN: {item.isbn}</p>
        <p className="text-sm">
          {item.quantity} × ${item.unitPrice.toFixed(2)} = ${item.totalPrice.toFixed(2)}
        </p>
      </div>
      <button
        className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
        onClick={() => onRemove(item.isbn)}
      >
        Remove
      </button>
    </div>
  );
};

export default CartItem;
