import { Badge } from "../ui/badge";

const variants = {
    OPEN: "bg-blue-100 text-blue-700 hover:bg-blue-100",
    IN_PROGRESS: "bg-amber-100 text-amber-700 hover:bg-amber-100",
    RESOLVED: "bg-emerald-100 text-emerald-700 hover:bg-emerald-100",
    CLOSED: "bg-slate-200 text-slate-700 hover:bg-slate-200",
};

export default function StatusBadge({ status }) {
    return (
        <Badge className={variants[status] ?? variants.OPEN}>
            {status.replace("_", " ")}
        </Badge>
    );
}