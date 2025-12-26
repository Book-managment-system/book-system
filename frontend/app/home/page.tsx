import { DataTableDemo } from "@/components/datatable";
import LogoutButton from "@/components/LogoutButton";

export default function Home() {
    return (
        <div className="min-h-screen bg-gray-50">
            <div className="flex justify-end p-4">
                <LogoutButton />
            </div>
            <DataTableDemo />
        </div>
    );
}