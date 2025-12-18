"use server";
import { responseErrorToString } from "@/lib/error";
import { SignUpFormData } from "@/types/authentication/signup";

export const signUp = async (data: SignUpFormData) => {
    console.log(process.env.BACKEND_URL);
    const response = await fetch(`${process.env.BACKEND_URL}/auth/signup`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            username: data.username,
            password: data.password,
            firstName: data.firstName,
            lastName: data.lastName,
            email: data.email,
            phone: data.phone,
            shippingAddress: data.address,
        }),
    });

    if (!response.ok) {
        throw new Error(
            "Failed to sign up: " + (await responseErrorToString(response))
        );
    }
};
