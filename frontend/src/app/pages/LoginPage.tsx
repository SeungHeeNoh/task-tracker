import { useState } from "react";
import { useTask } from "../context/TaskContext";
import { useNavigate } from "react-router";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { CheckCircle2, Lock, Mail } from "lucide-react";

export default function LoginPage() {
    const { login, currentUser } = useTask();
    const navigate = useNavigate();
    const [id, setId] = useState("");
    const [password, setPassword] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    // Redirect if already logged in
    if (currentUser) {
        navigate("/");
        return null;
    }

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);

        // Simulate API call
        setTimeout(() => {
            login(); // This sets the mock user
            setIsLoading(false);
            navigate("/");
        }, 1000);
    };

    return (
        <div className="min-h-screen w-full flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100 p-4">
            <div className="w-full max-w-md bg-white rounded-2xl shadow-xl overflow-hidden">
                <div className="p-8 pb-6 text-center">
                    <div className="mx-auto size-12 bg-blue-100 rounded-full flex items-center justify-center mb-4">
                        <CheckCircle2 className="size-6 text-blue-600" />
                    </div>
                    <h1 className="text-2xl font-bold text-gray-900">Task Space</h1>
                    <p className="text-gray-500 mt-2">나만의 기록부터 우리 가족의 할 일까지 한곳에</p>
                </div>

                <div className="p-8 pt-0">
                    <form onSubmit={handleLogin} className="space-y-4">
                        <div className="space-y-2">
                            <Label htmlFor="id">Id</Label>
                            <div className="relative">
                                <Mail className="absolute left-3 top-3 size-4 text-gray-400" />
                                <Input
                                    id="id"
                                    type="id"
                                    placeholder="ID"
                                    className="pl-9"
                                    value={id}
                                    onChange={(e) => setId(e.target.value)}
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
                                    placeholder="••••••••"
                                    className="pl-9"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                />
                            </div>
                        </div>
                        <Button type="submit" className="w-full bg-blue-600 hover:bg-blue-700" disabled={isLoading}>
                            {isLoading ? "로그인 중..." : "로그인"}
                        </Button>
                    </form>

                    <div className="mt-6 text-center text-sm text-gray-500">
                        <p>아직 계정이 없으신가요? <a href="#" className="text-blue-600 hover:underline font-medium">회원가입</a></p>
                    </div>

                    <div className="mt-6 pt-6 border-t text-center text-xs text-gray-400">
                        <p>비밀번호를 잊으셨나요?</p>
                    </div>
                </div>
            </div>
        </div>
    );
}
