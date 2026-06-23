# SoundWave: Music Library & Playlist Engine

## Java Version
- Java 17+ (tested with standard `javac`/`java` commands)

## Compile
```bash
javac -d out src/*.java
```

## Run Tests (Main.java requirement)
```bash
java -cp out Main
```

This runs the full path-coverage harness and prints:
- `PASS X / Y tests` when all pass
- or `FAIL X / Y tests` plus per-test failure details (expected vs actual and context)

## Run CLI
```bash
java -cp out Main cli
```

## Clean Build Artifacts
```bash
rm -f src/*.class
```

If you ever ran `javac src/*.java`, this removes generated `.class` files from the source folder.

Supported commands:
- `LOAD filename`
- `ADDSONG songId title artist album year durationSec genres popularity`
- `REMOVESONG songId`
- `GETSONG songId`
- `LISTARTIST artist`
- `SEARCHTITLE keyword`
- `CREATEPLAYLIST playlistId [name]`
- `DELETEPLAYLIST playlistId`
- `ADDPLAYLIST playlistId songId [position]`
- `REMOVEPLAYLIST playlistId (songId|position)`
- `SHOWPLAYLIST playlistId`
- `ADDNEXT songId`
- `PLAYNEXT`
- `PEEKNEXT`
- `SHOWUPNEXT`
- `UNDO playlistId`
- `RECOMMEND`
- `QUIT`

### Quoted CLI arguments
Use double quotes for values with spaces:
```bash
ADDSONG s1 "Late Night Drive" "The Echoes" "Road Album" 2024 220 "Rock|Indie" 88
```

## CSV Loading
Header must be:
```csv
songId,title,artist,album,year,durationSec,genres,popularity
```

- `genres` uses pipe separators, e.g. `Rock|Alternative|Indie`
- whitespace is trimmed around each field
- quoted CSV fields are supported
- malformed lines are skipped (not fatal)

## Design/Policy Notes
- No built-in Java collections are used for core structures.
- Implemented custom structures: dynamic array, hash map, stack, queue.
- Playlist duplicate policy: **duplicates are allowed**.
- `REMOVEPLAYLIST playlistId songId` removes the **first occurrence** in that playlist.
- Undo history is **per playlist** and supports 20+ steps.
- Removing a song from library also removes **all occurrences** from every playlist and from the up-next queue.
- Title/artist matching:
  - `LISTARTIST`: case-insensitive exact artist match (trimmed)
  - `SEARCHTITLE`: case-insensitive normalized substring search; punctuation treated as spaces and repeated spaces collapsed
