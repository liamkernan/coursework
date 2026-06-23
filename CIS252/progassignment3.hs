-- Name: Liam Kernan
-- Email: lakernan@syr.edu
-- Assistance: No assistance received on this assignment.

import Robots


-- Problem 1: Move the robot by the given displacement
-- move (dx,dy) rbt moves the robot dx units east and dy units north
move :: (Integer,Integer) -> Robot -> Robot
move (dx, dy) (Drone (x,y) item log) = Drone (x+dx, y+dy) item log
move (dx, dy) (Rover (x,y) items log) = Rover (x+dx, y+dy) items log

-- Test cases for move:
test_move1 = move (10,20) sampleDrone
  -- Expected: Drone (15,24) (Just Game) ["Pickup Game","wake"]
  -- Tests positive displacement with a Drone
test_move2 = move (-3,-7) sampleRover1
  -- Expected: Rover (-1,3) [Book,Device,Toy,Book] [("Pickup Book",(0,5))]
  -- Tests negative displacement with a Rover
test_move3 = move (0,0) sampleDrone
  -- Expected: Drone (5,4) (Just Game) ["Pickup Game","wake"]
  -- Tests zero displacement (robot shouldn't move)



-- Problem 2: Reset the robot's log and items
-- reset rbt erases the robot's log and removes all items
reset :: Robot -> Robot
reset (Drone loc _ _) = Drone loc Nothing []
reset (Rover loc _ _) = Rover loc [] []

-- Test cases for reset:
test_reset1 = reset sampleDrone
  -- Expected: Drone (5,4) Nothing []
  -- Tests resetting a Drone with an item
test_reset2 = reset sampleRover2
  -- Expected: Rover (-1,-4) [] []
  -- Tests resetting a Rover with items and a multi-entry log
test_reset3 = reset (Drone (0,0) Nothing [])
  -- Expected: Drone (0,0) Nothing []
  -- Tests resetting an already empty Drone


-- Problem 3: Add an entry to the robot's log
-- addEntry str rbt adds string str to the front of rbt's log
addEntry :: String -> Robot -> Robot
addEntry str (Drone loc item log) = Drone loc item (str:log)
addEntry str (Rover loc items log) = Rover loc items ((str,loc):log)

-- Test cases for addEntry:
test_addEntry1 = addEntry "sleep" sampleDrone
  -- Expected: Drone (5,4) (Just Game) ["sleep","Pickup Game","wake"]
  -- Tests adding to a Drone's log
test_addEntry2 = addEntry "sleep" sampleRover1
  -- Expected: Rover (2,10) [Book,Device,Toy,Book] [("sleep",(2,10)),("Pickup Book",(0,5))]
  -- Tests adding to a Rover's log (includes location)
test_addEntry3 = addEntry "test" (Drone (7,8) Nothing [])
  -- Expected: Drone (7,8) Nothing ["test"]
  -- Tests adding to an empty Drone log



-- Problem 4: Remove all occurrences of a string from the log
-- redact str rbt removes all log entries with exactly the string str
redact :: String -> Robot -> Robot
redact str (Drone loc item log) = Drone loc item (filter (/= str) log)
redact str (Rover loc items log) = Rover loc items (filter (\(s,_) -> s /= str) log)

-- Test cases for redact:
test_redact1 = redact "Pickup Game" sampleDrone
  -- Expected: Drone (5,4) (Just Game) ["wake"]
  -- Tests removing an existing entry from a Drone
test_redact2 = redact "Game" sampleDrone
  -- Expected: Drone (5,4) (Just Game) ["Pickup Game","wake"]
  -- Tests that partial matches are NOT removed
test_redact3 = redact "Pickup Book" sampleRover2
  -- Expected: Rover (-1,-4) [Book] [("DropAll Book",(3,2))]
  -- Tests removing multiple occurrences from a Rover (2 entries removed)
test_redact4 = redact "nonexistent" sampleDrone
  -- Expected: Drone (5,4) (Just Game) ["Pickup Game","wake"]
  -- Tests redacting a string that doesn't exist


-- Problem 5: Pickup an item
-- pickup thing rbt has the robot pick up thing and log the action
pickup :: Item -> Robot -> Robot
pickup thing (Drone loc _ log) = Drone loc (Just thing) (("Pickup " ++ show thing):log)
pickup thing (Rover loc items log) = Rover loc (thing:items) (("Pickup " ++ show thing, loc):log)

-- Test cases for pickup:
test_pickup1 = pickup Toy sampleDrone
  -- Expected: Drone (5,4) (Just Toy) ["Pickup Toy","Pickup Game","wake"]
  -- Tests Drone replacing its current item
test_pickup2 = pickup Toy sampleRover1
  -- Expected: Rover (2,10) [Toy,Book,Device,Toy,Book] [("Pickup Toy",(2,10)),("Pickup Book",(0,5))]
  -- Tests Rover adding to its item collection
test_pickup3 = pickup Book (Drone (1,1) Nothing [])
  -- Expected: Drone (1,1) (Just Book) ["Pickup Book"]
  -- Tests Drone picking up when it has no item


-- Problem 6: Drop all copies of an item
-- dropAll thing rbt drops all copies of thing and logs the action
dropAll :: Item -> Robot -> Robot
dropAll thing (Drone loc (Just item) log)
    | item == thing = Drone loc Nothing (("DropAll " ++ show thing):log)
    | otherwise = Drone loc (Just item) (("DropAll " ++ show thing):log)
dropAll thing (Drone loc Nothing log) = Drone loc Nothing (("DropAll " ++ show thing):log)
dropAll thing (Rover loc items log) = Rover loc (filter (/= thing) items) (("DropAll " ++ show thing, loc):log)

-- Test cases for dropAll:
test_dropAll1 = dropAll Game sampleDrone
  -- Expected: Drone (5,4) Nothing ["DropAll Game","Pickup Game","wake"]
  -- Tests Drone dropping the item it currently holds
test_dropAll2 = dropAll Toy sampleDrone
  -- Expected: Drone (5,4) (Just Game) ["DropAll Toy","Pickup Game","wake"]
  -- Tests Drone dropping an item it doesn't hold (log entry added, item unchanged)
test_dropAll3 = dropAll Book sampleRover1
  -- Expected: Rover (2,10) [Device,Toy] [("DropAll Book",(2,10)),("Pickup Book",(0,5))]
  -- Tests Rover dropping multiple copies (2 Books removed)
test_dropAll4 = dropAll Game sampleRover1
  -- Expected: Rover (2,10) [Book,Device,Toy,Book] [("DropAll Game",(2,10)),("Pickup Book",(0,5))]
  -- Tests Rover dropping an item it doesn't have (log entry added, items unchanged)
test_dropAll5 = dropAll Book (Drone (0,0) Nothing [])
  -- Expected: Drone (0,0) Nothing ["DropAll Book"]
  -- Tests Drone with no item dropping something


-- Problem 7: Perform a single instruction
-- perform i rbt returns the state after robot rbt performs instruction i
perform :: Instr -> Robot -> Robot
perform (Move displacement) robot = move displacement robot
perform Reset robot = reset robot
perform (Log str) robot = addEntry str robot
perform (Redact str) robot = redact str robot
perform (Pickup item) robot = pickup item robot
perform (DropAll item) robot = dropAll item robot

-- Test cases for perform:
test_perform1 = perform (Move (10,20)) sampleDrone
  -- Expected: Drone (15,24) (Just Game) ["Pickup Game","wake"]
  -- Tests Move instruction
test_perform2 = perform (Redact "Pickup Book") sampleRover2
  -- Expected: Rover (-1,-4) [Book] [("DropAll Book",(3,2))]
  -- Tests Redact instruction
test_perform3 = perform (DropAll Book) sampleRover1
  -- Expected: Rover (2,10) [Device,Toy] [("DropAll Book",(2,10)),("Pickup Book",(0,5))]
  -- Tests DropAll instruction
test_perform4 = perform Reset sampleDrone
  -- Expected: Drone (5,4) Nothing []
  -- Tests Reset instruction
test_perform5 = perform (Log "test message") sampleDrone
  -- Expected: Drone (5,4) (Just Game) ["test message","Pickup Game","wake"]
  -- Tests Log instruction
test_perform6 = perform (Pickup Device) sampleDrone
  -- Expected: Drone (5,4) (Just Device) ["Pickup Device","Pickup Game","wake"]
  -- Tests Pickup instruction


-- Problem 8: Run a program (sequence of instructions)
-- runProgram instrs rbt returns the state after rbt executes all instructions in order
runProgram :: [Instr] -> Robot -> Robot
runProgram [] robot = robot
runProgram (instr:instrs) robot = runProgram instrs (perform instr robot)

-- Test cases for runProgram:
test_runProgram1 = runProgram sampleProgram sampleDrone
  -- Expected: Drone (15,24) (Just Toy) ["DropAll Book","Pickup Toy","moved","Pickup Game","wake"]
  -- Tests the provided sample program with a Drone
test_runProgram2 = runProgram sampleProgram sampleRover2
  -- Expected: Rover (9,16) [Toy] [("DropAll Book",(9,16)),("Pickup Toy",(9,16)),("moved",(9,16)),("Pickup Book",(2,1)),("DropAll Book",(3,2)),("Pickup Book",(0,5))]
  -- Tests the sample program with a Rover
test_runProgram3 = runProgram [] sampleDrone
  -- Expected: Drone (5,4) (Just Game) ["Pickup Game","wake"]
  -- Tests empty program (robot should be unchanged)
test_runProgram4 = runProgram [Reset] sampleDrone
  -- Expected: Drone (5,4) Nothing []
  -- Tests single instruction program
test_runProgram5 = runProgram [Pickup Book, Pickup Device, Move (5,5)] (Drone (0,0) Nothing [])
  -- Expected: Drone (5,5) (Just Device) ["Pickup Device","Pickup Book"]
  -- Tests multiple pickups (Drone should only hold last item)


-- reasons for Test Cases:
-- 1. Each function is tested with both Drone and Rover types where needed
-- 2. empty logs, no items, zero displacement, etc.
-- 3. Boundary behaviors are verified
-- 4. the distinction between exact string matches and substrings is tested
-- 5. Operations that always add log entries are tested even when they don't change items
-- 6. multi-step programs test between different instructions
-- 7. verify the visible effects (location, items) and the log data