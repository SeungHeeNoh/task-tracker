import { useState, useEffect } from "react";
import { useChecklist } from "../context/ChecklistContext";
import { ChecklistItem } from "../components/ChecklistItem";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Calendar as CalendarComponent } from "../components/ui/calendar";
import { Popover, PopoverContent, PopoverTrigger } from "../components/ui/popover";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select";
import { Tabs, TabsList, TabsTrigger } from "../components/ui/tabs";
import { Plus, Calendar, CalendarDays, CalendarRange, CalendarClock } from "lucide-react";
import { format, startOfWeek, endOfWeek, startOfMonth, endOfMonth, isWithinInterval, isSameDay, addDays, parseISO } from "date-fns";
import { Group } from "../context/ChecklistContext";

export default function Home() {
  const { items, addItem, updateItem, toggleItem, deleteItem, groups, fetchTasks, isLoading, error } = useChecklist();
  const [inputValue, setInputValue] = useState("");
  const [selectedDate, setSelectedDate] = useState<Date>();
  const [selectedGroup, setSelectedGroup] = useState<Group | undefined>(groups[0]);
  const [viewMode, setViewMode] = useState<string>("weekly");

  useEffect(() => {
    fetchTasks(viewMode);
  }, [viewMode]);

  const handleAddItem = () => {
    if (inputValue.trim() && selectedGroup) {
      addItem(inputValue.trim(), selectedGroup, selectedDate ? format(selectedDate, 'yyyy-MM-dd') : undefined);
      setInputValue("");
      setSelectedDate(undefined);
      setSelectedGroup(groups[0]);
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === "Enter") {
      handleAddItem();
    }
  };

  const getGroupedItems = () => {
    const today = new Date();
    const groups: { [key: string]: typeof items } = {};

    if (viewMode === "daily") {
      for (let i = 0; i < 7; i++) {
        const date = addDays(today, i);
        const dateKey = format(date, 'yyyy-MM-dd');
        groups[dateKey] = items.filter(item =>
          item.dueDate && isSameDay(parseISO(item.dueDate), date)
        );
      }
    } else if (viewMode === "weekly") {
      const weekStart = startOfWeek(today, { weekStartsOn: 0 });
      const weekEnd = endOfWeek(today, { weekStartsOn: 0 });

      groups["This Week"] = items.filter(item => {
        if (!item.dueDate) return false;
        const dueDate = parseISO(item.dueDate);
        return isWithinInterval(dueDate, { start: weekStart, end: weekEnd });
      });

      const nextWeekStart = addDays(weekEnd, 1);
      const nextWeekEnd = endOfWeek(nextWeekStart, { weekStartsOn: 0 });
      groups["Next Week"] = items.filter(item => {
        if (!item.dueDate) return false;
        const dueDate = parseISO(item.dueDate);
        return isWithinInterval(dueDate, { start: nextWeekStart, end: nextWeekEnd });
      });

      groups["Later"] = items.filter(item => {
        if (!item.dueDate) return false;
        const dueDate = parseISO(item.dueDate);
        return dueDate > nextWeekEnd;
      });
    } else if (viewMode === "monthly") {
      const monthStart = startOfMonth(today);
      const monthEnd = endOfMonth(today);

      groups["This Month"] = items.filter(item => {
        if (!item.dueDate) return false;
        const dueDate = parseISO(item.dueDate);
        return isWithinInterval(dueDate, { start: monthStart, end: monthEnd });
      });

      groups["Later"] = items.filter(item => {
        if (!item.dueDate) return false;
        const dueDate = parseISO(item.dueDate);
        return dueDate > monthEnd;
      });
    }

    const noDueDate = items.filter(item => !item.dueDate);
    if (noDueDate.length > 0) {
      groups["No Due Date"] = noDueDate;
    }

    const overdue = items.filter(item => {
      if (!item.dueDate || item.completed) return false;
      const dueDate = parseISO(item.dueDate);
      today.setHours(0, 0, 0, 0);
      dueDate.setHours(0, 0, 0, 0);
      return dueDate < today;
    });
    if (overdue.length > 0) {
      groups["Overdue"] = overdue;
    }

    return groups;
  };

  const completedCount = items.filter(item => item.completed).length;
  const totalCount = items.length;
  const groupedItems = getGroupedItems();
  const hasItemsInCurrentView = Object.values(groupedItems).some(group => group.length > 0);

  return (
    <div className="size-full bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-6">
      <div className="w-full max-w-4xl bg-white rounded-2xl shadow-xl p-8">
        <div className="mb-8">
          <h1 className="text-3xl mb-2">My Checklist</h1>
          <p className="text-gray-500">
            {completedCount} of {totalCount} completed
          </p>
        </div>

        <div className="flex gap-2 mb-6">
          <Select value={selectedGroup?.id} onValueChange={(value) => {
            const group = groups.find(g => g.id === value);
            setSelectedGroup(group);
          }}>
            <SelectTrigger className="w-48">
              <SelectValue placeholder="Select group">
                {selectedGroup && (
                  <div className="flex items-center gap-2">
                    <span>{selectedGroup.icon}</span>
                    <span className="truncate">
                      {selectedGroup.name.length > 15
                        ? selectedGroup.name.substring(0, 15) + '...'
                        : selectedGroup.name}
                    </span>
                  </div>
                )}
              </SelectValue>
            </SelectTrigger>
            <SelectContent>
              {groups.map((group) => (
                <SelectItem key={group.id} value={group.id}>
                  <div className="flex items-center gap-2">
                    <span>{group.icon}</span>
                    <span>
                      {group.name.length > 15
                        ? group.name.substring(0, 15) + '...'
                        : group.name}
                    </span>
                  </div>
                </SelectItem>
              ))}
            </SelectContent>
          </Select>

          <Input
            type="text"
            placeholder="Add a new item..."
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            onKeyPress={handleKeyPress}
            className="flex-1"
          />

          <Popover>
            <PopoverTrigger asChild>
              <button
                className={`inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-hidden focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4 border border-input bg-background hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 ${selectedDate ? "text-blue-600" : ""}`}
              >
                <Calendar className="size-4" />
                {selectedDate ? format(selectedDate, "MMM d") : "Due date"}
              </button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0" align="start">
              <CalendarComponent
                mode="single"
                selected={selectedDate}
                onSelect={setSelectedDate}
                initialFocus
              />
            </PopoverContent>
          </Popover>

          <Button onClick={handleAddItem}>
            <Plus className="size-5" />
            Add
          </Button>
        </div>

        <Tabs value={viewMode} onValueChange={setViewMode} className="mb-6">
          <TabsList>
            <TabsTrigger value="daily">
              <CalendarClock className="size-4" />
              Daily
            </TabsTrigger>
            <TabsTrigger value="weekly">
              <CalendarDays className="size-4" />
              Weekly
            </TabsTrigger>
            <TabsTrigger value="monthly">
              <CalendarRange className="size-4" />
              Monthly
            </TabsTrigger>
          </TabsList>
        </Tabs>

        <div className="space-y-8">
          {items.length === 0 ? (
            <div className="text-center py-12 text-gray-400">
              No items yet. Add one to get started!
            </div>
          ) : !hasItemsInCurrentView ? (
            <div className="flex flex-col items-center justify-center py-16 px-4">
              <div className="relative mb-6">
                <div className="size-24 rounded-full bg-gradient-to-br from-blue-100 to-indigo-100 flex items-center justify-center">
                  {viewMode === "daily" && <CalendarClock className="size-12 text-blue-500" />}
                  {viewMode === "weekly" && <CalendarDays className="size-12 text-blue-500" />}
                  {viewMode === "monthly" && <CalendarRange className="size-12 text-blue-500" />}
                </div>
                <div className="absolute -top-2 -right-2 size-8 rounded-full bg-green-100 flex items-center justify-center">
                  <span className="text-2xl">✨</span>
                </div>
              </div>

              <h3 className="text-xl font-semibold text-gray-900 mb-2">
                {viewMode === "daily" && "No tasks for the next 7 days"}
                {viewMode === "weekly" && "All clear this week!"}
                {viewMode === "monthly" && "No tasks this month"}
              </h3>

              <p className="text-gray-500 text-center max-w-md mb-6">
                {viewMode === "daily" && "You have no upcoming tasks in the next week. Add a task with a due date to see it here."}
                {viewMode === "weekly" && "You don't have any tasks scheduled for this week or next. Enjoy your free time or add new tasks to stay organized."}
                {viewMode === "monthly" && "You're all set for this month! Add new tasks or switch to a different view to see your other items."}
              </p>

              <div className="flex gap-3">
                <Button onClick={() => {
                  const input = document.querySelector('input[type="text"]') as HTMLInputElement;
                  input?.focus();
                }} variant="outline" className="gap-2">
                  <Plus className="size-4" />
                  Add New Task
                </Button>
                {viewMode !== "monthly" && (
                  <Button
                    onClick={() => setViewMode("monthly")}
                    variant="outline"
                  >
                    View Monthly
                  </Button>
                )}
              </div>
            </div>
          ) : (
            Object.entries(groupedItems).map(([groupName, groupItems]) => {
              if (groupItems.length === 0) return null;

              let dateLabel = groupName;
              if (viewMode === "daily" && groupName.match(/^\d{4}-\d{2}-\d{2}$/)) {
                const date = parseISO(groupName);
                const today = new Date();
                if (isSameDay(date, today)) {
                  dateLabel = "Today";
                } else if (isSameDay(date, addDays(today, 1))) {
                  dateLabel = "Tomorrow";
                } else {
                  dateLabel = format(date, "EEEE, MMM d");
                }
              }

              return (
                <div key={groupName}>
                  <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
                    {groupName === "Overdue" && <span className="text-red-600">⚠️</span>}
                    {dateLabel}
                    <span className="text-sm font-normal text-gray-400">
                      ({groupItems.length})
                    </span>
                  </h2>
                  <div className="space-y-4">
                    {groupItems.map(item => (
                      <ChecklistItem
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
              );
            })
          )}
        </div>
      </div>
    </div>
  );
}