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
  logout: () => Promise<void> | void;
  signup: (userData: { userId: string; userName: string; password: string; avatarImg?: string }) => Promise<{ success: boolean; message?: string }>;
}

const TaskContext = createContext<TaskContextType | undefined>(undefined);

export const fetchWithAuth = async (url: string, options: RequestInit = {}) => {
  const token = localStorage.getItem("accessToken");
  const headers = new Headers(options.headers);
  if (token && token !== "null" && token !== "undefined") {
    headers.set('X-AccessToken', `Bearer ${token}`);
  }

  let response = await fetch(url, { ...options, headers });

  if (response.status === 401 || response.status === 403) {
    const refreshToken = localStorage.getItem("refreshToken");
    if (refreshToken && refreshToken !== "null" && refreshToken !== "undefined") {
      try {
        const reissueRes = await fetch('/api/v1/auth/reissue', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ refreshToken })
        });

        const reissueData = await reissueRes.json();

        if (reissueRes.ok && reissueData.status === 'SC' && reissueData.data?.accessToken) {
          const newAccessToken = reissueData.data.accessToken;
          localStorage.setItem("accessToken", newAccessToken);

          headers.set('X-AccessToken', `Bearer ${newAccessToken}`);
          response = await fetch(url, { ...options, headers });

          if (response.status === 401 || response.status === 403) {
            alert("세션이 만료되었습니다. 다시 로그인해주세요.");
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            window.location.href = '/login';
            throw new Error('SessionExpiredRedirect');
          }
        } else {
          alert("세션이 만료되었습니다. 다시 로그인해주세요.");
          localStorage.removeItem("accessToken");
          localStorage.removeItem("refreshToken");
          window.location.href = '/login';
          throw new Error('SessionExpiredRedirect');
        }
      } catch (e) {
        if ((e as Error).message !== 'SessionExpiredRedirect') {
          alert("세션이 만료되었습니다. 다시 로그인해주세요.");
          localStorage.removeItem("accessToken");
          localStorage.removeItem("refreshToken");
          window.location.href = '/login';
        }
        throw e;
      }
    } else {
      alert("세션이 만료되었습니다. 다시 로그인해주세요.");
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      window.location.href = '/login';
      throw new Error('SessionExpiredRedirect');
    }
  } else if (response.status === 404) {
    window.location.href = '/404';
  }

  return response;
};

export function TaskProvider({ children }: { children: ReactNode }) {

  const [items, setItems] = useState<Task[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [accessToken, setAccessToken] = useState<string | null>(localStorage.getItem("accessToken"));
  const [refreshToken, setRefreshToken] = useState<string | null>(localStorage.getItem("refreshToken"));

  const fetchTasks = async (viewMode: string) => {
    const token = localStorage.getItem("accessToken");
    if (!token || token === "null" || token === "undefined") {
      setItems([]);
      return;
    }

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
          name: result.data?.userInfo?.userName || "Unknown User",
          avatar: result.data?.userInfo?.avatarImg || "https://github.com/shadcn.png"
        });
        if (result.data?.token?.accessToken) {
          setAccessToken(result.data.token.accessToken);
          localStorage.setItem("accessToken", result.data.token.accessToken);
        }
        if (result.data?.token?.refreshToken) {
          setRefreshToken(result.data.token.refreshToken);
          localStorage.setItem("refreshToken", result.data.token.refreshToken);
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

  const logout = async () => {
    // 1. Clear state immediately to prevent React re-renders from triggering API calls
    setCurrentUser(null);
    setAccessToken(null);
    setRefreshToken(null);

    try {
      const token = localStorage.getItem("accessToken");

      // 2. Clear localStorage before the async fetch so any other component
      // checking localStorage sees that we are logged out
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      localStorage.removeItem("taskTrackerViewMode");

      if (token && token !== "null" && token !== "undefined") {
        await fetch('/api/v1/auth/logout', {
          method: 'POST',
          headers: {
            'X-AccessToken': `Bearer ${token}`
          }
        });
      }
    } catch (e) {
      console.error("Logout API failed:", e);
    } finally {
      alert("로그아웃 되었습니다.");
      window.location.href = "/login";
    }
  };

  const signup = async (userData: { userId: string; userName: string; password: string; avatarImg?: string }): Promise<{ success: boolean; message?: string }> => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch('/api/v1/auth/join', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
      });

      if (!response.ok) {
        if (response.status === 409) {
          throw new Error("이미 존재하는 ID입니다.");
        }
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();

      if (result.status === 'SC') {
        return { success: true };
      } else {
        setError(result.message || "회원가입에 실패했습니다.");
        return { success: false, message: result.message || "회원가입에 실패했습니다." };
      }
    } catch (e: any) {
      console.error("Signup failed:", e);
      setError(e.message || "회원가입 중 오류가 발생했습니다.");
      return { success: false, message: e.message || "회원가입 중 오류가 발생했습니다." };
    } finally {
      setIsLoading(false);
    }
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
      if (e.message !== 'SessionExpiredRedirect') {
        alert(e.message || "Failed to add task");
        window.location.reload();
      }
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
      if (e.message !== 'SessionExpiredRedirect') {
        alert(e.message || "Failed to update task");
        window.location.reload();
      }
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
      if (e.message !== 'SessionExpiredRedirect') {
        alert(e.message || "Failed to toggle task status");
        window.location.reload();
      }
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
      if (e.message !== 'SessionExpiredRedirect') {
        alert(e.message || "Failed to delete task");
        window.location.reload();
      }
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
        logout,
        signup
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
