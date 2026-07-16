export default function InfoRow({ label, value }) {
    return (
        <div className="flex items-start justify-between gap-4 py-3 border-b last:border-b-0">
            <span className="text-sm font-medium text-slate-500">{label}</span>
            <span className="text-sm text-slate-900 text-right break-all">{value ?? "-"}</span>
        </div>
    );
}