import { Button } from "./ui/Button"
import { Checkbox } from "./ui/Checkbox"
import { Avatar, AvatarImage, AvatarFallback } from "./ui/Avatar"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogTrigger } from "./ui/Dialog"
import { Trash2, CircleCheck, History, Clock, Calendar } from "lucide-react"
import { cn } from "../lib/utils"

/**
 * 개별 체크리스트 아이템 컴포넌트
 * 카드 형태의 레이아웃으로 변경되었습니다.
 * 
 * @param {Object} props
 * @param {number} props.id - 아이템 고유 ID
 * @param {string} props.text - 할 일 텍스트
 * @param {boolean} props.completed - 완료 여부
 * @param {Object} props.creator - 작성자 정보 { name, avatarUrl }
 * @param {string} [props.dueDate] - 마감 기한 (선택) e.g., "Due in 1 day"
 * @param {Object} [props.completedBy] - 완료자 정보 (선택) { name, avatarUrl, at }
 * @param {Function} props.onToggle - 완료 상태 변경 핸들러
 * @param {Function} props.onDelete - 아이템 삭제 핸들러
 */
export function ChecklistItem({ id, text, completed, creator, dueDate, completedBy, onToggle, onDelete }) {
  return (
    <div className="relative rounded-xl border bg-white shadow-sm hover:shadow-md transition-shadow p-5 group">
      <div className="flex items-start gap-4">
        <div className="pt-1">
          <Checkbox
            id={`item-${id}`}
            checked={completed}
            onCheckedChange={() => onToggle(id)}
          />
        </div>

        <div className="flex-1 min-w-0">
          <div className="flex items-start justify-between gap-2 mb-3">
            <label
              htmlFor={`item-${id}`}
              className={cn(
                "block cursor-pointer flex-1 text-base font-medium transition-colors",
                completed ? "line-through text-gray-400" : "text-gray-900"
              )}
            >
              {text}
            </label>
            {dueDate && !completed && (
              <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-medium text-orange-600 bg-orange-50 shrink-0">
                <Calendar className="size-3" />
                {dueDate}
              </span>
            )}
          </div>

          <div className="flex items-center justify-between gap-4 flex-wrap">
            {/* 작성자 정보 */}
            <div className="flex items-center gap-2">
              <Avatar>
                <AvatarImage src={creator.avatarUrl} alt={creator.name} />
                <AvatarFallback>{creator.name[0]}</AvatarFallback>
              </Avatar>
              <span className="text-sm text-gray-500">Created by {creator.name}</span>
            </div>

            {/* 완료 정보 (완료된 경우에만 표시) */}
            {completed && completedBy && (
              <div className="flex items-center gap-2">
                <CircleCheck className="size-4 text-green-500" />
                <Avatar>
                  <AvatarImage src={completedBy.avatarUrl} alt={completedBy.name} />
                  <AvatarFallback>{completedBy.name[0]}</AvatarFallback>
                </Avatar>
                <span className="text-sm text-gray-500">
                  Completed by {completedBy.name}
                  <span className="text-gray-400"> • {completedBy.at}</span>
                </span>
              </div>
            )}
          </div>
        </div>

        <div className="flex gap-1 shrink-0">
          <Dialog>
            <DialogTrigger asChild>
              <Button
                variant="ghost"
                size="icon"
                className="h-9 w-9 opacity-0 group-hover:opacity-100 transition-opacity text-blue-500 hover:text-blue-600 hover:bg-blue-50"
              >
                <History className="size-4" />
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-lg max-w-md">
              <DialogHeader>
                <DialogTitle>Checklist History</DialogTitle>
                <DialogDescription>
                  View the complete timeline of events for this checklist item.
                </DialogDescription>
              </DialogHeader>
              <div className="mt-4">
                <div className="relative">
                  {/* Vertical line through timeline */}
                  <div className="absolute left-4 top-0 bottom-0 w-0.5 bg-gray-200" />

                  <div className="space-y-6">
                    {/* Event: Completed (Conditional) */}
                    {completed && completedBy && (
                      <div className="relative flex gap-3">
                        <div className="relative z-10 flex items-center justify-center size-8 rounded-full bg-white border-2 border-gray-200">
                          <CircleCheck className="size-4 text-green-500" />
                        </div>
                        <div className="flex-1 pt-1">
                          <div className="flex items-center gap-2 mb-1">
                            <Avatar className="size-6">
                              <AvatarImage src={completedBy.avatarUrl} alt={completedBy.name} />
                              <AvatarFallback>{completedBy.name[0]}</AvatarFallback>
                            </Avatar>
                            <span className="text-sm">
                              <span className="font-medium">{completedBy.name}</span>{" "}
                              <span className="text-gray-600">completed this item</span>
                            </span>
                          </div>
                          <p className="text-xs text-gray-400">{completedBy.at} at 3:45 PM</p>
                        </div>
                      </div>
                    )}

                    {/* Event: Created */}
                    <div className="relative flex gap-3">
                      <div className="relative z-10 flex items-center justify-center size-8 rounded-full bg-white border-2 border-gray-200">
                        <Clock className="size-4 text-blue-500" />
                      </div>
                      <div className="flex-1 pt-1">
                        <div className="flex items-center gap-2 mb-1">
                          <Avatar className="size-6">
                            <AvatarImage src={creator.avatarUrl} alt={creator.name} />
                            <AvatarFallback>{creator.name[0]}</AvatarFallback>
                          </Avatar>
                          <span className="text-sm">
                            <span className="font-medium">{creator.name}</span>{" "}
                            <span className="text-gray-600">created this item</span>
                          </span>
                        </div>
                        <p className="text-xs text-gray-400">Feb 13, 2026 at 10:00 AM</p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </DialogContent>
          </Dialog>

          <Button
            variant="ghost"
            size="icon"
            className="h-9 w-9 opacity-0 group-hover:opacity-100 transition-opacity text-red-500 hover:text-red-600 hover:bg-red-50"
            onClick={() => onDelete(id)}
          >
            <Trash2 className="h-4 w-4" />
          </Button>
        </div>
      </div>
    </div>
  )
}