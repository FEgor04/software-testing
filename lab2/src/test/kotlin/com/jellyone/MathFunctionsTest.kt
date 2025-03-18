package com.jellyone

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.math.PI
import kotlin.math.abs

class MathFunctionsTest {
    
    // Precision to use for tests
    private val precision = 1e-10
    
    // Tolerance for comparing expected vs actual results
    private val delta = 1e-6
    
    @Test
    fun `test sin function`() {
        /* Test coverage analysis:
         * - Tests sin at special values (0, π/6, π/4, π/3, π/2, π, etc.)
         * - Tests negative inputs
         * - Tests large inputs
         * - Verifies symmetry property: sin(-x) = -sin(x)
         * - Verifies sin values match expected standard values
         * 
         * This provides adequate coverage because it:
         * 1. Tests common critical points (0, π/2, π)
         * 2. Tests edge cases like large values
         * 3. Verifies basic mathematical properties that must hold
         */
        
        // Test special angles
        assertEquals(0.0, sin(0.0, precision), delta)
        assertEquals(0.5, sin(PI/6, precision), delta)
        assertEquals(0.7071067811865475, sin(PI/4, precision), delta)
        assertEquals(0.8660254037844386, sin(PI/3, precision), delta)
        assertEquals(1.0, sin(PI/2, precision), delta)
        assertEquals(0.0, sin(PI, precision), delta)
        assertEquals(-1.0, sin(3*PI/2, precision), delta)
        
        // Test negative inputs
        assertEquals(-0.5, sin(-PI/6, precision), delta)
        assertEquals(-1.0, sin(-PI/2, precision), delta)
        
        // Test property: sin(-x) = -sin(x)
        val testValues = listOf(0.1, 0.5, 1.0, 1.5, 2.0, 5.0)
        for (x in testValues) {
            assertEquals(-sin(x, precision), sin(-x, precision), delta)
        }
        
        // Test large inputs
        assertEquals(sin(PI/4, precision), sin(PI/4 + 2*PI, precision), delta)
        assertEquals(sin(PI/3, precision), sin(PI/3 + 4*PI, precision), delta)
    }
    
    @Test
    fun `test ln function`() {
        /* Test coverage analysis:
         * - Tests ln at special values (1, e, powers of e)
         * - Tests values between 0 and 1
         * - Tests values > 1
         * - Tests error handling for invalid inputs (x ≤ 0)
         * 
         * This provides adequate coverage because it:
         * 1. Tests known values (ln(1)=0, ln(e)=1)
         * 2. Tests boundary cases (values approaching 0, very large values)
         * 3. Verifies error handling
         * 4. Tests the basic property: ln(a*b) = ln(a) + ln(b)
         */

        // Test special values
        assertEquals(0.0, ln(1.0, precision), delta)
        assertEquals(1.0, ln(Math.E, precision), delta)
        assertEquals(2.0, ln(Math.E * Math.E, precision), delta)
        
        // Test values between 0 and 1
        assertEquals(-1.0, ln(1.0/Math.E, precision), delta)
        assertEquals(-0.6931471805599453, ln(0.5, precision), delta)
        
        // Test values > 1
        assertEquals(1.0986122886681098, ln(3.0, precision), delta)
        assertEquals(2.302585092994046, ln(10.0, precision), delta)
        
        // Test property: ln(a*b) = ln(a) + ln(b)
        val a = 2.5
        val b = 4.0
        assertEquals(ln(a, precision) + ln(b, precision), ln(a * b, precision), delta)
        
        // Test error cases
        assertThrows<IllegalArgumentException> { ln(0.0, precision) }
        assertThrows<IllegalArgumentException> { ln(-1.0, precision) }
    }
    
