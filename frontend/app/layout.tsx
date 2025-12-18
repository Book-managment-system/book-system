import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import { Toaster } from "react-hot-toast";

const geistSans = Geist({
    variable: "--font-geist-sans",
    subsets: ["latin"],
});

const geistMono = Geist_Mono({
    variable: "--font-geist-mono",
    subsets: ["latin"],
});

export const metadata: Metadata = {
    title: "Book Store App",
    description:
        "Discover and manage your favorite books. Browse, search, and organize your personal library with our intuitive book management system.",
};

export default function RootLayout({
    children,
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html lang="en">
            <body
                className={`${geistSans.variable} ${geistMono.variable} antialiased`}>
                {children}
                <Toaster
                    position="bottom-right"
                    reverseOrder={false}
                    gutter={8}
                    containerClassName=""
                    containerStyle={{}}
                    toasterId="default"
                    toastOptions={{
                        // Define default options
                        className: "",
                        duration: 5000,
                        removeDelay: 1000,
                        style: {
                            background: "white",
                            color: "oklch(66.6% 0.179 58.318)",
                        },

                        iconTheme: {
                            primary: "oklch(66.6% 0.179 58.318)",
                            secondary: "white",
                        },
                    }}
                />
            </body>
        </html>
    );
}
