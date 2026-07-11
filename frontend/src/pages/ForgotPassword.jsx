import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import { forgotPassword } from "../api/authApi";

import Card from "../components/common/Card";
import Button from "../components/common/Button";

export default function ForgotPassword() {
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            setLoading(true);
            setError("");

            await forgotPassword({ email });

            navigate("/reset-password", {
                state: { email },
                replace: true,
            });
        } catch (err) {
            setError(
                err.response?.data?.message ??
                "Failed to send OTP."
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex min-h-screen items-center justify-center bg-slate-100 px-4">
            <Card className="w-full max-w-md">
                <h1 className="mb-2 text-3xl font-bold">Forgot Password</h1>
                <p className="mb-6 text-gray-500">Enter your registered email.</p>
                {error && (
                    <div className="mb-4 rounded border border-red-200 bg-red-50 p-3 text-red-600">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-4">
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" required className="w-full rounded border p-3"/>
                    <Button type="submit" loading={loading} className="w-full">Send OTP</Button>
                </form>

                <div className="mt-6 text-center">
                    <Link to="/login" className="text-blue-600 hover:underline">Back to Login</Link>
                </div>
            </Card>
        </div>
    );
}