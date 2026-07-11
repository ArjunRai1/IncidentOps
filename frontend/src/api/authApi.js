import api from "./axios";

export const login = async (credentials) => {
    const response = await api.post("/auth/login", credentials);
    return response.data;
};

export const register = async (user) => {
    const response = await api.post("/auth/register", user);
    return response.data;
};

export const verifyOtp = async (data) => {
    const response = await api.post("/auth/verify", data);
    return response.data;
};

export const getCurrentUser = async () => {
    const response = await api.get("/auth/me");
    return response.data;
};

export const forgotPassword = async (data) => {
    const response = await api.post("/auth/forgot-password", data);
    return response.data;
};

export const resetPassword = async (data) => {
    const response = await api.post("/auth/reset-password", data);
    return response.data;
};