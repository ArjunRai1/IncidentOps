import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { verifyOtp } from "../api/authApi";

export default function VerifyOtp() {
    const navigate = useNavigate();
    const location = useLocation();

    const email = location.state?.email;
    const [error, setError] = useState("");

    const {
        register,
        handleSubmit,
        formState: {
            errors,
            isSubmitting,
        },
    } = useForm();

    useEffect(() => {
        if (!email) {
            navigate("/register", { replace: true });
        }
    }, [email, navigate]);

    const onSubmit = async (data) => {
        try {
            setError("");

            await verifyOtp({
                email,
                otp: data.otp,
            });

            navigate("/login", {
                replace: true,
            });

        } catch (err) {
            setError(
                err.response?.data?.message ??
                "OTP verification failed."
            );
        }
    };

    if (!email) {
        return null;
    }

    return (
        <div className="flex min-h-screen items-center justify-center bg-slate-100">
            <div className="w-full max-w-md rounded-lg bg-white p-8 shadow">
                <h1 className="mb-2 text-center text-3xl font-bold">Verify OTP</h1>
                <p className="mb-6 text-center text-sm text-slate-600">Enter the OTP sent to
                    <br />
                    <strong>{email}</strong>
                </p>
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                    <div>
                        <label className="mb-1 block">OTP</label>
                        <input type="text" className="w-full rounded border p-2" placeholder="Enter 6-digit OTP"
                            {...register("otp", {
                                required: "OTP is required",
                                minLength: {
                                    value: 6,
                                    message: "OTP must be 6 digits",
                                },
                                maxLength: {
                                    value: 6,
                                    message: "OTP must be 6 digits",
                                },
                            })}/>

                        {errors.otp && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.otp.message}
                            </p>
                        )}
                    </div>
                    {error && (
                        <p className="text-sm text-red-500">
                            {error}
                        </p>
                    )}
                    <button type="submit" disabled={isSubmitting} className="w-full rounded bg-blue-600 py-2 text-white hover:bg-blue-700 disabled:bg-gray-400">
                        {isSubmitting
                            ? "Verifying..."
                            : "Verify OTP"}
                    </button>
                </form>
                <p className="mt-6 text-center text-sm">
                    Wrong email?{" "}
                    <Link to="/register" className="text-blue-600 hover:underline">Register Again</Link>
                </p>
            </div>
        </div>
    );
}