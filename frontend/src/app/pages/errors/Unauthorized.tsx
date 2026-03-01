import { useNavigate } from "react-router";
import { Button } from "../../components/ui/button";
import { ShieldAlert, ArrowLeft } from "lucide-react";

export default function Unauthorized() {
    const navigate = useNavigate();

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-6">
            <div className="max-w-md w-full text-center space-y-6 bg-white p-8 rounded-2xl shadow-xl">
                <div className="flex justify-center">
                    <div className="p-4 bg-red-100 rounded-full">
                        <ShieldAlert className="size-12 text-red-600" />
                    </div>
                </div>
                <div className="space-y-2">
                    <h1 className="text-3xl font-bold text-gray-900">401</h1>
                    <h2 className="text-xl font-semibold text-gray-700">로그인이 필요합니다</h2>
                    <p className="text-gray-500">
                        이 페이지에 접근하려면 로그인이 필요합니다. 계속하려면 로그인해 주세요.
                    </p>
                </div>
                <div className="pt-4">
                    <Button onClick={() => navigate("/login")} className="w-full gap-2">
                        <ArrowLeft className="size-4" />
                        로그인 화면으로
                    </Button>
                </div>
            </div>
        </div>
    );
}
