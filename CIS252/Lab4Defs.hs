------------------------------------------------------------
--  Lab4Defs.hs
--       Definitions for Lab 4  (CIS 252: Fall 2021)
------------------------------------------------------------

module Lab4Defs where

import Data.Char   -- needed for various Char-related functions


challenge :: Integer -> Bool 
challenge w = (4*w /= 20)

conundrum :: Integer -> Char -> Bool
conundrum p t =   isDigit t && odd p

dilemma :: Bool -> Integer -> Integer
dilemma b k 
    | not b     = k+30
    | k == 0    = -10
    | otherwise = (k-1)^2

enigma :: Char -> Bool
enigma ch =  toLower ch == toUpper ch

knot :: Integer -> Float -> [Float]
knot s e 
    | s < 25     = [4.3]
    | otherwise  = e : e : knot (s-1) e

pickle :: Integer -> Bool
pickle y = y < 60 || even y

ponder :: Integer -> Integer -> Integer
ponder x y = (div x y) + (mod x y)

puzzler :: Integer -> Integer -> [Integer]
puzzler j k = [j+k, j, j-k]

twister :: Integer -> Integer -> Integer -> Bool
twister x y z = (x > y) && (y < z) && (x > 100) && (z < 13)


