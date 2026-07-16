import { Badge } from "../ui/badge";

const variants = {
    LOW: "bg-slate-100 text-slate-700 hover:bg-slate-100",
    MEDIUM: "bg-yellow-100 text-yellow-700 hover:bg-yellow-100",
    HIGH: "bg-orange-100 text-orange-700 hover:bg-orange-100",
    CRITICAL: "bg-red-100 text-red-700 hover:bg-red-100",
};

export default function PriorityBadge({ priority }) {
    return (
        <Badge className={variants[priority] ?? variants.LOW}>
            {priority}
        </Badge>
    );
}