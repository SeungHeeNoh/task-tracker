import { createBrowserRouter } from "react-router";
import { Layout } from "./components/Layout";
import { ProtectedRoute } from "./components/ProtectedRoute";
import Home from "./pages/Home";
import CalendarView from "./pages/CalendarView";
import TasksPage from "./pages/TasksPage";
import TaskDetail from "./pages/TaskDetail";
import LoginPage from "./pages/LoginPage";
import Unauthorized from "./pages/errors/Unauthorized";
import Forbidden from "./pages/errors/Forbidden";
import NotFound from "./pages/errors/NotFound";

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
  {
    path: "/401",
    Component: Unauthorized,
  },
  {
    path: "/403",
    Component: Forbidden,
  },
  {
    path: "/404",
    Component: NotFound,
  },
  {
    path: "*",
    Component: NotFound,
  },
]);
