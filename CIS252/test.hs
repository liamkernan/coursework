import Data.Char

lister :: Integer -> Integer -> [Integer]
lister n m = n : m : [6]

isFive :: Integer -> Bool
isFive 5 = True
isFive _ = False

data TrafficLight = Red | Yellow | Green deriving Show

next :: TrafficLight -> TrafficLight
next Red = Green
next Yellow = Red
next Green = Yellow

canGo :: TrafficLight -> Bool
canGo Green = True
canGo _ = False

timeRemaining :: TrafficLight -> Int
timeRemaining Green = 45
timeRemaining Yellow = 5
timeRemaining Red = 30


data Grade = A | B | C | D | F deriving Show
data Student = Student { name :: String
                       , grade :: Grade
                       , credits :: Int } deriving Show

-- Write:
isPassing :: Student -> Bool
isPassing (Student _ F _) = False
isPassing _ = True

gpa :: Grade -> Float
gpa A = 4.0
gpa B = 3.0
gpa C = 2.0
gpa D = 1.0
gpa F = 0.0

neighbors :: [a] -> [(a,a)]
neighbors (g:h:z) = (g,h) : neighbors (h:z)
neighbors t = []

frontOdds :: [Integer] -> [Integer]
frontOdds [] = []
frontOdds (h:t)
    | (odd h) = h : frontOdds(t)
    | otherwise = []

removeCap :: String -> String
removeCap "" = ""
removeCap (c:str)
    | isUpper c == True = str
    | otherwise = c : removeCap str

spooky :: [(Char, Integer, Float)] -> [Bool]
spooky [] = []
spooky ((a,m,e): rest)
          | (m > 30) && (isLower a) = True : spooky rest
          | (isLower a) = False : spooky rest
          | otherwise = spooky rest

cheer2 :: [(Integer, Integer)] -> [Bool]
cheer2 [] = []
cheer2 ((e, b):rest)
       | (b > 5*e) = even (e+b) : cheer2 rest
       | otherwise = cheer2 rest