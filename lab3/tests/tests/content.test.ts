import { test, expect } from '@playwright/test';
import { getFrames, findContentFrame, takeScreenshot } from './utils';

test.describe('Angely Sveta Content', () => {
  test('should have readable content in main content frame', async ({ page }) => {
    await page.goto('/russian/default_ru.htm');
    
    // Use utility functions to get frames and find content frame
    const frames = await getFrames(page);
    const contentFrame = await findContentFrame(frames);
    
    // Skip test if we can't identify a content frame
    test.skip(!contentFrame, 'No content frame identified');
    
    if (contentFrame) {
      // Check for text content in the content frame
      const textElements = await contentFrame.locator('p, div, h1, h2, h3, h4, h5, h6, span').all();
      
      // Verify there is some text content
      expect(textElements.length).toBeGreaterThan(0);
      
      // Check that at least one element has text
      let hasText = false;
      for (const element of textElements) {
        const text = await element.textContent();
        if (text && text.trim().length > 0) {
          hasText = true;
          break;
        }
      }
      expect(hasText).toBeTruthy();
      
      // Take a screenshot of the content
      await takeScreenshot(page, 'content');
    }
  });
  
  test('should load images correctly', async ({ page }) => {
    await page.goto('/russian/default_ru.htm');
    
    // Use utility function to get frames
    const frames = await getFrames(page);
    
    // Check for images across all frames
    let totalImages = 0;
    
    for (const frame of frames) {
      const images = await frame.locator('img').all();
      totalImages += images.length;
      
      // Check natural width and height of images to verify they loaded
      for (const img of images) {
        const naturalWidth = await img.evaluate(el => (el as HTMLImageElement).naturalWidth);
        const naturalHeight = await img.evaluate(el => (el as HTMLImageElement).naturalHeight);
        
        // If image has loaded, it should have non-zero dimensions
        expect(naturalWidth).toBeGreaterThan(0);
        expect(naturalHeight).toBeGreaterThan(0);
      }
    }
    
    // Note: This test will pass even if there are no images
    // Some spiritual sites might be text-heavy with few or no images
    test.info().annotations.push({
      type: 'info',
      description: `Found ${totalImages} images across all frames`
    });
    
    // Take a screenshot of the page with images
    if (totalImages > 0) {
      await takeScreenshot(page, 'images');
    }
  });
}); 