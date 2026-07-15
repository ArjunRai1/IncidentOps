import React from "react";

export default function AnalysisCard({ analysis, loading }) {

    if (loading) {
        return (
            <div className="bg-white rounded-lg shadow p-6 mt-6">
                <h2 className="text-xl font-semibold mb-4">AI Incident Analysis</h2>
                <p className="text-gray-500">Generating analysis...</p>
            </div>
        );
    }

    if (!analysis) {
        return null;
    }

    const renderList = (items = []) => {
        if(!Array.isArray(items) || items.length === 0){
            return(
                <p className="text-gray-500 mt-2">No information available.</p>
            );
        }
        return(
            <ul className="list-disc ml-5 mt-2 space-y-1">
                {items.map((item, index) => (
                    <li key={index}>{item}</li>
                ))}
            </ul>
        );
    };

    return (
        <div className="bg-white rounded-lg shadow p-6 mt-6">
            <h2 className="text-xl font-semibold mb-6">AI Incident Analysis</h2>
            <div className="mb-6">
                <h3 className="font-semibold">Root Cause</h3>
                <p className="mt-2 text-gray-700">{analysis.rootCause}</p>
            </div>

            <div className="mb-6">
                <h3 className="font-semibold">Confidence</h3>

                <span className={`inline-block mt-2 px-3 py-1 rounded-full text-sm font-medium ${
                        analysis.confidence === "HIGH"
                            ? "bg-red-100 text-red-700"
                            : analysis.confidence === "MEDIUM"
                            ? "bg-yellow-100 text-yellow-700"
                            : "bg-green-100 text-green-700"
                    }`}>
                {analysis.confidence}
                </span>
            </div>

            <div className="mb-6">

                <h3 className="font-semibold">Evidence</h3>
                {renderList(analysis.evidence)}
            </div>

            <div className="mb-6">
                <h3 className="font-semibold">Immediate Actions</h3>
                {renderList(analysis.immediateActions)}
            </div>

            <div>
                <h3 className="font-semibold">Preventive Recommendations</h3>
                {renderList(analysis.preventiveRecommendations)}
            </div>
        </div>
    );
}