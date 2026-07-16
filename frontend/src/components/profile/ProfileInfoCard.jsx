import { useEffect, useState } from "react";

import { updateProfile } from "../../api/profileApi";

import SectionCard from "../dashboard/SectionCard";

import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { Label } from "../ui/label";

export default function ProfileInfoCard({ profile, onProfileUpdated }) {

    const [username, setUsername] = useState("");
    const [saving, setSaving] = useState(false);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    useEffect(() => {
        if (profile) {
            setUsername(profile.username);
        }
    }, [profile]);

    const handleSave = async () => {
        try {
            setSaving(true);
            setMessage("");
            setError("");
            await updateProfile({
                username,
            });
            setMessage("Profile updated successfully.");
            onProfileUpdated?.();
        } catch (err) {
            setError(
                err.response?.data?.message ??
                "Failed to update profile."
            );
        } finally {
            setSaving(false);
        }
    };

    return (
        <SectionCard title="Personal Information" description="Update your account information.">
            <div className="space-y-6">
                <div className="space-y-2">
                    <Label>Username</Label>
                    <Input value={username} onChange={(e) => setUsername(e.target.value)}/>
                </div>

                <div className="space-y-2">
                    <Label>Email</Label>
                    <Input value={profile.email} disabled/>
                </div>

                <div className="grid gap-6 md:grid-cols-2">
                    <div className="space-y-2">
                        <Label>Role</Label>
                        <Input value={profile.role} disabled/>
                    </div>

                    <div className="space-y-2">
                        <Label>Created</Label>
                        <Input value={new Date(profile.createdAt).toLocaleString()} disabled/>
                    </div>
                </div>

                <div className="space-y-2">
                    <Label>Last Updated</Label>
                    <Input value={new Date(profile.updatedAt).toLocaleString()} disabled/>
                </div>

                {message && (
                    <p className="text-sm text-green-600">{message}</p>
                )}

                {error && (
                    <p className="text-sm text-destructive">{error}</p>
                )}

                <Button onClick={handleSave} disabled={saving}>
                    {saving ? "Saving..." : "Save Changes"}
                </Button>
            </div>

        </SectionCard>
    );
}