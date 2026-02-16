import { RouterProvider } from "react-router";
import { ChecklistProvider } from "./context/ChecklistContext";
import { router } from "./routes";

export default function App() {
  return (
    <ChecklistProvider>
      <RouterProvider router={router} />
    </ChecklistProvider>
  );
}
