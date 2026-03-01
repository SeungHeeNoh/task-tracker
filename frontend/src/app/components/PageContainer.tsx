import { ReactNode } from "react";

interface PageContainerProps {
    title: string;
    description?: ReactNode;
    children: ReactNode;
    className?: string; // Additional classes for the inner container if needed
}

export function PageContainer({ title, description, children, className = "" }: PageContainerProps) {
    return (
        <div className="size-full bg-gradient-to-br from-blue-50 to-indigo-100 p-6 overflow-auto">
            <div className="max-w-4xl mx-auto">
                <div className={`bg-white rounded-2xl shadow-xl p-8 ${className}`}>
                    <div className="mb-8">
                        <h1 className="text-3xl mb-2">{title}</h1>
                        {description && (
                            <p className="text-gray-500">
                                {description}
                            </p>
                        )}
                    </div>
                    {children}
                </div>
            </div>
        </div>
    );
}
