import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, it, expect, vi, beforeEach } from "vitest";
import { MemoryRouter } from "react-router";
import SignupPage from "./SignupPage";
import { useTask } from "../context/TaskContext";

// context, router 모킹
vi.mock("../context/TaskContext", () => ({
    useTask: vi.fn(),
}));

const mockNavigate = vi.fn();
vi.mock("react-router", async () => {
    const actual = await vi.importActual("react-router");
    return {
        ...actual as any,
        useNavigate: () => mockNavigate,
    };
});

describe("SignupPage", () => {
    const mockSignup = vi.fn();

    beforeEach(() => {
        vi.clearAllMocks();
        (useTask as any).mockReturnValue({
            signup: mockSignup,
        });
    });

    it("회원가입 폼이 정상적으로 렌더링되어야 한다", () => {
        render(
            <MemoryRouter>
                <SignupPage />
            </MemoryRouter>
        );

        expect(screen.getByRole("heading", { name: /sign up/i })).toBeInTheDocument();
        expect(screen.getByLabelText(/id/i, { selector: 'input' })).toBeInTheDocument();
        expect(screen.getByLabelText(/nickname/i, { selector: 'input' })).toBeInTheDocument();
        expect(screen.getByLabelText(/password/i, { selector: 'input' })).toBeInTheDocument();
        expect(screen.getByRole("button", { name: /회원가입/i })).toBeInTheDocument();
    });

    it("잘못된 ID를 입력하면 유효성 검사 에러 메세지를 띄워야 한다", async () => {
        render(
            <MemoryRouter>
                <SignupPage />
            </MemoryRouter>
        );

        const user = userEvent.setup();
        const idInput = screen.getByLabelText(/id/i, { selector: 'input' });
        const nicknameInput = screen.getByLabelText(/nickname/i, { selector: 'input' });
        const passwordInput = screen.getByLabelText(/password/i, { selector: 'input' });
        const submitButton = screen.getByRole("button", { name: /회원가입/i });

        // 4글자 미만 입력 세팅
        await user.type(idInput, "abc");
        await user.type(nicknameInput, "Nickname123");
        await user.type(passwordInput, "Password123!");
        
        fireEvent.click(submitButton);

        expect(await screen.findByText(/ID는 4~50자의 영문 대소문자 및 숫자만 가능합니다/i)).toBeInTheDocument();
        expect(mockSignup).not.toHaveBeenCalled();
    });

    it("올바른 형태의 데이터를 입력하면 signup API가 호출되고 /login 으로 이동해야 한다", async () => {
        mockSignup.mockResolvedValue({ success: true, message: "" });

        // window.alert 모킹
        const alertMock = vi.spyOn(window, "alert").mockImplementation(() => { });

        render(
            <MemoryRouter>
                <SignupPage />
            </MemoryRouter>
        );

        const user = userEvent.setup();
        await user.type(screen.getByLabelText(/id/i, { selector: 'input' }), "testUser123");
        await user.type(screen.getByLabelText(/nickname/i, { selector: 'input' }), "TestNickname");
        await user.type(screen.getByLabelText(/password/i, { selector: 'input' }), "Password123!");

        const submitButton = screen.getByRole("button", { name: /회원가입/i });
        fireEvent.click(submitButton);

        await waitFor(() => {
            expect(mockSignup).toHaveBeenCalledWith({
                userId: "testUser123",
                userName: "TestNickname",
                password: "Password123!",
                avatarImg: undefined,
            });
        });

        expect(alertMock).toHaveBeenCalledWith("회원가입이 완료되었습니다. 로그인해 주세요.");
        expect(mockNavigate).toHaveBeenCalledWith("/login");

        alertMock.mockRestore();
    });
});
