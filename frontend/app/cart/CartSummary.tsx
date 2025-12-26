'use client';
import React from "react";

interface CartSummaryProps {
  total: number;
  onClear: () => void;
  onCheckout: () => void;
}

const CartSummary: React.FC<CartSummaryProps> = ({ total, onClear, onCheckout }) => {
  return (
    <div className="mt-4 p-4 border rounded shadow flex justify-between items-center">
      <p className="text-lg font-bold">Total: ${total}</p>
      <div className="space-x-2">
        <button
          className="bg-gray-500 text-white px-3 py-1 rounded hover:bg-gray-600"
          onClick={onClear}
        >
          Clear Cart
        </button>
        <button
          className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
          onClick={onCheckout}
        >
          Checkout
        </button>
      </div>
    </div>
  );
};

export default CartSummary;
