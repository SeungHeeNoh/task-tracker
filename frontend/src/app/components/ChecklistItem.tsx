import { Checkbox } from "./ui/checkbox";
import { Button } from "./ui/button";
import { Avatar, AvatarImage, AvatarFallback } from "./ui/avatar";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogTrigger } from "./ui/dialog";
import { Trash2, CheckCircle2, History, Clock, Calendar, ExternalLink } from "lucide-react";
import { Link } from "react-router";

export interface HistoryEvent {
  id: string;
  type: "created" | "completed" | "uncompleted";
  userName: string;
  userAvatar: string;
  timestamp: string;
}

interface ChecklistItemProps {
  id: string;
  text: string;
  completed: boolean;
  creatorName: string;
  creatorAvatar: string;
  completerName?: string;
  completerAvatar?: string;
  completedDate?: string;
  dueDate?: string;
  history: HistoryEvent[];
  onToggle: (id: string) => void;
  onDelete: (id: string) => void;
}

export function ChecklistItem({ 
  id, 
  text, 
  completed, 
  creatorName,
  creatorAvatar,
  completerName,
  completerAvatar,
  completedDate,
  dueDate,
  history,
  onToggle, 
  onDelete 
}: ChecklistItemProps) {
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
        return "created this item";
      case "completed":
        return "completed this item";
      case "uncompleted":
        return "marked as incomplete";
      default:
        return "updated this item";
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
            id={`item-${id}`}
          />
        </div>
        
        <div className="flex-1 min-w-0">
          <div className="flex items-start justify-between gap-2 mb-3">
            <label
              htmlFor={`item-${id}`}
              className={`block cursor-pointer flex-1 ${
                completed ? "line-through text-gray-400" : "text-gray-900"
              }`}
            >
              {text}
            </label>
            {dueDateStatus && (
              <span className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-medium ${dueDateStatus.color}`}>
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
                <DialogTitle>Checklist History</DialogTitle>
                <DialogDescription>
                  View the complete timeline of events for this checklist item.
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