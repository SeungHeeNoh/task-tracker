import { useParams, useNavigate, Link } from "react-router";
import { useTask } from "../context/TaskContext";
import { Button } from "../components/ui/button";
import { Avatar, AvatarImage, AvatarFallback } from "../components/ui/avatar";
import { ArrowLeft, Calendar, CheckCircle2, Clock, User, Trash2, Tag } from "lucide-react";
import { format, parseISO } from "date-fns";

export default function TaskDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { getItemById, toggleItem, deleteItem } = useTask();

  const item = id ? getItemById(id) : undefined;

  if (!item) {
    return (
      <div className="size-full bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-6">
        <div className="max-w-2xl w-full bg-white rounded-2xl shadow-xl p-8 text-center">
          <h1 className="text-2xl font-semibold mb-4">Task not found</h1>
          <p className="text-gray-500 mb-6">The task you're looking for doesn't exist.</p>
          <Button onClick={() => navigate("/")}>
            <ArrowLeft className="size-4" />
            Back to Home
          </Button>
        </div>
      </div>
    );
  }

  const handleDelete = () => {
    deleteItem(item.id);
    navigate("/");
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
    if (!item.dueDate || item.completed) return null;

    const due = new Date(item.dueDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    due.setHours(0, 0, 0, 0);

    const diffTime = due.getTime() - today.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays < 0) {
      return { color: "text-red-600 bg-red-50 border-red-200", label: "Overdue" };
    } else if (diffDays === 0) {
      return { color: "text-orange-600 bg-orange-50 border-orange-200", label: "Due Today" };
    } else if (diffDays <= 3) {
      return { color: "text-orange-600 bg-orange-50 border-orange-200", label: `Due in ${diffDays} day${diffDays > 1 ? 's' : ''}` };
    } else {
      return { color: "text-blue-600 bg-blue-50 border-blue-200", label: `Due ${format(parseISO(item.dueDate), 'MMM d, yyyy')}` };
    }
  };

  const dueDateStatus = getDueDateStatus();

  return (
    <div className="size-full bg-gradient-to-br from-blue-50 to-indigo-100 p-6 overflow-auto">
      <div className="max-w-3xl mx-auto">
        <div className="mb-6">
          <Link to="/" className="inline-flex items-center gap-2 text-gray-600 hover:text-gray-900 transition-colors">
            <ArrowLeft className="size-4" />
            Back to Home
          </Link>
        </div>

        <div className="bg-white rounded-2xl shadow-xl overflow-hidden">
          {/* Header */}
          <div className="p-8 border-b bg-gradient-to-r from-blue-50 to-indigo-50">
            <div className="flex items-start justify-between gap-4 mb-4">
              <h1 className={`text-3xl flex-1 ${item.completed ? "line-through text-gray-400" : "text-gray-900"}`}>
                {item.text}
              </h1>
              <div className="flex gap-2">
                <Button
                  onClick={() => toggleItem(item.id)}
                  variant={item.completed ? "outline" : "default"}
                  className="gap-2"
                >
                  <CheckCircle2 className="size-4" />
                  {item.completed ? "Mark Incomplete" : "Mark Complete"}
                </Button>
                <Button
                  onClick={handleDelete}
                  variant="outline"
                  className="text-red-600 hover:text-red-700 hover:bg-red-50"
                >
                  <Trash2 className="size-4" />
                </Button>
              </div>
            </div>

            {dueDateStatus && (
              <div className={`inline-flex items-center gap-2 px-3 py-1.5 rounded-full text-sm font-medium border ${dueDateStatus.color}`}>
                <Calendar className="size-4" />
                {dueDateStatus.label}
              </div>
            )}
          </div>

          {/* Task Details */}
          <div className="p-8 space-y-6">
            {/* Creator Info */}
            <div className="flex items-center gap-3 p-4 bg-gray-50 rounded-lg">
              <div className="p-2 bg-blue-100 rounded-full">
                <User className="size-5 text-blue-600" />
              </div>
              <div className="flex-1">
                <p className="text-sm text-gray-500">Created by</p>
                <div className="flex items-center gap-2 mt-1">
                  <Avatar className="size-8">
                    <AvatarImage src={item.creatorAvatar} alt={item.creatorName} />
                    <AvatarFallback className="bg-blue-100 text-blue-600 text-xs">
                      {item.creatorName.split(' ').map(n => n[0]).join('')}
                    </AvatarFallback>
                  </Avatar>
                  <span className="font-medium text-gray-900">{item.creatorName}</span>
                </div>
              </div>
            </div>

            {/* Completer Info */}
            {item.completed && item.completerName && (
              <div className="flex items-center gap-3 p-4 bg-green-50 rounded-lg">
                <div className="p-2 bg-green-100 rounded-full">
                  <CheckCircle2 className="size-5 text-green-600" />
                </div>
                <div className="flex-1">
                  <p className="text-sm text-gray-500">Completed by</p>
                  <div className="flex items-center gap-2 mt-1">
                    <Avatar className="size-8">
                      <AvatarImage src={item.completerAvatar} alt={item.completerName} />
                      <AvatarFallback className="bg-green-100 text-green-600 text-xs">
                        {item.completerName.split(' ').map(n => n[0]).join('')}
                      </AvatarFallback>
                    </Avatar>
                    <span className="font-medium text-gray-900">{item.completerName}</span>
                    {item.completedDate && (
                      <span className="text-sm text-gray-500">• {item.completedDate}</span>
                    )}
                  </div>
                </div>
              </div>
            )}

            {/* Due Date Info */}
            {item.dueDate && (
              <div className="flex items-center gap-3 p-4 bg-gray-50 rounded-lg">
                <div className="p-2 bg-purple-100 rounded-full">
                  <Calendar className="size-5 text-purple-600" />
                </div>
                <div className="flex-1">
                  <p className="text-sm text-gray-500">Due Date</p>
                  <p className="font-medium text-gray-900 mt-1">
                    {format(parseISO(item.dueDate), "EEEE, MMMM d, yyyy")}
                  </p>
                </div>
              </div>
            )}

            {/* Group Info */}
            {item.group && (
              <div className="flex items-center gap-3 p-4 bg-gray-50 rounded-lg">
                <div className="p-2 bg-indigo-100 rounded-full">
                  <Tag className="size-5 text-indigo-600" />
                </div>
                <div className="flex-1">
                  <p className="text-sm text-gray-500">Group</p>
                  <div className="mt-1">
                    <span className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-md text-sm font-medium border ${item.group.color}`}>
                      <span>{item.group.icon}</span>
                      {item.group.name}
                    </span>
                  </div>
                </div>
              </div>
            )}

            {/* History Timeline */}
            <div>
              <h2 className="text-xl font-semibold mb-4 flex items-center gap-2">
                <Clock className="size-5 text-gray-600" />
                Activity Timeline
              </h2>

              <div className="relative">
                {/* Timeline line */}
                <div className="absolute left-4 top-0 bottom-0 w-0.5 bg-gray-200" />

                <div className="space-y-6">
                  {item.history.map((event) => (
                    <div key={event.id} className="relative flex gap-4">
                      <div className="relative z-10 flex items-center justify-center size-8 rounded-full bg-white border-2 border-gray-200 shrink-0">
                        {getEventIcon(event.type)}
                      </div>
                      <div className="flex-1 pt-1 pb-4">
                        <div className="flex items-center gap-2 mb-1">
                          <Avatar className="size-7">
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
                        <p className="text-xs text-gray-400 ml-9">{event.timestamp}</p>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}