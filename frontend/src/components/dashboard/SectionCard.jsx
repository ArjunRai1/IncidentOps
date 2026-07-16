import { Card } from "../ui/card";

export default function SectionCard({title, description, children, className = "",}) {
    return (
        <Card className={`rounded-xl border shadow-sm ${className}`}>
            <div className="border-b px-6 py-4">
                <h2 className="text-lg font-semibold">{title}
                </h2>
                {description && (
                    <p className="mt-1 text-sm text-slate-500">{description}</p>
                )}
            </div>
            <div className="p-6">
                {children}
            </div>
        </Card>
    );
}