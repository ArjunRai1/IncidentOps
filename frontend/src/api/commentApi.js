import api from "./axios";

export const getComments = async (incidentId) => {
    const response = await api.get(`/incidents/${incidentId}/comments`);
    return response.data;
};

export const addComment = async (incidentId, comment) => {
    const response = await api.post(`/incidents/${incidentId}/comments`, comment);
    return response.data;
};