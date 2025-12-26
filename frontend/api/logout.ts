const BACKEND_URL = process.env.BACKEND_URL || process.env.NEXT_PUBLIC_BACKEND_URL || "http://localhost:8080";

export const logoutUser = async (refreshToken: string | null): Promise<void> => {
    if (!refreshToken) {
        return;
    }

    try {
        const response = await fetch(`${BACKEND_URL}/v1/api/auth/logout`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify({ refreshToken }),
        });

        if (response.status === 401) {
            throw new Error("UNAUTHORIZED");
        }

        if (response.status === 403) {
            throw new Error("FORBIDDEN");
        }

        if (!response.ok) {
            const text = await response.text();
            throw new Error(text || "SERVER_ERROR");
        }

        return response.json();
    } catch (error) {
        // Silently fail - logout should work even if backend call fails
        console.warn("Logout API call failed:", error);
    }
};