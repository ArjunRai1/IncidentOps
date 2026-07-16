import api from "./axios";

export const getProfile = async () => {
    const response = await api.get("/profile");
    return response.data;
};

export const updateProfile = async (data) => {
    const response = await api.put("/profile", data);
    return response.data;
};

export const requestPasswordChange = async (data) => {
    const response = await api.post("/profile/password/request", data);
    return response.data;
};

export const verifyPasswordChange = async (data) => {
    const response = await api.post("/profile/password/verify", data);
    return response.data;
};

export const requestEmailChange = async (data) => {
    const response = await api.post("/profile/email/request", data);
    return response.data;
};

export const verifyEmailChange = async (data) => {
    const response = await api.post("/profile/email/verify", data);
    return response.data;
};