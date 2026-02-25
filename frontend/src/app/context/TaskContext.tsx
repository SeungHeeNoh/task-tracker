import { createContext, useContext, useState, ReactNode } from "react";
import { HistoryEvent } from "../components/TaskItem";

export interface Group {
  id: string;
  name: string;
  color: string;
  icon: string;
}

export const predefinedGroups: Group[] = [
  { id: "work", name: "Work", color: "bg-blue-100 text-blue-700 border-blue-200", icon: "💼" },
  { id: "personal", name: "Personal", color: "bg-green-100 text-green-700 border-green-200", icon: "🏠" },
  { id: "shopping", name: "Shopping", color: "bg-purple-100 text-purple-700 border-purple-200", icon: "🛒" },
  { id: "health", name: "Health", color: "bg-red-100 text-red-700 border-red-200", icon: "❤️" },
  { id: "finance", name: "Finance", color: "bg-yellow-100 text-yellow-700 border-yellow-200", icon: "💰" },
  { id: "family", name: "Family", color: "bg-pink-100 text-pink-700 border-pink-200", icon: "👨‍👩‍👧‍👦" },
  { id: "education", name: "Education", color: "bg-indigo-100 text-indigo-700 border-indigo-200", icon: "📚" },
  { id: "other", name: "Other", color: "bg-gray-100 text-gray-700 border-gray-200", icon: "📌" },
];

export interface Task {
  id: string;
  text: string;
  completed: boolean;
  creatorName: string;
  creatorAvatar: string;
  completerName?: string;
  completerAvatar?: string;
  completedDate?: string;
  dueDate?: string;
  group: Group;
  history: HistoryEvent[];
}

interface TaskContextType {
  items: Task[];
  setItems: (items: Task[]) => void;
  addItem: (text: string, group: Group, groupSeq: number, dueDate: string) => Promise<boolean>;
  updateItem: (id: string, text: string, group: Group, dueDate?: string) => Promise<boolean>;
  toggleItem: (id: string) => Promise<boolean>;
  deleteItem: (id: string) => Promise<boolean>;
  getItemById: (id: string) => Task | undefined;
  fetchTasks: (viewMode: string) => Promise<void>;
  isLoading: boolean;
  error: string | null;
  groups: Group[];
  currentUser: {
    name: string;
    avatar: string;
  } | null;
  login: (userId: string, password: string) => Promise<boolean>;
  logout: () => void;
}

const TaskContext = createContext<TaskContextType | undefined>(undefined);

export const fetchWithAuth = async (url: string, options: RequestInit = {}) => {
  const token = localStorage.getItem('accessToken');
  const headers = new Headers(options.headers);
  if (token) {
    headers.set('X-AccessToken', `Bearer ${token}`);
  }

  const response = await fetch(url, { ...options, headers });

  if (response.status === 401) {
    window.location.href = '/401';
  } else if (response.status === 403) {
    window.location.href = '/403';
  } else if (response.status === 404) {
    window.location.href = '/404';
  }

  return response;
};

export function TaskProvider({ children }: { children: ReactNode }) {
  const [items, setItems] = useState<Task[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchTasks = async (viewMode: string) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetchWithAuth(`/api/v1/tasks?viewMode=${viewMode}`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result = await response.json();

      if (result.status === 'SC') {
        const defaultGroup = predefinedGroups.find(g => g.id === "other") || predefinedGroups[0];
        const mappedItems = (result.data || []).map((task: any) => ({
          id: String(task.taskId),
          text: task.title,
          completed: task.taskStatus === 'COMPLETED',
          dueDate: task.duedate,
          creatorName: task.creator,
          creatorAvatar: "https://github.com/shadcn.png", // Default avatar
          group: defaultGroup, // Default group until API supports it
          history: [],
        }));
        setItems(mappedItems);
      } else {
        console.warn("API returned non-success status:", result);
        setError(result.message || "Failed to fetch tasks");
      }
    } catch (e: any) {
      console.error("Failed to fetch tasks:", e);
      setError(e.message);
    } finally {
      setIsLoading(false);
    }
  };

  const [currentUser, setCurrentUser] = useState<{ name: string; avatar: string } | null>(null);

  const login = async (userId: string, password: string): Promise<boolean> => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch('/api/v1/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userId, password })
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();

      if (result.status === 'SC') {
        setCurrentUser({
          name: result.data?.userName || "Unknown User",
          avatar: result.data?.avatarImg || "https://github.com/shadcn.png"
        });
        if (result.data?.accessToken) {
          localStorage.setItem('accessToken', result.data.accessToken);
        }
        return true;
      } else {
        setError(result.message || "로그인에 실패했습니다.");
        return false;
      }
    } catch (e: any) {
      console.error("Login failed:", e);
      setError(e.message || "로그인 중 오류가 발생했습니다.");
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    setCurrentUser(null);
    localStorage.removeItem('accessToken');
  };

  const addItem = async (text: string, group: Group, groupSeq: number, dueDate: string) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetchWithAuth('/api/v1/tasks', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          title: text,
          groupSeq: groupSeq,
          duedate: dueDate,
        })
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();

      if (result.status === 'SC') {
        window.location.reload();
        return true;
      } else {
        alert(result.message || "Failed to add task");
        window.location.reload();
        return false;
      }
    } catch (e: any) {
      alert(e.message || "Failed to add task");
      window.location.reload();
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  const updateItem = async (id: string, text: string, group: Group, dueDate?: string) => {
    if (!dueDate) {
      alert("Due date is required for updating a task.");
      return false;
    }

    setIsLoading(true);
    setError(null);
    try {
      const groupIndex = predefinedGroups.findIndex(g => g.id === group.id);
      const groupSeq = groupIndex !== -1 ? groupIndex + 1 : 1;

      const response = await fetchWithAuth(`/api/v1/tasks/${id}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          title: text,
          groupSeq: groupSeq,
          duedate: dueDate,
        })
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();

      if (result.status === 'SC') {
        window.location.reload();
        return true;
      } else {
        alert(result.message || "Failed to update task");
        window.location.reload();
        return false;
      }
    } catch (e: any) {
      alert(e.message || "Failed to update task");
      window.location.reload();
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  const toggleItem = async (id: string) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetchWithAuth(`/api/v1/tasks/${id}/status`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();

      if (result.status === 'SC') {
        window.location.reload();
        return true;
      } else {
        alert(result.message || "Failed to toggle task status");
        window.location.reload();
        return false;
      }
    } catch (e: any) {
      alert(e.message || "Failed to toggle task status");
      window.location.reload();
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteItem = async (id: string) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetchWithAuth(`/api/v1/tasks/${id}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();

      if (result.status === 'SC') {
        window.location.reload();
        return true;
      } else {
        alert(result.message || "Failed to delete task");
        window.location.reload();
        return false;
      }
    } catch (e: any) {
      alert(e.message || "Failed to delete task");
      window.location.reload();
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  const getItemById = (id: string) => {
    return items.find(item => item.id === id);
  };

  return (
    <TaskContext.Provider
      value={{
        items,
        setItems,
        addItem,
        updateItem,
        toggleItem,
        deleteItem,
        getItemById,
        groups: predefinedGroups,
        fetchTasks,
        isLoading,
        error,
        currentUser,
        login,
        logout
      }}
    >
      {children}
    </TaskContext.Provider>
  );
}

export function useTask() {
  const context = useContext(TaskContext);
  if (!context) {
    throw new Error("useTask must be used within a TaskProvider");
  }
  return context;
}
