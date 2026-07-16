import {AlertCircle, Brain, CheckCircle2, ShieldCheck, Sparkles} from "lucide-react";

import { Badge } from "../ui/badge";
import { Skeleton } from "../ui/skeleton";

export default function AnalysisCard({ analysis, loading }) {
    if (loading) {
        return (
            <div className="space-y-6">
                    <Skeleton className="h-20 w-full" />
                    <div className="space-y-3">
                        <Skeleton className="h-5 w-40" />
                        <Skeleton className="h-4 w-full" />
                        <Skeleton className="h-4 w-5/6" />
                        <Skeleton className="h-4 w-3/4" />
                    </div>

                    <div className="space-y-3">
                        <Skeleton className="h-5 w-48" />
                        <Skeleton className="h-4 w-full" />
                        <Skeleton className="h-4 w-5/6" />
                    </div>

                    <div className="space-y-3">
                        <Skeleton className="h-5 w-56" />
                        <Skeleton className="h-4 w-full" />
                        <Skeleton className="h-4 w-4/5" />
                    </div>
                </div>
        );
    }

    if (!analysis) {
        return (
            <div className="flex flex-col items-center justify-center py-12 text-center">
                    <Brain className="mb-4 h-10 w-10 text-slate-400" />
                    <h3 className="text-lg font-semibold">No Analysis Available</h3>
                    <p className="mt-2 max-w-md text-sm text-muted-foreground">Upload logs or wait for AI analysis to generate a root cause and recommendations.</p>
                </div>
        );
    }

    const renderList = (items = []) => {
        if (!Array.isArray(items) || items.length === 0) {
            return(
                <p className="text-sm text-muted-foreground">No information available.</p>
            );
        }

        return (
            <ul className="space-y-3">
                {items.map((item, index) => (
                    <li key={index} className="flex items-start gap-3">
                        <CheckCircle2 className="mt-0.5 h-4 w-4 flex-shrink-0 text-primary" />
                        <span className="text-sm leading-6 text-slate-700">{item}</span>
                    </li>
                ))}
            </ul>
        );
    };

    const confidenceVariant = {
        HIGH: "destructive",
        MEDIUM: "secondary",
        LOW: "outline",
    };

    return (
            <div className="space-y-8">
                <Badge variant={confidenceVariant[analysis.confidence] ?? "outline"}>
                    {analysis.confidence} Confidence
                </Badge>

                <section className="space-y-3">
                    <div className="flex items-center gap-2">
                        <AlertCircle className="h-5 w-5 text-red-500" />
                        <h3 className="font-semibold">Root Cause</h3>
                    </div>

                    <p className="leading-7 text-slate-700">{analysis.rootCause}</p>
                </section>

                <section className="space-y-4 border-t pt-6">
                    <div className="flex items-center gap-2">
                        <Sparkles className="h-5 w-5 text-amber-500" />
                        <h3 className="font-semibold">Supporting Evidence</h3>
                    </div>
                    {renderList(analysis.evidence)}
                </section>

                <section className="space-y-4 border-t pt-6">
                    <div className="flex items-center gap-2">
                        <CheckCircle2 className="h-5 w-5 text-emerald-500" />
                        <h3 className="font-semibold">Immediate Actions</h3>
                    </div>
                    {renderList(analysis.immediateActions)}
                </section>

                <section className="space-y-4 border-t pt-6">
                    <div className="flex items-center gap-2">
                        <ShieldCheck className="h-5 w-5 text-blue-500" />
                        <h3 className="font-semibold">Preventive Recommendations</h3>
                    </div>
                    {renderList(analysis.preventiveRecommendations)}
                </section>
            </div>
    );
}