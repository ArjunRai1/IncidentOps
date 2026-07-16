import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Plus, Search } from "lucide-react";

import { getIncidents } from "../api/incidentApi";

import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "../components/ui/select";

import Loader from "../components/common/Loader";
import EmptyState from "../components/common/EmptyState";
import IncidentCard from "../components/incidents/IncidentCard";

import PageHeader from "../components/dashboard/PageHeader";
import SectionCard from "../components/dashboard/SectionCard";

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
            setFilters((prev) => ({
                ...prev,
                title: search,
            }));
        }, 400);

        return () => clearTimeout(timer);
    }, [search]);

    const loadIncidents = async () => {
        try{
            setLoading(true);
            setError("");

            const response = await getIncidents({
                page,
                size: 10,
                ...filters,
            });

            setIncidents(response.content);
            setTotalPages(response.totalPages);
        } catch(err){
            setError(
                err.response?.data?.message ??
                "Failed to load incidents."
            );
        } finally {
            setLoading(false);
        }
    };

    if(loading)
        return <Loader />;

    if(error){
        return (
            <div className="rounded-xl border border-red-200 bg-red-50 p-4 text-red-700">
                {error}
            </div>
        );
    }

    return (
        <div className="space-y-8">
            <PageHeader
                title="Incidents"
                description="Search, filter and manage incidents across your workspace."
                actions={
                    <Button onClick={() => navigate("/incidents/create")}>
                        <Plus className="mr-2 h-4 w-4" />
                        Create Incident
                    </Button>
                }
            />

            <SectionCard title="Filters" description="Refine incidents using search and filters.">
                <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-5">
                    <div className="relative">
                        <Search className="absolute left-3 top-3.5 h-4 w-4 text-slate-400" />
                        <Input className="pl-9" placeholder="Search title..." value={search} onChange={(e) => setSearch(e.target.value)}/>
                    </div>
                    <Select value={filters.status || "ALL"} onValueChange={(value) => {
                            setPage(0);
                            setFilters((prev) => ({...prev, status: value === "ALL" ? "" : value}));
                        }}
                    >
                        <SelectTrigger>
                            <SelectValue placeholder="Status" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="ALL">All Status</SelectItem>
                            <SelectItem value="OPEN">Open</SelectItem>
                            <SelectItem value="IN_PROGRESS">In Progress</SelectItem>
                            <SelectItem value="RESOLVED">Resolved</SelectItem>
                            <SelectItem value="CLOSED">Closed</SelectItem>
                        </SelectContent>
                    </Select>

                    <Select value={filters.priority || "ALL"} onValueChange={(value) => {
                            setPage(0);
                            setFilters((prev) => ({...prev, priority: value === "ALL" ? "" : value}));
                        }}
                    >
                        <SelectTrigger>
                            <SelectValue placeholder="Priority" />
                        </SelectTrigger>

                        <SelectContent>
                            <SelectItem value="ALL">All Priority</SelectItem>
                            <SelectItem value="LOW">Low</SelectItem>
                            <SelectItem value="MEDIUM">Medium</SelectItem>
                            <SelectItem value="HIGH">High</SelectItem>
                            <SelectItem value="CRITICAL">Critical</SelectItem>
                        </SelectContent>
                    </Select>

                    <Select value={filters.sortBy} onValueChange={(value) => {
                            setPage(0);
                            setFilters((prev) => ({...prev, sortBy: value}));
                        }}
                    >
                        <SelectTrigger>
                            <SelectValue />
                        </SelectTrigger>

                        <SelectContent>
                            <SelectItem value="createdAt">Created</SelectItem>
                            <SelectItem value="updatedAt">Updated</SelectItem>
                            <SelectItem value="priority">Priority</SelectItem>
                            <SelectItem value="status">Status</SelectItem>
                            <SelectItem value="title">Title</SelectItem>
                        </SelectContent>
                    </Select>

                    <Select value={filters.direction} onValueChange={(value) => {
                            setPage(0);
                            setFilters((prev) => ({...prev, direction: value}));
                        }}
                    >
                        <SelectTrigger>
                            <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="DESC">Newest First</SelectItem>
                            <SelectItem value="ASC">Oldest First</SelectItem>
                        </SelectContent>
                    </Select>
                </div>
            </SectionCard>

            {incidents.length === 0 ? (
                <EmptyState title="No incidents found" message="Create your first incident to start tracking operational issues."/>
            ) : (
                <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
                    {incidents.map((incident) => (
                        <IncidentCard key={incident.id} incident={incident}/>
                    ))}
                </div>
            )}
            <div className="flex items-center justify-center gap-3 pt-2">
                <Button variant="outline" disabled={page === 0} onClick={() => setPage((p) => p - 1)}>
                    Previous
                </Button>

                <span className="text-sm text-slate-500">
                    {totalPages === 0
                        ? "No results"
                        : `Page ${page + 1} of ${totalPages}`}
                </span>

                <Button variant="outline" disabled={page + 1 >= totalPages} onClick={() => setPage((p) => p + 1)}>
                    Next
                </Button>
            </div>
        </div>
    );
}