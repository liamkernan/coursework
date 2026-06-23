rotate3 :: Integer -> Char -> Char -> Char -> String
rotate3 n item1 item2 item3
    | n < 0     = error "n must be non-negative"
    | n == 0    = []
    | n == 1    = [item1]
    | n == 2    = [item1, item2]
    | otherwise = item1 : item2 : item3 : rotate3 (n-3) item1 item2 item3

descend :: Integer -> Char -> Char -> [String]
descend n item1 item2
  | n == 0    = []
  | n > 0     = rotate3 n item1 item1 item1 : descend (n-1) item2 item1
  | otherwise = error "descend: requires nonnegative input"