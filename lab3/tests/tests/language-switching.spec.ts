import { test, expect, Page } from '@playwright/test';

function getUkLink(page: Page) {
  return page.locator('frame[name="vpravo"]').contentFrame().getByRole('link', { name: 'English' }).nth(1)
}

test.describe('Language Switching Tests', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to the main page before each test
    await page.goto('/russian/default_ru.htm');
    // Wait for the page to be fully loaded
    await page.waitForLoadState('networkidle');
  });

  test('should have visible UK flag', async ({ page }) => {
    // Check if the UK flag is visible
    const ukFlag = getUkLink(page);
    await expect(ukFlag).toBeVisible();
  });

  test('should have clickable UK flag', async ({ page }) => {
    // Check if the UK flag is clickable
    const ukFlag = getUkLink(page);
    await expect(ukFlag).toBeEnabled();
  });

  test('should redirect to English site', async ({ page }) => {
    // Click the UK flag
    const ukFlag = getUkLink(page);
    await ukFlag.click();

    // Wait for navigation and check URL
    await expect(page).toHaveURL(/universe-people\.com/);
  });

}); 