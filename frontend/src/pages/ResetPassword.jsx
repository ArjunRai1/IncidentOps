import { useState } from "react";
import { useLocation, useNavigate, Navigate } from "react-router-dom";

import { resetPassword } from "../api/authApi";

import { Card } from "../components/ui/card";
import { Button } from "../components/ui/button";

export default function ResetPassword() {
    const location = useLocation();
    const navigate = useNavigate();

    const email = location.state?.email;

    const [otp, setOtp] = useState("");
    const [newPassword, setNewPassword] = useState("");

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    if (!email) {
        return <Navigate to="/forgot-password" replace />;
    }

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            setLoading(true);
            setError("");

            await resetPassword({
                email,
                otp,
                newPassword,
            });

            navigate("/login", { replace: true });
        } catch (err) {
            setError(
                err.response?.data?.message ??
                "Failed to reset password."
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex min-h-screen items-center justify-center bg-slate-100 px-4">
            <Card className="w-full max-w-md">
                <h1 className="mb-2 text-3xl font-bold">Reset Password</h1>
                <p className="mb-6 text-gray-500">Enter the OTP sent to your email.</p>
                {error && (
                    <div className="mb-4 rounded border border-red-200 bg-red-50 p-3 text-red-600">
                        {error}
                    </div>
                )}
                <form onSubmit={handleSubmit} className="space-y-4">
                    <input type="text" value={email} disabled className="w-full rounded border bg-gray-100 p-3"/>
                    <input type="text" value={otp} onChange={(e) => setOtp(e.target.value)} placeholder="OTP" required className="w-full rounded border p-3"/>
                    <input type="password"  value={newPassword} onChange={(e) => setNewPassword(e.target.value)} placeholder="New Password"  minLength={8} required className="w-full rounded border p-3"/>
                    <Button type="submit" loading={loading} className="w-full">Reset Password</Button>
                </form>
            </Card>
        </div>
    );
}