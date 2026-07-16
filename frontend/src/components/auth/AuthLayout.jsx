import { Card, CardContent } from "@/components/ui/card";

export default function AuthLayout({title, description, children, footer}) {
    return (
        <div className="min-h-screen bg-slate-50">
            <div className="mx-auto flex min-h-screen max-w-6xl flex-col items-center justify-center px-6 py-16">
                <div className="mb-8 text-center">
                    <h1 className="text-4xl font-bold tracking-tight text-slate-900">IncidentOps</h1>
                    <p className="mt-2 text-lg text-slate-600">Incident Management Platform</p>
                    <p className="mx-auto mt-2 max-w-xl text-slate-500">Track incidents, collaborate with your team,
                        and resolve operational issues efficiently.
                    </p>
                </div>

                <Card className="w-full max-w-lg border-slate-200 shadow-lg">
                    <CardContent className="p-8">
                        <div className="mb-8 text-center">
                            <h2 className="text-2xl font-semibold">{title}</h2>
                            {description && (
                                <p className="mt-3 text-slate-500">{description}</p>
                            )}
                        </div>
                        {children}
                    </CardContent>

                </Card>
                {footer && (
                    <div className="mt-6 text-sm text-slate-600">
                        {footer}
                    </div>
                )}

            </div>
        </div>
    );
}