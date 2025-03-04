package com.jellyone

data class Star(val name: String, val size: String, val color: String, val temperature: Int, val brightness: Double)

data class BinaryStarSystem(val primary: Star, val secondary: Star, val orbitalPeriod: Double)

data class Planet(val name: String, val radius: Double, val mass: Double, val atmosphere: Atmosphere, val orbit: Orbit, val hasLife: Boolean)

data class Atmosphere(val composition: Map<String, Double>, val pressure: Double)

data class Orbit(val semiMajorAxis: Double, val eccentricity: Double, val inclination: Double)

data class Moon(val name: String, val radius: Double, val mass: Double, val orbit: Orbit)

data class Galaxy(val name: String, val type: String, val distanceFromEarth: Double, val starSystems: List<BinaryStarSystem>)

data class SpaceScene(val galaxies: List<Galaxy>, val planets: List<Planet>, val moons: List<Moon>, val screen: Screen)

data class Screen(val width: Double, val height: Double, val elements: List<ScreenElement>)

data class ScreenElement(val position: Position, val type: ElementType, val color: String)

data class Position(val x: Double, val y: Double)

enum class ElementType { STAR, PLANET, MOON, LIGHT, CRESCENT }

fun main() {
    val star1 = Star("Red Star", "Plate-sized", "Red", 5000, 1.2)
    val star2 = Star("Red Star 2", "Plate-sized", "Red", 5000, 1.2)
    val binarySystem = BinaryStarSystem(star1, star2, 365.0)
    val planet = Planet("Mystery Planet", 6371.0, 5.972E24, Atmosphere(mapOf("O2" to 21.0, "N2" to 78.0), 1.0), Orbit(1.0, 0.0167, 0.0), false)
    val crescent = ScreenElement(Position(1500.0, 800.0), ElementType.CRESCENT, "Red-Black Gradient")
    val screen = Screen(1920.0, 1080.0, listOf(ScreenElement(Position(50.0, 500.0), ElementType.STAR, "Red"), crescent))
    val scene = SpaceScene(listOf(Galaxy("Milky Way", "Spiral", 0.0, listOf(binarySystem))), listOf(planet), emptyList(), screen)

    println("Space Scene Rendered: ")
    scene.screen.elements.forEach { println("Element: ${it.type} at position (${it.position.x}, ${it.position.y}) with color ${it.color}") }
}