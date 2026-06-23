------------------------------------------------------------
--  CIS 252: Lab 6  (Spring 2022)
--     Name: Liam Kernan
--     Email: lakernan@syr.edu
------------------------------------------------------------
import Lab6Defs

replaceThisTextWithYourAnswer = error "Question not yet answered"

--------------------------------------------------------
-- Part One: Fill in blanks to produce required results
--------------------------------------------------------

--   An example problem
--         (see the lab writeup for a solution)
--
--  Fill in the blank(s) so that partOneExample evaluates to 2.0:
--      partOneExample = alpha ____ ____
--

partOneExample = alpha blank1 blank2
  where
   blank1 = 'R'
   blank2 = 31

--
--  Fill in the blank(s) so that one evaluates to 25.0:
--      one = alpha ____ ____
--
one = alpha blank1 blank2
  where
   blank1 = 'Q'
   blank2 = 19

--
--  Fill in the blank(s) so that two evaluates to 16:
--      two = beta ____
--

two = beta blank
  where
   blank = (1, 11, 2)

--
--  Fill in the blank(s) so that three evaluates to 50:
--      three = gamma ____ ____
--

three = gamma blank1 blank2
  where
   blank1 = 20
   blank2 = (30, 'r')

--
--  Fill in the blank(s) so that four evaluates to 2.2:
--      four = delta ____
--

four = delta blank
  where
   blank = [True, True, False, True]

--
--  Fill in the blank(s) so that five evaluates to 3.3:
--      five = delta ____
--

five = delta blank
  where
   blank = [True, True]

--
--  Fill in the blank(s) so that six evaluates to "!!!!!":
--      six = zeta ____
--

six = zeta blank
  where
   blank = ""

--
--  Fill in the blank(s) so that seven evaluates to "252":
--      seven = zeta ____
--

seven = zeta blank
  where
   blank = "1524"

--
--  Fill in the blank(s) so that eight evaluates to [37]:
--      eight = eta ____
--

eight = eta blank
  where
   blank = [('h', 4), ('t', 11), ('z', 8)]

--
--  Fill in the blank(s) so that nine evaluates to "T":
--      nine = theta ____
--

nine = theta blank
  where
   blank = [('1', 'T', '3')]


--
--  Fill in the blank(s) so that ten evaluates to [1,2,3]:
--      ten = theta ____
--

ten = theta blank
  where
   blank = [(1, 3, 2), (7, 8, 8), (4, 5, 1), (4, 1, 1)]


--------------------------------------------------------
--
-- Part Two:
--    For each of the function definitions below,
--       uncomment the function, and replace PATTERN with
--       a Haskell pattern that will result in the
--       function having the stated behavior.
--
--------------------------------------------------------


--  Replace PATTERN so that:
--    upsilon (5,[2,9,7]) evaluates to [2,5]
--    upsilon (-8,[10,3]) evaluates to [10,-8]
--
--    partTwoExample evaluates to ([2,5],[10,-8])

upsilon :: (Integer,[Integer]) -> [Integer]
upsilon (e, (q: _)) = [q,e]

partTwoExample = (upsilon (5,[2,9,7]),  upsilon (-8,[10,3]))


------------

--  Replace PATTERN so that:
--    iota ('e',3,9) evaluates to 13
--    iota ('r',8,False) evaluates to 18
--
--    eleven evaluates to (13,18)

iota :: (a,Integer,b) -> Integer
iota (_, k, _) = k+10

eleven = (iota ('e',3,9),  iota ('r',8,False))

------------

--  Replace PATTERN so that:
--    lambda "orange" evaluates to "ange"
--    lambda [9,2,3,7,1] evaluates to [3,7,1]
--
--    twelve evaluates to ("ange", [3,7,1])

lambda :: [a] -> [a]
lambda (_:_:g) = g
lambda _ = []

twelve = (lambda "orange",  lambda [9,2,3,7,1])

------------

--  Replace PATTERN so that:
--    omicron [6,4,2,3,5,9,1] evaluates to [3,2]
--    omicron "harvest" evaluates to "vr"
--
--    thirteen evaluates to ([3,2], "vr")

omicron :: [a] -> [a]
omicron (_:_:e:d:_) = [d,e]
omicron _ = []

thirteen = (omicron [6,4,2,3,5,9,1],  omicron "harvest")

------------

--  Replace PATTERN so that:
--    rho [(10,20),(30,40)] evaluates to 30
--    rho [(5,6),(7,8),(9,0)] evaluates to 7
--
--    fourteen evaluates to (30, 7)

rho :: [(Integer,a)] -> Integer
rho [_,(k,_)] = k
rho _ = 0

fourteen = (rho [(10,20),(30,40)],  rho [(5,6),(7,8),(9,0)])

------------

--  Replace PATTERN so that:
--    sigma [10,5,2,8,93] evaluates to (10,[10,5,2,8,93])
--    sigma "hello" evaluates to ('h',"hello")
--
--    fifteen evaluates to ((10,[10,5,2,8,93]), ('h',"hello"))

sigma :: [a] -> (a,[a])
sigma stuff@(item:_) = (item,stuff)

fifteen = (sigma [10,5,2,8,93], sigma "hello")