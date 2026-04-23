import { test, expect, type Page, type TestInfo } from '@playwright/test';
import path from 'path';
import fs from 'fs';

/**
 * SB(스토리보드/사양서) 추출용 자동 캡처 스크립트.
 *
 * 전제 조건:
 * - 백엔드(Spring Boot)가 localhost:8080 에서 기동 중
 * - backend/check-tracker/src/main/resources/db/test-data.sql 적용 상태
 * - guest01 은 본인의 기본 그룹이 DB에 삽입되어 있어 홈 진입이 정상이어야 함
 *
 * 실행:
 *   npx playwright test sb-capture.spec.ts
 *   (chromium → *-desktop.png, mobile-chrome → *-mobile.png)
 *
 * 결과물: repo/docs/sb/*.png
 */

// npx playwright test 는 frontend/ 에서 실행되므로 CWD 기준 상대경로 사용
const SB_ROOT = path.resolve(process.cwd(), '../docs/sb');

test.beforeAll(() => {
  fs.mkdirSync(SB_ROOT, { recursive: true });
});

function deviceSuffix(info: TestInfo): string {
  return info.project.name === 'mobile-chrome' ? 'mobile' : 'desktop';
}

async function shoot(
  page: Page,
  info: TestInfo,
  name: string,
  opts: { fullPage?: boolean } = {},
): Promise<void> {
  const file = path.join(SB_ROOT, `${name}-${deviceSuffix(info)}.png`);
  await page.screenshot({
    path: file,
    fullPage: opts.fullPage ?? true,
    animations: 'disabled',
  });
}

async function login(page: Page, userId: string, password: string): Promise<void> {
  await page.goto('/login');
  await page.fill('input#id', userId);
  await page.fill('input#password', password);
  await page.click('button[type="submit"]');
  await expect(page).toHaveURL('/');
}

// 시나리오 간 순서 보장 (초대 코드 발급→수락 흐름 때문)
test.describe.configure({ mode: 'serial' });

test.describe('SB 캡처', () => {
  test('01-02. 로그인 / 회원가입', async ({ page }, info) => {
    await page.goto('/login');
    await expect(page.locator('input#id')).toBeVisible();
    await shoot(page, info, '01-login');

    await page.goto('/signup');
    await expect(page.locator('input#id')).toBeVisible();
    await shoot(page, info, '02-signup');
  });

  test('03-07. owner01 메인 플로우', async ({ page }, info) => {
    await login(page, 'owner01', 'password');

    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await shoot(page, info, '03-home');

    await page.goto('/tasks');
    await page.waitForLoadState('networkidle');
    await shoot(page, info, '04-tasks-list');

    await page.goto('/tasks/1');
    await page.waitForLoadState('networkidle');
    await shoot(page, info, '05-task-detail');

    await page.goto('/calendar');
    await page.waitForLoadState('networkidle');
    await shoot(page, info, '06-calendar');

    await page.goto('/profile');
    await expect(page.getByRole('heading', { name: '내 그룹 관리' })).toBeVisible();
    await shoot(page, info, '07-profile');
  });

  test('03b. guest01 홈 (기본 개인 그룹만)', async ({ page }, info) => {
    await login(page, 'guest01', 'password');
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await shoot(page, info, '03b-home-guest');
  });

  test('08. 초대 코드 발급 다이얼로그', async ({ page }, info) => {
    await login(page, 'owner01', 'password');
    await page.goto('/profile');

    await page.getByRole('button', { name: '초대 코드 발급' }).first().click();
    const dialog = page.getByRole('dialog');
    await expect(dialog).toBeVisible();
    await shoot(page, info, '08-invite-dialog');

    await dialog.getByRole('button', { name: '발급하기' }).click();
    await expect(page.getByText('초대 코드가 발급되었습니다.')).toBeVisible();
    await shoot(page, info, '08b-invite-dialog-issued');
  });

  test('09-10. 초대 수락 / 오류 화면 (guest01)', async ({ browser }, info) => {
    // 1) owner01 이 "우리 가족" 초대 코드 발급
    const ownerCtx = await browser.newContext();
    const ownerPage = await ownerCtx.newPage();
    await login(ownerPage, 'owner01', 'password');
    await ownerPage.goto('/profile');
    await ownerPage.getByRole('button', { name: '초대 코드 발급' }).first().click();
    const dialog = ownerPage.getByRole('dialog');
    await dialog.getByRole('button', { name: '발급하기' }).click();
    await expect(ownerPage.getByText('초대 코드가 발급되었습니다.')).toBeVisible();
    const code = (await dialog.locator('p.font-mono').first().innerText()).trim();
    await ownerCtx.close();

    // 2) guest01 가 초대 수락 화면 진입
    const guestCtx = await browser.newContext();
    const guestPage = await guestCtx.newPage();
    await login(guestPage, 'guest01', 'password');

    await guestPage.goto(`/invite/${code}`);
    await expect(guestPage.getByRole('button', { name: '수락하기' })).toBeVisible();
    await shoot(guestPage, info, '09-invite-accept');

    // 3) 잘못된 코드로 오류 화면
    await guestPage.goto('/invite/INVALIDCODE123');
    await expect(guestPage.getByRole('heading', { name: '초대 오류' })).toBeVisible();
    await shoot(guestPage, info, '10-invite-error');

    await guestCtx.close();
  });

  test('11. full01 프로필 (그룹 상한 5개)', async ({ page }, info) => {
    await login(page, 'full01', 'password');
    await page.goto('/profile');
    await expect(page.getByRole('heading', { name: '내 그룹 관리' })).toBeVisible();
    await shoot(page, info, '11-profile-full');
  });

  test('12. 비밀번호 변경', async ({ page }, info) => {
    await login(page, 'owner01', 'password');
    await page.goto('/profile/password');
    await page.waitForLoadState('networkidle');
    await shoot(page, info, '12-password-change');
  });

  test('13-15. 에러 페이지', async ({ page }, info) => {
    await page.goto('/401');
    await page.waitForLoadState('networkidle');
    await shoot(page, info, '13-401');

    await page.goto('/403');
    await page.waitForLoadState('networkidle');
    await shoot(page, info, '14-403');

    await page.goto('/404');
    await page.waitForLoadState('networkidle');
    await shoot(page, info, '15-404');
  });
});
