import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { getIncident, updateIncident } from "../api/incidentApi";
import { analyzeIncident, uploadLog } from "../api/aiApi";

import Loader from "../components/common/Loader";
import StatusBadge from "../components/common/StatusBadge";
import PriorityBadge from "../components/common/PriorityBadge";

import PageHeader from "../components/dashboard/PageHeader";
import SectionCard from "../components/dashboard/SectionCard";

import CommentsSection from "../components/incidents/CommentSection";
import TimelineSection from "../components/incidents/TimelineSection";

import IncidentSummary from "../components/ai/IncidentSummary";
import SimilarIncidents from "../components/ai/SimilarIncidents";
import AnalysisCard from "../components/ai/AnalysisCard";
import LogUpload from "../components/ai/LogUpload";

import { Button } from "../components/ui/button";
import { Card } from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { Textarea } from "../components/ui/textarea";

import { formatDate } from "../utils/formatters";

export default function IncidentDetails() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [incident, setIncident] = useState(null);
    const [formData, setFormData] = useState(null);

    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    const [editing, setEditing] = useState(false);

    const [error, setError] = useState("");

    const [analysis, setAnalysis] = useState(null);
    const [analysisLoading, setAnalysisLoading] = useState(false);

    useEffect(() => {
        const loadData = async () => {
            await Promise.all([
                loadIncident(),
                loadAnalysis(),
            ]);
        };

        loadData();
    }, [id]);

    const loadIncident = async () => {
        try {
            setLoading(true);
            setError("");

            const data = await getIncident(id);
            setIncident(data);
            setFormData({
                title: data.title,
                description: data.description,
                priority: data.priority,
                status: data.status,
            });
        } catch (err) {
            setError(
                err.response?.data?.message ??
                    "Failed to load incident."
            );
        } finally {
            setLoading(false);
        }
    };

    const loadAnalysis = async () => {
        try {
            setAnalysisLoading(true);
            const response = await analyzeIncident(id);
            setAnalysis(response);
        } finally {
            setAnalysisLoading(false);
        }
    };

    const handleLogUpload = async (file) => {
        await uploadLog(id, file);
        await loadAnalysis();
    };

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData((previous) => ({...previous,[name]: value}));
    };

    const hasChanges =
        JSON.stringify({
            title: incident?.title,
            description: incident?.description,
            priority: incident?.priority,
            status: incident?.status,
        }) !== JSON.stringify(formData);

    const handleSave = async () => {
        try {
            setSaving(true);
            const updated = await updateIncident(
                id,
                formData
            );
            setIncident(updated);
            setEditing(false);
        } catch (err) {
            setError(
                err.response?.data?.message ??
                    "Failed to update incident."
            );
        } finally {
            setSaving(false);
        }
    };

    const handleCancel = () => {
        setFormData({
            title: incident.title,
            description: incident.description,
            priority: incident.priority,
            status: incident.status,
        });

        setEditing(false);
    };

    if (loading) {
        return <Loader />;
    }

    if (error && !incident) {
        return (
            <div className="rounded-xl border border-red-200 bg-red-50 p-4 text-red-700">
                {error}
            </div>
        );
    }

    return (
        <div className="mx-auto max-w-7xl space-y-8">
            <PageHeader title="Incident Details" description="View, manage and investigate this incident."
                actions={
                    !editing ? (
                        <div className="flex gap-3">
                            <Button variant="outline" onClick={() => navigate(-1)}>Back</Button>
                            <Button onClick={() => setEditing(true)}>Edit Incident</Button>
                        </div>
                    ) : (
                        <div className="flex gap-3">
                            <Button variant="outline" onClick={handleCancel}>Cancel</Button>
                            <Button disabled={!hasChanges} loading={saving} onClick={handleSave}>Save Changes</Button>
                        </div>
                    )
                }
            />

            {error && (
                <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
                    {error}
                </div>
            )}

            <SectionCard title="Incident Information" description="Core incident information and current status.">
                {!editing ? (
                    <div className="grid gap-8 lg:grid-cols-[2fr_320px]">
                        <div>
                            <h1 className="text-3xl font-bold tracking-tight">{incident.title}</h1>
                            <p className="mt-6 whitespace-pre-wrap leading-7 text-slate-600">{incident.description}</p>
                        </div>
                        <Card className="rounded-xl border shadow-none">
                            <div className="space-y-6 p-6">
                                <div>
                                    <p className="mb-2 text-sm text-slate-500">Status</p>
                                    <StatusBadge status={incident.status}/>
                                </div>

                                <div>
                                    <p className="mb-2 text-sm text-slate-500">Priority</p>
                                    <PriorityBadge priority={incident.priority}/>
                                </div>

                                <div>
                                    <p className="text-sm text-slate-500">Created</p>
                                    <p className="mt-1 text-sm font-medium">
                                        {formatDate(incident.createdAt)}
                                    </p>
                                </div>
                            </div>
                        </Card>
                    </div>
                ) : (
                    <div className="space-y-6">
                        <div className="space-y-2">
                            <Label htmlFor="title">Title</Label>
                            <Input id="title" name="title" value={formData.title} onChange={handleChange}className="h-11"/>
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="description">Description</Label>
                            <Textarea id="description" name="description" rows={8}
                                value={
                                    formData.description
                                }
                                onChange={handleChange}
                            />
                        </div>

                        <div className="grid gap-6 md:grid-cols-2">
                            <div className="space-y-2">
                                <Label htmlFor="priority">Priority</Label>
                                <select id="priority" name="priority" value={formData.priority} onChange={handleChange} className="flex h-11 w-full rounded-md border border-input bg-background px-3 py-2 text-sm shadow-sm transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring">
                                    <option value="LOW">Low</option>
                                    <option value="MEDIUM">Medium</option>
                                    <option value="HIGH">High</option>
                                </select>
                            </div>
                            <div className="space-y-2">
                                <Label htmlFor="status">Status</Label>
                                <select id="status" name="status" value={formData.status} onChange={handleChange} className="flex h-11 w-full rounded-md border border-input bg-background px-3 py-2 text-sm shadow-sm transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring">
                                    <option value="OPEN">Open</option>
                                    <option value="IN_PROGRESS">In Progress</option>
                                    <option value="RESOLVED">Resolved</option>
                                </select>
                            </div>
                        </div>
                    </div>
                )}
            </SectionCard>
            <SectionCard title="Logs" description="Upload server or application logs for AI analysis.">
                <LogUpload onUpload={handleLogUpload} />
            </SectionCard>

            <SectionCard title="AI Summary" description="Automatically generated summary of the incident." >
                <IncidentSummary incidentId={incident.id}/>
            </SectionCard>

            <SectionCard title="Root Cause Analysis" description="AI-generated analysis based on incident data and uploaded logs.">
                <AnalysisCard analysis={analysis} loading={analysisLoading}/>
            </SectionCard>

            <SectionCard title="Similar Incidents" description="Incidents with related context and symptoms.">
                <SimilarIncidents incidentId={incident.id}/>
            </SectionCard>

            <div className="grid gap-6 lg:grid-cols-2">
                <SectionCard title="Comments" description="Discussion related to this incident.">
                    <CommentsSection incidentId={id}/>
                </SectionCard>

                <SectionCard title="Timeline" description="Chronological history of incident activity.">
                    <TimelineSection incidentId={id}/>
                </SectionCard>
            </div>
        </div>
    );
}