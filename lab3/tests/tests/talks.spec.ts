import { test, expect, Page } from '@playwright/test';

function getTalksLink(page: Page) {
  return page.locator('frame[name="vpravo"]').contentFrame().getByRole('link', { name: 'РАЗГОВОРЫ С НАЗИДАНИЕМ ОТ МОИХ ДРУЗЬЕЙ ИЗ ВСЕЛЕННОЙ' })
}

test.describe('Talks Tests', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to the main page before each test
    await page.goto('/russian/default_ru.htm');
  });

  test('should have visible talks link', async ({ page }) => {
    // Check if the talks link is visible
    const talksLink = getTalksLink(page);
    await expect(talksLink).toBeVisible();
  });

  test('should have clickable talks link', async ({ page }) => {
    // Check if the talks link is clickable
    const talksLink = getTalksLink(page);
    await expect(talksLink).toBeEnabled();
  });

  test('should navigate to talks page', async ({ page }) => {
    // Click the talks link
    const talksLink = getTalksLink(page);
    await talksLink.click();

    // Wait for navigation
    await page.waitForLoadState('networkidle');

    expect(page).toHaveURL("https://angely-sveta.ru/russian/default_ru.htm");
    expect(page.locator('frame[name="vpravo"]').contentFrame().getByText('Любой человек является духовным лицом, но люди почти уже это не сознают')).toBeVisible()
  });
}); 