import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";

import { createIncident } from "../api/incidentApi";

import Button from "../components/common/Button";
import Card from "../components/common/Card";
import Input from "../components/common/Input";

export default function CreateIncident() {
    const navigate = useNavigate();

    const [apiError, setApiError] = useState("");
    const [loading, setLoading] = useState(false);

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        defaultValues: {
            title: "",
            description: "",
            priority: "MEDIUM",
        },
    });

    const onSubmit = async (data) => {
        try {
            setLoading(true);
            setApiError("");

            const incident = await createIncident(data);

            navigate(`/incidents/${incident.id}`, {
                replace: true,
            });
        } catch (err) {
            setApiError(
                err.response?.data?.message ??
                "Failed to create incident."
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="mx-auto max-w-3xl">
            <Card>
                <h1 className="mb-2 text-3xl font-bold">Create Incident</h1>
                <p className="mb-8 text-gray-500">Report a new incident for investigation.</p>
                {apiError && (
                    <div className="mb-6 rounded-md border border-red-200 bg-red-50 px-4 py-3 text-red-700">
                        {apiError}
                    </div>
                )}
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                    <Input label="Title" placeholder="Database connection failure"
                        error={errors.title?.message}
                        {...register("title", {
                            required: "Title is required",
                        })}
                    />
                    <div>
                        <label className="mb-1 block text-sm font-medium text-gray-700">Description</label>
                        <textarea rows={6} placeholder="Describe the incident..."
                            className="w-full rounded-md border border-gray-300 p-3 focus:border-blue-500 focus:outline-none focus:ring-1 focus:ring-blue-500"
                            {...register("description", {
                                required: "Description is required",
                            })}
                        />
                        {errors.description && (
                            <p className="mt-1 text-sm text-red-500">
                                {errors.description.message}
                            </p>
                        )}
                    </div>
                    <div>
                        <label className="mb-1 block text-sm font-medium text-gray-700">Priority</label>
                        <select className="w-full rounded-md border border-gray-300 p-3 focus:border-blue-500 focus:outline-none focus:ring-1 focus:ring-blue-500"{...register("priority")}>
                            <option value="LOW">LOW</option>
                            <option value="MEDIUM">MEDIUM</option>
                            <option value="HIGH">HIGH</option>
                        </select>
                    </div>
                    <div className="flex justify-end gap-3">
                        <Button type="button" variant="secondary"onClick={() => navigate(-1)}>Cancel</Button>
                        <Button type="submit" loading={loading}>Create Incident</Button>
                    </div>
                </form>
            </Card>
        </div>
    );
}