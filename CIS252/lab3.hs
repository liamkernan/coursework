--Liam Kernan (lakernan@syr.edu)

simple :: Integer -> Integer -> Integer
simple a b = a + 3*b

price :: Integer -> Integer -> Bool -> Integer
price slices drinks coupon
    | slices < 0 || drinks < 0 = 0
    | coupon == True && slices > 4 = slices*175 + drinks*135
    | otherwise             = slices*225 + drinks*150