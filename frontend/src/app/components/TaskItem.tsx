import { Checkbox } from "./ui/checkbox";
import { Button } from "./ui/button";
import { Avatar, AvatarImage, AvatarFallback } from "./ui/avatar";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogTrigger } from "./ui/dialog";
import { Trash2, CheckCircle2, History, Clock, Calendar, ExternalLink, Pencil } from "lucide-react";
import { Link } from "react-router";
import { Group } from "../context/TaskContext";
import { useState } from "react";
import { Input } from "./ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select";
import { Calendar as CalendarComponent } from "./ui/calendar";
import { Popover, PopoverContent, PopoverTrigger } from "./ui/popover";
import { format, parseISO } from "date-fns";

export interface HistoryEvent {
  id: string;
  type: "created" | "completed" | "uncompleted" | "updated";
  userName: string;
  userAvatar: string;
  timestamp: string;
}

interface TaskItemProps {
  id: string;
  text: string;
  completed: boolean;
  creatorName: string;
  creatorAvatar: string;
  completerName?: string;
  completerAvatar?: string;
  completedDate?: string;
  dueDate?: string;
  group?: Group;
  history: HistoryEvent[];
  onToggle: (id: string) => void;
  onDelete: (id: string) => void;
  onUpdate?: (id: string, text: string, group: Group, dueDate?: string) => void;
  availableGroups?: Group[];
}

