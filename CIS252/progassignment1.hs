--   Name: Liam Kernan
--   Email: lakernan@syr.edu
--   I received no assistance on this assignment.

-- Representing bits and bytes
type Bit = Char
type Byte = [Bit]

-- Problem 1: Define test variables
seq0010 :: Byte
seq0010 = ['0', '0', '1', '0']

seq0110 :: Byte
seq0110 = ['0', '1', '1', '0']

seq1011 :: Byte
seq1011 = ['1', '0', '1', '1']

seq1111 :: Byte
seq1111 = ['1', '1', '1', '1']

deg0100 :: Byte
deg0100 = ['1', 'X', '0', '0']

-- seq0010 should be "0010"
-- seq0110 should be "0110"
-- seq1011 should be "1011"
-- seq1111 should be "1111"
-- deg0100 should contain a degenerate bit (non-'0' or '1' character)

-- Problem 2: Returns the opposite bit
flipBit :: Bit -> Bit
flipBit '1' = '0'
flipBit '0' = '1'

-- flipBit '0' should evaluate to '1'
-- flipBit '1' should evaluate to '0'
-- flipBit 'X' should evaluate to '1' (degenerate bit treated as 0)
-- Rationale: I tested '0' and '1' and also a character to make sure it works

-- Problem 3: Returns the pure form of the bit
cleanseBit :: Bit -> Bit
cleanseBit '1' = '1'
cleanseBit b = '0'

-- cleanseBit '0' should evaluate to '0'
-- cleanseBit '1' should evaluate to '1'
-- cleanseBit 'N' should evaluate to '0'
-- cleanseBit '#' should evaluate to '0'
-- Rationale: I tested normal bits and some characters to see if they get turned into '0'

-- Problem 4: Calculates the value of a bit at a given exponent position
bitVal :: Bit -> Integer -> Integer
bitVal b exp
  | cleanseBit b == '1' = 2^exp
  | otherwise = 0

-- bitVal '0' 2 should evaluate to 0
-- bitVal '0' 3 should evaluate to 0
-- bitVal '1' 2 should evaluate to 4
-- bitVal '1' 3 should evaluate to 8
-- bitVal '1' 0 should evaluate to 1
-- bitVal 'X' 2 should evaluate to 0
-- Rationale: I tested with different exponents and made sure it works with '0' and '1' bits

-- Problem 5: Calculates the decimal value using unsigned-integer interpretation
unsignedVal :: Byte -> Integer
unsignedVal [b3, b2, b1, b0] = bitVal b3 3 + bitVal b2 2 + bitVal b1 1 + bitVal b0 0

-- unsignedVal seq1011 should evaluate to 11 (1*8 + 0*4 + 1*2 + 1*1)
-- unsignedVal seq0010 should evaluate to 2 (0*8 + 0*4 + 1*2 + 0*1)
-- unsignedVal seq0110 should evaluate to 6 (0*8 + 1*4 + 1*2 + 0*1)
-- unsignedVal seq1111 should evaluate to 15 (1*8 + 1*4 + 1*2 + 1*1)
-- unsignedVal ['0','0','0','0'] should evaluate to 0
-- Rationale: I tested different bit patterns including the smallest (0) and largest (15) values

-- Problem 6: Increments the unsigned 4-bit representation by one
increment :: Byte -> Byte
increment [b3, b2, b1, b0]
  | cleanseBit b0 == '0' = [b3, b2, b1, '1']
  | cleanseBit b1 == '0' = [b3, b2, '1', '0']
  | cleanseBit b2 == '0' = [b3, '1', '0', '0']
  | cleanseBit b3 == '0' = ['1', '0', '0', '0']
  | otherwise = ['0', '0', '0', '0']  -- overflow case

-- increment seq1011 should evaluate to "1100" (11 + 1 = 12)
-- increment seq0010 should evaluate to "0011" (2 + 1 = 3)
-- increment seq1111 should evaluate to "0000" (overflow)
-- increment ['0','0','0','1'] should evaluate to "0010"
-- increment ['0','1','1','1'] should evaluate to "1000"
-- Rationale: I tested simple cases and also when you need to carry, plus the overflow when 1111 goes to 0000

-- Problem 7: Inverts all bits in the 4-bit sequence
invertBits :: Byte -> Byte
invertBits [b3, b2, b1, b0] = [flipBit (cleanseBit b3), flipBit (cleanseBit b2),
                                 flipBit (cleanseBit b1), flipBit (cleanseBit b0)]

-- invertBits seq1011 should evaluate to "0100"
-- invertBits seq0010 should evaluate to "1101"
-- invertBits seq0110 should evaluate to "1001"
-- invertBits seq1111 should evaluate to "0000"
-- invertBits ['0','0','0','0'] should evaluate to "1111"
-- Rationale: I tested different patterns to make sure all the bits flip correctly

-- Problem 8: Calculates the decimal value using signed-integer interpretation
signedVal :: Byte -> Integer
signedVal [b3, b2, b1, b0]
  | cleanseBit b3 == '0' = unsignedVal [b3, b2, b1, b0]
  | otherwise = -(unsignedVal (increment (invertBits [b3, b2, b1, b0])))

-- signedVal seq1011 should evaluate to -5 (negative number)
-- signedVal seq0010 should evaluate to 2 (positive number)
-- signedVal seq0110 should evaluate to 6 (positive number)
-- signedVal seq1111 should evaluate to -1 (all 1s = -1 in two's complement)
-- signedVal ['1','0','0','0'] should evaluate to -8 (most negative 4-bit value)
-- signedVal ['0','1','1','1'] should evaluate to 7 (most positive 4-bit value)
-- Rationale: I tested positive and negative numbers including the smallest (-8) and largest (7) possible values