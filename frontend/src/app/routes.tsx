import { createBrowserRouter } from "react-router";
import { Layout } from "./components/Layout";
import { ProtectedRoute } from "./components/ProtectedRoute";
import Home from "./pages/Home";
import CalendarView from "./pages/CalendarView";
import TasksPage from "./pages/TasksPage";
import TaskDetail from "./pages/TaskDetail";
import LoginPage from "./pages/LoginPage";

export const router = createBrowserRouter([
  {
    path: "/",
    Component: ProtectedRoute,
    children: [
      {
        path: "/",
        Component: Layout,
        children: [
          {
            index: true,
            Component: Home,
          },
          {
            path: "calendar",
            Component: CalendarView,
          },
          {
            path: "tasks",
            Component: TasksPage,
          },
          {
            path: "tasks/:id",
            Component: TaskDetail,
          },
        ],
      },
    ],
  },
  {
    path: "/login",
    Component: LoginPage,
  },
]);
