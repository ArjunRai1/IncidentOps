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

    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const [filters, setFilters] = useState({
        title: "",
        status: "",
        priority: "",
        sortBy: "createdAt",
        direction: "DESC",
    });

    const [search, setSearch] = useState("");

    useEffect(() => {
        loadIncidents();
    }, [page, filters]);

    useEffect(() => {
        const timer = setTimeout(() => {
            setPage(0);
            setFilters((previous) => ({...previous, title: search,}));}, 400);
        return () => clearTimeout(timer);
    }, [search]);

    const loadIncidents = async () => {
        try {
            setLoading(true);
            setError("");

            const response = await getIncidents({
                page,
                size: 10,
                ...filters,
            });

            setIncidents(response.content);
            setTotalPages(response.totalPages);

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
        <>
        <div className="mb-6 grid gap-4 md:grid-cols-5">
            <input type="text" placeholder="Search title..." value={search} onChange={(e) => setSearch(e.target.value)} className="rounded-md border border-gray-300 p-2"/>
            <select value={filters.status} onChange={(e) => {setPage(0); setFilters((prev) => ({...prev, status: e.target.value,}));}} className="rounded-md border border-gray-300 p-2">
                <option value="">All Status</option>
                <option value="OPEN">OPEN</option>
                <option value="IN_PROGRESS">IN_PROGRESS</option>
                <option value="RESOLVED">RESOLVED</option>
                <option value="CLOSED">CLOSED</option>
            </select>

            <select value={filters.priority} onChange={(e) => {setPage(0); setFilters((prev) => ({...prev,priority: e.target.value,}));}} className="rounded-md border border-gray-300 p-2">
                <option value="">All Priority</option>
                <option value="LOW">LOW</option>
                <option value="MEDIUM">MEDIUM</option>
                <option value="HIGH">HIGH</option>
                <option value="CRITICAL">CRITICAL</option>
            </select>

            <select value={filters.sortBy} onChange={(e) => {setPage(0); setFilters((prev) => ({...prev, sortBy: e.target.value,}));}} className="rounded-md border border-gray-300 p-2">
                <option value="createdAt">Created</option>
                <option value="updatedAt">Updated</option>
                <option value="priority">Priority</option>
                <option value="status">Status</option>
                <option value="title">Title</option>
            </select>

            <select value={filters.direction} onChange={(e) => {setPage(0); setFilters((prev) => ({...prev, direction: e.target.value,}));}} className="rounded-md border border-gray-300 p-2">
                <option value="DESC">Newest First</option>
                <option value="ASC">Oldest First</option>
            </select>
        </div>
        <div className="space-y-6">
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-3xl font-bold">Incidents</h1>
                    <p className="mt-1 text-gray-500">View and manage all incidents.</p>
                </div>
                <Button onClick={() => navigate("/incidents/create")}>Create Incident</Button>
            </div>
            {incidents.length === 0 ? (
                <EmptyState title="No Incidents" message="Create your first incident to get started."/>) : (
                <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
                    {incidents.map((incident) => (
                        <IncidentCard key={incident.id}incident={incident}/>
                    ))}
                </div>
            )}
        </div>
        <div className="mt-8 flex items-center justify-center gap-4">
            <button disabled={page === 0} onClick={() => setPage((p) => p - 1)} className="rounded border px-4 py-2 disabled:opacity-50">Previous</button>
            <span>{totalPages === 0 ? "No results" : `Page ${page + 1} of ${totalPages}`}</span>
            <button disabled={page + 1 >= totalPages} onClick={() => setPage((p) => p + 1)} className="rounded border px-4 py-2 disabled:opacity-50">Next</button>
        </div>
        </>
    );
}