------------------------------------------------------------
--  CIS 252: Lab 4  (Fall 2021)
--     Name: Liam Kernan
--     Email: lakernan@syr.edu
------------------------------------------------------------

import Data.Char       -- needed for isLower, isAlphaNum, etc.
import Lab4Defs        -- needed for various functions

replaceThisTextWithYourAnswer = error "Question not yet answered"

------------------------------------------------------------
--   An example problem 
--         (see the lab writeup for a solution)
--
--  Fill in the blanks so that zero evaluates to 5:
--      zero = ponder ____ ____
--
-- Problem zero is worked out in the lab writeup.
--
zero :: Integer
zero = ponder blank1 blank2
  where
    blank1 = 11
    blank2 = 3

------------------------------------------------------------
--  Fill in the blank so that one evaluates to 16.0:
--      one = sqrt ____
--
one :: Float
one = sqrt blank
  where
   blank = 256

------------------------------------------------------------
--  Fill in the blank so that two evaluates to True:
--      two = isAlpha ___
--
two :: Bool
two = isAlpha blank
  where
    blank = 'A'

------------------------------------------------------------
--  Fill in the blank so that three evaluates to False:
--      three = pickle ___
--
three :: Bool
three = pickle blank
  where
   blank = 61

------------------------------------------------------------
--  Fill in the blanks so that four evaluates to True:
--      four = twister ___ ___ ___ 
--
four :: Bool
four = twister blank1 blank2 blank3 
  where
   blank1 = 101
   blank2 = 10
   blank3 = 12

------------------------------------------------------------
--  Fill in the blanks so that five evaluates to [5,2,8,6,8]:
--      five = ____ : ____
--
five :: [Integer]
five = blank1 : blank2
  where
   blank1 = 5
   blank2 = [2, 8, 6, 8]

------------------------------------------------------------
--  Fill in the blanks so that six evaluates to True:
--      six = conundrum ____ ____
--
six :: Bool
six = conundrum blank1 blank2
  where
   blank1 = 3
   blank2 = '9'

------------------------------------------------------------
--  Fill in the blank so that seven evaluates to 64:
--      seven = dilemma True ____
--
seven :: Integer
seven = dilemma True blank 
  where
   blank = 9

------------------------------------------------------------
--  Fill in the blank so that eight evaluates to "CIS 252":
--      eight = ____ : "IS 252" 
--
eight :: String
eight = blank : "IS 252" 
  where
   blank = 'C'

------------------------------------------------------------
--  Fill in the blank so that nine evaluates to [25,20,15]:
--      nine = puzzler ____ ____
--
nine :: [Integer]
nine = puzzler blank1 blank2
  where
   blank1 = 20
   blank2 = 5

------------------------------------------------------------
--  Fill in the blank so that ten evaluates to [7.1,7.1,7.1,7.1,4.3]:
--      ten = knot ___ ___
--
ten :: [Float]
ten = knot blank1 blank2
  where
   blank1 = 26
   blank2 = 7.1

------------------------------------------------------------
--  Fill in the blank so that eleven evaluates to "Syracuse":
--      eleven = ___ ++ "yracuse"
--
eleven :: String
eleven = blank ++ "yracuse"
  where
   blank = "S"

------------------------------------------------------------
--  Fill in the blank so that twelve evaluates to True:
--      twelve = not (challenge ____)
--
twelve :: Bool
twelve = not (challenge blank)
  where
   blank = 5


