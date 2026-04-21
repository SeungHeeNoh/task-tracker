# 🏠 Check Tracker (Homeware Project)

가족 및 소규모 그룹을 위한 맞춤형 체크리스트 관리 시스템.
단순한 할 일 목록을 넘어, 누가 작업을 등록하고 완료했는지에 대한 **히스토리**와 **일정 시각화**에 집중한 프로젝트.

---

## 🛠 기술 스택

### Frontend
- **Core**: React (TSX)
- **Styling**: Tailwind CSS, Lucide React (Icons)
- **UI Components**: Radix UI (Popover), React Day Picker
- **Utilities**: date-fns, clsx, tailwind-merge, CVA (class-variance-authority)

### Backend (개발 중)
- **Framework**: Spring Boot
- **Persistence**: MyBatis
- **Database/Cache**: Redis

---

## ✨ 주요 기능

1. **스마트 체크리스트 관리**: 참여자 식별(아바타/이름), 완료 상태 토글
2. **마감 기한 및 일정 시각화**: D-Day 배지(Overdue / Due Today / Due in X days), Monthly/Weekly/Daily 뷰
3. **히스토리 추적 (Activity Log)**: 생성/완료/취소 이력 타임라인, 변경 이력 투명성
4. **반응형 레이아웃**: PC(사이드바 네비게이션), Mobile(하단 탭 바)

---

## 🔒 보안 규칙

### 절대 커밋하지 말 것
- `.claude/`, `.env`, `*.key`, `*.pem`, `application-local.yml` 등 로컬/시크릿 파일
- 하드코딩된 비밀번호, API 키, JWT 시크릿, DB 접속 정보
- 의심되면 멈추고 확인 요청

### 시크릿 처리
- `application.yml`의 민감 값은 환경변수 또는 GitHub Secrets로 주입
- 평문 시크릿 발견 시 수정 제안만 하고 직접 수정 금지

### 인증/인가
- 비밀번호 저장은 반드시 `BCryptPasswordEncoder.encode()` 경유
- 신규 API는 기본 "인증 필요"로 설계. 익명 허용은 명시적 결정 시에만.
- 권한 체크는 Controller가 아닌 Service 또는 필터 레이어에서
---

## 🌿 Git 규칙

### 브랜치
- `main` 직접 커밋 금지
- 작업 브랜치: `feature/issue-<번호>-<요약>` (예: `feature/issue-43-invitation`)
- 현재 진행 중인 다른 브랜치 침범 금지

### 커밋
- `git add .` 또는 `git add -A` 금지 — 반드시 파일 명시
- 커밋 전 `git status` + `git diff --staged` 결과 보고 후 승인 대기
- 커밋 메시지: `<type>: <한글 요약>` (예: `feat: 그룹 초대 API 추가`)
    - type: feat, fix, refactor, test, docs, chore

### Worktree 작업 시
- 현재 워킹 디렉토리 경로와 브랜치를 작업 시작 전 보고
- 다른 worktree 디렉토리 참조 금지
---

## 📁 프로젝트 구조

