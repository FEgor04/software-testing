import { test, expect, Page } from '@playwright/test';

function getConnectionTermsLink(page: Page) {
  return page.locator('frame[name="Obsah"]').contentFrame().getByRole('link', { name: 'Условия соединения с Космическими друзьми Сил света' })
}

test.describe('Connection Terms Tests', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to the main page before each test
    await page.goto('/russian/default_ru.htm');
  });

  test('should have visible connection terms link', async ({ page }) => {
    // Check if the connection terms link is visible
    const connectionTermsLink = getConnectionTermsLink(page);
    await expect(connectionTermsLink).toBeVisible();
  });

  test('should have clickable connection terms link', async ({ page }) => {
    // Check if the connection terms link is clickable
    const connectionTermsLink = getConnectionTermsLink(page);
    await expect(connectionTermsLink).toBeEnabled();
  });

  test('should navigate to connection terms page', async ({ page }) => {
    // Click the connection terms link
    const connectionTermsLink = getConnectionTermsLink(page);
    await connectionTermsLink.click();

    // Wait for navigation
    await page.waitForLoadState('networkidle');

    // Check if the page loaded successfully
    await expect(page.locator('frame[name="vpravo"]').contentFrame().getByText('Девиз: Коммуникация с миролюбивыми инопланетянами в полном размахе')).toBeVisible();
  });

}); 