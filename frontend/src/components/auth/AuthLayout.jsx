import { Card, CardContent, CardHeader } from "@/components/ui/card";

export default function AuthLayout({title, description, children}) {
  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-50 px-4">
      <Card className="w-full max-w-md border shadow-sm">
        <CardHeader className="space-y-2 text-center">
          <h1 className="text-2xl font-semibold tracking-tight">IncidentOps</h1>
          <div>
            <h2 className="text-lg font-medium">{title}</h2>
            {description && (
              <p className="mt-1 text-sm text-slate-500">{description}</p>
            )}
          </div>
        </CardHeader>

        <CardContent>
          {children}
        </CardContent>
      </Card>
    </div>
  );
}