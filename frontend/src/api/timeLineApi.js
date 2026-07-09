import api from "./axios";

export const getTimeline = async (incidentId) => {
    const response = await api.get(`/incidents/${incidentId}/timeline`);
    return response.data;
};