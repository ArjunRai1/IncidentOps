import { useEffect, useState } from "react";
import { getIncidentSummary } from "../../api/aiApi";

const IncidentSummary = ({ incidentId }) => {
    const [summary, setSummary] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        if(!incidentId){
            return;
        }

        const fetchSummary = async () => {
            setLoading(true);
            setError("");

            try{
                const response = await getIncidentSummary(incidentId);
                setSummary(response.summary);
            } catch (err) {
                setError("Unable to generate incident summary.");
            } finally{
                setLoading(false);
            }
        };

        fetchSummary();
    }, [incidentId]);

    return (
        <div className="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
            <div className="flex items-center gap-2 mb-4">
                <span className="text-xl">🤖</span>
                <h2 className="text-lg font-semibold text-gray-900">AI Incident Summary</h2>
            </div>

            {loading && (
                <p className="text-gray-500">Generating summary...</p>
            )}

            {!loading && error && (
                <p className="text-red-600">
                    {error}
                </p>
            )}

            {!loading && !error && (
                <p className="text-gray-700 leading-7 whitespace-pre-line">{summary}</p>
            )}
        </div>
    );
};

export default IncidentSummary;