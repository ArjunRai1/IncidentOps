import { useAuth } from "../hooks/useAuth";
import { User, ShieldCheck, Mail, Activity } from "lucide-react";

import PageHeader from "../components/dashboard/PageHeader";
import StatCard from "../components/dashboard/StatCard";
import SectionCard from "../components/dashboard/SectionCard";

export default function Dashboard() {
    const { user } = useAuth();

    return (
        <div className="space-y-8">
            <PageHeader title="Dashboard" description="Monitor your workspace and manage incidents from a single place."/>
            <div className="grid gap-6 sm:grid-cols-2 xl:grid-cols-4">
                <StatCard title="Account" value="Active" subtitle="Authenticated session" icon={ShieldCheck} iconColor="text-emerald-600"/>
                <StatCard title="Role" value={user?.role ?? "-"} subtitle="Current access level" icon={User} iconColor="text-indigo-600"/>
                <StatCard title="Email" value={user?.email ? "Verified" : "-"} subtitle="Account email" icon={Mail} iconColor="text-sky-600"/>
                <StatCard title="Workspace" value="Ready" subtitle="System operational" icon={Activity} iconColor="text-amber-600"/>
            </div>

            <SectionCard title="Account Information" description="Information associated with your currently authenticated account.">
                <dl className="grid gap-6 sm:grid-cols-2">
                    <div>
                        <dt className="text-sm font-medium text-slate-500">Email</dt>
                        <dd className="mt-1 text-base font-medium text-slate-900 break-all">
                            {user?.email}
                        </dd>
                    </div>

                    <div>
                        <dt className="text-sm font-medium text-slate-500">Role</dt>
                        <dd className="mt-1 text-base font-medium text-slate-900">
                            {user?.role}
                        </dd>
                    </div>
                </dl>
            </SectionCard>
        </div>
    );
}