export function TaskItem({
  id,
  text,
  completed,
  creatorName,
  creatorAvatar,
  completerName,
  completerAvatar,
  completedDate,
  dueDate,
  group,
  history,
  onToggle,
  onDelete,
  onUpdate,
  availableGroups
}: TaskItemProps) {
  const [isEditOpen, setIsEditOpen] = useState(false);
  const [editText, setEditText] = useState(text);
  const [editGroup, setEditGroup] = useState<Group | undefined>(group);
  const [editDueDate, setEditDueDate] = useState<Date | undefined>(dueDate ? parseISO(dueDate) : undefined);

  const handleSaveEdit = () => {
    if (editText.trim() && editGroup && onUpdate) {
      onUpdate(id, editText.trim(), editGroup, editDueDate ? format(editDueDate, 'yyyy-MM-dd') : undefined);
      setIsEditOpen(false);
    }
  };

  const getEventIcon = (type: string) => {
    switch (type) {
      case "created":
        return <Clock className="size-4 text-blue-500" />;
      case "completed":
        return <CheckCircle2 className="size-4 text-green-500" />;
      case "uncompleted":
        return <Clock className="size-4 text-orange-500" />;
      default:
        return <Clock className="size-4 text-gray-500" />;
    }
  };

  const getEventText = (type: string) => {
    switch (type) {
      case "created":
        return "created this task";
      case "completed":
        return "completed this task";
      case "uncompleted":
        return "marked as incomplete";
      default:
        return "updated this task";
    }
  };

  const getDueDateStatus = () => {
    if (!dueDate || completed) return null;

    const due = new Date(dueDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    due.setHours(0, 0, 0, 0);

    const diffTime = due.getTime() - today.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays < 0) {
      return { color: "text-red-600 bg-red-50", label: "Overdue" };
    } else if (diffDays === 0) {
      return { color: "text-orange-600 bg-orange-50", label: "Due Today" };
    } else if (diffDays <= 3) {
      return { color: "text-orange-600 bg-orange-50", label: `Due in ${diffDays} day${diffDays > 1 ? 's' : ''}` };
    } else {
      return { color: "text-blue-600 bg-blue-50", label: `Due ${new Date(dueDate).toLocaleDateString('en-US', { month: 'short', day: 'numeric' })}` };
    }
  };

  const dueDateStatus = getDueDateStatus();

  return (
    <div className="relative rounded-xl border bg-white shadow-sm hover:shadow-md transition-shadow p-5 group">
      <div className="flex items-start gap-4">
        <div className="pt-1">
          <Checkbox
            checked={completed}
            onCheckedChange={() => onToggle(id)}
            id={`task-${id}`}
          />
        </div>

        <div className="flex-1 min-w-0">
          <div className="flex items-start justify-between gap-2 mb-3">
            <div className="flex-1 flex items-start gap-2">
              <label
                htmlFor={`task-${id}`}
                className={`block cursor-pointer flex-1 ${completed ? "line-through text-gray-400" : "text-gray-900"
                  }`}
              >
                {text}
              </label>
              {group && (
                <span className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-md text-xs font-medium border ${group.color} shrink-0`}>
                  <span>{group.icon}</span>
                  {group.name}
                </span>
              )}
            </div>
            {dueDateStatus && (
              <span className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-medium ${dueDateStatus.color} shrink-0`}>
                <Calendar className="size-3" />
                {dueDateStatus.label}
              </span>
            )}
          </div>

          <div className="flex items-center justify-between gap-4">
            <div className="flex items-center gap-2">
              <Avatar className="size-7">
                <AvatarImage src={creatorAvatar} alt={creatorName} />
                <AvatarFallback className="bg-blue-100 text-blue-600 text-xs">
                  {creatorName.split(' ').map(n => n[0]).join('')}
                </AvatarFallback>
              </Avatar>
              <span className="text-sm text-gray-500">Created by {creatorName}</span>
            </div>

            {completed && completerName && (
              <div className="flex items-center gap-2">
                <CheckCircle2 className="size-4 text-green-500" />
                <Avatar className="size-7">
                  <AvatarImage src={completerAvatar} alt={completerName} />
                  <AvatarFallback className="bg-green-100 text-green-600 text-xs">
                    {completerName.split(' ').map(n => n[0]).join('')}
                  </AvatarFallback>
                </Avatar>
                <span className="text-sm text-gray-500">
                  Completed by {completerName}
                  {completedDate && <span className="text-gray-400"> • {completedDate}</span>}
                </span>
              </div>
            )}
          </div>
        </div>

        <div className="flex gap-1 shrink-0">
          <Link to={`/tasks/${id}`}>
            <button
              className="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-hidden focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4 hover:bg-accent hover:text-accent-foreground size-9 opacity-0 group-hover:opacity-100"
            >
              <ExternalLink className="size-4 text-gray-500" />
            </button>
          </Link>

          {onUpdate && availableGroups && (
            <Dialog open={isEditOpen} onOpenChange={setIsEditOpen}>
              <DialogTrigger asChild>
                <button
                  className="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-hidden focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4 hover:bg-accent hover:text-accent-foreground size-9 opacity-0 group-hover:opacity-100"
                >
                  <Pencil className="size-4 text-indigo-500" />
                </button>
              </DialogTrigger>
              <DialogContent className="max-w-lg">
                <DialogHeader>
                  <DialogTitle>Edit Task</DialogTitle>
                  <DialogDescription>
                    Update the task details below.
                  </DialogDescription>
                </DialogHeader>
                <div className="space-y-4 mt-4">
                  <div>
                    <label className="text-sm font-medium text-gray-700 mb-2 block">
                      Task Name
                    </label>
                    <Input
                      type="text"
                      value={editText}
                      onChange={(e) => setEditText(e.target.value)}
                      placeholder="Enter task name"
                    />
                  </div>

                  <div>
                    <label className="text-sm font-medium text-gray-700 mb-2 block">
                      Group
                    </label>
                    <Select value={editGroup?.id} onValueChange={(value) => {
                      const selectedGroup = availableGroups.find(g => g.id === value);
                      setEditGroup(selectedGroup);
                    }}>
                      <SelectTrigger>
                        <SelectValue placeholder="Select group">
                          {editGroup && (
                            <div className="flex items-center gap-2">
                              <span>{editGroup.icon}</span>
                              <span className="truncate">
                                {editGroup.name.length > 15
                                  ? editGroup.name.substring(0, 15) + '...'
                                  : editGroup.name}
                              </span>
                            </div>
                          )}
                        </SelectValue>
                      </SelectTrigger>
                      <SelectContent>
                        {availableGroups.map((grp) => (
                          <SelectItem key={grp.id} value={grp.id}>
                            <div className="flex items-center gap-2">
                              <span>{grp.icon}</span>
                              <span>
                                {grp.name.length > 15
                                  ? grp.name.substring(0, 15) + '...'
                                  : grp.name}
                              </span>
                            </div>
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>

                  <div>
                    <label className="text-sm font-medium text-gray-700 mb-2 block">
                      Due Date
                    </label>
                    <Popover>
                      <PopoverTrigger asChild>
                        <button
                          className={`w-full inline-flex items-center justify-between gap-2 whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-hidden focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4 border border-input bg-background hover:bg-accent hover:text-accent-foreground h-10 px-4 py-2 ${editDueDate ? "text-blue-600" : ""}`}
                        >
                          <span className="flex items-center gap-2">
                            <Calendar className="size-4" />
                            {editDueDate ? format(editDueDate, "MMM d, yyyy") : "Select due date"}
                          </span>
                          {editDueDate && (
                            <button
                              onClick={(e) => {
                                e.stopPropagation();
                                setEditDueDate(undefined);
                              }}
                              className="text-gray-400 hover:text-gray-600"
                            >
                              ×
                            </button>
                          )}
                        </button>
                      </PopoverTrigger>
                      <PopoverContent className="w-auto p-0" align="start">
                        <CalendarComponent
                          mode="single"
                          selected={editDueDate}
                          onSelect={setEditDueDate}
                          initialFocus
                        />
                      </PopoverContent>
                    </Popover>
                  </div>

                  <div className="flex gap-2 justify-end pt-4">
                    <Button
                      variant="outline"
                      onClick={() => {
                        setIsEditOpen(false);
                        setEditText(text);
                        setEditGroup(group);
                        setEditDueDate(dueDate ? parseISO(dueDate) : undefined);
                      }}
                    >
                      Cancel
                    </Button>
                    <Button onClick={handleSaveEdit} disabled={!editText.trim() || !editGroup}>
                      Save Changes
                    </Button>
                  </div>
                </div>
              </DialogContent>
            </Dialog>
          )}

          <Dialog>
            <DialogTrigger asChild>
              <button
                className="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-hidden focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4 hover:bg-accent hover:text-accent-foreground size-9 opacity-0 group-hover:opacity-100"
              >
                <History className="size-4 text-blue-500" />
              </button>
            </DialogTrigger>
            <DialogContent className="max-w-md">
              <DialogHeader>
                <DialogTitle>Task History</DialogTitle>
                <DialogDescription>
                  View the complete timeline of events for this task.
                </DialogDescription>
              </DialogHeader>
              <div className="mt-4">
                <div className="relative">
                  {/* Timeline line */}
                  <div className="absolute left-4 top-0 bottom-0 w-0.5 bg-gray-200" />

                  <div className="space-y-6">
                    {history.map((event) => (
                      <div key={event.id} className="relative flex gap-3">
                        <div className="relative z-10 flex items-center justify-center size-8 rounded-full bg-white border-2 border-gray-200">
                          {getEventIcon(event.type)}
                        </div>
                        <div className="flex-1 pt-1">
                          <div className="flex items-center gap-2 mb-1">
                            <Avatar className="size-6">
                              <AvatarImage src={event.userAvatar} alt={event.userName} />
                              <AvatarFallback className="bg-gray-100 text-gray-600 text-xs">
                                {event.userName.split(' ').map(n => n[0]).join('')}
                              </AvatarFallback>
                            </Avatar>
                            <span className="text-sm">
                              <span className="font-medium">{event.userName}</span>
                              {' '}
                              <span className="text-gray-600">{getEventText(event.type)}</span>
                            </span>
                          </div>
                          <p className="text-xs text-gray-400">{event.timestamp}</p>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </DialogContent>
          </Dialog>

          <Button
            variant="ghost"
            size="sm"
            onClick={() => onDelete(id)}
            className="opacity-0 group-hover:opacity-100 transition-opacity"
          >
            <Trash2 className="size-4 text-red-500" />
          </Button>
        </div>
      </div>
    </div>
  );
}