import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

export default function DashboardLayout() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const handleLogout = () => {
        logout();
        navigate("/login", { replace: true });
    };
    return (
        <div className="min-h-screen bg-slate-100">
            <header className="flex items-center justify-between border-b bg-white px-6 py-4 shadow-sm">
                <h1 className="text-xl font-bold">IncidentOps</h1>
                <div className="flex items-center gap-4">
                    <span className="text-sm text-slate-600">
                        {user?.email}
                    </span>
                    <button onClick={handleLogout} className="rounded bg-red-500 px-3 py-2 text-white hover:bg-red-600">
                        Logout
                    </button>
                </div>
            </header>

            <div className="flex">
                <aside className="min-h-[calc(100vh-73px)] w-60 border-r bg-white p-4">
                    <nav className="flex flex-col gap-2">
                        <NavLink to="/dashboard">Dashboard</NavLink>
                        <NavLink to="/incidents">Incidents</NavLink>
                        <NavLink to="/profile">Profile</NavLink>
                    </nav>
                </aside>
                <main className="flex-1 p-6">
                    <Outlet />
                </main>
            </div>
        </div>
    );
}