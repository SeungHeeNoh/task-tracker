import { useState } from "react";
import { useTask } from "../context/TaskContext";
import { Link } from "react-router";
import { TaskItem } from "../components/TaskItem";
import { Tabs, TabsList, TabsTrigger } from "../components/ui/tabs";
import { CalendarDays, CalendarRange, ChevronLeft, ChevronRight } from "lucide-react";
import { format, startOfMonth, endOfMonth, startOfWeek, endOfWeek, addMonths, subMonths, addWeeks, subWeeks, eachDayOfInterval, isSameDay, isSameMonth, parseISO, startOfDay } from "date-fns";

export default function CalendarView() {
  const { items, toggleItem, deleteItem, updateItem, groups } = useTask();
  const [currentDate, setCurrentDate] = useState(new Date());
  const [viewMode, setViewMode] = useState<"week" | "month">("month");

  const getCalendarDays = () => {
    if (viewMode === "month") {
      const start = startOfWeek(startOfMonth(currentDate), { weekStartsOn: 0 });
      const end = endOfWeek(endOfMonth(currentDate), { weekStartsOn: 0 });
      return eachDayOfInterval({ start, end });
    } else {
      const start = startOfWeek(currentDate, { weekStartsOn: 0 });
      const end = endOfWeek(currentDate, { weekStartsOn: 0 });
      return eachDayOfInterval({ start, end });
    }
  };

  const getTasksForDate = (date: Date) => {
    return items.filter(item => {
      if (!item.dueDate) return false;
      return isSameDay(parseISO(item.dueDate), date);
    });
  };

  const navigate = (direction: "prev" | "next") => {
    if (viewMode === "month") {
      setCurrentDate(direction === "next" ? addMonths(currentDate, 1) : subMonths(currentDate, 1));
    } else {
      setCurrentDate(direction === "next" ? addWeeks(currentDate, 1) : subWeeks(currentDate, 1));
    }
  };

  const calendarDays = getCalendarDays();
  const today = startOfDay(new Date());

  return (
    <div className="size-full bg-gradient-to-br from-blue-50 to-indigo-100 p-6 overflow-auto">
      <div className="max-w-7xl mx-auto">
        <div className="bg-white rounded-2xl shadow-xl p-8">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h1 className="text-3xl mb-2">Calendar View</h1>
              <p className="text-gray-500">
                {viewMode === "month" ? format(currentDate, "MMMM yyyy") : `Week of ${format(startOfWeek(currentDate, { weekStartsOn: 0 }), "MMM d, yyyy")}`}
              </p>
            </div>

            <Tabs value={viewMode} onValueChange={(value) => setViewMode(value as "week" | "month")} className="w-auto">
              <TabsList>
                <TabsTrigger value="week">
                  <CalendarDays className="size-4" />
                  Week
                </TabsTrigger>
                <TabsTrigger value="month">
                  <CalendarRange className="size-4" />
                  Month
                </TabsTrigger>
              </TabsList>
            </Tabs>
          </div>

          <div className="flex items-center gap-4 mb-6">
            <button
              onClick={() => navigate("prev")}
              className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <ChevronLeft className="size-5" />
            </button>
            <button
              onClick={() => setCurrentDate(new Date())}
              className="px-4 py-2 text-sm font-medium text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
            >
              Today
            </button>
            <button
              onClick={() => navigate("next")}
              className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <ChevronRight className="size-5" />
            </button>
          </div>

          {/* Calendar Grid */}
          <div className="border rounded-lg overflow-hidden">
            {/* Day Headers */}
            <div className="grid grid-cols-7 bg-gray-50 border-b">
              {["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"].map((day) => (
                <div key={day} className="p-3 text-center text-sm font-semibold text-gray-600 border-r last:border-r-0">
                  {day}
                </div>
              ))}
            </div>

            {/* Calendar Days */}
            <div className={`grid grid-cols-7 ${viewMode === "month" ? "auto-rows-fr" : ""}`}>
              {calendarDays.map((day, index) => {
                const tasksForDay = getTasksForDate(day);
                const isToday = isSameDay(day, today);
                const isCurrentMonth = isSameMonth(day, currentDate);

                return (
                  <div
                    key={day.toISOString()}
                    className={`min-h-32 p-3 border-r border-b last:border-r-0 ${!isCurrentMonth && viewMode === "month" ? "bg-gray-50" : "bg-white"
                      }`}
                  >
                    <div className={`text-sm font-medium mb-2 ${isToday
                        ? "inline-flex items-center justify-center size-7 rounded-full bg-blue-600 text-white"
                        : !isCurrentMonth && viewMode === "month"
                          ? "text-gray-400"
                          : "text-gray-900"
                      }`}>
                      {format(day, "d")}
                    </div>

                    <div className="space-y-1">
                      {tasksForDay.slice(0, 3).map((task) => (
                        <Link
                          key={task.id}
                          to={`/tasks/${task.id}`}
                          className={`block text-xs px-2 py-1 rounded truncate ${task.completed
                              ? "bg-green-100 text-green-700 line-through"
                              : "bg-blue-100 text-blue-700 hover:bg-blue-200"
                            }`}
                        >
                          {task.text}
                        </Link>
                      ))}
                      {tasksForDay.length > 3 && (
                        <div className="text-xs text-gray-500 px-2">
                          +{tasksForDay.length - 3} more
                        </div>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Tasks without due dates */}
          {items.filter(item => !item.dueDate).length > 0 && (
            <div className="mt-8">
              <h2 className="text-lg font-semibold mb-4">Tasks without due dates</h2>
              <div className="space-y-4">
                {items.filter(item => !item.dueDate).map(item => (
                  <TaskItem
                    key={item.id}
                    id={item.id}
                    text={item.text}
                    completed={item.completed}
                    creatorName={item.creatorName}
                    creatorAvatar={item.creatorAvatar}
                    completerName={item.completerName}
                    completerAvatar={item.completerAvatar}
                    completedDate={item.completedDate}
                    dueDate={item.dueDate}
                    group={item.group}
                    history={item.history}
                    onToggle={toggleItem}
                    onDelete={deleteItem}
                    onUpdate={updateItem}
                    availableGroups={groups}
                  />
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}