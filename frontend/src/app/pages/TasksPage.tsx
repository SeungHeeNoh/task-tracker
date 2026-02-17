import { useChecklist } from "../context/ChecklistContext";
import { Link } from "react-router";
import { ChecklistItem } from "../components/ChecklistItem";

export default function TasksPage() {
  const { items, toggleItem, deleteItem, updateItem, groups } = useChecklist();

  const activeTasks = items.filter(item => !item.completed);
  const completedTasks = items.filter(item => item.completed);

  return (
    <div className="size-full bg-gradient-to-br from-blue-50 to-indigo-100 p-6 overflow-auto">
      <div className="max-w-4xl mx-auto">
        <div className="bg-white rounded-2xl shadow-xl p-8">
          <div className="mb-8">
            <h1 className="text-3xl mb-2">All Tasks</h1>
            <p className="text-gray-500">
              {completedTasks.length} completed, {activeTasks.length} active
            </p>
          </div>

          {items.length === 0 ? (
            <div className="text-center py-12 text-gray-400">
              <p>No tasks yet. Go to <Link to="/" className="text-blue-600 hover:underline">Home</Link> to add one!</p>
            </div>
          ) : (
            <div className="space-y-8">
              {activeTasks.length > 0 && (
                <div>
                  <h2 className="text-lg font-semibold mb-4">Active Tasks ({activeTasks.length})</h2>
                  <div className="space-y-4">
                    {activeTasks.map(item => (
                      <Link key={item.id} to={`/tasks/${item.id}`}>
                        <ChecklistItem
                          id={item.id}
                          text={item.text}
                          completed={item.completed}
                          creatorName={item.creatorName}
                          creatorAvatar={item.creatorAvatar}
                          completerName={item.completerName}
                          completerAvatar={item.completerAvatar}
                          completedDate={item.completedDate}
                          dueDate={item.dueDate}
                          group={item.group}
                          history={item.history}
                          onToggle={toggleItem}
                          onDelete={deleteItem}
                          onUpdate={updateItem}
                          availableGroups={groups}
                        />
                      </Link>
                    ))}
                  </div>
                </div>
              )}

              {completedTasks.length > 0 && (
                <div>
                  <h2 className="text-lg font-semibold mb-4">Completed Tasks ({completedTasks.length})</h2>
                  <div className="space-y-4">
                    {completedTasks.map(item => (
                      <Link key={item.id} to={`/tasks/${item.id}`}>
                        <ChecklistItem
                          id={item.id}
                          text={item.text}
                          completed={item.completed}
                          creatorName={item.creatorName}
                          creatorAvatar={item.creatorAvatar}
                          completerName={item.completerName}
                          completerAvatar={item.completerAvatar}
                          completedDate={item.completedDate}
                          dueDate={item.dueDate}
                          group={item.group}
                          history={item.history}
                          onToggle={toggleItem}
                          onDelete={deleteItem}
                          onUpdate={updateItem}
                          availableGroups={groups}
                        />
                      </Link>
                    ))}
                  </div>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}