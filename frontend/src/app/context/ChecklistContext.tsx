import { createContext, useContext, useState, ReactNode } from "react";
import { HistoryEvent } from "../components/ChecklistItem";

export interface ChecklistItemType {
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
}

interface ChecklistContextType {
  items: ChecklistItemType[];
  setItems: (items: ChecklistItemType[]) => void;
  addItem: (text: string, dueDate?: string) => void;
  toggleItem: (id: string) => void;
  deleteItem: (id: string) => void;
  getItemById: (id: string) => ChecklistItemType | undefined;
  fetchTasks: (viewMode: string) => Promise<void>;
  isLoading: boolean;
  error: string | null;
  currentUser: {
    name: string;
    avatar: string;
  };
}

const ChecklistContext = createContext<ChecklistContextType | undefined>(undefined);

export function ChecklistProvider({ children }: { children: ReactNode }) {
  const [items, setItems] = useState<ChecklistItemType[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchTasks = async (viewMode: string) => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await fetch(`/api/v1/tasks?viewMode=${viewMode}`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result = await response.json();

      if (result.status === 'SC') {
        const mappedItems = (result.data || []).map((task: any) => ({
          id: String(task.taskId),
          text: task.title,
          completed: task.taskStatus === 'COMPLETED',
          dueDate: task.duedate, // Keep API format, parsing handled in UI
          creatorName: task.creator,
          creatorAvatar: "https://github.com/shadcn.png", // Default
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

  const currentUser = {
    name: "Sarah Johnson",
    avatar: "https://images.unsplash.com/photo-1649589244330-09ca58e4fa64?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9mZXNzaW9uYWwlMjB3b21hbiUyMHBvcnRyYWl0fGVufDF8fHx8MTc3MTAzNjQyMXww&ixlib=rb-4.1.0&q=80&w=1080"
  };

  const addItem = (text: string, dueDate?: string) => {
    const now = new Date();
    const timestamp = now.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric'
    }) + ' at ' + now.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: '2-digit',
      hour12: true
    });

    const newItem: ChecklistItemType = {
      id: Date.now().toString(),
      text: text,
      completed: false,
      creatorName: currentUser.name,
      creatorAvatar: currentUser.avatar,
      dueDate: dueDate,
      history: [
        {
          id: `h-${Date.now()}`,
          type: "created",
          userName: currentUser.name,
          userAvatar: currentUser.avatar,
          timestamp: timestamp
        }
      ]
    };
    setItems([...items, newItem]);
  };

  const toggleItem = (id: string) => {
    setItems(items.map(item => {
      if (item.id === id) {
        const now = new Date();
        const timestamp = now.toLocaleDateString('en-US', {
          month: 'short',
          day: 'numeric',
          year: 'numeric'
        }) + ' at ' + now.toLocaleTimeString('en-US', {
          hour: 'numeric',
          minute: '2-digit',
          hour12: true
        });

        const updatedItem = { ...item, completed: !item.completed };

        if (updatedItem.completed) {
          updatedItem.completerName = currentUser.name;
          updatedItem.completerAvatar = currentUser.avatar;
          updatedItem.completedDate = new Date().toLocaleDateString('en-US', {
            month: 'short',
            day: 'numeric',
            year: 'numeric'
          });
          updatedItem.history = [
            {
              id: `h-${Date.now()}`,
              type: "completed",
              userName: currentUser.name,
              userAvatar: currentUser.avatar,
              timestamp: timestamp
            },
            ...item.history
          ];
        } else {
          delete updatedItem.completerName;
          delete updatedItem.completerAvatar;
          delete updatedItem.completedDate;
          updatedItem.history = [
            {
              id: `h-${Date.now()}`,
              type: "uncompleted",
              userName: currentUser.name,
              userAvatar: currentUser.avatar,
              timestamp: timestamp
            },
            ...item.history
          ];
        }
        return updatedItem;
      }
      return item;
    }));
  };

  const deleteItem = (id: string) => {
    setItems(items.filter(item => item.id !== id));
  };

  const getItemById = (id: string) => {
    return items.find(item => item.id === id);
  };

  return (
    <ChecklistContext.Provider
      value={{
        items,
        setItems,
        addItem,
        toggleItem,
        deleteItem,
        getItemById,
        fetchTasks,
        isLoading,
        error,
        currentUser
      }}
    >
      {children}
    </ChecklistContext.Provider>
  );
}

export function useChecklist() {
  const context = useContext(ChecklistContext);
  if (!context) {
    throw new Error("useChecklist must be used within a ChecklistProvider");
  }
  return context;
}
