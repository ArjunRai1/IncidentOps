import { useAuth } from "../hooks/useAuth";

export default function Dashboard() {
    const { user } = useAuth();
    return (
        <>
            <h1 className="text-3xl font-bold">Dashboard</h1>
            <p className="mt-4">Welcome, {user?.email}</p>
            <p className="mt-2">Role: {user?.role}</p>
        </>
    );
}