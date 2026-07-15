import api from "./axios";

export const getSimilarIncidents = async (incidentId) => {
    const response = await api.get(`/ai/incidents/${incidentId}/similar`);
    return response.data;
};

export const getIncidentSummary = async (incidentId) => {
    const response = await api.get(`/ai/incidents/${incidentId}/summary`);
    return response.data;
};

export const analyzeIncident = async (incidentId) => {
    const response = await api.post(`/ai/incidents/${incidentId}/analysis`);
    return response.data;
};

export const uploadLog = async (incidentId, file) => {
    const formData = new FormData();
    formData.append("file", file);
    const response = await api.post(`/incidents/${incidentId}/logs`, formData);
    return response.data;
};