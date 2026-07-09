import { useState } from "react";
import { useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router-dom";

import { register as registerUser } from "../api/authApi";

export default function Register() {
    const navigate = useNavigate();
    const [error, setError] = useState("");

    const {
        register,
        handleSubmit,
        formState: {
            errors,
            isSubmitting,
        },
    } = useForm();

    const onSubmit = async (data) => {
        try {
            setError("");
            await registerUser(data);
            navigate("/verify", {
                replace: true,
                state: {
                    email: data.email,
                },
            });

        } catch (err) {
            setError(
                err.response?.data?.message ??
                "Registration failed."
            );
        }
    };

    return (
        <div className="flex min-h-screen items-center justify-center bg-slate-100">
            <div className="w-full max-w-md rounded-lg bg-white p-8 shadow">
                <h1 className="mb-6 text-center text-3xl font-bold">Create Account</h1>
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                    <div>
                        <label className="mb-1 block">Username</label>
                        <input type="text" className="w-full rounded border p-2" {...register("username", {
                                required: "Username is required", minLength: {value: 3, message: "Minimum 3 characters",},
                            })}/>
                        {errors.username && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.username.message}
                            </p>
                        )}
                    </div>
                    <div>
                        <label className="mb-1 block">Email</label>
                        <input type="email" className="w-full rounded border p-2" {...register("email", { required: "Email is required",})}/>

                        {errors.email && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.email.message}
                            </p>
                        )}
                    </div>
                    <div>
                        <label className="mb-1 block">Password</label>
                        <input type="password" className="w-full rounded border p-2"{...register("password", {
                                required: "Password is required",
                                minLength: {
                                    value: 8,
                                    message: "Minimum 8 characters",
                                },
                            })}/>
                        {errors.password && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.password.message}
                            </p>
                        )}
                    </div>

                    {error && (
                        <p className="text-sm text-red-500">
                            {error}
                        </p>
                    )}
                    <button type="submit" disabled={isSubmitting} className="w-full rounded bg-blue-600 py-2 text-white hover:bg-blue-700 disabled:bg-gray-400">
                        {isSubmitting ? "Creating Account..." : "Register"}
                    </button>
                </form>

                <p className="mt-6 text-center">Already have an account?{" "}
                    <Link to="/login" className="text-blue-600 hover:underline">Login</Link>
                </p>
            </div>
        </div>
    );
}