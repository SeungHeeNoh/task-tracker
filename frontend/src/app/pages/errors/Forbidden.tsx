import { useNavigate } from "react-router";
import { Button } from "../../components/ui/button";
import { Lock } from "lucide-react";

export default function Forbidden() {
    const navigate = useNavigate();

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-6">
            <div className="max-w-md w-full text-center space-y-6 bg-white p-8 rounded-2xl shadow-xl">
                <div className="flex justify-center">
                    <div className="p-4 bg-indigo-100 rounded-full">
                        <Lock className="size-12 text-indigo-600" />
                    </div>
                </div>
                <div className="space-y-2">
                    <h1 className="text-3xl font-bold text-gray-900">403</h1>
                    <h2 className="text-xl font-semibold text-gray-700">접근 권한이 없습니다</h2>
                    <p className="text-gray-500">
                        이 콘텐츠를 볼 수 있는 권한이 없습니다.
                    </p>
                </div>
                <div className="pt-4">
                    <Button onClick={() => navigate("/login")} className="w-full gap-2">
                        <Lock className="size-4" />
                        로그인 페이지로 이동
                    </Button>
                </div>
            </div>
        </div>
    );
}
