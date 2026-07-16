import { useEffect, useState } from "react";
import { Brain, FileText } from "lucide-react";
import { getIncidentSummary } from "../../api/aiApi";

import { Skeleton } from "../ui/skeleton";

const IncidentSummary = ({ incidentId }) => {
    const [summary, setSummary] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        if (!incidentId) {
            return;
        }

        const fetchSummary = async () => {
            setLoading(true);
            setError("");

            try {
                const response = await getIncidentSummary(incidentId);
                setSummary(response.summary);
            } catch {
                setError("Unable to generate incident summary.");
            } finally {
                setLoading(false);
            }
        };

        fetchSummary();
    }, [incidentId]);

    if (loading) {
        return (
            <div className="space-y-3">
                    <Skeleton className="h-4 w-full" />
                    <Skeleton className="h-4 w-full" />
                    <Skeleton className="h-4 w-5/6" />
                    <Skeleton className="h-4 w-3/4" />
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

    if (!summary?.trim()) {
        return (
            <div className="flex flex-col items-center justify-center py-12 text-center">
                    <Brain className="mb-4 h-10 w-10 text-slate-400" />
                    <h3 className="text-lg font-semibold">No Summary Available</h3>
                    <p className="mt-2 max-w-md text-sm text-muted-foreground">The AI could not generate a summary for this incident yet.</p>
                </div>
        );
    }

    return (
        <p className="whitespace-pre-line leading-8 text-slate-700">{summary}</p>
    );
};

export default IncidentSummary;