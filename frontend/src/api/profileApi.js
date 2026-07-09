import api from "./axios";

export const getProfile = async () => {
    const response = await api.get("/users/me");
    return response.data;
};

export const updateProfile = async (profile) => {
    const response = await api.put("/users/me", profile);
    return response.data;
};

export const changePassword = async (data) => {
    const response = await api.put("/users/me/password", data);
    return response.data;
};