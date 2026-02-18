import { useState } from 'react'
import { Plus, Calendar, CalendarClock, CalendarDays, CalendarRange } from 'lucide-react'
import { Button } from './components/ui/Button'
import { Input } from './components/ui/Input'
import { ChecklistItem } from './components/ChecklistItem'
import { Tabs, TabsList, TabsTrigger, TabsContent } from './components/ui/Tabs'
import { DatePicker } from './components/ui/DatePicker'
import { format } from "date-fns"
import { cn } from "./lib/utils"

/**
 * 메인 애플리케이션 컴포넌트
 * 체크리스트의 상태 관리 및 전체적인 레이아웃을 담당합니다.
 */
function App() {
  // 현재 사용자 (더미)
  const currentUser = {
    name: "Me",
    avatarUrl: "https://github.com/shadcn.png"
  }

  // 더미 데이터 생성 헬퍼
  const createHistory = (action, user, date) => ({
    id: Math.random().toString(36).substr(2, 9),
    type: action, // "created", "completed", "uncompleted"
    userName: user.name,
    userAvatar: user.avatarUrl,
    timestamp: format(date, "MMM d, yyyy 'at' h:mm a")
  });

  // 상태 관리
  // viewMode: 'daily' | 'weekly' | 'monthly'
  const [viewMode, setViewMode] = useState('monthly');
  const [items, setItems] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  // 입력 필드 상태 관리
  const [inputValue, setInputValue] = useState("")
  const [date, setDate] = useState()

  // API 데이터 가져오기
  useEffect(() => {
    const fetchTasks = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const response = await fetch(`/api/v1/tasks?viewMode=${viewMode}`);
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const result = await response.json();

        if (result.status === 'SC') { // Success Code
          // API 데이터 매핑
          //   taskId -> id
          //   title -> text
          //   duedate -> dueDate
          //   taskStatus -> completed (CREATED/UNCOMPLETED -> false, COMPLETED -> true)
          //   creator -> creatorName
          const mappedItems = (result.data || []).map(task => ({
            id: String(task.taskId),
            text: task.title,
            completed: task.taskStatus === 'COMPLETED',
            dueDate: task.duedate ? new Date(task.duedate).toISOString() : undefined,
            creatorName: task.creator,
            // 더미 데이터나 기본값 사용 (API에 없는 필드)
            creatorAvatar: "https://github.com/shadcn.png", // Default avatar
            history: [], // History not in API yet
          }));
          setItems(mappedItems);
        } else {
          // FA (Failure) or other status
          console.warn("API returned non-success status:", result);
          setItems([]); // Clear items or handle error
          if (result.message) {
            setError(result.message);
          }
        }
      } catch (e) {
        console.error("Failed to fetch tasks:", e);
        setError(e.message);
        setItems([]);
      } finally {
        setIsLoading(false);
      }
    };

    fetchTasks();
  }, [viewMode]); // viewMode 변경 시 재호출

  /**
   * 새로운 아이템 추가 핸들러
   * 입력값이 비어있지 않으면 새로운 아이템을 생성하여 목록에 추가합니다.
   */
  const handleAddItem = () => {
    if (!inputValue.trim()) return

    const now = new Date();
    const newItem = {
      id: Date.now().toString(),
      text: inputValue,
      completed: false,
      creatorName: currentUser.name,
      creatorAvatar: currentUser.avatarUrl,
      dueDate: date ? date.toISOString() : undefined,
      history: [
        createHistory("created", currentUser, now)
      ]
    }

    // Optimistic update (API 연동 시 실제로는 POST 요청 후 다시 fetch 해야 함)
    setItems([...items, newItem])
    setInputValue("") // 입력 필드 초기화
    setDate(undefined) // 날짜 초기화
  }

  /**
   * 입력 필드에서 엔터 키 입력 시 아이템 추가 처리
   */
  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      handleAddItem()
    }
  }

  /**
   * 아이템 완료 상태 토글 핸들러
   * @param {string} id - 토글할 아이템의 ID
   */
  const handleToggleItem = (id) => {
    setItems(items.map(item => {
      if (item.id !== id) return item

      const newCompleted = !item.completed
      const now = new Date();

      let newHistory = [...(item.history || [])];

      if (newCompleted) {
        newHistory.push(createHistory("completed", currentUser, now));
      } else {
        newHistory.push(createHistory("uncompleted", currentUser, now));
      }

      return {
        ...item,
        completed: newCompleted,
        completerName: newCompleted ? currentUser.name : undefined,
        completerAvatar: newCompleted ? currentUser.avatarUrl : undefined,
        completedDate: newCompleted ? format(now, "MMM d, yyyy") : undefined,
        history: newHistory
      }
    }))
  }

  /**
   * 아이템 삭제 핸들러
   * @param {string} id - 삭제할 아이템의 ID
   */
  const handleDeleteItem = (id) => {
    setItems(items.filter(item => item.id !== id))
  }



  // 완료된 아이템 수 계산
  const completedCount = items.filter(i => i.completed).length
  const totalCount = items.length

  return (
    <div className="size-full min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-6">
      <div className="w-full max-w-4xl bg-white rounded-2xl shadow-xl p-8">
        {/* 헤더 영역: 제목 및 진행 상황 표시 */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold mb-2">My Tasks</h1>
          <p className="text-gray-500">
            {completedCount} of {totalCount} completed
          </p>
        </div>

        {/* 입력 및 추가 버튼 영역 */}
        <div className="flex gap-2 mb-6">
          <Input
            placeholder="Add a new item..."
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            onKeyDown={handleKeyDown}
            className="text-base"
          />
          <DatePicker
            selected={date}
            onChange={(date) => setDate(date)}
            dateFormat="yyyy.MM.dd"
            shouldCloseOnSelect
            minDate={new Date('2000-01-01')}
            maxDate={undefined}
            placeholder="Due date"
            className="w-auto gap-2 border-gray-200 hover:bg-gray-50"
          />
          <Button onClick={handleAddItem} className="gap-2">
            <Plus className="h-5 w-5" />
            Add
          </Button>
        </div>

        {/* 탭 네비게이션 */}
        <Tabs value={viewMode} onValueChange={setViewMode} className="w-full">
          <TabsList className="grid w-full grid-cols-3 mb-6 bg-muted p-1 rounded-xl h-auto">
            <TabsTrigger value="daily" className="rounded-xl py-2 gap-2">
              <CalendarClock className="size-4" /> Daily
            </TabsTrigger>
            <TabsTrigger value="weekly" className="rounded-xl py-2 gap-2">
              <CalendarDays className="size-4" /> Weekly
            </TabsTrigger>
            <TabsTrigger value="monthly" className="rounded-xl py-2 gap-2">
              <CalendarRange className="size-4" /> Monthly
            </TabsTrigger>
          </TabsList>

          <TabsContent value={viewMode} className="space-y-8 mt-0">
            {/* Loading & Error States */}
            {isLoading && <div className="text-center py-10 text-gray-500">Loading tasks...</div>}
            {error && <div className="text-center py-10 text-red-500">Error: {error}</div>}

            {/* Task List */}
            {!isLoading && !error && (
              <div>
                <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
                  Tasks <span className="text-sm font-normal text-gray-400">({items.length})</span>
                </h2>
                <div className="space-y-4">
                  {items.map((item) => (
                    <ChecklistItem
                      key={item.id}
                      {...item}
                      onToggle={handleToggleItem}
                      onDelete={handleDeleteItem}
                    />
                  ))}
                  {items.length === 0 && (
                    <div className="text-center py-10 text-gray-500 bg-gray-50 rounded-xl border border-dashed">
                      No tasks found for this view.
                    </div>
                  )}
                </div>
              </div>
            )}
          </TabsContent>
        </Tabs>
      </div>
    </div>
  )

}

export default App
