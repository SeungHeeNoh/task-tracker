import { useState } from "react";
import { useTask } from "../context/TaskContext";
import { useNavigate, Link } from "react-router";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { PageContainer } from "../components/PageContainer";
import { User, Image as ImageIcon, AlertCircle, Save, Users, Plus, Copy, Link as LinkIcon } from "lucide-react";
import { Avatar, AvatarFallback, AvatarImage } from "../components/ui/avatar";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter, DialogClose } from "../components/ui/dialog";
import { toast } from "sonner";

export default function ProfilePage() {
    const { currentUser, updateProfile } = useTask();
    const navigate = useNavigate();

    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [successMsg, setSuccessMsg] = useState<string | null>(null);

    // Form states
    const [nickname, setNickname] = useState(currentUser?.name || "");
    const [avatarImg, setAvatarImg] = useState<File | null>(null);

    // Group Invitation states
    const { groups, issueInvitation } = useTask();
    const [selectedGroupSeq, setSelectedGroupSeq] = useState<number | null>(null);
    const [isInviteDialogOpen, setIsInviteDialogOpen] = useState(false);
    const [inviteMaxUses, setInviteMaxUses] = useState(10);
    const [isIssuing, setIsIssuing] = useState(false);
    const [issuedInvite, setIssuedInvite] = useState<{ code: string; maxUses: number; expiresInSeconds: number } | null>(null);

    const handleIssueInviteClick = (groupSeq: number) => {
        setSelectedGroupSeq(groupSeq);
        setInviteMaxUses(10);
        setIssuedInvite(null);
        setIsInviteDialogOpen(true);
    };

    const handleIssueSubmit = async () => {
        if (!selectedGroupSeq) return;
        setIsIssuing(true);
        const res = await issueInvitation(selectedGroupSeq, inviteMaxUses);
        setIsIssuing(false);

        if (res.success && res.data) {
            setIssuedInvite(res.data);
            toast.success("초대 코드가 발급되었습니다.");
        } else {
            if (res.code === 'GROUP_ACCESS_DENIED') {
                toast.error("그룹의 OWNER만 초대 코드를 발급할 수 있습니다.");
            } else {
                toast.error(res.message || "초대 코드 발급에 실패했습니다.");
            }
        }
    };

    const copyToClipboard = (text: string, isLink: boolean = false) => {
        navigator.clipboard.writeText(text).then(() => {
            toast.success(isLink ? "초대 링크가 복사되었습니다." : "초대 코드가 복사되었습니다.");
        }).catch(() => {
            toast.error("복사에 실패했습니다.");
        });
    };

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

                <div className="mt-12 pt-8 border-t">
                    <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center gap-2">
                        <Users className="size-5" /> 내 그룹 관리
                    </h3>
                    <div className="space-y-4">
                        {groups && groups.filter(g => !isNaN(Number(g.id))).length > 0 ? (
                            groups.filter(g => !isNaN(Number(g.id))).map(group => (
                                <div key={group.id} className="flex flex-col sm:flex-row sm:items-center justify-between p-4 border rounded-lg bg-gray-50/50 gap-4">
                                    <div className="flex items-center gap-3">
                                        <div className="size-10 rounded-full flex items-center justify-center bg-gray-100 text-gray-600">
                                            <Users className="size-5" />
                                        </div>
                                        <div>
                                            <h4 className="font-medium text-gray-900">{group.name}</h4>
                                        </div>
                                    </div>
                                    <Button
                                        variant="outline"
                                        size="sm"
                                        onClick={() => handleIssueInviteClick(Number(group.id))}
                                        className="w-full sm:w-auto flex items-center gap-2"
                                    >
                                        <Plus className="size-4" /> 초대 코드 발급
                                    </Button>
                                </div>
                            ))
                        ) : (
                            <p className="text-sm text-gray-500 text-center py-4">가입된 그룹이 없습니다.</p>
                        )}
                    </div>
                </div>
            </div>

            <Dialog open={isInviteDialogOpen} onOpenChange={(open) => !open && setIsInviteDialogOpen(false)}>
                <DialogContent className="sm:max-w-md">
                    <DialogHeader>
                        <DialogTitle>그룹 초대 코드 발급</DialogTitle>
                        <DialogDescription>
                            새로운 멤버를 그룹에 초대하기 위한 코드를 생성합니다.
                        </DialogDescription>
                    </DialogHeader>

                    {!issuedInvite ? (
                        <div className="space-y-4 py-4">
                            <div className="space-y-2">
                                <Label htmlFor="maxUses">최대 사용 횟수 (1~100)</Label>
                                <Input
                                    id="maxUses"
                                    type="number"
                                    min={1}
                                    max={100}
                                    value={inviteMaxUses}
                                    onChange={(e) => setInviteMaxUses(Number(e.target.value) || 10)}
                                    className="bg-gray-50"
                                />
                            </div>
                            <DialogFooter className="pt-4">
                                <Button variant="outline" onClick={() => setIsInviteDialogOpen(false)}>취소</Button>
                                <Button onClick={handleIssueSubmit} disabled={isIssuing} className="bg-blue-600 hover:bg-blue-700 text-white">
                                    {isIssuing ? "발급 중..." : "발급하기"}
                                </Button>
                            </DialogFooter>
                        </div>
                    ) : (
                        <div className="space-y-6 py-4">
                            <div className="bg-gray-50 p-4 rounded-lg border text-center space-y-2">
                                <p className="text-sm text-gray-500">초대 코드</p>
                                <p className="text-3xl font-mono font-bold tracking-widest text-blue-600">{issuedInvite.code}</p>
                                <p className="text-xs text-gray-500 mt-2">
                                    만료: {Math.floor(issuedInvite.expiresInSeconds / 3600)}시간 남음 • 잔여 {issuedInvite.maxUses}회
                                </p>
                            </div>
                            <div className="space-y-4">
                                <div className="space-y-1">
                                    <Label className="text-xs text-gray-500 font-medium">초대 링크</Label>
                                    <div className="flex items-center gap-2">
                                        <div className="flex-1 bg-white border rounded-md p-2 text-sm text-gray-600 truncate select-all font-mono">
                                            {`${window.location.origin}/invite/${issuedInvite.code}`}
                                        </div>
                                        <Button variant="default" size="icon" className="shrink-0 bg-blue-600 hover:bg-blue-700 text-white" onClick={() => copyToClipboard(`${window.location.origin}/invite/${issuedInvite.code}`, true)}>
                                            <Copy className="size-4" />
                                        </Button>
                                    </div>
                                </div>
                                
                                <div className="space-y-1">
                                    <Label className="text-xs text-gray-500 font-medium">초대 코드 (직접 입력용)</Label>
                                    <div className="flex items-center gap-2">
                                        <div className="flex-1 bg-white border rounded-md p-2 text-sm text-gray-600 truncate select-all font-mono tracking-widest text-center">
                                            {issuedInvite.code}
                                        </div>
                                        <Button variant="outline" size="icon" className="shrink-0" onClick={() => copyToClipboard(issuedInvite.code)}>
                                            <Copy className="size-4" />
                                        </Button>
                                    </div>
                                </div>
                            </div>

                            <div className="mt-4 border-t pt-4">
                                <Button variant="ghost" className="w-full" onClick={() => setIsInviteDialogOpen(false)}>
                                    닫기
                                </Button>
                            </div>
                        </div>
                    )}
                </DialogContent>
            </Dialog>
        </PageContainer>
    );
}
