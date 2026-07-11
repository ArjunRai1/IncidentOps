import { useEffect, useState } from "react";

import { getProfile } from "../api/profileApi";

import Card from "../components/common/Card";
import Loader from "../components/common/Loader";

export default function Profile() {
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadProfile();
    }, []);

    const loadProfile = async () => {
        try {
            setLoading(true);
            setError("");

            const data = await getProfile();

            setProfile(data);
        } catch (err) {
            setError(
                err.response?.data?.message ??
                "Failed to load profile."
            );
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <Loader />;
    }

    if (error) {
        return (
            <div className="rounded-md border border-red-200 bg-red-50 p-4 text-red-700">
                {error}
            </div>
        );
    }

    return (
        <div className="mx-auto max-w-3xl">
            <Card>
                <h1 className="mb-2 text-3xl font-bold">Profile</h1>
                <p className="mb-8 text-gray-500">Account information</p>
                <div className="grid gap-6 md:grid-cols-2">
                    <div>
                        <p className="text-sm text-gray-500">User ID</p>
                        <p className="mt-1 font-medium">{profile.id}</p>
                    </div>

                    <div>
                        <p className="text-sm text-gray-500">Role</p>
                        <p className="mt-1 font-medium">{profile.role}</p>
                    </div>

                    <div className="md:col-span-2">
                        <p className="text-sm text-gray-500">Email</p>
                        <p className="mt-1 font-medium">{profile.email}</p>
                    </div>
                </div>
            </Card>
        </div>
    );
}