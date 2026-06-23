-- convert Celsius temp to Fahrenheit

celsiusToFahr :: Float -> Float

celsiusToFahr c = c*9/5 + 32



-- is Celsius temperature greater than 80F?

isTooHot :: Float -> Bool

isTooHot t = celsiusToFahr t > 80

--Thrice returns number triple of the input

thrice :: Float -> Float

thrice z = z * 3

--isPositive returns if the input is positive or negative

isPositive :: Float -> Bool

isPositive g = (g > 0)

--mystery

mystery :: Integer -> Integer -> Integer

mystery x y = x + y

--mod (can use math commands directly in ghci)

modder :: Integer -> Integer -> Integer

modder m t = mod m t

--tuition (conditional guards)

tuition :: Integer -> Integer
tuition cr
    | cr <= 0 = 0
    | cr <= 11 = cr * 2434
    | cr >= 20 = cr * 1679
    | otherwise = 27960

--tuition2 (condition guards w support for undergrad/graduate)

tuition2 :: Integer -> Bool -> Integer
tuition2 cr ugrad
    | cr <= 0 = 0
    | ugrad == False = cr * 1734
    | cr <= 11 = cr * 2434
    | cr >= 20 = cr * 1679
    | otherwise = 27960