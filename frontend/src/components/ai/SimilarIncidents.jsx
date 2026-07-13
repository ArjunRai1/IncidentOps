import { useEffect, useState } from "react";
import { getSimilarIncidents } from "../../api/aiApi";

const SimilarIncidents = ({ incidentId }) => {
    const [incidents, setIncidents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        if (!incidentId) {
            return;
        }

        const fetchSimilarIncidents = async () => {
            setLoading(true);
            setError("");

            try {
                const response = await getSimilarIncidents(incidentId);
                setIncidents(response);
            } catch (err) {
                setError("Unable to load similar incidents.");
            } finally {
                setLoading(false);
            }
        };

        fetchSimilarIncidents();
    }, [incidentId]);

    return (
        <div className="bg-white rounded-lg shadow-sm border p-6">
            <h2 className="text-lg font-semibold mb-4">Similar Incidents</h2>

            {loading && (
                <p className="text-gray-500">Finding similar incidents...</p>
            )}

            {!loading && error && (
                <p className="text-red-600">{error}</p>
            )}

            {!loading && !error && incidents.length === 0 && (
                <p className="text-gray-500">No similar incidents found.</p>
            )}

            {!loading && !error && incidents.length > 0 && (
                <div className="space-y-3">
                    {incidents.map((incident) => (
                        <div key={incident.id} className="border rounded-md p-4 hover:bg-gray-50 transition-colors">
                            <h3 className="font-medium text-gray-900">{incident.title}</h3>
                            <div className="flex gap-4 mt-2 text-sm text-gray-600">
                                <span>Status: {incident.status}</span>
                                <span>Priority: {incident.priority}</span>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default SimilarIncidents;