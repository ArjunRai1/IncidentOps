import { useState } from "react";
import { useNavigate } from "react-router-dom";

import {
    requestPasswordChange,
    verifyPasswordChange,
} from "../../api/profileApi";

import { useAuth } from "../../context/AuthContext";

import SectionCard from "../dashboard/SectionCard";

import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { Label } from "../ui/label";

export default function PasswordCard() {

    const navigate = useNavigate();
    const { logout } = useAuth();

    const [newPassword, setNewPassword] = useState("");
    const [otp, setOtp] = useState("");

    const [otpSent, setOtpSent] = useState(false);

    const [sending, setSending] = useState(false);
    const [verifying, setVerifying] = useState(false);

    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    const handleRequestOtp = async () => {

        try {
            setSending(true);
            setError("");
            setMessage("");

            await requestPasswordChange({newPassword});
            setOtpSent(true);
            setMessage("OTP has been sent to your registered email.");

        } catch (err) {

            setError(
                err.response?.data?.message ??
                "Unable to send OTP."
            );

        } finally {
            setSending(false);
        }
    };

    const handleVerify = async () => {

        try {
            setVerifying(true);
            setError("");
            setMessage("");
            await verifyPasswordChange({
                otp,
            });

            alert("Password changed successfully. Please login again.");
            logout();
            navigate("/login");

        } catch (err) {

            setError(
                err.response?.data?.message ??
                "OTP verification failed."
            );

        } finally {
            setVerifying(false);
        }
    };

    return (

        <SectionCard title="Change Password" description="Changing your password requires OTP verification.">
            <div className="space-y-5">
                <div className="space-y-2">
                    <Label>New Password</Label>
                    <Input type="password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)}/>
                </div>

                <Button onClick={handleRequestOtp} disabled={sending || !newPassword.trim()}>
                    {sending ? "Sending..." : "Send OTP"}
                </Button>

                {otpSent && (
                    <>
                        <div className="space-y-2">
                            <Label>OTP</Label>
                            <Input value={otp} onChange={(e) => setOtp(e.target.value)}/>
                        </div>

                        <Button onClick={handleVerify} disabled={verifying || !otp.trim()}>
                            {verifying ? "Verifying..." : "Verify & Change Password"}
                        </Button>
                    </>
                )}

                {message && (
                    <p className="text-sm text-green-600">{message}</p>
                )}

                {error && (
                    <p className="text-sm text-destructive">{error}</p>
                )}
            </div>
        </SectionCard>

    );
}