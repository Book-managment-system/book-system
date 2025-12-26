"use client";

import AdminDashboard from "@/components/admindashboard";
import LogoutButton from "@/components/LogoutButton";

export default function Dashboard()
{
    return (
        <div className="min-h-screen bg-gray-50">
            <div className="flex justify-end p-4">
                <LogoutButton />
            </div>
            <AdminDashboard/>
        </div>
    );
}