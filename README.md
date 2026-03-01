# 🏠 Check Tracker (Homeware Project)

가족 및 소규모 그룹을 위한 맞춤형 체크리스트 관리 시스템, **Check Tracker**입니다.  
단순한 할 일 목록을 넘어, 누가 작업을 등록하고 완료했는지에 대한 '히스토리'와 '일정 시각화'에 집중한 프로젝트입니다.

---

## ✨ 주요 기능 (Key Features)

### 1. 스마트 체크리스트 관리
* **신속한 추가**: 텍스트 입력과 엔터키만으로 간편하게 할 일을 등록할 수 있습니다.
* **참여자 식별**: 각 항목마다 작성자와 완료자의 아바타 및 이름을 표시하여 그룹 내 협업 투명성을 높였습니다.
* **상태 제어**: 체크박스를 통해 완료 상태를 즉시 토글하며, 완료 시 취소선 등의 시각적 피드백을 제공합니다.

### 2. 마감 기한 및 일정 시각화
* **D-Day 배지**: 마감일에 따라 `Overdue`, `Due Today`, `Due in X days` 등의 상태 배지를 자동으로 부여합니다.
* **다양한 뷰 모드**: Monthly(월간), Weekly(주간), Daily(일간) 필터를 통해 일정의 규모에 맞는 최적의 뷰를 제공합니다.

### 3. 히스토리 추적 (Activity Log)
* **상세 타임라인**: 'i' 아이콘 클릭 시 해당 태스크의 생성, 완료, 취소 이력을 시간순으로 보여주는 전용 다이얼로그를 제공합니다.
* **데이터 투명성**: 누가 언제 무엇을 변경했는지 기록하여 가족 간 소통의 오류를 방지합니다.

---

## 🛠 기술 스택 (Tech Stack)

### Frontend
* **Core**: React (TSX)
* **Styling**: Tailwind CSS, Lucide React (Icons)
* **UI Components**: Radix UI (Popover), React Day Picker
* **Utilities**: date-fns, clsx, tailwind-merge, class-variance-authority (CVA)

### Backend (Developing)
* **Framework**: Spring Boot
* **Persistence**: MyBatis
* **Database/Cache**: Redis

---

## 📱 반응형 레이아웃 (Responsive Design)
* **PC**: 넓은 화면을 활용한 사이드바(Sidebar) 네비게이션으로 모든 메뉴에 빠르게 접근 가능합니다.
* **Mobile**: 모바일 환경에서 한 손 조작이 편리하도록 하단 탭 바(Bottom Tab Bar) 인터페이스를 지원합니다.

---

## 📅 로드맵 (Roadmap)
- [x] 1차 프로토타입 UI 구현 (2026.02.12 완료)
- [ ] Spring Boot & MyBatis 백엔드 API 연동
- [ ] Redis를 활용한 데이터 캐싱 및 성능 최적화
- [ ] 다국어 지원 및 다크모드 테마 적용