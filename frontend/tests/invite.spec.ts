import { test, expect, Page } from '@playwright/test';

const login = async (page: Page, userId: string, password: string) => {
  await page.goto('/login');
  await page.fill('input#id', userId);
  await page.fill('input#password', password);
  await page.click('button[type="submit"]');
  await expect(page).toHaveURL('/');
};

test.describe('그룹 초대 수락 플로우', () => {
  test('유효하지 않은 초대 코드는 오류 화면을 보여준다', async ({ page }) => {
    await login(page, 'owner01', 'password');

    await page.goto('/invite/INVALIDCODE123');

    await expect(page.getByRole('heading', { name: '초대 오류' })).toBeVisible();
    await expect(page.getByText('유효하지 않거나 만료된 초대입니다')).toBeVisible();

    await page.getByRole('button', { name: '홈으로 돌아가기' }).click();
    await expect(page).toHaveURL('/');
  });

  test('owner가 발급한 코드로 guest가 그룹에 참여할 수 있다', async ({ browser }) => {
    // 1. owner01이 "우리 가족" 그룹 초대 코드 발급
    const ownerCtx = await browser.newContext();
    const ownerPage = await ownerCtx.newPage();
    await login(ownerPage, 'owner01', 'password');

    await ownerPage.goto('/profile');
    await ownerPage.getByRole('button', { name: '초대 코드 발급' }).first().click();

    const dialog = ownerPage.getByRole('dialog');
    await dialog.getByRole('button', { name: '발급하기' }).click();
    await expect(ownerPage.getByText('초대 코드가 발급되었습니다.')).toBeVisible();

    // 3자리 이상 영숫자 (코드는 숫자/문자 조합) — 발급된 코드 캡처
    const codeText = await dialog.locator('p.font-mono').first().innerText();
    const code = codeText.trim();
    expect(code.length).toBeGreaterThan(0);
    await ownerCtx.close();

    // 2. guest01이 초대 링크 방문 후 수락
    const guestCtx = await browser.newContext();
    const guestPage = await guestCtx.newPage();
    await login(guestPage, 'guest01', 'password');

    await guestPage.goto(`/invite/${code}`);

    await expect(guestPage.getByRole('heading', { name: '우리 가족' })).toBeVisible();
    await expect(guestPage.getByText(/현재 멤버 \d+명/)).toBeVisible();

    await guestPage.getByRole('button', { name: '수락하기' }).click();

    await expect(guestPage.getByText('가입되었습니다')).toBeVisible();
    await expect(guestPage).toHaveURL('/');

    await guestCtx.close();
  });
});
