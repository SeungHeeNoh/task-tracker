import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router";
import { useTask } from "../context/TaskContext";
import { Button } from "../components/ui/button";
import { PageContainer } from "../components/PageContainer";
import { AlertCircle, CheckCircle2, Users } from "lucide-react";
import { toast } from "sonner";

export default function InvitePage() {
    const { code } = useParams<{ code: string }>();
    const navigate = useNavigate();
    const { getInvitationPreview, acceptInvitation } = useTask();

    const [isLoading, setIsLoading] = useState(true);
    const [isAccepting, setIsAccepting] = useState(false);
    const [errorMsg, setErrorMsg] = useState<string | null>(null);
    const [previewData, setPreviewData] = useState<{ groupSeq: number; groupName: string; memberCount: number } | null>(null);

    useEffect(() => {
        const fetchPreview = async () => {
            if (!code) {
                setErrorMsg("초대 코드가 없습니다.");
                setIsLoading(false);
                return;
            }

            const res = await getInvitationPreview(code);
            if (res.success && res.data) {
                setPreviewData(res.data);
            } else {
                if (res.code === 'INVITATION_NOT_FOUND') {
                    setErrorMsg("유효하지 않거나 만료된 초대입니다");
                } else if (res.code === 'GROUP_NOT_FOUND') {
                    setErrorMsg("존재하지 않는 그룹입니다");
                } else {
                    setErrorMsg(res.message || "초대 정보를 불러오는데 실패했습니다.");
                }
            }
            setIsLoading(false);
        };

        fetchPreview();
    }, [code, getInvitationPreview]);

    const handleAccept = async () => {
        if (!code) return;
        setIsAccepting(true);
        const res = await acceptInvitation(code);
        setIsAccepting(false);

        if (res.success) {
            toast.success("가입되었습니다");
            navigate('/', { replace: true });
        } else {
            if (res.code === 'INVITATION_NOT_FOUND' || res.code === 'INVITATION_EXHAUSTED') {
                toast.error("유효하지 않거나 만료된 초대입니다");
            } else if (res.code === 'GROUP_NOT_FOUND') {
                toast.error("존재하지 않는 그룹입니다");
            } else if (res.code === 'GROUP_MEMBER_LIMIT_EXCEEDED') {
                toast.error("그룹 정원이 찼습니다");
            } else if (res.code === 'USER_GROUP_LIMIT_EXCEEDED') {
                toast.error("참여할 수 있는 그룹 수를 초과했습니다 (최대 5개)");
            } else {
                toast.error(res.message || "그룹 참여에 실패했습니다");
            }
        }
    };

    if (isLoading) {
        return (
            <PageContainer title="그룹 초대" description="초대 정보를 확인 중입니다...">
                <div className="flex justify-center py-12">
                    <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
                </div>
            </PageContainer>
        );
    }

    if (errorMsg || !previewData) {
        return (
            <PageContainer title="그룹 초대" description="초대 수락을 진행할 수 없습니다.">
                <div className="max-w-md mx-auto text-center mt-12 bg-red-50 p-6 rounded-lg border border-red-100">
                    <AlertCircle className="size-10 text-red-500 mx-auto mb-4" />
                    <h3 className="text-lg font-semibold text-red-800 mb-2">초대 오류</h3>
                    <p className="text-red-600">{errorMsg || "알 수 없는 오류가 발생했습니다."}</p>
                    <Button className="mt-6 w-full" variant="outline" onClick={() => navigate('/')}>
                        홈으로 돌아가기
                    </Button>
                </div>
            </PageContainer>
        );
    }

    return (
        <PageContainer title="그룹 초대" description="새로운 그룹에 참여하세요.">
            <div className="max-w-md mx-auto mt-12 bg-white p-8 rounded-xl shadow-sm border border-gray-100 text-center">
                <div className="size-16 bg-blue-100 text-blue-600 rounded-full flex items-center justify-center mx-auto mb-6">
                    <Users className="size-8" />
                </div>
                
                <h2 className="text-2xl font-bold text-gray-900 mb-2">{previewData.groupName}</h2>
                <p className="text-gray-500 mb-8 flex items-center justify-center gap-2">
                    <Users className="size-4" /> 현재 멤버 {previewData.memberCount}명
                </p>

                <div className="space-y-4">
                    <p className="text-gray-700 font-medium">이 그룹에 참여하시겠습니까?</p>
                    
                    <div className="flex gap-3">
                        <Button
                            variant="outline"
                            className="flex-1"
                            onClick={() => navigate('/')}
                            disabled={isAccepting}
                        >
                            거절
                        </Button>
                        <Button
                            className="flex-1 bg-blue-600 hover:bg-blue-700 text-white"
                            onClick={handleAccept}
                            disabled={isAccepting}
                        >
                            {isAccepting ? "수락 중..." : "수락하기"}
                        </Button>
                    </div>
                </div>
            </div>
        </PageContainer>
    );
}
