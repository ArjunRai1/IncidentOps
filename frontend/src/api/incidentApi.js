import api from "./axios";

export const getIncidents = async (params) => {
    const response = await api.get("/incidents", { params });
    return response.data.content;
};

export const getIncident = async (id) => {
    const response = await api.get(`/incidents/${id}`);
    return response.data;
};

export const createIncident = async (incident) => {
    const response = await api.post("/incidents", incident);
    return response.data;
};

export const updateIncident = async (id, incident) => {
    const response = await api.put(`/incidents/${id}`, incident);
    return response.data;
};