import { Card } from "../ui/card";

export default function FormSection({
    title,
    description,
    children,
}) {
    return (
        <Card className="rounded-xl border shadow-sm">
            <div className="border-b px-6 py-4">
                <h2 className="text-lg font-semibold text-slate-900">{title}</h2>
                {description && (
                    <p className="mt-1 text-sm text-slate-500">{description}</p>
                )}
            </div>
            <div className="space-y-6 p-6">
                {children}
            </div>
        </Card>
    );
}