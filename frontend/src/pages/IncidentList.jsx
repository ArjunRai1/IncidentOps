import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getIncidents } from "../api/incidentApi";

import Button from "../components/common/Button";
import Loader from "../components/common/Loader";
import EmptyState from "../components/common/EmptyState";

import IncidentCard from "../components/incidents/IncidentCard";

export default function IncidentList() {
    const navigate = useNavigate();

    const [incidents, setIncidents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadIncidents();
    }, []);

    const loadIncidents = async () => {
        try {
            setLoading(true);
            setError("");

            const data = await getIncidents();

            setIncidents(data);
        } catch (err) {
            setError(
                err.response?.data?.message ??
                "Failed to load incidents."
            );
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <Loader />;
    }

    if (error) {
        return (
            <div className="rounded-lg border border-red-200 bg-red-50 p-4 text-red-700">
                {error}
            </div>
        );
    }

    return (
        <div className="space-y-6">
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-3xl font-bold">Incidents</h1>
                    <p className="mt-1 text-gray-500">View and manage all incidents.</p>
                </div>
                <Button onClick={() => navigate("/incidents/create")}>Create Incident</Button>
            </div>
            {incidents.length === 0 ? (
                <EmptyState
                    title="No Incidents"
                    message="Create your first incident to get started."
                />
            ) : (
                <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
                    {incidents.map((incident) => (
                        <IncidentCard key={incident.id}incident={incident}/>
                    ))}
                </div>
            )}
        </div>
    );
}