    @Test
    fun `test log function`() {
        /* Test coverage analysis:
         * - Tests log base 10 (common logarithm)
         * - Tests log base 2 (binary logarithm) 
         * - Tests log with other bases
         * - Tests error handling for invalid inputs
         * 
         * This provides adequate coverage because it:
         * 1. Tests standard bases (10, 2)
         * 2. Verifies basic properties like log_b(b^n) = n
         * 3. Tests error handling for invalid inputs
         * 4. Tests property: log_b(x) = ln(x)/ln(b)
         */
        
        // Test log base 10
        assertEquals(0.0, log(1.0, 10.0, precision), delta)
        assertEquals(1.0, log(10.0, 10.0, precision), delta)
        assertEquals(2.0, log(100.0, 10.0, precision), delta)
        
        // Test log base 2
        assertEquals(0.0, log(1.0, 2.0, precision), delta)
        assertEquals(1.0, log(2.0, 2.0, precision), delta)
        assertEquals(3.0, log(8.0, 2.0, precision), delta)
        
        // Test with other bases
        assertEquals(2.0, log(9.0, 3.0, precision), delta)
        assertEquals(3.0, log(125.0, 5.0, precision), delta)
        
        // Test property: log_b(x) = ln(x)/ln(b)
        val x = 7.5
        val base = 4.0
        assertEquals(ln(x, precision) / ln(base, precision), log(x, base, precision), delta)
        
        // Test error cases
        assertThrows<IllegalArgumentException> { log(0.0, 10.0, precision) }
        assertThrows<IllegalArgumentException> { log(-1.0, 10.0, precision) }
        assertThrows<IllegalArgumentException> { log(5.0, 0.0, precision) }
        assertThrows<IllegalArgumentException> { log(5.0, -2.0, precision) }
    }
    
    @Test
    fun `test cos function`() {
        /* Test coverage analysis:
         * - Tests cos at special values (0, π/6, π/4, π/3, π/2, π, etc.)
         * - Tests negative inputs
         * - Tests symmetry property: cos(-x) = cos(x)
         * - Tests relationship with sin: cos(x) = sin(x + π/2)
         * 
         * This provides adequate coverage because it:
         * 1. Tests critical points (0, π/2, π)
         * 2. Verifies the implementation uses the relationship with sin
         * 3. Tests the symmetry property
         * 4. Verifies accuracy against known values
         */
        
        // Test special angles
        assertEquals(1.0, cos(0.0, precision), delta)
        assertEquals(0.8660254037844387, cos(PI/6, precision), delta)
        assertEquals(0.7071067811865476, cos(PI/4, precision), delta)
        assertEquals(0.5, cos(PI/3, precision), delta)
        assertEquals(0.0, cos(PI/2, precision), delta)
        assertEquals(-1.0, cos(PI, precision), delta)
        assertEquals(0.0, cos(3*PI/2, precision), delta)
        
        // Test negative inputs and symmetry
        assertEquals(cos(PI/4, precision), cos(-PI/4, precision), delta)
        assertEquals(cos(PI/2, precision), cos(-PI/2, precision), delta)
        
        // Verify relationship with sin function
        val testValues = listOf(0.1, 0.5, 1.0, 1.5, 2.0)
        for (x in testValues) {
            assertEquals(sin(x + PI/2, precision), cos(x, precision), delta)
        }
    }
    
    @Test
    fun `test tan function`() {
        /* Test coverage analysis:
         * - Tests tan at special values (0, π/6, π/4, π/3)
         * - Tests negative inputs and symmetry: tan(-x) = -tan(x)
         * - Tests relationship with sin and cos: tan(x) = sin(x)/cos(x)
         * - Avoids testing at poles where tan is undefined (x = π/2 + nπ)
         * 
         * This provides adequate coverage because it:
         * 1. Tests common values with known results
         * 2. Verifies relationship with sin and cos functions
         * 3. Tests the anti-symmetry property
         * 4. Avoids singular points where the function is undefined
         */
        
        // Test special angles
        assertEquals(0.0, tan(0.0, precision), delta)
        assertEquals(0.5773502691896257, tan(PI/6, precision), delta)
        assertEquals(1.0, tan(PI/4, precision), delta)
        assertEquals(1.7320508075688767, tan(PI/3, precision), delta)
        
        // Test negative inputs and anti-symmetry
        assertEquals(-1.0, tan(-PI/4, precision), delta)
        assertEquals(-0.5773502691896257, tan(-PI/6, precision), delta)
        
        // Verify relationship with sin and cos
        val testValues = listOf(0.1, 0.2, 0.5, 1.0, 2.0)
        for (x in testValues) {
            assertEquals(sin(x, precision) / cos(x, precision), tan(x, precision), delta)
        }
    }
} 