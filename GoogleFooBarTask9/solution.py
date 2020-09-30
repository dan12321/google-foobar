# At first I did this in java using big integer methods from a previous task
# but after a long time debugging decided to just translate my method to python

# Returns the number of distinct matrices with width w, height h and s states (e.g. s = 2 would be a binary matrix)
# where 2 matrices are equivalent if you can get from one to another by swapping rows and columns
def solution(w, h, s):
    rowPartitions = partitions(h)
    colPartitions = partitions(w)

    totalNumerator = 0
    for rowCycleLengths in rowPartitions:
        for colCycleLengths in colPartitions:
            power = degreesOfFreedom(rowCycleLengths, colCycleLengths)
            coef = cycleLengthsToCoef(rowCycleLengths, h) * cycleLengthsToCoef(colCycleLengths, w)

            fix = coef * (s ** power)
            totalNumerator += fix

    groupCard = factorial(w) * factorial(h)
    return str(int(totalNumerator // groupCard))

# Returns the ways n can be partitioned
def partitions(n):
    result = []
    partition = [0] * n
    partition[0] = n
    lastPos = 0

    while True:
        result.append(partition[0:lastPos+1])

        remVal = 0
        while (lastPos >= 0 and partition[lastPos] == 1):
            remVal += 1
            lastPos -= 1

        if (lastPos < 0):
            break

        partition[lastPos] -= 1
        remVal += 1

        while (remVal > partition[lastPos]):
            partition[lastPos + 1] = partition[lastPos]
            remVal -= partition[lastPos]
            lastPos += 1

        partition[lastPos + 1] = remVal
        lastPos += 1

    return result

# Returns how many independent elements there are for the matrix to stay the same
# after row and column operations of length rowCycleLengths and colCycleLengths
# are applied
def degreesOfFreedom(rowCycleLengths, colCycleLengths):
    result = 0
    for rowCycleLength in rowCycleLengths:
        for colCycleLength in colCycleLengths:
            result += gcd(rowCycleLength, colCycleLength)

    return result

# Returns the greatest common denominator of a and b
def gcd(a, b):
    if (a == 0):
        return b

    return gcd(b % a, a)

# Returns the number of permutations with the given cycleLengths
# where n is the number of elements to permute
def cycleLengthsToCoef(cycleLengths, n):
    result = factorial(n)
    previous = 0
    repeats = []
    repeatsPos = -1
    for cycleLength in cycleLengths:
        if (cycleLength == previous):
            repeats[repeatsPos] += 1
        else:
            repeats.append(1)
            repeatsPos += 1

        previous = cycleLength
        result = result // cycleLength

    for repeatCount in repeats:
        result = result // factorial(repeatCount)

    return result

# Returns n!
def factorial(n):
    result = 1
    for i in range(2, n + 1):
        result *= i

    return result
