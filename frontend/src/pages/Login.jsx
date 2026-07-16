import { useState } from "react";
import { useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router-dom";

import { useAuth } from "../hooks/useAuth";
import AuthLayout from "../components/auth/AuthLayout";

import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";

export default function Login() {
    const navigate = useNavigate();
    const { login } = useAuth();

    const [error, setError] = useState("");

    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm();

    const onSubmit = async (data) => {
        try {
            setError("");
            await login(data);
            navigate("/dashboard");
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Invalid email or password."
            );
        }
    };

    return (
        <AuthLayout
            title="Welcome back"
            description="Sign in to access your IncidentOps workspace."
            footer={
                <>
                    Don't have an account?{" "}
                    <Link to="/register" className="font-medium text-indigo-600 hover:text-indigo-700">
                        Register
                    </Link>
                </>
            }
        >
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                <div className="space-y-2">
                    <Label htmlFor="email" className="font-medium">Email</Label>
                    <Input id="email" type="email" placeholder="name@example.com" className="h-11"
                        {...register("email", {
                            required: "Email is required",
                        })}
                    />

                    {errors.email && (
                        <p className="text-sm text-red-600">{errors.email.message}</p>
                    )}
                </div>

                <div className="space-y-2">
                    <div className="flex items-center justify-between">
                        <Label htmlFor="password" className="font-medium">Password</Label>
                        <Link to="/forgot-password" className="text-sm font-medium text-indigo-600 hover:text-indigo-700">
                            Forgot password?
                        </Link>
                    </div>
                    <Input id="password" type="password" {...register("password", { required: "Password is required",})}/>
                    {errors.password && (
                        <p className="text-sm text-red-600">{errors.password.message}</p>
                    )}
                </div>

                {error && (
                    <div className="rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">
                        {error}
                    </div>
                )}

                <Button type="submit" className="h-11 w-full" disabled={isSubmitting}>
                    {isSubmitting ? "Signing in..." : "Sign In"}
                </Button>
            </form>
        </AuthLayout>
    );
}