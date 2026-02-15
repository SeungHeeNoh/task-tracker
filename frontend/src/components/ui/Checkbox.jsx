import * as React from "react"
import { Check } from "lucide-react"
import { cn } from "../../lib/utils"

/**
 * 커스텀 체크박스 컴포넌트
 * 기본 input[type=checkbox] 대신 button을 사용하여 스타일링했습니다.
 * Lucide React의 Check 아이콘을 사용하여 체크 상태를 표시합니다.
 */
const Checkbox = React.forwardRef(({ className, checked, onCheckedChange, ...props }, ref) => {
    return (
        <button
            type="button"
            role="checkbox"
            aria-checked={checked}
            data-state={checked ? "checked" : "unchecked"}
            onClick={() => onCheckedChange?.(!checked)}
            className={cn(
                "peer h-4 w-4 shrink-0 rounded-sm border border-primary ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 data-[state=checked]:bg-primary data-[state=checked]:text-primary-foreground",
                className
            )}
            ref={ref}
            {...props}
        >
            {checked && (
                <span className="flex items-center justify-center text-current">
                    <Check className="h-4 w-4" />
                </span>
            )}
        </button>
    )
})
Checkbox.displayName = "Checkbox"

export { Checkbox }
