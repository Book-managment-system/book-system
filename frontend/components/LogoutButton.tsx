"use client";
import { LogOut } from "lucide-react";
import { logoutUser } from "@/api/logout";
import { clearAllTokens, getRefreshToken } from "@/lib/token-storage";
import { useRouter } from "next/navigation";
import toast from "react-hot-toast";

interface LogoutButtonProps {
    className?: string;
    variant?: "button" | "icon";
}

export default function LogoutButton({ className = "", variant = "button" }: LogoutButtonProps) {
    const router = useRouter();

    const handleLogout = async () => {
        try {
            // Get refresh token for backend logout
            const refreshToken = getRefreshToken();

            // Call backend logout
            await logoutUser(refreshToken);

            // Clear all tokens from localStorage
            clearAllTokens();

            // Show success message
            toast.success("Logged out successfully!");

            // Redirect to login page
            router.push("/");
        } catch (error) {
            // Even if backend logout fails, clear local tokens and redirect
            clearAllTokens();
            toast.success("Logged out successfully!");
            router.push("/");
        }
    };

    if (variant === "icon") {
        return (
            <button
                onClick={handleLogout}
                className={`p-2 rounded-lg hover:bg-gray-100 transition-colors ${className}`}
                title="Logout"
            >
                <LogOut className="h-5 w-5 text-gray-600" />
            </button>
        );
    }

    return (
        <button
            onClick={handleLogout}
            className={`flex items-center gap-2 bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition-colors ${className}`}
        >
            <LogOut className="h-4 w-4" />
            Logout
        </button>
    );
}