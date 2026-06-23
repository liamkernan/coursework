-- Liam Kernan (lakernan@syr.edu)
--
-- CIS 252 (Fall 2021): lab2.hs
--
--   a very simple function just to check the software installation

sample :: Integer -> (Integer, Bool)
sample w = (w*10 + 6, odd w)