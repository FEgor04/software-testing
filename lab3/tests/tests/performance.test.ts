import { test, expect } from '@playwright/test';

test.describe('Angely Sveta Performance', () => {
  test('should load homepage within reasonable time', async ({ page }) => {
    // Start timing
    const startTime = Date.now();
    
    // Navigate to the homepage
    const response = await page.goto('/russian/default_ru.htm');
    
    // Ensure the response is successful
    expect(response?.status()).toBeLessThan(400);
    
    // Calculate load time for initial HTML
    const initialLoadTime = Date.now() - startTime;
    
    // Wait for frames to load
    await page.waitForLoadState('domcontentloaded');
    
    // Calculate full load time
    const fullLoadTime = Date.now() - startTime;
    
    // Log performance metrics
    test.info().annotations.push({
      type: 'info',
      description: `Initial load time: ${initialLoadTime}ms, Full load time: ${fullLoadTime}ms`
    });
    
    // This is a flexible assertion as the site might be slow or hosting might vary
    // Adjust these thresholds based on actual performance
    expect(fullLoadTime).toBeLessThan(15000); // 15 seconds is generous
  });
  
  test('should have acceptable resource usage', async ({ page }) => {
    // Navigate to the homepage
    await page.goto('/russian/default_ru.htm');
    
    // Wait for the page to be fully loaded
    await page.waitForLoadState('load');
    
    // Get performance metrics
    const performanceMetrics = await page.evaluate(() => {
      const resources = performance.getEntriesByType('resource');
      
      // Calculate total transfer size
      let totalBytes = 0;
      let resourceCount = resources.length;
      
      resources.forEach((resource: any) => {
        if (resource.transferSize) {
          totalBytes += resource.transferSize;
        }
      });
      
      return {
        resourceCount,
        totalBytes,
      };
    });
    
    // Log resource usage
    test.info().annotations.push({
      type: 'info',
      description: `Resource count: ${performanceMetrics.resourceCount}, Total transfer size: ${(performanceMetrics.totalBytes / 1024 / 1024).toFixed(2)}MB`
    });
    
    // This is a flexible assertion as website size may vary
    // Old spiritual websites are typically lightweight
    expect(performanceMetrics.totalBytes).toBeLessThan(10 * 1024 * 1024); // Less than 10MB
  });
}); 