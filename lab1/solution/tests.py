from math import sqrt, sin
from solution import sin_power_series

# Pi объявлен как отдельная переменная, т.к. тогда мы не зависим
# от стандартной библиотеки Python (а вдруг там бага!!!)
# Это значение было получено от надежных источников
# и его точности достаточно для решения поставленной бизнес-задачи
pi = 3.14159265359
eps = 1e-6

def test_eps(a, b):
    return abs(a - b) <= eps

def test_sin_value(x, expected):
    assert test_eps(sin_power_series(x), expected)

def test_sin_values(x, expected):
    return list(map(lambda x: test_sin_value(x[0], x[1]), zip(x, expected)))

def test_trigonometry_values():
    values = [0, pi/6, pi/4, pi/3, pi/2]
    expected = [ 0, 1/2, sqrt(2)/2, sqrt(3)/2, 1]
    test_sin_values(values, expected)
    print("test_trigonometry_values passed!")

def test_symmetry():
    start_x = -10
    dx = 0.01

    current_x = start_x
    while current_x < 0:
        first_value = sin_power_series(current_x)
        second_value = sin_power_series(- current_x)

        test_eps(first_value, second_value)

        current_x += dx
    print("test_symmetry passed!")

def test_period():
    x = -10
    dx = 0.01

    while x < 10:
        first = sin_power_series(x)
        second = sin_power_series(x + 2 * pi)

        test_eps(first, second)

        x += dx

    print("test_period passed!")

def test_simple_pbt():
    x = -10
    dx = 0.01

    while x < 10:
        actual = sin_power_series(x)
        expected = sin(x)

        test_eps(actual, expected)

        x += dx

    print("test_simple_pbt passed!")



if __name__ == "__main__":
    test_trigonometry_values()
    test_symmetry()
    test_period()
    test_simple_pbt()
