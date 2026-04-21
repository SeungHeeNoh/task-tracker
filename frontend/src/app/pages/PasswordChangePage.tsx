import { useState } from "react";
import { useNavigate } from "react-router";
import { useTask } from "../context/TaskContext";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { PageContainer } from "../components/PageContainer";
import { Lock, AlertCircle, Save } from "lucide-react";

export default function PasswordChangePage() {
    const navigate = useNavigate();
    const { changePassword } = useTask();

    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [successMsg, setSuccessMsg] = useState<string | null>(null);

    // Form states
    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const validateForm = () => {
        if (!currentPassword) {
            setError("현재 비밀번호를 입력해주세요.");
            return false;
        }

        // Password Validation: >= 6 chars, uppercase, lowercase, special char
        if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*[\W_]).{6,}/.test(newPassword)) {
            setError("새 비밀번호는 6자 이상이며, 대문자, 소문자, 특수문자를 각각 최소 1개 이상 포함해야 합니다.");
            return false;
        }

        if (newPassword !== confirmPassword) {
            setError("새 비밀번호가 일치하지 않습니다.");
            return false;
        }

        setError(null);
        return true;
    };

    const handleChangePassword = async (e: React.FormEvent) => {
        e.preventDefault();
        setSuccessMsg(null);

        if (!validateForm()) return;

        setIsLoading(true);
        setError(null);

        try {
            const { success, message } = await changePassword(currentPassword, newPassword);

            if (success) {
                setSuccessMsg("비밀번호가 성공적으로 변경되었습니다.");

                // clear form
                setCurrentPassword("");
                setNewPassword("");
                setConfirmPassword("");
            } else {
                setError(message || "비밀번호 변경에 실패했습니다.");
            }

        } catch (e: any) {
            setError(e.message || "비밀번호 변경 중 오류가 발생했습니다.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <PageContainer
            title="Change Password"
            description="Ensure your account is using a long, random password to stay secure."
        >
            <div className="max-w-md">
                <form onSubmit={handleChangePassword} className="space-y-6">
                    {error && (
                        <div className="bg-red-50 text-red-600 p-3 rounded-md flex items-start gap-2 text-sm break-keep">
                            <AlertCircle className="size-4 shrink-0 mt-0.5" />
                            <span>{error}</span>
                        </div>
                    )}
                    {successMsg && (
                        <div className="bg-green-50 text-green-700 p-3 rounded-md flex items-start gap-2 text-sm break-keep border border-green-200">
                            <Save className="size-4 shrink-0 mt-0.5" />
                            <span>{successMsg}</span>
                        </div>
                    )}

                    <div className="space-y-2">
                        <Label htmlFor="currentPassword">Current Password</Label>
                        <div className="relative">
                            <Lock className="absolute left-3 top-3 size-4 text-gray-400" />
                            <Input
                                id="currentPassword"
                                type="password"
                                placeholder="Enter current password"
                                className="pl-9 bg-gray-50"
                                value={currentPassword}
                                onChange={(e) => setCurrentPassword(e.target.value)}
                                required
                            />
                        </div>
                    </div>

                    <div className="space-y-2 pt-2 border-t mt-4">
                        <Label htmlFor="newPassword">New Password</Label>
                        <div className="relative">
                            <Lock className="absolute left-3 top-3 size-4 text-gray-400" />
                            <Input
                                id="newPassword"
                                type="password"
                                placeholder="대/소문자, 특수문자 포함 6자 이상"
                                className="pl-9 bg-gray-50"
                                value={newPassword}
                                onChange={(e) => setNewPassword(e.target.value)}
                                required
                            />
                        </div>
                    </div>

                    <div className="space-y-2">
                        <Label htmlFor="confirmPassword">Confirm New Password</Label>
                        <div className="relative">
                            <Lock className="absolute left-3 top-3 size-4 text-gray-400" />
                            <Input
                                id="confirmPassword"
                                type="password"
                                placeholder="Confirm new password"
                                className="pl-9 bg-gray-50"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                            />
                        </div>
                    </div>

                    <div className="pt-4 flex flex-col sm:flex-row items-center justify-end gap-3 border-t">
                        <Button type="button" variant="outline" onClick={() => navigate(-1)} className="w-full sm:w-auto">
                            Cancel
                        </Button>
                        <Button type="submit" className="w-full sm:w-auto bg-blue-600 hover:bg-blue-700 text-white shadow-md disabled:bg-blue-400" disabled={isLoading}>
                            {isLoading ? "Updating..." : "Update Password"}
                        </Button>
                    </div>
                </form>
            </div>
        </PageContainer>
    );
}
