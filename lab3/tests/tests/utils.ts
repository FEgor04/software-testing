import { Page, Frame } from '@playwright/test';
import * as fs from 'fs';
import * as path from 'path';

/**
 * Utility functions for angely-sveta website tests
 */

/**
 * Waits for frames to load and returns all frames
 * @param page The Playwright page object
 * @returns Array of frames
 */
export async function getFrames(page: Page): Promise<Frame[]> {
  // Wait for frames to load
  await page.waitForTimeout(2000);
  
  // Get all frames
  return page.frames();
}

/**
 * Attempts to find the main content frame
 * @param frames Array of frames to search through
 * @returns Content frame or undefined if not found
 */
export async function findContentFrame(frames: Frame[]): Promise<Frame | undefined> {
  // Try to find a content frame by URL patterns
  let contentFrame = frames.find(frame => 
    frame.url().includes('content') || 
    frame.url().includes('main') ||
    frame.url().includes('right')
  );
  
  // If not found by URL, try to find by content
  if (!contentFrame) {
    for (const frame of frames) {
      // Check if the frame has a significant number of text elements
      // This is a simple heuristic and may need adjustment
      const textElems = await frame.locator('p, div, h1, h2, h3, h4, h5, h6').count();
      if (textElems > 5) {
        contentFrame = frame;
        break;
      }
    }
  }
  
  return contentFrame;
}

/**
 * Attempts to find the navigation frame
 * @param frames Array of frames to search through
 * @returns Navigation frame or undefined if not found
 */
export async function findNavigationFrame(frames: Frame[]): Promise<Frame | undefined> {
  // Try to find a navigation frame by URL patterns
  let navFrame = frames.find(frame => 
    frame.url().includes('menu') || 
    frame.url().includes('nav') ||
    frame.url().includes('left')
  );
  
  // If not found by URL, try to find by content
  if (!navFrame) {
    for (const frame of frames) {
      // Navigation frames typically have multiple links
      const linkCount = await frame.locator('a').count();
      if (linkCount > 3) {
        navFrame = frame;
        break;
      }
    }
  }
  
  return navFrame;
}

/**
 * Creates a formatted timestamp for logs and filenames
 * @returns Formatted timestamp string
 */
export function getTimestamp(): string {
  const now = new Date();
  return now.toISOString().replace(/:/g, '-').replace(/\..+/, '');
}

/**
 * Ensures the screenshots directory exists
 */
function ensureScreenshotsDirExists(): void {
  const screenshotsDir = path.resolve(__dirname, 'screenshots');
  if (!fs.existsSync(screenshotsDir)) {
    fs.mkdirSync(screenshotsDir, { recursive: true });
  }
}

/**
 * Takes a full-page screenshot with a timestamped filename
 * @param page The Playwright page object
 * @param namePrefix Prefix for the screenshot filename
 */
export async function takeScreenshot(page: Page, namePrefix: string): Promise<void> {
  // Ensure screenshots directory exists
  ensureScreenshotsDirExists();
  
  const timestamp = getTimestamp();
  const screenshotPath = path.resolve(__dirname, `screenshots/${namePrefix}-${timestamp}.png`);
  
  await page.screenshot({ 
    path: screenshotPath,
    fullPage: true 
  });
} 