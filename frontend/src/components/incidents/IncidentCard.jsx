import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { CalendarDays, ArrowRight } from "lucide-react";
import { useNavigate } from "react-router-dom";

import StatusBadge from "../common/StatusBadge";
import PriorityBadge from "../common/PriorityBadge";

export default function IncidentCard({ incident }) {
    const navigate = useNavigate();
    return (
        <Card className="flex h-full flex-col rounded-xl border shadow-sm transition-colors hover:border-slate-300">
            <div className="flex flex-1 flex-col p-6">
                <div className="space-y-3">
                    <h3 className="line-clamp-1 text-lg font-semibold">{incident.title}</h3>
                    <p className="line-clamp-3 text-sm leading-6 text-slate-600">{incident.description}</p>
                </div>

                <div className="mt-6 flex flex-wrap gap-2">
                    <StatusBadge status={incident.status} />
                    <PriorityBadge priority={incident.priority} />
                </div>

                <div className="mt-6 flex items-center gap-2 text-sm text-slate-500">
                    <CalendarDays size={16} />
                    {new Date(incident.createdAt).toLocaleDateString()}
                </div>

                <div className="mt-auto pt-6">
                    <Button variant="outline" className="w-full justify-between" onClick={() => navigate(`/incidents/${incident.id}`)}>
                        View Details
                        <ArrowRight size={16} />
                    </Button>
                </div>
            </div>
        </Card>
    );
}