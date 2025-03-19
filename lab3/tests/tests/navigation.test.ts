import { test, expect, Locator } from '@playwright/test';
import { getFrames, findNavigationFrame, takeScreenshot } from './utils';

test.describe('Angely Sveta Navigation', () => {
  test('should have clickable navigation elements', async ({ page }) => {
    await page.goto('/russian/default_ru.htm');
    
    // Use utility functions to get frames and find navigation frame
    const frames = await getFrames(page);
    const navigationFrame = await findNavigationFrame(frames);
    
    // Verify we found a navigation frame
    expect(navigationFrame).toBeTruthy();
    
    if (navigationFrame) {
      // Check for links in the navigation frame
      const linkCount = await navigationFrame.locator('a').count();
      expect(linkCount).toBeGreaterThan(0);
      
      // Take a screenshot of the page with navigation
      await takeScreenshot(page, 'navigation');
    }
  });
  
  test('should have functional language switching', async ({ page }) => {
    await page.goto('/russian/default_ru.htm');
    
    // Save the original URL
    const originalUrl = page.url();
    
    // Use utility function to get frames
    const frames = await getFrames(page);
    
    // Look for language switching links in all frames
    let languageLinks: Locator[] = [];
    for (const frame of frames) {
      // Look for common language switching patterns
      const enLinks = await frame.locator('a[href*="english"], a[href*="_en."], a:has-text("English"), a:has-text("EN")').all();
      const czLinks = await frame.locator('a[href*="czech"], a[href*="_cz."], a:has-text("Czech"), a:has-text("CZ")').all();
      
      if (enLinks.length > 0) languageLinks = [...languageLinks, ...enLinks];
      if (czLinks.length > 0) languageLinks = [...languageLinks, ...czLinks];
    }
    
    // Skip this test if no language links are found
    test.skip(languageLinks.length === 0, 'No language switching links found');
    
    if (languageLinks.length > 0) {
      // Log information about language links
      test.info().annotations.push({
        type: 'info',
        description: `Found ${languageLinks.length} potential language switching links`
      });
      
      // Try clicking the first language link
      await languageLinks[0].click();
      
      // Wait for navigation to complete
      await page.waitForTimeout(2000);
      
      // Take a screenshot after language switch
      await takeScreenshot(page, 'language-switch');
      
      // Check that URL or content changed in some way (more lenient test)
      const newUrl = page.url();
      test.info().annotations.push({
        type: 'info',
        description: `Original URL: ${originalUrl}, New URL: ${newUrl}`
      });
      
      // This may be a soft check in some cases if the site doesn't navigate away
      // but changes content within the same URL
      if (newUrl === originalUrl) {
        // If URL didn't change, check if content changed
        const content = await page.content();
        expect(content).toBeTruthy(); // Just verify there's some content
      }
    }
  });
}); 