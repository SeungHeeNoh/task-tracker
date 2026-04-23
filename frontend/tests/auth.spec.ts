import { test, expect } from '@playwright/test';

/**
 * 전제 조건:
 * - 백엔드(Spring Boot)가 localhost:8080 에서 기동 중
 * - backend/check-tracker/src/main/resources/db/test-data.sql 적용 상태
 *   (owner01 / password 계정 + "우리 가족" / "스터디 모임" 그룹 OWNER)
 */
test.describe('로그인 & 프로필 그룹 관리', () => {
  test('owner01 로그인 후 프로필에서 초대 코드를 발급한다', async ({ page }) => {
    await page.goto('/login');

    await page.fill('input#id', 'owner01');
    await page.fill('input#password', 'password');
    await page.click('button[type="submit"]');

    await expect(page).toHaveURL('/');

    await page.goto('/profile');
    await expect(page.getByRole('heading', { name: '내 그룹 관리' })).toBeVisible();

    await page.getByRole('button', { name: '초대 코드 발급' }).first().click();

    const dialog = page.getByRole('dialog');
    await expect(dialog.getByText('그룹 초대 코드 발급')).toBeVisible();

    await dialog.getByRole('button', { name: '발급하기' }).click();

    await expect(page.getByText('초대 코드가 발급되었습니다.')).toBeVisible();
    await expect(dialog.getByText('초대 링크')).toBeVisible();
  });
});
