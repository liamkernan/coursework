-- Name: Liam Kernan
-- Email: lakernan@syr.edu
-- I received no assistance on this assignment.

-- Problem 1: allBits
-- Purpose: Determines whether every character in a string is either '0' or '1'
allBits :: String -> Bool
allBits (s:t)
    | (s == '0') || (s == '1') = allBits t
    | otherwise = False
allBits [] = True

-- Test cases for allBits:
-- allBits "0010111101" should be True (all valid bits)
-- allBits "0010b11101" should be False (contains 'b')
-- allBits "hello" should be False (no valid bits)
-- allBits "" should be True (empty string)
-- allBits "1111111111" should be True (all ones)
-- allBits "0000000000" should be True (all zeros)
-- Rationale: Tests base case, valid/invalid characters, and edge cases


-- Problem 2: triplicate
-- Purpose: Produces a string with three copies of each character
triplicate :: String -> String
triplicate (t:l) = t:t:t: triplicate l
triplicate _ = []

-- Test cases for triplicate:
-- triplicate "bits" should be "bbbiiitttsss"
-- triplicate "1101" should be "111111000111"
-- triplicate "" should be ""
-- triplicate "a" should be "aaa"
-- triplicate "01" should be "000111"
-- Rationale: Tests empty string, single character, and various lengths


-- Problem 3: voteResult
-- Purpose: Takes three bit characters and returns the majority vote
-- along with whether the vote was unanimous
voteResult :: Char -> Char -> Char -> (Char, Bool)
voteResult '1' '1' '1' = ('1', True)
voteResult '0' '0' '0' = ('0', True)
voteResult b1 b2 b3 = (majority, False)
    where
        count1 = length (filter (== '1') [b1, b2, b3])
        majority = if count1 >= 2 then '1' else '0'

-- Test cases for voteResult:
-- voteResult '0' '1' '0' should be ('0', False)
-- voteResult '1' '1' '1' should be ('1', True)
-- voteResult '1' '1' '0' should be ('1', False)
-- voteResult '0' '0' '1' should be ('0', False)
-- voteResult '0' '0' '0' should be ('0', True)
-- voteResult '1' '0' '1' should be ('1', False)
-- Rationale: Tests unanimous and majority cases for both 0s and 1s


-- Problem 4: decode
-- Purpose: Decodes a bit string by processing three characters at a time,
-- using majority voting to determine the intended bit and detect corruption
decode :: String -> [(Char, Bool)]
decode [] = []
decode (b1:b2:b3:rest) = voteResult b1 b2 b3 : decode rest
decode (b1:b2:[]) = [voteResult b1 b2 '0']
decode (b1:[]) = [voteResult b1 '0' '0']

-- Test cases for decode:
-- decode "111001000" should be [('1',True),('0',False),('0',True)]
-- decode "00011" should be [('0',True),('1',False)]
-- decode "0001" should be [('0',True),('0',False)]
-- decode "00001" should be [('0',True),('0',False)]
-- decode "" should be []
-- decode "111111111" should be [('1',True),('1',True),('1',True)]
-- decode "1" should be [('0',False)]
-- decode "11" should be [('1',False)]
-- Rationale: Tests complete triplets, incomplete packets, and edge cases


-- Problem 5: errorFree
-- Purpose: Extracts the decoded string and determines if any corruption was detected
errorFree :: String -> (String, Bool)
errorFree str
    | not (allBits str) = ("", False)
    | otherwise = extractData (decode str)
    where
        extractData :: [(Char, Bool)] -> (String, Bool)
        extractData [] = ("", True)
        extractData ((c, t):rest) =
            let (restStr, restClean) = extractData rest
            in (c : restStr, t && restClean)

-- Test cases for errorFree:
-- errorFree "111001000" should be ("100", False)
-- errorFree "111000000111000" should be ("10010", True)
-- errorFree "1110000#0111000" should be ("", False)
-- errorFree "" should be ("", True)
-- errorFree "111111111" should be ("111", True)
-- errorFree "abc" should be ("", False)
-- errorFree "000000000" should be ("000", True)
-- Rationale: Tests valid strings, non-bit characters, and corruption scenarios


-- Problem 6: toDigitsRev
-- Purpose: Converts an integer to a list of its digits in reverse order
toDigitsRev :: Integer -> [Integer]
toDigitsRev n
    | n <= 0 = []
    | otherwise = (n `mod` 10) : toDigitsRev (n `div` 10)

-- Test cases for toDigitsRev:
-- toDigitsRev 15865 should be [5,6,8,5,1]
-- toDigitsRev 4 should be [4]
-- toDigitsRev 567438931627 should be [7,2,6,1,3,9,8,3,4,7,6,5]
-- toDigitsRev 0 should be []
-- toDigitsRev (-5) should be []
-- toDigitsRev 100 should be [0,0,1]
-- Rationale: Tests single digit, multiple digits, zero, negative, and trailing zeros


-- Problem 7: doubleEveryOther
-- Purpose: Doubles every second element in a list (starting from the left)
doubleEveryOther :: [Integer] -> [Integer]
doubleEveryOther xs = doubleHelper xs False
    where
        doubleHelper :: [Integer] -> Bool -> [Integer]
        doubleHelper [] _ = []
        doubleHelper (x:xs) shouldDouble
            | shouldDouble = (x * 2) : doubleHelper xs False
            | otherwise = x : doubleHelper xs True

-- Test cases for doubleEveryOther:
-- doubleEveryOther [6,2,8,7,7,9] should be [6,4,8,14,7,18]
-- doubleEveryOther [6,2,8] should be [6,4,8]
-- doubleEveryOther [7,2,6,1,3,9,8,3,4,7,6,5] should be [7,4,6,2,3,18,8,6,4,14,6,10]
-- doubleEveryOther [] should be []
-- doubleEveryOther [5] should be [5]
-- doubleEveryOther [1,2] should be [1,4]
-- Rationale: Tests various list lengths and alternating pattern from left


-- Problem 8: addDigits
-- Purpose: Sums all digits in a list, treating two-digit numbers as separate digits
addDigits :: [Integer] -> Integer
addDigits [] = 0
addDigits (x:xs)
    | x < 10 = x + addDigits xs
    | otherwise = (x `div` 10) + (x `mod` 10) + addDigits xs

-- Test cases for addDigits:
-- addDigits [3,16,7,10,5] should be 23
-- addDigits [7,4,6,2,3,18,8,6,4,14,6,10] should be 61
-- addDigits [] should be 0
-- addDigits [5] should be 5
-- addDigits [15] should be 6
-- addDigits [99,88,77] should be 60
-- Rationale: Tests empty list, one/two-digit numbers, and digit splitting


-- Problem 9: luhnValidate
-- Purpose: Validates a number using Luhn's algorithm
luhnValidate :: Integer -> Bool
luhnValidate num = (addDigits (doubleEveryOther (toDigitsRev num))) `mod` 10 == 0

-- Test cases for luhnValidate:
-- luhnValidate 567438931627 should be False
-- luhnValidate 567438931626 should be True
-- luhnValidate 0 should be True
-- luhnValidate 18 should be True
-- luhnValidate 59 should be True
-- luhnValidate 123456789 should be False
-- Rationale: Tests given examples, edge case (0), and small valid/invalid numbers
