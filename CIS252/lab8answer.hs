----------------------------------------------------------------------
--   CIS 252: Lab 8 (Spring 2022)
--
--  Name: Liam Kernan
--  Email: lakernan@syr.edu
----------------------------------------------------------------------

data Species = Duck | Flea | Ocelot | Gator | Platypus
        deriving (Eq, Ord, Enum, Show)

mammal :: Species -> Bool
mammal Ocelot = True
mammal Platypus = True
mammal _ = False



----------------------------------------------------------------------
-- Lab Problem #1:  Introduce the definition for new datatype Trait
--      (see lab writeup for requirements)
----------------------------------------------------------------------

data Trait = Brawn | Speed | Wits | GoodLuck
        deriving (Eq, Ord, Show)




----------------------------------------------------------------------
-- Preamble for the last three problems
--
--  Uncomment the following lines *AFTER* you have Problem 1 complete.
----------------------------------------------------------------------

data Animal = Critter String Species Trait
              deriving (Show)

-- some sample Animals (used for examples)
denny, finn, bella :: Animal
denny = Critter "Denise" Duck GoodLuck
finn  = Critter "Finn the Fierce" Flea Brawn
bella = Critter "Isabel" Ocelot Speed


----------------------------------------------------------------------
-- Lab Problem #2:
--   Add your answer for Problem 2 below this comment (include the
--     type signature for newGator!)
----------------------------------------------------------------------

newGator :: Animal
newGator = Critter "Greta" Gator Speed



-- A sample function
compareBeasts :: Animal -> Animal -> (String, Species)
compareBeasts (Critter name1 spec1 tr1) (Critter name2 spec2 tr2)
    | spec1 > spec2 = (name1, spec1)
    | spec2 > spec1 = (name2, spec2)
    | otherwise = ("Same species!", spec1)



----------------------------------------------------------------------
-- Lab Problem #3: define the function boost (see the lab writeup)
----------------------------------------------------------------------
boost :: Trait -> Animal -> Animal
boost new (Critter name spec oldTrait)
    | new > oldTrait = Critter name spec new
    | otherwise = Critter name spec oldTrait


----------------------------------------------------------------------
-- LabProblem #4: define the function combine (see the lab writeup)
----------------------------------------------------------------------
combine :: Animal -> Animal -> Animal
combine (Critter name1 spec1 tr1) (Critter name2 spec2 tr2)
    | spec1 >= spec2 = boost tr2 (Critter (name1 ++ name2) spec1 tr1)
    | otherwise = boost tr1 (Critter (name1 ++ name2) spec2 tr2)