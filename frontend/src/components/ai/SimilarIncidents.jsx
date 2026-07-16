import { useEffect, useState } from "react";
import {Clock3, Search} from "lucide-react";

import { getSimilarIncidents } from "../../api/aiApi";

import { Skeleton } from "../ui/skeleton";

import StatusBadge from "../common/StatusBadge";
import PriorityBadge from "../common/PriorityBadge";
import { formatDate } from "../../utils/formatters";

const SimilarIncidents = ({ incidentId }) => {
    const [incidents, setIncidents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        if (!incidentId) {
            return;
        }

        const fetchSimilarIncidents = async () => {
            setLoading(true);
            setError("");

            try {
                const response = await getSimilarIncidents(incidentId);
                setIncidents(response);
            } catch{
                setError("Unable to load similar incidents.");
            } finally {
                setLoading(false);
            }
        };

        fetchSimilarIncidents();
    }, [incidentId]);

    if (loading) {
        return (
            <div className="space-y-4">
                    {[1, 2, 3].map((item) => (
                        <div key={item} className="space-y-3 rounded-xl border p-5">
                            <Skeleton className="h-5 w-2/3" />
                            <div className="flex gap-2">
                                <Skeleton className="h-6 w-20" />
                                <Skeleton className="h-6 w-24" />
                            </div>
                            <Skeleton className="h-4 w-36" />
                        </div>
                    ))}
                </div>
        );
    }

    if (error) {
        return (
            <div className="rounded-lg border border-destructive/20 bg-destructive/10 px-4 py-3 text-sm text-destructive">
                    {error}
            </div>
        );
    }

    if (incidents.length === 0) {
        return (
                <div className="flex flex-col items-center justify-center py-12 text-center">
                    <Search className="mb-4 h-10 w-10 text-slate-400" />
                    <h3 className="text-lg font-semibold">No Similar Incidents</h3>
                    <p className="mt-2 max-w-md text-sm text-muted-foreground">No previously reported incidents closely match this incident.</p>
                </div>
            );
    }

    return (
        <div className="space-y-4">
                {incidents.map((incident) => (
                    <div key={incident.id} className="rounded-xl border transition-colors hover:bg-muted/40">
                        <div className="space-y-4 p-5">
                            <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
                                <div className="min-w-0 flex-1">
                                    <h3 className="truncate text-base font-semibold">
                                        {incident.title}
                                    </h3>
                                </div>

                                <div className="flex flex-wrap gap-2">
                                    <StatusBadge status={incident.status} />
                                    <PriorityBadge priority={incident.priority} />
                                </div>
                            </div>

                            {incident.createdAt && (
                                <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                    <Clock3 className="h-4 w-4" />
                                    <span>{formatDate(incident.createdAt)}</span>
                                </div>
                            )}
                        </div>
                    </div>
                ))}
            </div>
    );
};

export default SimilarIncidents;