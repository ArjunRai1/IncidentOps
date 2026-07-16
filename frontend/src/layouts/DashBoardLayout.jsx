import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useState } from "react";
import {LayoutDashboard, TriangleAlert, User, LogOut} from "lucide-react";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";

import { useAuth } from "../hooks/useAuth";
import AIChatButton from "../components/ai/AIChatButton";
import AIChatDrawer from "../components/ai/AIChatDrawer";

export default function DashboardLayout() {
  const [chatOpen, setChatOpen] = useState(false);

  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login", { replace: true });
  };

  const navItems = [
    {
      to: "/dashboard",
      label: "Dashboard",
      icon: LayoutDashboard,
    },
    {
      to: "/incidents",
      label: "Incidents",
      icon: TriangleAlert,
    },
    {
      to: "/profile",
      label: "Profile",
      icon: User,
    },
  ];

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="flex min-h-screen">

        <aside className="w-64 border-r bg-white">
          <div className="flex h-16 items-center border-b px-6">
            <h1 className="text-lg font-semibold tracking-tight">IncidentOps</h1>
          </div>

          <nav className="space-y-2 p-4">
            {navItems.map(({ to, label, icon: Icon }) => (
              <NavLink
                key={to}
                to={to}
                className={({ isActive }) =>
                  [
                    "flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors",
                    isActive
                      ? "bg-indigo-50 text-indigo-700"
                      : "text-slate-600 hover:bg-slate-100 hover:text-slate-900",
                  ].join(" ")
                }
              >
                <Icon size={18} />
                {label}
              </NavLink>
            ))}
          </nav>
        </aside>

        <div className="flex flex-1 flex-col">
          <header className="flex h-16 items-center justify-between border-b bg-white px-8">
            <div>
              <h2 className="text-lg font-semibold text-slate-900">Incident Management</h2>
            </div>

            <div className="flex items-center gap-4">
              <span className="text-sm text-slate-500">{user?.email}</span>
              <Button variant="outline" onClick={handleLogout} className="gap-2">
                <LogOut size={16} />
                Logout
              </Button>
            </div>
          </header>

          <main className="flex-1 bg-slate-100 p-8">
              <Outlet />
          </main>
        </div>
      </div>

      <AIChatButton onClick={() => setChatOpen(true)} />
      <AIChatDrawer open={chatOpen} onClose={() => setChatOpen(false)}/>
    </div>
  );
}