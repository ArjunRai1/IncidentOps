import { createContext, useContext, useEffect, useState } from "react";
import { login as loginApi, getCurrentUser } from "../api/authApi";
import { getToken, removeToken, setToken } from "../utils/token";

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const initializeAuth = async () => {
            const token = getToken();
            if (!token) {
                setLoading(false);
                return;
            }
            try {
                const currentUser = await getCurrentUser();
                setUser(currentUser);
            } catch (error) {
                removeToken();
                setUser(null);
            } finally {
                setLoading(false);
            }
        };
        initializeAuth();
    }, []);

    const login = async (credentials) => {
        const response = await loginApi(credentials);
        setToken(response.token);
        const currentUser = await getCurrentUser();
        setUser(currentUser);
        return response;
    };

    const logout = () => {
        removeToken();
        setUser(null);
    };

    const value = {
        user,
        loading,
        login,
        logout,
        isAuthenticated: !!user,
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}