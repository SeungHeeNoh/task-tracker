import { useState } from "react";
import { useTask } from "../context/TaskContext";
import { useNavigate, Link } from "react-router";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { PageContainer } from "../components/PageContainer";
import { User, Image as ImageIcon, AlertCircle, Save } from "lucide-react";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar";

export default function ProfilePage() {
    const { currentUser, updateProfile } = useTask();
    const navigate = useNavigate();

    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [successMsg, setSuccessMsg] = useState<string | null>(null);

    // Form states
    const [nickname, setNickname] = useState(currentUser?.name || "");
    const [avatarImg, setAvatarImg] = useState<File | null>(null);

    // This converts a file to base64 for preview, or to send to an API.
    const fileToBase64 = (file: File): Promise<string> => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => resolve(reader.result as string);
            reader.onerror = error => reject(error);
        });
    };

    const validateForm = () => {
        if (nickname.trim().length < 1 || nickname.trim().length > 50) {
            setError("닉네임은 1~50자 사이로 입력해주세요.");
            return false;
        }

        if (avatarImg && avatarImg.size > 2 * 1024 * 1024) {
            setError("아바타 이미지는 2MB 이하여야 합니다.");
            return false;
        }

        setError(null);
        return true;
    };

    const handleUpdateProfile = async (e: React.FormEvent) => {
        e.preventDefault();
        setSuccessMsg(null);

        if (!validateForm()) return;

        setIsLoading(true);
        setError(null);

        try {
            let base64Avatar = undefined;
            if (avatarImg) {
                const dataUrl = await fileToBase64(avatarImg);
                base64Avatar = dataUrl.split(',')[1];
            }

            const { success, message } = await updateProfile({
                userName: nickname,
                avatarImg: base64Avatar
            });

            if (success) {
                setSuccessMsg("프로필 정보가 성공적으로 수정되었습니다.");
            } else {
                setError(message || "프로필 정보 수정에 실패했습니다.");
            }

        } catch (e: any) {
            setError(e.message || "프로필 수정 중 오류가 발생했습니다.");
        } finally {
            setIsLoading(false);
        }
    };

    if (!currentUser) return null;

    return (
        <PageContainer
            title="프로필 상세설정"
            description="계정 프로필 정보를 관리합니다."
        >
            <div className="max-w-xl">
                <form onSubmit={handleUpdateProfile} className="space-y-6">
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

                    <div className="flex flex-col sm:flex-row items-center sm:items-start gap-6 mb-8">
                        <Avatar className="size-24 border-2 shadow-sm">
                            <AvatarImage src={avatarImg ? URL.createObjectURL(avatarImg) : currentUser.avatar} alt={currentUser.name} />
                            <AvatarFallback className="text-2xl">{currentUser.name.charAt(0)}</AvatarFallback>
                        </Avatar>
                        <div className="space-y-2 flex-grow w-full">
                            <Label htmlFor="avatarImg">아바타 이미지 변경</Label>
                            <div className="relative w-full">
                                <ImageIcon className="absolute left-3 top-3 size-4 text-gray-400" />
                                <Input
                                    id="avatarImg"
                                    type="file"
                                    accept="image/*"
                                    className="pl-9 pt-1.5 w-full bg-gray-50 border-gray-200 file:bg-transparent file:text-sm file:font-medium"
                                    onChange={(e) => {
                                        if (e.target.files && e.target.files.length > 0) {
                                            setAvatarImg(e.target.files[0]);
                                        } else {
                                            setAvatarImg(null);
                                        }
                                    }}
                                />
                            </div>
                            <p className="text-xs text-gray-500 mt-1">최대 2MB. 이미지는 자동으로 리사이징됩니다.</p>
                        </div>
                    </div>

                    <div className="space-y-2">
                        <Label htmlFor="nickname">닉네임</Label>
                        <div className="relative">
                            <User className="absolute left-3 top-3 size-4 text-gray-400" />
                            <Input
                                id="nickname"
                                type="text"
                                placeholder="닉네임 (1~50자)"
                                className="pl-9 bg-gray-50"
                                value={nickname}
                                onChange={(e) => setNickname(e.target.value)}
                                required
                            />
                        </div>
                    </div>

                    <div className="pt-4 border-t flex flex-col sm:flex-row items-center justify-between gap-4">
                        <div className="text-sm text-gray-500">
                            <Link to="/profile/password" className="text-blue-600 hover:text-blue-700 hover:underline font-medium">
                                비밀번호 변경
                            </Link>
                        </div>
                        <div className="flex gap-3 w-full sm:w-auto">
                            <Button type="button" variant="outline" onClick={() => navigate(-1)} className="w-full sm:w-auto">
                                취소
                            </Button>
                            <Button type="submit" className="w-full sm:w-auto bg-blue-600 hover:bg-blue-700 text-white shadow-md disabled:bg-blue-400" disabled={isLoading}>
                                {isLoading ? "저장 중..." : "변경사항 저장"}
                            </Button>
                        </div>
                    </div>
                </form>
            </div>
        </PageContainer>
    );
}
