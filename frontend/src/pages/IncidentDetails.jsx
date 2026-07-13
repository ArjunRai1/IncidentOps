import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {getIncident, updateIncident} from "../api/incidentApi";

import Button from "../components/common/Button";
import Card from "../components/common/Card";
import Loader from "../components/common/Loader";
import Badge from "../components/common/Badge";

import CommentsSection from "../components/incidents/CommentSection";
import TimelineSection from "../components/incidents/TimelineSection";

import IncidentSummary from "../components/ai/IncidentSummary";
import SimilarIncidents from "../components/ai/SimilarIncidents";

import {formatDate, getPriorityVariant, getStatusVariant} from "../utils/formatters";

export default function IncidentDetails() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [incident, setIncident] = useState(null);
    const [formData, setFormData] = useState(null);

    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    const [editing, setEditing] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        loadIncident();
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

    const handleChange = (event) => {
        const { name, value } = event.target;

        setFormData((previous) => ({
            ...previous,
            [name]: value,
        }));
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

            const updated = await updateIncident(id, formData);

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
            <div className="rounded-md border border-red-200 bg-red-50 p-4 text-red-700">
                {error}
            </div>
        );
    }

    return (
        <div className="space-y-6">
            <div className="flex items-center justify-between">
                <Button variant="secondary" onClick={() => navigate(-1)}>Back</Button>

                {!editing ? (
                    <Button onClick={() => setEditing(true)}>Edit Incident</Button>
                ) : (
                    <div className="flex gap-3">
                        <Button variant="secondary" onClick={handleCancel}>Cancel</Button>
                        <Button loading={saving} disabled={!hasChanges} onClick={handleSave}>Save</Button>
                    </div>
                )}
            </div>
            {error && (
                <div className="rounded-md border border-red-200 bg-red-50 p-4 text-red-700">
                    {error}
                </div>
            )}

            <Card>
                {!editing ? (
                    <>
                        <div className="flex items-start justify-between">
                            <div>
                                <h1 className="text-3xl font-bold">{incident.title}</h1>
                                <p className="mt-2 text-gray-600">{incident.description}</p>
                            </div>

                            <div className="flex flex-col gap-2">
                                <Badge variant={getPriorityVariant(incident.priority)}>
                                    {incident.priority}
                                </Badge>
                                <Badge variant={getStatusVariant(incident.status)}>
                                    {incident.status}
                                </Badge>
                            </div>
                        </div>

                        <div className="mt-8 grid gap-4 md:grid-cols-2">
                            <div>
                                <p className="text-sm font-medium text-gray-500">Created</p>
                                <p className="mt-1">{formatDate(incident.createdAt)}</p>
                            </div>
                        </div>
                    </>
                ) : (
                    <div className="space-y-6">
                        <div>
                            <label className="mb-1 block text-sm font-medium">Title</label>
                            <input name="title" value={formData.title} onChange={handleChange}className="w-full rounded-md border border-gray-300 p-2 focus:border-blue-500 focus:outline-none"/>
                        </div>

                        <div>
                            <label className="mb-1 block text-sm font-medium">Description</label>
                            <textarea rows={6} name="description" value={formData.description} onChange={handleChange}className="w-full rounded-md border border-gray-300 p-2 focus:border-blue-500 focus:outline-none"/>
                        </div>

                        <div className="grid gap-6 md:grid-cols-2">
                            <div>
                                <label className="mb-1 block text-sm font-medium">Priority</label>
                                <select name="priority" value={formData.priority} onChange={handleChange}className="w-full rounded-md border border-gray-300 p-2">
                                    <option value="LOW">LOW</option>
                                    <option value="MEDIUM">MEDIUM</option>
                                    <option value="HIGH">HIGH</option>
                                </select>
                            </div>
                            <div>
                                <label className="mb-1 block text-sm font-medium">Status</label>
                                <select name="status" value={formData.status} onChange={handleChange}className="w-full rounded-md border border-gray-300 p-2">
                                    <option value="OPEN">OPEN</option>
                                    <option value="IN_PROGRESS">IN PROGRESS</option>
                                    <option value="RESOLVED">RESOLVED</option>
                                </select>
                            </div>
                        </div>
                    </div>
                )}
            </Card>
            <IncidentSummary incidentId={incident.id} />
            <SimilarIncidents incidentId={incident.id} />
            <div className="grid gap-6 lg:grid-cols-2">
                <CommentsSection incidentId={id} />
                <TimelineSection incidentId={id} />
            </div>
        </div>
    );
}