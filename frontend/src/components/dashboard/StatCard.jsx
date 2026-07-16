import { Card } from "@/components/ui/card";

export default function StatCard({title, value, subtitle, icon: Icon, iconColor = "text-indigo-600"}) {
    return (
        <Card className="rounded-xl border shadow-sm transition-colors hover:border-slate-300">
            <div className="flex items-start justify-between p-6">
                <div>
                    <p className="text-sm font-medium text-slate-500">{title}</p>
                    <h3 className="mt-3 text-4xl font-bold tracking-tight text-slate-900">{value}</h3>
                    {subtitle && (
                        <p className="mt-2 text-sm text-slate-500">{subtitle}</p>
                    )}
                </div>

                {Icon && (
                    <div className={`rounded-lg bg-slate-100 p-3 ${iconColor}`}>
                        <Icon size={22} />
                    </div>
                )}
            </div>
        </Card>
    );
}