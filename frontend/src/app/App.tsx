import { RouterProvider } from "react-router";
import { TaskProvider } from "./context/TaskContext";
import { router } from "./routes";
import { Toaster } from "./components/ui/sonner";

export default function App() {
  return (
    <TaskProvider>
      <RouterProvider router={router} />
      <Toaster />
    </TaskProvider>
  );
}
