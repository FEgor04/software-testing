import { test, expect } from '@playwright/test';
import { getFrames, takeScreenshot } from './utils';

test.describe('Angely Sveta Homepage', () => {
  test('should load the homepage', async ({ page }) => {
    // Navigate to the homepage using baseURL from config
    await page.goto('/russian/default_ru.htm');
    
    // Make sure the page loaded with the correct title
    await expect(page).toHaveTitle(/РАЗГОВОРЫ|angely-sveta/);
    
    // Take a screenshot of the homepage
    await takeScreenshot(page, 'homepage');
  });

  test('should have proper meta tags', async ({ page }) => {
    await page.goto('/russian/default_ru.htm');
    
    // Check meta description exists
    const metaDescription = await page.locator('meta[name="description"]').getAttribute('content');
    expect(metaDescription).toBeTruthy();
    
    // Check meta keywords exist
    const metaKeywords = await page.locator('meta[name="keywords"]').getAttribute('content');
    expect(metaKeywords).toBeTruthy();
    expect(metaKeywords).toContain('Light');
  });

  test('should have frames structure', async ({ page }) => {
    await page.goto('/russian/default_ru.htm');
    
    // Use utility function to get frames
    const frames = await getFrames(page);
    
    // Check if frames are present (the site uses frames for navigation)
    expect(frames.length).toBeGreaterThan(1);
  });
}); 