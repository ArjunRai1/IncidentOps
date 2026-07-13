import api from "./axios";

export const getSimilarIncidents = async (incidentId) => {
    const response = await api.get(`/ai/incidents/${incidentId}/similar`);
    return response.data;
};

export const getIncidentSummary = async (incidentId) => {
    const response = await api.get(`/ai/incidents/${incidentId}/summary`);
    return response.data;
};