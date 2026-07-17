import { useEffect, useState } from "react";

import { getTimeline } from "../../api/timeLineApi";

import { Card } from "../ui/card";
import Loader from "../common/Loader";
import EmptyState from "../common/EmptyState";

import { formatDate } from "../../utils/formatters";

export default function TimelineSection({ incidentId }) {
    const [events, setEvents] = useState([]);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadTimeline();
    }, [incidentId]);

    const loadTimeline = async () => {
        try {
            setLoading(true);
            setError("");

            const data = await getTimeline(incidentId);

            setEvents(data);
        } catch (err) {
            setError(
                err.response?.data?.message ??
                "Failed to load timeline."
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="space-y-4">
            {loading && <Loader />}

            {!loading && error && (
                <div className="rounded-md border border-red-200 bg-red-50 p-4 text-red-700">
                    {error}
                </div>
            )}

            {!loading &&
                !error &&
                events.length === 0 && (
                    <div className="py-8">
                        <EmptyState title="No Timeline" message="No activity has been recorded yet."/>
                    </div>
                )}

            <div className="space-y-4">

                {events.map((event) => (
                    <Card key={event.id} className="rounded-xl border shadow-none p-4">
                        <div className="flex flex-wrap items-start justify-between gap-3">
                            <span className="font-semibold">{event.eventType}</span>
                            <span className="text-sm text-gray-500">{formatDate(event.createdAt)}</span>
                        </div>
                        {event.description && (
                            <p className="mt-3 text-gray-700">{event.description}</p>
                        )}
                    </Card>
                ))}
            </div>
        </div>
    );
}