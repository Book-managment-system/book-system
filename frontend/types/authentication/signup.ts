import z from "zod";

export const signupSchema = z
    .object({
        firstName: z
            .string()
            .min(1, "First name is required")
            .min(2, "First name must be at least 2 characters"),
        lastName: z
            .string()
            .min(1, "Last name is required")
            .min(2, "Last name must be at least 2 characters"),
        email: z.email("Invalid email address"),
        phone: z
            .string()
            .min(1, "Phone number is required")
            .regex(
                /^[+]?[(]?[0-9]{1,4}[)]?[-\s.]?[(]?[0-9]{1,4}[)]?[-\s.]?[0-9]{1,9}$/,
                "Invalid phone number format"
            ),
        address: z.string().min(1, "Shipping address is required"),
        username: z.string().min(1, "Username is required"),
        password: z.string().min(6, "Password must be at least 6 characters"),
        passwordConfirmation: z
            .string()
            .min(6, "Password confirmation must be at least 6 characters"),
    })
    .refine((data) => data.password === data.passwordConfirmation, {
        message: "Passwords do not match",
        path: ["passwordConfirmation"],
    });

export type SignUpFormData = z.infer<typeof signupSchema>;
