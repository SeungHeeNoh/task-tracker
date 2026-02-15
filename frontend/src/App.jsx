import { useState } from 'react'
import { Plus } from 'lucide-react'
import { Button } from './components/ui/Button'
import { Input } from './components/ui/Input'
import { ChecklistItem } from './components/ChecklistItem'

/**
 * 메인 애플리케이션 컴포넌트
 * 체크리스트의 상태 관리 및 전체적인 레이아웃을 담당합니다.
 */
function App() {
  // 체크리스트 아이템 상태 관리 (상세 더미 데이터 포함)
  const [items, setItems] = useState([
    {
      id: 1,
      text: "Buy groceries",
      completed: false,
      creator: { name: "Sarah Johnson", avatarUrl: "https://images.unsplash.com/photo-1649589244330-09ca58e4fa64?w=100&auto=format&fit=crop&q=60" }
    },
    {
      id: 2,
      text: "Finish project report",
      completed: true,
      creator: { name: "Michael Chen", avatarUrl: "https://images.unsplash.com/photo-1554765345-6ad6a5417cde?w=100&auto=format&fit=crop&q=60" },
      completedBy: { name: "Emily Rodriguez", avatarUrl: "https://images.unsplash.com/photo-1573497019940-1c28c88b4f3e?w=100&auto=format&fit=crop&q=60", at: "Feb 14, 2026" }
    },
    {
      id: 3,
      text: "Call dentist",
      completed: false,
      creator: { name: "Alex Kim", avatarUrl: "https://images.unsplash.com/photo-1510947565940-a38e2443c426?w=100&auto=format&fit=crop&q=60" }
    },
  ])

  // 입력 필드 상태 관리
  const [inputValue, setInputValue] = useState("")

  // 현재 사용자 (더미)
  const currentUser = {
    name: "Me",
    avatarUrl: "https://github.com/shadcn.png"
  }

  /**
   * 새로운 아이템 추가 핸들러
   * 입력값이 비어있지 않으면 새로운 아이템을 생성하여 목록에 추가합니다.
   */
  const handleAddItem = () => {
    if (!inputValue.trim()) return
    const newItem = {
      id: Date.now(),
      text: inputValue,
      completed: false,
      creator: currentUser
    }
    setItems([...items, newItem])
    setInputValue("") // 입력 필드 초기화
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
   * @param {number} id - 토글할 아이템의 ID
   */
  const handleToggleItem = (id) => {
    setItems(items.map(item => {
      if (item.id !== id) return item

      const newCompleted = !item.completed
      return {
        ...item,
        completed: newCompleted,
        // 완료 시 현재 사용자가 완료한 것으로 기록, 미완료 시 정보 제거
        completedBy: newCompleted
          ? { name: currentUser.name, avatarUrl: currentUser.avatarUrl, at: new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }) }
          : undefined
      }
    }))
  }

  /**
   * 아이템 삭제 핸들러
   * @param {number} id - 삭제할 아이템의 ID
   */
  const handleDeleteItem = (id) => {
    setItems(items.filter(item => item.id !== id))
  }

  // 완료된 아이템 수 계산
  const completedCount = items.filter(i => i.completed).length
  const totalCount = items.length

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center p-6">
      <div className="w-full max-w-3xl bg-white rounded-2xl shadow-xl p-8">
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
          <Button onClick={handleAddItem} className="gap-2">
            <Plus className="h-5 w-5" />
            Add
          </Button>
        </div>

        {/* 체크리스트 목록 영역 */}
        <div className="space-y-4">
          {items.map(item => (
            <ChecklistItem
              key={item.id}
              {...item}
              onToggle={handleToggleItem}
              onDelete={handleDeleteItem}
            />
          ))}
          {/* 아이템이 없을 경우 안내 메시지 표시 */}
          {items.length === 0 && (
            <p className="text-center text-gray-400 py-8">No items yet. Add one above!</p>
          )}
        </div>
      </div>
    </div>
  )
}

export default App
