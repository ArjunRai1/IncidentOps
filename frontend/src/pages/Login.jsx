import { useState } from "react";
import { useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

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
        <div className="min-h-screen flex items-center justify-center bg-slate-100">
            <div className="w-full max-w-md rounded-lg bg-white p-8 shadow">
                <h1 className="mb-6 text-center text-3xl font-bold">IncidentOps</h1>
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                    <div>
                        <label className="mb-1 block">Email</label>
                        <input type="email" className="w-full rounded border p-2" {...register("email", { required: "Email is required", })}/>
                        {errors.email && (<p className="mt-1 text-sm text-red-500">{errors.email.message}</p>
                        )}
                    </div>
                    <div>
                        <label className="mb-1 block">Password</label>
                        <input type="password" className="w-full rounded border p-2" {...register("password", { required: "Password is required", })}/>
                        {errors.password && ( <p className="mt-1 text-sm text-red-500"> {errors.password.message}</p>
                        )}
                    </div>
                    {error && (
                        <p className="text-sm text-red-500">{error}</p>
                    )}
                    <button type="submit" disabled={isSubmitting} className="w-full rounded bg-blue-600 py-2 text-white hover:bg-blue-700 disabled:bg-gray-400">
                        {isSubmitting ? "Logging in..." : "Login"}
                    </button>
                </form>
                <p className="mt-6 text-center">Don't have an account?{" "}
                    <Link to="/register" className="text-blue-600 hover:underline">Register</Link>
                </p>
            </div>
        </div>
    );
}