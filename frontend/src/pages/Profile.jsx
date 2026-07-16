import { useEffect, useState } from "react";

import { getProfile } from "../api/profileApi";

import { Card } from "../components/ui/card";
import Loader from "../components/common/Loader";

import PageHeader from "../components/dashboard/PageHeader";
import SectionCard from "../components/dashboard/SectionCard";

import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";

import ProfileInfoCard from "../components/profile/ProfileInfoCard";
import PasswordCard from "../components/profile/PasswordCard";
import EmailCard from "../components/profile/EmailCard";

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

    if(loading){
        return <Loader />;
    }

    if(error){
        return(
            <div className="mx-auto max-w-5xl">
                <div className="rounded-lg border border-destructive/20 bg-destructive/10 px-4 py-3 text-sm text-destructive">
                    {error}
                </div>
            </div>
        );
    }

    return (
        <div className="mx-auto max-w-5xl space-y-8">
            <PageHeader title="Profile" description="Manage your personal information and account security."/>
            <ProfileInfoCard profile={profile} onProfileUpdated={loadProfile}/>
            <PasswordCard />
            <EmailCard />
         </div>
    );
}