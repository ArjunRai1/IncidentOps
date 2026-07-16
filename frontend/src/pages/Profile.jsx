import { useEffect, useState } from "react";

import { getProfile } from "../api/profileApi";

import { Card } from "../components/ui/card";
import Loader from "../components/common/Loader";

import PageHeader from "../components/dashboard/PageHeader";
import SectionCard from "../components/dashboard/SectionCard";

import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";

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
                <SectionCard title="Personal Information" description="Update your account details.">
                    <div className="grid gap-6 md:grid-cols-2">
                        <div className="space-y-2">
                            <Label htmlFor="name">Full Name</Label>
                            <Input id="name" name="name" value={profile.name} onChange={handleProfileChange} className="h-11"/>
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="email">Email Address</Label>
                            <Input id="email" value={profile.email} disabled className="h-11 bg-muted"/>
                        </div>
                    </div>

                    <div className="flex justify-end pt-6">
                        <Button loading={savingProfile} onClick={handleProfileSave}>Save Changes</Button>
                    </div>
                </SectionCard>

                <SectionCard title="Security" description="Update your account password.">
                    <div className="space-y-6">
                        <div className="space-y-2">
                            <Label htmlFor="currentPassword">Current Password</Label>
                            <Input id="currentPassword" name="currentPassword" type="password" value={password.currentPassword} onChange={handlePasswordChange} className="h-11"/>
                        </div>

                        <div className="grid gap-6 md:grid-cols-2">
                            <div className="space-y-2">
                                <Label htmlFor="newPassword">New Password</Label>
                                <Input id="newPassword" name="newPassword" type="password" value={password.newPassword} onChange={handlePasswordChange} className="h-11"/>
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="confirmPassword">Confirm Password</Label>
                                <Input id="confirmPassword" name="confirmPassword" type="password" value={password.confirmPassword} onChange={handlePasswordChange} className="h-11"/>
                            </div>
                        </div>

                        <div className="flex justify-end">
                            <Button loading={changingPassword} onClick={handlePasswordSave}>Change Password</Button>
                        </div>
                    </div>
                </SectionCard>
        </div>
    );
}