import math as m

# Returns the sum of floor(i * sqrt(2)) where i is in [1, n]
def solution(s):
    n = int(s)
    if (n == 1):
        return 1
    elif (n == 0):
        return 0
    else:
        a = g(n)
        b = f(a)
        partA = int(a * (a + 1) // 2)
        partB = b * (b + 1)
        partC = int(solution(b))
        return str(partA - partB - partC)

# returns the integer part of a/s where s is such that 1/s + 1/sqrt(2) = 1
def f(a):
    result = a - isqrt(100 * (a ** 2) // 2) // 10
    if (isqrt(100 * (a ** 2) // 2) % 10):
        result -= 1
    return result

# returns floor(n * sqrt(2))
def g(n):
    return isqrt(2 * n ** 2)

# find the integer part of a square root using Newton's method
def isqrt(n):
    step = n // 2
    while (True):
        nextStep = takeStep(step, n)
        if (step <= nextStep):
            return step
        else:
            step = nextStep

# returns x - (x^2 - n)/(2x)
def takeStep(x, n):
    numerator = x ** 2 + n
    denom = 2 * x
    return numerator // denom