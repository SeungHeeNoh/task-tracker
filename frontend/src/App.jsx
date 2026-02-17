import { useState } from 'react'
import { Plus, Calendar, CalendarClock, CalendarDays, CalendarRange } from 'lucide-react'
import { Button } from './components/ui/Button'
import { Input } from './components/ui/Input'
import { ChecklistItem } from './components/ChecklistItem'
import { Tabs, TabsList, TabsTrigger, TabsContent } from './components/ui/Tabs'
import { DatePicker } from './components/ui/DatePicker'
import { format, addDays, isSameDay, isAfter, isBefore, startOfDay } from "date-fns"
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

  // 체크리스트 아이템 상태 관리 (상세 더미 데이터 포함)
  const [items, setItems] = useState([
    {
      id: "1",
      text: "Buy groceries",
      completed: false,
      dueDate: addDays(new Date(), 1).toISOString(), // Tomorrow
      creatorName: "Sarah Johnson",
      creatorAvatar: "https://images.unsplash.com/photo-1649589244330-09ca58e4fa64?w=100&auto=format&fit=crop&q=60",
      history: [
        createHistory("created", { name: "Sarah Johnson", avatarUrl: "https://images.unsplash.com/photo-1649589244330-09ca58e4fa64?w=100&auto=format&fit=crop&q=60" }, new Date())
      ]
    },
    {
      id: "2",
      text: "Finish project report",
      completed: true,
      dueDate: new Date().toISOString(), // Today (completed)
      creatorName: "Michael Chen",
      creatorAvatar: "https://images.unsplash.com/photo-1554765345-6ad6a5417cde?w=100&auto=format&fit=crop&q=60",
      completerName: "Emily Rodriguez",
      completerAvatar: "https://images.unsplash.com/photo-1573497019940-1c28c88b4f3e?w=100&auto=format&fit=crop&q=60",
      completedDate: "Feb 14, 2026",
      history: [
        createHistory("created", { name: "Michael Chen", avatarUrl: "https://images.unsplash.com/photo-1554765345-6ad6a5417cde?w=100&auto=format&fit=crop&q=60" }, addDays(new Date(), -2)),
        createHistory("completed", { name: "Emily Rodriguez", avatarUrl: "https://images.unsplash.com/photo-1573497019940-1c28c88b4f3e?w=100&auto=format&fit=crop&q=60" }, new Date())
      ]
    },
    {
      id: "3",
      text: "Call dentist",
      completed: false,
      dueDate: addDays(new Date(), 3).toISOString(), // 3 days later
      creatorName: "Alex Kim",
      creatorAvatar: "https://images.unsplash.com/photo-1510947565940-a38e2443c426?w=100&auto=format&fit=crop&q=60",
      history: [
        createHistory("created", { name: "Alex Kim", avatarUrl: "https://images.unsplash.com/photo-1510947565940-a38e2443c426?w=100&auto=format&fit=crop&q=60" }, new Date())
      ]
    },
    {
      id: "4",
      text: "Team meeting",
      completed: false,
      dueDate: new Date().toISOString(), // Due Today
      creatorName: "Sarah Johnson",
      creatorAvatar: "https://images.unsplash.com/photo-1649589244330-09ca58e4fa64?w=100&auto=format&fit=crop&q=60",
      history: [
        createHistory("created", { name: "Sarah Johnson", avatarUrl: "https://images.unsplash.com/photo-1649589244330-09ca58e4fa64?w=100&auto=format&fit=crop&q=60" }, new Date())
      ]
    },
    {
      id: "5",
      text: "Review quarterly reports",
      completed: false,
      dueDate: "2026-02-28T00:00:00.000Z", // Fixed date
      creatorName: "Michael Chen",
      creatorAvatar: "https://images.unsplash.com/photo-1554765345-6ad6a5417cde?w=100&auto=format&fit=crop&q=60",
      history: [
        createHistory("created", { name: "Michael Chen", avatarUrl: "https://images.unsplash.com/photo-1554765345-6ad6a5417cde?w=100&auto=format&fit=crop&q=60" }, new Date())
      ]
    },
  ])

  // 입력 필드 상태 관리
  const [inputValue, setInputValue] = useState("")
  const [date, setDate] = useState()

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

  // Helper functions for date categorization
  const isDueToday = (dueDateStr) => {
    if (!dueDateStr) return false;
    return isSameDay(new Date(dueDateStr), new Date());
  }

  const isDueTomorrow = (dueDateStr) => {
    if (!dueDateStr) return false;
    return isSameDay(new Date(dueDateStr), addDays(new Date(), 1));
  }

  const isDidThisWeek = (dueDateStr) => {
    if (!dueDateStr) return false;
    const due = new Date(dueDateStr);
    const today = startOfDay(new Date());
    const nextWeek = addDays(today, 7);
    return isAfter(due, today) && isBefore(due, nextWeek);
  }

  const isDueThisMonth = (dueDateStr) => {
    if (!dueDateStr) return false;
    const due = new Date(dueDateStr);
    const today = new Date();
    return due.getMonth() === today.getMonth() && due.getFullYear() === today.getFullYear();
  }


  // 완료된 아이템 수 계산
  const completedCount = items.filter(i => i.completed).length
  const totalCount = items.length

  return (
    <div className="size-full min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-6">
      <div className="w-full max-w-4xl bg-white rounded-2xl shadow-xl p-8">
        {/* 헤더 영역: 제목 및 진행 상황 표시 */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold mb-2">My Checklist</h1>
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
        <Tabs defaultValue="monthly" className="w-full">
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

          <TabsContent value="monthly" className="space-y-8 mt-0">
            {/* 섹션 헤더 */}
            <div>
              <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
                This Month <span className="text-sm font-normal text-gray-400">({items.filter(i => isDueThisMonth(i.dueDate)).length})</span>
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
                    No items for this month. Add one above!
                  </div>
                )}
              </div>
            </div>
          </TabsContent>

          <TabsContent value="weekly" className="space-y-8 mt-0">
            {/* This Week Section */}
            <div>
              <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
                This Week <span className="text-sm font-normal text-gray-400">({items.filter(i => isDidThisWeek(i.dueDate)).length})</span>
              </h2>
              <div className="space-y-4">
                {items.filter(i => isDidThisWeek(i.dueDate)).map(item => (
                  <ChecklistItem
                    key={item.id}
                    {...item}
                    onToggle={handleToggleItem}
                    onDelete={handleDeleteItem}
                  />
                ))}
              </div>
            </div>

            {/* Next Week Section (Dummy logic for now, using fixed date) */}
            <div>
              <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
                Future <span className="text-sm font-normal text-gray-400">({items.filter(i => i.dueDate && !isDidThisWeek(i.dueDate) && !isDueToday(i.dueDate) && !isDueTomorrow(i.dueDate)).length})</span>
              </h2>
              <div className="space-y-4">
                {items.filter(i => i.dueDate && !isDidThisWeek(i.dueDate) && !isDueToday(i.dueDate) && !isDueTomorrow(i.dueDate)).map(item => (
                  <ChecklistItem
                    key={item.id}
                    {...item}
                    onToggle={handleToggleItem}
                    onDelete={handleDeleteItem}
                  />
                ))}
              </div>
            </div>
          </TabsContent>

          <TabsContent value="daily" className="space-y-8 mt-0">
            {/* Today Section */}
            <div>
              <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
                Today <span className="text-sm font-normal text-gray-400">({items.filter(i => isDueToday(i.dueDate)).length})</span>
              </h2>
              <div className="space-y-4">
                {items.filter(i => isDueToday(i.dueDate)).map(item => (
                  <ChecklistItem
                    key={item.id}
                    {...item}
                    onToggle={handleToggleItem}
                    onDelete={handleDeleteItem}
                  />
                ))}
              </div>
            </div>

            {/* Tomorrow Section */}
            <div>
              <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
                Tomorrow <span className="text-sm font-normal text-gray-400">({items.filter(i => isDueTomorrow(i.dueDate)).length})</span>
              </h2>
              <div className="space-y-4">
                {items.filter(i => isDueTomorrow(i.dueDate)).map(item => (
                  <ChecklistItem
                    key={item.id}
                    {...item}
                    onToggle={handleToggleItem}
                    onDelete={handleDeleteItem}
                  />
                ))}
              </div>
            </div>

            {/* Later Section */}
            <div>
              <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
                Later <span className="text-sm font-normal text-gray-400">({items.filter(i => i.dueDate && !isDueToday(i.dueDate) && !isDueTomorrow(i.dueDate)).length})</span>
              </h2>
              <div className="space-y-4">
                {items.filter(i => i.dueDate && !isDueToday(i.dueDate) && !isDueTomorrow(i.dueDate)).map(item => (
                  <ChecklistItem
                    key={item.id}
                    {...item}
                    onToggle={handleToggleItem}
                    onDelete={handleDeleteItem}
                  />
                ))}
              </div>
            </div>
          </TabsContent>
        </Tabs>
      </div>
    </div>
  )
}

export default App
