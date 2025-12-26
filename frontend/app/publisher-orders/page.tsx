"use client";

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { getAllPublisherOrders, confirmPublisherOrder } from "@/lib/publisherOrderApi";
import { PublisherOrder, PublisherOrderStatus } from "@/types/publisher-orders";
import { Button } from "@/components/ui/button";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import toast from "react-hot-toast";
import { CheckCircle2, Clock, Package } from "lucide-react";

export default function PublisherOrdersPage() {
    const router = useRouter();
    const [orders, setOrders] = useState<PublisherOrder[]>([]);
    const [loading, setLoading] = useState(true);
    const [filterStatus, setFilterStatus] = useState<PublisherOrderStatus | "all">("all");
    const [confirmingOrderId, setConfirmingOrderId] = useState<number | null>(null);

    const loadOrders = async () => {
        try {
            setLoading(true);
            const status = filterStatus === "all" ? undefined : filterStatus;
            const data = await getAllPublisherOrders(status);
            setOrders(data);
        } catch (error) {
            console.error("Error loading publisher orders:", error);
            toast.error(
                error instanceof Error
                    ? error.message
                    : "Failed to load publisher orders"
            );
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadOrders();
    }, [filterStatus]);

    const handleConfirmOrder = async (orderId: number) => {
        try {
            setConfirmingOrderId(orderId);
            await confirmPublisherOrder(orderId);
            toast.success("Order confirmed successfully! Stock has been updated.");
            await loadOrders();
        } catch (error) {
            console.error("Error confirming order:", error);
            toast.error(
                error instanceof Error
                    ? error.message
                    : "Failed to confirm order"
            );
        } finally {
            setConfirmingOrderId(null);
        }
    };

    if (loading) {
        return (
            <div className="max-w-7xl mx-auto p-6">
                <p className="text-center p-4">Loading publisher orders...</p>
            </div>
        );
    }

    return (
        <div className="max-w-7xl mx-auto p-6">
            <div className="flex justify-between items-center mb-6">
                <div className="flex items-center gap-3">
                    <Package className="h-6 w-6" />
                    <h1 className="text-3xl font-bold">Publisher Orders</h1>
                </div>
                <Button
                    variant="outline"
                    onClick={() => router.push("/admin_dashboard")}
                >
                    Back to Dashboard
                </Button>
            </div>

            <div className="mb-4 flex gap-2">
                <Button
                    variant={filterStatus === "all" ? "default" : "outline"}
                    size="sm"
                    onClick={() => setFilterStatus("all")}
                >
                    All Orders
                </Button>
                <Button
                    variant={filterStatus === PublisherOrderStatus.Pending ? "default" : "outline"}
                    size="sm"
                    onClick={() => setFilterStatus(PublisherOrderStatus.Pending)}
                >
                    <Clock className="h-4 w-4 mr-1" />
                    Pending
                </Button>
                <Button
                    variant={filterStatus === PublisherOrderStatus.Confirmed ? "default" : "outline"}
                    size="sm"
                    onClick={() => setFilterStatus(PublisherOrderStatus.Confirmed)}
                >
                    <CheckCircle2 className="h-4 w-4 mr-1" />
                    Confirmed
                </Button>
            </div>

            {orders.length === 0 ? (
                <div className="text-center p-8 border rounded-lg">
                    <p className="text-gray-600">
                        {filterStatus === "all"
                            ? "No publisher orders found."
                            : `No ${filterStatus.toLowerCase()} orders found.`}
                    </p>
                </div>
            ) : (
                <div className="border rounded-lg overflow-hidden">
                    <Table>
                        <TableHeader>
                            <TableRow>
                                <TableHead>Order ID</TableHead>
                                <TableHead>Book Title</TableHead>
                                <TableHead>ISBN</TableHead>
                                <TableHead>Quantity</TableHead>
                                <TableHead>Order Date</TableHead>
                                <TableHead>Status</TableHead>
                                <TableHead className="text-right">Actions</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {orders.map((order) => (
                                <TableRow key={order.orderId}>
                                    <TableCell className="font-medium">
                                        #{order.orderId}
                                    </TableCell>
                                    <TableCell>{order.bookTitle}</TableCell>
                                    <TableCell className="font-mono text-sm">
                                        {order.isbn}
                                    </TableCell>
                                    <TableCell>{order.quantity}</TableCell>
                                    <TableCell>
                                        {new Date(order.orderDate).toLocaleString()}
                                    </TableCell>
                                    <TableCell>
                                        <span
                                            className={`inline-flex items-center gap-1 px-2 py-1 rounded-full text-xs font-medium ${
                                                order.status ===
                                                PublisherOrderStatus.Confirmed
                                                    ? "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200"
                                                    : "bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200"
                                            }`}
                                        >
                                            {order.status ===
                                            PublisherOrderStatus.Confirmed ? (
                                                <>
                                                    <CheckCircle2 className="h-3 w-3" />
                                                    Confirmed
                                                </>
                                            ) : (
                                                <>
                                                    <Clock className="h-3 w-3" />
                                                    Pending
                                                </>
                                            )}
                                        </span>
                                    </TableCell>
                                    <TableCell className="text-right">
                                        {order.status ===
                                        PublisherOrderStatus.Pending ? (
                                            <Button
                                                size="sm"
                                                onClick={() =>
                                                    handleConfirmOrder(order.orderId)
                                                }
                                                disabled={
                                                    confirmingOrderId === order.orderId
                                                }
                                            >
                                                {confirmingOrderId === order.orderId
                                                    ? "Confirming..."
                                                    : "Confirm Order"}
                                            </Button>
                                        ) : (
                                            <span className="text-sm text-gray-500">
                                                Already confirmed
                                            </span>
                                        )}
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </div>
            )}
        </div>
    );
}

