ticketPrice :: Int -> Bool -> Bool -> Float
ticketPrice age isStudent isTuesday
    | age <= 0 = (0.0)
    | age <= 12 = if isTuesday then 9.0-2.0 else 9.0
    | age >= 65 = 15 * 0.7
    | (isStudent == True) = 15 * 0.8

finalGrade :: Float -> Float -> Float -> Float -> Float -> Float
finalGrade exam1 exam2 exam3 project participation
    | totalexam >= 90 = 4.0
    | total >= 90 = 4.0
    | total >= 80 = 3.0
    | total >= 70 = 2.0
    | total >= 60 = 1.0
    | otherwise = 0.0
    where
    totalexam :: Float
    totalexam = (exam1 + exam2 + exam3) / 3

    total :: Float
    total = (totalexam * 0.4) + (project * 0.3) + (participation * 0.3)