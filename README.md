# College Coding Coursework

This repository collects programming coursework from my college classes. It includes smaller lab exercises, practice files, data structure implementations, and a larger final project.

## Repository Structure

### `CIS252/`

Coursework for CIS252, mostly written in Haskell. These files focus on functional programming fundamentals and problem solving with recursion, pattern matching, custom data types, and list processing.

Some of the work in this folder includes:

- Basic Haskell labs covering functions, recursion, lists, and data transformations.
- Bit and byte exercises, including unsigned and signed 4-bit binary interpretation.
- Error detection and decoding exercises using repeated bits and majority voting.
- Robot modeling exercises using custom types for drones, rovers, items, movement, logs, pickup/drop behavior, and reset/redaction operations.
- Programming assignments and lab answer files used for practice and submission.

Representative files:

- `progassignment1.hs` - bit/byte representation, bit flipping, cleansing, incrementing, and signed/unsigned values.
- `progassignment2.hs` - bit-string validation, triplication, majority-vote decoding, and error detection.
- `progassignment3.hs` - robot operations built on the `Robots` module.
- `lab1.hs` through `lab9.hs` - weekly lab exercises.

### `CIS351/`

Coursework for CIS351, mostly written in Java. This folder focuses on data structures, object-oriented programming, and practice implementations.

Some of the work in this folder includes:

- ArrayList examples and playlist practice.
- Stack implementations and stack applications.
- Singly linked lists and linked stacks.
- Binary tree and binary search tree practice.
- Smaller data structure practice files in `datapractice1/`.

Representative files:

- `MyStack.java` - a simple integer stack backed by an array.
- `SinglyLinkedList.java` - linked list implementation practice.
- `SinglyLinkedStack.java` - stack behavior implemented with linked nodes.
- `IntroBinaryTree.java` - binary tree introduction/practice.
- `StudentBST.java` - binary search tree implementation with insert, remove, and traversal methods.
- `PlaylistDriver.java` and `Song.java` - playlist/object practice using Java classes.

### `MusicLibrary&Engine(cis351final)/`

Final project for CIS351: a Java music library and playlist engine called SoundWave.

This project is a larger backend/CLI application that supports:

- Adding, removing, and looking up songs.
- Creating, deleting, and editing playlists.
- Queue behavior for "up next" songs.
- Undo support for playlist edits.
- CSV import for song data.
- Searching songs by title and listing songs by artist.
- Recommendation output.
- A test harness in `Main.java`.

The project intentionally avoids Java's built-in collection classes for core data structures. Instead, it implements custom structures such as:

- `DynamicArray`
- `SimpleHashMap`
- `SimpleStack`
- `SongQueue`

Useful project docs:

- `MusicLibrary&Engine(cis351final)/README.md` - compile/run instructions and CLI command reference.
- `MusicLibrary&Engine(cis351final)/DESIGN.md` - design notes.
- `MusicLibrary&Engine(cis351final)/FINAL_REPORT.md` - final project report, complexity discussion, testing notes, and future improvements.

## Languages Used

- Haskell
- Java

## Running Code

Most files are individual coursework exercises and can be run or loaded directly with the appropriate language tools.

For the CIS351 final project:

```bash
cd "MusicLibrary&Engine(cis351final)"
javac -d out src/*.java
java -cp out Main
```

To run the interactive CLI:

```bash
java -cp out Main cli
```
