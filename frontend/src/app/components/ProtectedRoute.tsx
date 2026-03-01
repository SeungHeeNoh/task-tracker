import { Navigate, Outlet } from "react-router";

export function ProtectedRoute() {
    const token = localStorage.getItem("accessToken");

    if (!token || token === "null" || token === "undefined") {
        return <Navigate to="/login" replace />;
    }

    return <Outlet />;
}
