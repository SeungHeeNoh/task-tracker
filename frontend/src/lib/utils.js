import { clsx } from "clsx"
import { twMerge } from "tailwind-merge"

/**
 * 클래스 이름 병합 유틸리티
 * Tailwind CSS 클래스 충돌을 해결하고(twMerge), 조건부 클래스를 결합(clsx)합니다.
 * @param {...string} inputs - 병합할 클래스 이름들
 * @returns {string} 병합된 최적화된 클래스 문자열
 */
export function cn(...inputs) {
    return twMerge(clsx(inputs))
}
