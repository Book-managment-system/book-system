'use server';

const BASE_URL =
    process.env.NEXT_PUBLIC_BACKEND_URL || "http://localhost:8080/v1/api";

const ACCESS_TOKEN =
    "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmZGoiLCJ1c2VySWQiOjEzLCJyb2xlIjoiQ3VzdG9tZXIiLCJ0eXBlIjoiYWNjZXNzIiwiaWF0IjoxNzY2NzY4MTMzLCJleHAiOjE3NjY3NzE3MzN9.FHG9qQCc8w3j7qg6dV1gKtpPyrqYVpXx3Uiac6AkY9oDopi2qMzeA4739lLEkcQGpU7xAJGEFIUvs5wqT1b3gg";

/* =========================
   Types
========================= */

export interface UserUpdate {
    userId: number;
    firstName: string;
    username: string;
    lastName: string;
    email: string;
    phone: string;
    shippingAddress: string;
}

export interface ChangePasswordRequest {
  userId: number; // add userId from token or context
  currentPassword: string;
  newPassword: string;
}

/* =========================
   API Calls
========================= */
export async function getProfile(): Promise<UserUpdate> {
  const res = await fetch(`${BASE_URL}/user/profile`, {
    method: "GET",
    headers: { "Authorization": ACCESS_TOKEN },
    mode: "cors",
  });
  const text = await res.text();
  return text ? JSON.parse(text) : {} as UserUpdate;
}
export async function updateProfile(user: UserUpdate): Promise<string> {
    try {
        const res = await fetch(`${BASE_URL}/user/profile`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": ACCESS_TOKEN
            },
            body: JSON.stringify(user),
            mode: "cors"
        });

        const text = await res.text();
        // return backend message even if status is 400/403/500
        return text || "No response from server";

    } catch (err) {
        console.error("Update profile error:", err);
        return "Network error or backend unreachable";
    }
}

export async function updatePassword(request: ChangePasswordRequest): Promise<{ success: boolean; message: string }> {
  try {
    const res = await fetch(`${BASE_URL}/user/password`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": ACCESS_TOKEN
      },
      body: JSON.stringify(request),
      mode: "cors"
    });

    const text = await res.text();
    // If backend returns an error message, we keep it
    return { success: res.status === 200, message: text || "No response from server" };
  } catch (err: any) {
    console.error("Update password error:", err);
    return { success: false, message: err.message || "Network error or backend unreachable" };
  }
}