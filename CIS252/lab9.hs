----------------------------------------------------------------------
--   CIS 252: Lab 9 (Spring 2022)
--
--       Name:  Liam Kernan
--       Email: lakernan@syr.edu
----------------------------------------------------------------------
import Data.Char

----------------------------------------------------------------------
-- Your problems:  (please read the requirements carefully!)
--
-- For each problem, uncomment the line(s) and replace the
--   underscores/blanks by your answers.
--
-- Some problems will ask you to use a built-in function: do not turn 
-- it into an anonymous function.  For example:
--
--            use:  isLower
--      don't use:  (\ c -> isLower c)
--
-- Other problems will require an anonymous function.


----------------------------------------------------------------------

--
-- Problem #1
--
-- Replace the blank with a **built-in Haskell function**

lengths :: [String] -> [Int]
lengths strs = map length strs

----------------------------------------------------------------------

--
-- Problem #2
--
-- Replace the blank with an **anonymous function**

listifyItems :: [a] -> [[a]]
listifyItems ws = map (\x -> [x]) ws


----------------------------------------------------------------------
--
-- Problem #3
--
-- Replace the blank with a **built-in Haskell function**

alphanums :: [Char] -> [Char]
alphanums cs = filter isAlphaNum cs


----------------------------------------------------------------------
--
-- Problem #4
-- Replace the blank with an **anonymous function**

subzeroes :: [(Char,Integer)] -> [(Char,Integer)]
subzeroes ps = filter (\(c, n) -> n < 0) ps


----------------------------------------------------------------------
--
-- Problem #5
--
-- Replace the blanks of mystery2 with **anonymous functions**,
--   so that mystery2 has exactly the same behavior as 
--   mystery
--

mystery :: Integer -> [Integer] -> [[Integer]] 
mystery k ms = [ [k,r] | r <- ms, odd (k+r)]

mystery2 :: Integer -> [Integer] ->  [[Integer]]
mystery2 k ms = map (\r -> [k, r]) (filter (\r -> odd (k + r)) ms)