```
check-tracker/
├── backend/check-tracker/              # Spring Boot 백엔드
│   └── src/main/java/com/hohohehe/tasktracker/
│       ├── common/                     # 공통 유틸리티
│       │   ├── enumCode/               # ResponseStatus, TaskStatus
│       │   ├── exception/              # JwtAuthenticationException, SystemException
│       │   ├── response/               # CommonResponse
│       │   ├── RootControllerAdvice.java  # 전역 예외 처리
│       │   └── SecurityContext.java    # 인증 컨텍스트 헬퍼
│       ├── config/                     # 설정
│       │   ├── jwt/                    # JWTAuthenticationFilter, TokenProvider, JwtProperties
│       │   ├── redis/                  # RedisConfig, RedisProperties
│       │   ├── MybatisConfig.java
│       │   ├── PasswordConfig.java
│       │   ├── SecurityConfig.java
│       │   └── WebConfig.java          # CORS 설정
│       ├── controller/v1/              # REST API 컨트롤러
│       │   ├── AuthController.java     # 로그인, 회원가입, 토큰 재발급
│       │   ├── TaskController.java     # 태스크 CRUD
│       │   └── UsersController.java    # 사용자 프로필 관리
│       ├── mapper/                     # MyBatis 매퍼 인터페이스
│       │   ├── TaskLogMapper.java
│       │   ├── TaskMapper.java
│       │   └── UsersMapper.java
│       ├── model/
│       │   ├── dto/                    # 응답 DTO (TaskDetail, TaskInfo, UserProfile 등)
│       │   │   └── request/            # 요청 DTO (Login, Join, ManageTask 등)
│       │   └── entity/                 # DB 엔티티 (Users, Task, TaskLog, Groups, UserGroupMap)
│       └── service/                    # 비즈니스 로직
│           ├── AuthService.java
│           ├── RedisService.java
│           ├── TaskService.java
│           └── UsersService.java
│
└── frontend/                           # React (Vite) 프론트엔드
    └── src/
        ├── app/
        │   ├── components/             # 레이아웃·공통 컴포넌트
        │   │   ├── ui/                 # shadcn/ui 기반 컴포넌트 모음
        │   │   ├── Layout.tsx          # 전체 레이아웃 (사이드바/탭바)
        │   │   ├── Navbar.tsx
        │   │   ├── ProtectedRoute.tsx  # 인증 보호 라우트
        │   │   └── TaskItem.tsx
        │   ├── context/
        │   │   └── TaskContext.tsx     # 태스크 전역 상태
        │   ├── pages/                  # 페이지 컴포넌트
        │   │   ├── errors/             # 403, 404, 401 에러 페이지
        │   │   ├── Home.tsx
        │   │   ├── TasksPage.tsx
        │   │   ├── TaskDetail.tsx
        │   │   ├── CalendarView.tsx
        │   │   ├── LoginPage.tsx
        │   │   ├── SignupPage.tsx
        │   │   ├── ProfilePage.tsx
        │   │   └── PasswordChangePage.tsx
        │   ├── App.tsx
        │   └── routes.tsx              # 라우트 정의
        ├── styles/                     # Tailwind, 폰트, 테마
        └── main.tsx
```

---

## ⚙️ 개발 명령어
```bash
npm run dev       # 개발 서버 실행
npm run build     # 프로덕션 빌드
```

---

## 📐 코딩 컨벤션

### 🔹 파일 및 디렉토리 네이밍
- **컴포넌트 파일 (.tsx, .jsx)**: PascalCase 사용 (예: `TaskCard.tsx`, `LoginPage.tsx`)
- **페이지 컴포넌트**: 명확한 구분을 위해 보통 `Page` 접미사(예: `TasksPage.tsx`) 혹은 직관적 명칭(`CalendarView.tsx`, `TaskDetail.tsx`) 사용
- **일반 로직 / 라우터 / 스타일 파일**: camelCase 또는 kebab-case 사용 (예: `routes.tsx`, `theme.css`)

### 🔹 코드 스타일 및 네이밍
- **기본 언어**: React + TypeScript (신규 작성 시 `.tsx` 권장)
- **컴포넌트 선언**: Arrow function 및 명시적 Props 타입 지정 지향
- **함수/변수**: camelCase (예: `handleToggle`, `fetchData`)
- **타입/인터페이스**: PascalCase, 필요시 `I` prefix 또는 `Type` suffix 사용

### 🔹 스타일 및 UI 구현 (Tailwind + shadcn/ui 기반)
- **Tailwind 클래스 결합**: `clsx`와 `tailwind-merge`를 사용하여 스타일 충돌 방지 및 안전한 결합
- **컴포넌트 변형 관리**: `class-variance-authority` (cva)를 통한 일관성 있는 디자인 변형 추적
- **공통 UI 관리**: Radix UI 기반의 컴포넌트를 `app/components/ui/`에 구성하여 적극 재사용
- **아이콘**: 표준 아이콘으로 `lucide-react` 사용

### 🔹 폼 및 상태 관리
- **전역 상태**: React Context API 사용 (예: `TaskContext.tsx`)
- **폼 핸들링**: 폼과 입력 검증 등은 `react-hook-form` 라이브러리 활용

---

## 🗂 현재 개발 상태
- **Frontend**: 기본 라우팅(인증, 프로필, 캘린더, 할 일 관리) 및 UI 세팅 완료. API 연동 및 테스트 고도화 진행 중.
- **Backend (Spring Boot + MyBatis + Redis)**: Auth(로그인, 회원가입, 로그아웃) 및 Task(조회, 추가, 토글, 삭제) API 등 주요 기능 개발 진행 중.
- **API 연동**: `docs/wiki`에 API 명세서 지속 기록 중이며, 인증 API 등 주요 기능 연동 작업이 적극적으로 진행되고 있음.

---

## ⚠️ 주의사항
- 화면 개발 및 API 연동 시 `docs/wiki` 폴더 내의 API 문서를 반드시 참고할 것.
- 반응형 작업 시 PC(사이드바)와 Mobile(탭 바) 두 레이아웃 모두 고려할 것.
