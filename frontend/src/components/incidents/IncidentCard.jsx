import { useNavigate } from "react-router-dom";
import Card from "../common/Card";
import Badge from "../common/Badge";
import {formatDate,getPriorityVariant,getStatusVariant} from "../../utils/formatters";

export default function IncidentCard({ incident }) {
    const navigate = useNavigate();

    return (
        <Card className="cursor-pointer transition-shadow hover:shadow-md" onClick={() => navigate(`/incidents/${incident.id}`)}>
            <div className="flex items-start justify-between gap-4">
                <div className="flex-1">
                    <h2 className="text-lg font-semibold text-gray-900">{incident.title}</h2>
                    <p className="mt-2 line-clamp-2 text-sm text-gray-600">{incident.description}</p>
                    <p className="mt-4 text-xs text-gray-500">Created {formatDate(incident.createdAt)}</p>
                </div>
                <div className="flex flex-col items-end gap-2">
                    <Badge variant={getStatusVariant(incident.status)}>
                        {incident.status}
                    </Badge>
                    <Badge variant={getPriorityVariant(incident.priority)}>
                        {incident.priority}
                    </Badge>
                </div>
            </div>
        </Card>
    );
}