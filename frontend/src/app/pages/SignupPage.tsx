import { useState } from "react";
import { useTask } from "../context/TaskContext";
import { useNavigate, Link } from "react-router";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { CheckCircle2, Lock, Mail, User, Image as ImageIcon, AlertCircle } from "lucide-react";

export default function SignupPage() {
    const { signup } = useTask();
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    // Form states
    const [id, setId] = useState("");
    const [nickname, setNickname] = useState("");
    const [password, setPassword] = useState("");
    const [avatarImg, setAvatarImg] = useState<File | null>(null);

    const fileToBase64 = (file: File): Promise<string> => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => resolve(reader.result as string);
            reader.onerror = error => reject(error);
        });
    };

    const validateForm = () => {
        // ID Validation: Alphanumeric, 4-50 chars
        if (!/^[a-zA-Z0-9]{4,50}$/.test(id)) {
            setError("ID는 4~50자의 영문 대소문자 및 숫자만 가능합니다.");
            return false;
        }

        // Nickname Validation: 1-50 chars
        if (nickname.trim().length < 1 || nickname.trim().length > 50) {
            setError("닉네임은 1~50자 사이로 입력해주세요.");
            return false;
        }

        // Password Validation: >= 6 chars, uppercase, lowercase, special char
        if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*[\W_]).{6,}/.test(password)) {
            setError("비밀번호는 6자 이상이며, 대문자, 소문자, 특수문자를 각각 최소 1개 이상 포함해야 합니다.");
            return false;
        }

        // Avatar Image Validation: <= 2MB
        if (avatarImg && avatarImg.size > 2 * 1024 * 1024) {
            setError("아바타 이미지는 2MB 이하여야 합니다.");
            return false;
        }

        setError(null);
        return true;
    };

    const handleSignup = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!validateForm()) return;

        setIsLoading(true);
        setError(null);

        try {
            let base64Avatar = undefined;
            if (avatarImg) {
                const dataUrl = await fileToBase64(avatarImg);
                // Extract just the base64 string without the data URI prefix
                base64Avatar = dataUrl.split(',')[1];
            }

            const { success, message } = await signup({
                userId: id,
                userName: nickname,
                password,
                avatarImg: base64Avatar,
            });

            if (success) {
                alert("회원가입이 완료되었습니다. 로그인해 주세요.");
                navigate("/login");
            } else {
                setError(message || "회원가입에 실패했습니다.");
            }
        } catch (e: any) {
            setError(e.message || "오류가 발생했습니다.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen w-full flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100 p-4 py-12">
            <div className="w-full max-w-md bg-white rounded-2xl shadow-xl overflow-hidden">
                <div className="p-8 pb-6 text-center">
                    <div className="mx-auto size-12 bg-green-100 rounded-full flex items-center justify-center mb-4">
                        <CheckCircle2 className="size-6 text-green-600" />
                    </div>
                    <h1 className="text-2xl font-bold text-gray-900">Sign Up</h1>
                    <p className="text-gray-500 mt-2">CheckTracker에 오신 것을 환영합니다</p>
                </div>

                <div className="p-8 pt-0">
                    <form onSubmit={handleSignup} className="space-y-4">
                        {error && (
                            <div className="bg-red-50 text-red-600 p-3 rounded-md flex items-start gap-2 text-sm break-keep">
                                <AlertCircle className="size-4 shrink-0 mt-0.5" />
                                <span>{error}</span>
                            </div>
                        )}
                        <div className="space-y-2">
                            <Label htmlFor="id">ID</Label>
                            <div className="relative">
                                <Mail className="absolute left-3 top-3 size-4 text-gray-400" />
                                <Input
                                    id="id"
                                    type="text"
                                    placeholder="영문 대소문자/숫자, 4~50자"
                                    className="pl-9"
                                    value={id}
                                    onChange={(e) => setId(e.target.value)}
                                    required
                                />
                            </div>
                        </div>
                        <div className="space-y-2">
                            <Label htmlFor="nickname">Nickname</Label>
                            <div className="relative">
                                <User className="absolute left-3 top-3 size-4 text-gray-400" />
                                <Input
                                    id="nickname"
                                    type="text"
                                    placeholder="닉네임 (1~50자)"
                                    className="pl-9"
                                    value={nickname}
                                    onChange={(e) => setNickname(e.target.value)}
                                    required
                                />
                            </div>
                        </div>
                        <div className="space-y-2">
                            <Label htmlFor="password">Password</Label>
                            <div className="relative">
                                <Lock className="absolute left-3 top-3 size-4 text-gray-400" />
                                <Input
                                    id="password"
                                    type="password"
                                    placeholder="대/소문자, 특수문자 포함 6자 이상"
                                    className="pl-9"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                />
                            </div>
                        </div>
                        <div className="space-y-2">
                            <Label htmlFor="avatarImg">Avatar Image (Optional)</Label>
                            <div className="relative">
                                <ImageIcon className="absolute left-3 top-3 size-4 text-gray-400" />
                                <Input
                                    id="avatarImg"
                                    type="file"
                                    accept="image/*"
                                    className="pl-9 pt-1.5"
                                    onChange={(e) => {
                                        if (e.target.files && e.target.files.length > 0) {
                                            setAvatarImg(e.target.files[0]);
                                        } else {
                                            setAvatarImg(null);
                                        }
                                    }}
                                />
                            </div>
                            <p className="text-xs text-gray-400 mt-1">2MB 이하의 이미지 파일만 가능합니다.</p>
                        </div>

                        <Button type="submit" className="w-full bg-green-600 hover:bg-green-700 mt-6" disabled={isLoading}>
                            {isLoading ? "가입 처리 중..." : "회원가입"}
                        </Button>
                    </form>

                    <div className="mt-6 text-center text-sm text-gray-500">
                        <p>이미 계정이 있으신가요? <Link to="/login" className="text-blue-600 hover:underline font-medium">로그인</Link></p>
                    </div>
                </div>
            </div>
        </div>
    );
}
