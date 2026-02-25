import { useNavigate } from "react-router";
import { Button } from "../../components/ui/button";
import { FileQuestion, Home } from "lucide-react";

export default function NotFound() {
    const navigate = useNavigate();

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-6">
            <div className="max-w-md w-full text-center space-y-6 bg-white p-8 rounded-2xl shadow-xl">
                <div className="flex justify-center">
                    <div className="p-4 bg-blue-100 rounded-full">
                        <FileQuestion className="size-12 text-blue-600" />
                    </div>
                </div>
                <div className="space-y-2">
                    <h1 className="text-3xl font-bold text-gray-900">404</h1>
                    <h2 className="text-xl font-semibold text-gray-700">페이지를 찾을 수 없습니다</h2>
                    <p className="text-gray-500">
                        요청하신 페이지가 존재하지 않거나 이동되었습니다.
                    </p>
                </div>
                <div className="pt-4">
                    <Button onClick={() => navigate("/")} className="w-full gap-2">
                        <Home className="size-4" />
                        홈으로 돌아가기
                    </Button>
                </div>
            </div>
        </div>
    );
}
