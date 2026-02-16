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
  currentUser: {
    name: string;
    avatar: string;
  };
}

const ChecklistContext = createContext<ChecklistContextType | undefined>(undefined);

export function ChecklistProvider({ children }: { children: ReactNode }) {
  const [items, setItems] = useState<ChecklistItemType[]>([
    { 
      id: "1", 
      text: "Buy groceries", 
      completed: false,
      creatorName: "Sarah Johnson",
      creatorAvatar: "https://images.unsplash.com/photo-1649589244330-09ca58e4fa64?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9mZXNzaW9uYWwlMjB3b21hbiUyMHBvcnRyYWl0fGVufDF8fHx8MTc3MTAzNjQyMXww&ixlib=rb-4.1.0&q=80&w=1080",
      dueDate: "2026-02-16",
      history: [
        {
          id: "h1",
          type: "created",
          userName: "Sarah Johnson",
          userAvatar: "https://images.unsplash.com/photo-1649589244330-09ca58e4fa64?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9mZXNzaW9uYWwlMjB3b21hbiUyMHBvcnRyYWl0fGVufDF8fHx8MTc3MTAzNjQyMXww&ixlib=rb-4.1.0&q=80&w=1080",
          timestamp: "Feb 15, 2026 at 9:30 AM"
        }
      ]
    },
    { 
      id: "2", 
      text: "Finish project report", 
      completed: true,
      creatorName: "Michael Chen",
      creatorAvatar: "https://images.unsplash.com/photo-1554765345-6ad6a5417cde?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9mZXNzaW9uYWwlMjBtYW4lMjBwb3J0cmFpdHxlbnwxfHx8fDE3NzEwOTI4OTB8MA&ixlib=rb-4.1.0&q=80&w=1080",
      completerName: "Emily Rodriguez",
      completerAvatar: "https://images.unsplash.com/photo-1573497019940-1c28c88b4f3e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxidXNpbmVzcyUyMHdvbWFuJTIwaGVhZHNob3R8ZW58MXx8fHwxNzcxMTAyNTEyfDA&ixlib=rb-4.1.0&q=80&w=1080",
      completedDate: "Feb 14, 2026",
      dueDate: "2026-02-14",
      history: [
        {
          id: "h3",
          type: "completed",
          userName: "Emily Rodriguez",
          userAvatar: "https://images.unsplash.com/photo-1573497019940-1c28c88b4f3e?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxidXNpbmVzcyUyMHdvbWFuJTIwaGVhZHNob3R8ZW58MXx8fHwxNzcxMTAyNTEyfDA&ixlib=rb-4.1.0&q=80&w=1080",
          timestamp: "Feb 14, 2026 at 3:45 PM"
        },
        {
          id: "h2",
          type: "created",
          userName: "Michael Chen",
          userAvatar: "https://images.unsplash.com/photo-1554765345-6ad6a5417cde?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9mZXNzaW9uYWwlMjBtYW4lMjBwb3J0cmFpdHxlbnwxfHx8fDE3NzEwOTI4OTB8MA&ixlib=rb-4.1.0&q=80&w=1080",
          timestamp: "Feb 13, 2026 at 10:00 AM"
        }
      ]
    },
    { 
      id: "3", 
      text: "Call dentist", 
      completed: false,
      creatorName: "Alex Kim",
      creatorAvatar: "https://images.unsplash.com/photo-1510947565940-a38e2443c426?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx5b3VuZyUyMHByb2Zlc3Npb25hbCUyMHBlcnNvbnxlbnwxfHx8fDE3NzEwNzI3MjN8MA&ixlib=rb-4.1.0&q=80&w=1080",
      dueDate: "2026-02-18",
      history: [
        {
          id: "h4",
          type: "created",
          userName: "Alex Kim",
          userAvatar: "https://images.unsplash.com/photo-1510947565940-a38e2443c426?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx5b3VuZyUyMHByb2Zlc3Npb25hbCUyMHBlcnNvbnxlbnwxfHx8fDE3NzEwNzI3MjN8MA&ixlib=rb-4.1.0&q=80&w=1080",
          timestamp: "Feb 14, 2026 at 11:20 AM"
        }
      ]
    },
    { 
      id: "4", 
      text: "Team meeting", 
      completed: false,
      creatorName: "Sarah Johnson",
      creatorAvatar: "https://images.unsplash.com/photo-1649589244330-09ca58e4fa64?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9mZXNzaW9uYWwlMjB3b21hbiUyMHBvcnRyYWl0fGVufDF8fHx8MTc3MTAzNjQyMXww&ixlib=rb-4.1.0&q=80&w=1080",
      dueDate: "2026-02-15",
      history: [
        {
          id: "h5",
          type: "created",
          userName: "Sarah Johnson",
          userAvatar: "https://images.unsplash.com/photo-1649589244330-09ca58e4fa64?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9mZXNzaW9uYWwlMjB3b21hbiUyMHBvcnRyYWl0fGVufDF8fHx8MTc3MTAzNjQyMXww&ixlib=rb-4.1.0&q=80&w=1080",
          timestamp: "Feb 14, 2026 at 2:00 PM"
        }
      ]
    },
    { 
      id: "5", 
      text: "Review quarterly reports", 
      completed: false,
      creatorName: "Michael Chen",
      creatorAvatar: "https://images.unsplash.com/photo-1554765345-6ad6a5417cde?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9mZXNzaW9uYWwlMjBtYW4lMjBwb3J0cmFpdHxlbnwxfHx8fDE3NzEwOTI4OTB8MA&ixlib=rb-4.1.0&q=80&w=1080",
      dueDate: "2026-02-28",
      history: [
        {
          id: "h6",
          type: "created",
          userName: "Michael Chen",
          userAvatar: "https://images.unsplash.com/photo-1554765345-6ad6a5417cde?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxwcm9mZXNzaW9uYWwlMjBtYW4lMjBwb3J0cmFpdHxlbnwxfHx8fDE3NzEwOTI4OTB8MA&ixlib=rb-4.1.0&q=80&w=1080",
          timestamp: "Feb 15, 2026 at 8:00 AM"
        }
      ]
    },
  ]);

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
