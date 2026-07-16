import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";

import { createIncident } from "../api/incidentApi";

import { Button } from "../components/ui/button";
import { Card } from "../components/ui/card";

import { Label } from "../components/ui/label";
import { Input } from "../components/ui/input";
import { Textarea } from "../components/ui/textarea";
import PageHeader from "../components/dashboard/PageHeader";
import FormSection from "../components/common/FormSection";

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
        <div className="mx-auto max-w-5xl space-y-8">
            <PageHeader title="Create Incident" description="Record a new incident for investigation and tracking."/>

            {apiError && (
                <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
                    {apiError}
                </div>
            )}

            <form onSubmit={handleSubmit(onSubmit)}className="space-y-8">
                <FormSection title="Incident Information" description="Provide the details required to create a new incident.">
                    <div className="space-y-2">
                        <Label htmlFor="title">Title</Label>
                        <Input id="title" placeholder="Enter incident title..." className="h-11"
                            {...register("title", {
                                required: "Title is required",
                            })}
                        />

                        {errors.title && (
                            <p className="text-sm text-red-600">{errors.title.message}</p>
                        )}
                    </div>

                    <div className="space-y-2">
                        <Label htmlFor="description">Description</Label>
                        <Textarea id="description" rows={7} placeholder="Describe the incident, its impact, observed symptoms, and any relevant context..."
                            {...register("description", {
                                required: "Description is required",})}
                        />

                        {errors.description && (
                            <p className="text-sm text-red-600">{errors.description.message}</p>
                        )}

                    </div>

                    <div className="max-w-sm space-y-2">
                        <Label htmlFor="priority">Priority</Label>
                        <select id="priority" className="flex h-11 w-full rounded-md border border-input bg-background px-3 py-2 text-sm shadow-xs transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                            {...register("priority")}
                        >
                            <option value="LOW">Low</option>
                            <option value="MEDIUM">Medium</option>
                            <option value="HIGH">High</option>
                        </select>

                    </div>

                </FormSection>

                <div className="flex justify-end gap-3">
                    <Button type="button" variant="outline" onClick={() => navigate(-1)}>Cancel</Button>

                    <Button type="submit" disabled={loading}>
                        {loading ? "Creating..." : "Create Incident"}
                    </Button>
                </div>
            </form>
        </div>
    );
}