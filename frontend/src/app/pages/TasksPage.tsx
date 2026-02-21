import { useTask } from "../context/TaskContext";
import { Link } from "react-router";
import { TaskItem } from "../components/TaskItem";
import { PageContainer } from "../components/PageContainer";

export default function TasksPage() {
  const { items, toggleItem, deleteItem, updateItem, groups } = useTask();

  const activeTasks = items.filter(item => !item.completed);
  const completedTasks = items.filter(item => item.completed);

  return (
    <PageContainer
      title="All Tasks"
      description={`${completedTasks.length} completed, ${activeTasks.length} active`}
    >
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
                  <TaskItem
                    key={item.id}
                    item={item}
                    onToggle={toggleItem}
                    onDelete={deleteItem}
                    onUpdate={updateItem}
                    availableGroups={groups}
                  />
                ))}
              </div>
            </div>
          )}

          {completedTasks.length > 0 && (
            <div>
              <h2 className="text-lg font-semibold mb-4">Completed Tasks ({completedTasks.length})</h2>
              <div className="space-y-4">
                {completedTasks.map(item => (
                  <TaskItem
                    key={item.id}
                    item={item}
                    onToggle={toggleItem}
                    onDelete={deleteItem}
                    onUpdate={updateItem}
                    availableGroups={groups}
                  />
                ))}
              </div>
            </div>
          )}
        </div>
      )}
    </PageContainer>
  );
}