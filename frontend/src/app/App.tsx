import { RouterProvider } from "react-router";
import { TaskProvider } from "./context/TaskContext";
import { router } from "./routes";

export default function App() {
  return (
    <TaskProvider>
      <RouterProvider router={router} />
    </TaskProvider>
  );
}
