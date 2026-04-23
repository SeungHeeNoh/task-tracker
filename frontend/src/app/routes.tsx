import { createBrowserRouter } from "react-router";
import { Layout } from "./components/Layout";
import { ProtectedRoute } from "./components/ProtectedRoute";
import Home from "./pages/Home";
import CalendarView from "./pages/CalendarView";
import TasksPage from "./pages/TasksPage";
import TaskDetail from "./pages/TaskDetail";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import Unauthorized from "./pages/errors/Unauthorized";
import Forbidden from "./pages/errors/Forbidden";
import NotFound from "./pages/errors/NotFound";
import ProfilePage from "./pages/ProfilePage";
import PasswordChangePage from "./pages/PasswordChangePage";
import InvitePage from "./pages/InvitePage";
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
          {
            path: "profile",
            Component: ProfilePage,
          },
          {
            path: "profile/password",
            Component: PasswordChangePage,
          },
          {
            path: "invite/:code",
            Component: InvitePage,
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
    path: "/signup",
    Component: SignupPage,
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
