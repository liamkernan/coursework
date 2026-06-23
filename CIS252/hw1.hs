--Liam Kernan (lakernan@syr.edu)

commonDivisor :: Integer -> Integer -> Integer -> Bool
commonDivisor h e y = (mod e h == 0) && (mod y h == 0)

--commonDivisor 4 8 16 evaluates to True
--commonDivisor 2 10 100 evaluates to True
--commonDivisor 3 9 11 evaluates to False

nand :: Bool -> Bool -> Bool
nand t x = (t == True && x == False) || (t == False && x == True) || (t == False && x == False)

--nand True False evaluates to True
--nand False False evaluates to True
--nand True True evaluates to False

distance :: Float -> Float -> Float -> Float -> Float
distance a b c d = sqrt(((a - c) ^2) + ((b - d) ^ 2))

--distance 10 4 8 1 evaluates to 3.6055512
--distance 2 5 7 9 evaluates to 6.4031243
--distance (-4) 8 3 15 evaluates to 9.899495