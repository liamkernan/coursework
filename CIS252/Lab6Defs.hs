------------------------------------------------------------
--  Lab5Defs.hs
--       Definitions for Lab 5  (CIS 252: Fall 2021)
------------------------------------------------------------
module Lab6Defs where

alpha :: Char -> Integer -> Float
alpha ch 31 = 2.0
alpha 'Q' x = 6.0 + fromInteger x
alpha d e  = 1.0

beta :: (Integer,Integer,Integer) -> Integer
beta (m,n,p) = 5 + n

gamma :: Integer -> (Integer,Char) -> Integer
gamma x (_,'b') = 17
gamma y (z,_) = y + z

delta :: [Bool] -> Float
delta [] = 1.1
delta (w:r:s:ws) = 2.2
delta _ = 3.3

zeta :: String -> String
zeta (_:a:b:cs) = [b,a,b]
zeta (c:_) = [c,c]
zeta _ = "!!!!!"


eta :: [(Char,Integer)] -> [Integer]
eta [] = [16]
eta ((ch,3):_:(_,w):rest) = [w+10]
eta _ = [1,2,3]

theta :: [(a,b,b)] -> [b]
theta ((k,d,e):_:(g,w,r):rest) = [r,e,d]
theta [(x,y,z)] = [y]
theta _ = []
