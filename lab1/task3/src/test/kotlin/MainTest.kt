import com.jellyone.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SpaceModelTest {
    @Test
    fun testStarCreation() {
        val star = Star(name = "Red Star", size = "Plate-sized", color = "Red", temperature = 5000, brightness = 1.2)
        assertEquals("Red Star", star.name)
        assertEquals("Plate-sized", star.size)
        assertEquals("Red", star.color)
        assertEquals(5000, star.temperature)
        assertEquals(1.2, star.brightness)
    }

    @Test
    fun testBinaryStarSystem() {
        val star1 = Star(name = "Star A", size = "Large", color = "Red", temperature = 6000, brightness = 1.5)
        val star2 = Star(name = "Star B", size = "Medium", color = "Red", temperature = 5500, brightness = 1.3)
        val system = BinaryStarSystem(star1, star2, orbitalPeriod = 365.0)
        assertEquals("Star A", system.primary.name)
        assertEquals("Star B", system.secondary.name)
        assertEquals(365.0, system.orbitalPeriod)
    }

    @Test
    fun testPlanetCreation() {
        val atmosphere = Atmosphere(composition = mapOf("O2" to 21.0, "N2" to 78.0), pressure = 1.0)
        val orbit = Orbit(semiMajorAxis = 1.0, eccentricity = 0.0167, inclination = 0.0)
        val planet = Planet(
            name = "Unnamed Planet",
            radius = 6371.0,
            mass = 5.972E24,
            atmosphere = atmosphere,
            orbit = orbit,
            hasLife = false
        )
        assertEquals("Unnamed Planet", planet.name)
        assertEquals(6371.0, planet.radius)
        assertEquals(5.972E24, planet.mass)
        assertEquals(1.0, planet.atmosphere.pressure)
        assertEquals(1.0, planet.orbit.semiMajorAxis)
    }

    @Test
    fun testScreenElements() {
        val starElement = ScreenElement(Position(10.0, 20.0), ElementType.STAR, "Red")
        val crescentElement = ScreenElement(Position(5.0, 15.0), ElementType.CRESCENT, "Red-Black Gradient")
        val screen = Screen(width = 1920.0, height = 1080.0, elements = listOf(starElement, crescentElement))
        assertEquals(1920.0, screen.width)
        assertEquals(1080.0, screen.height)
        assertEquals(ElementType.STAR, screen.elements[0].type)
        assertEquals("Red", screen.elements[0].color)
        assertEquals(ElementType.CRESCENT, screen.elements[1].type)
        assertEquals("Red-Black Gradient", screen.elements[1].color)
    }

    @Test
    fun testGalaxyContainsBinarySystem() {
        val star1 = Star("Star A", "Large", "Blue", 10000, 2.0)
        val star2 = Star("Star B", "Medium", "Blue", 9500, 1.8)
        val binarySystem = BinaryStarSystem(star1, star2, 500.0)
        val galaxy = Galaxy("Andromeda", "Spiral", 2.5E6, listOf(binarySystem))
        assertTrue(galaxy.starSystems.contains(binarySystem))
    }

    @Test
    fun testMainSceneRendering() {
        main()
    }
}