import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "../pages/Login";
import Register from "../pages/Register";
import Dashboard from "../pages/Dashboard";
import IncidentList from "../pages/IncidentList";
import CreateIncident from "../pages/CreateIncident";
import EditIncident from "../pages/EditIncident";
import IncidentDetails from "../pages/IncidentDetails";
import Profile from "../pages/Profile";
import ProtectedRoute from "./ProtectedRoute";
import DashboardLayout from "../layouts/DashBoardLayout";
import VerifyOtp from "../pages/VerifyOtp";
import ForgotPassword from "../pages/ForgotPassword";
import ResetPassword from "../pages/ResetPassword";
export default function AppRoutes() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/login" replace />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/verify" element={<VerifyOtp />} />
                <Route path="/forgot-password" element={<ForgotPassword />} />
                <Route path="/reset-password" element={<ResetPassword />} />
                <Route element={<ProtectedRoute />}>
                    <Route element={<DashboardLayout />}>
                        <Route path="/dashboard" element={<Dashboard />} />
                        <Route path="/incidents" element={<IncidentList />} />
                        <Route path="/incidents/create" element={<CreateIncident />} />
                        <Route path="/incidents/:id" element={<IncidentDetails />} />
                        <Route path="/incidents/:id/edit" element={<EditIncident />} />
                        <Route path="/profile" element={<Profile />} />
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    );
}