# Final Report

## 1) Project Outcome

This project delivers a complete SoundWave backend and CLI that satisfies the required feature set: library management, playlist management, up-next queue behavior, undo support, CSV import, search/list queries, and recommendation output. The implementation intentionally avoids Java built-in collections for core logic by using custom data structures (`DynamicArray`, `SimpleHashMap`, `SimpleStack`, and `SongQueue`).

`Main.java` provides an executable path-oriented test harness. Running it gives deterministic pass/fail reporting and detailed diagnostics when any assertion fails.

## 2) Complexity Discussion

Let:
- `S` = total songs in library
- `P` = total playlists
- `N` = songs in a specific playlist
- `Q` = songs in up-next queue

### Time Complexity by Operation

| Operation | Time | Notes |
|---|---|---|
| `addSong` | O(1) expected | Hash map insert by `songId` |
| `removeSong` | O(P + total playlist entries + Q) | Removes from library and cascades to playlists + queue |
| `getSongById` | O(1) expected | Hash map lookup |
| `listSongsByArtist` | O(S) | Scans all songs |
| `searchSongsByTitleKeyword` | O(S * T) | `T` is normalization/substring cost |
| `createPlaylist` / `deletePlaylist` | O(1) expected | Hash map insert/remove |
| `addSongToPlaylist` | O(1) append, O(N) indexed insert | Dynamic array shift on middle insert |
| `removeSongFromPlaylistByPosition` | O(N) | Shift after delete |
| `removeSongFromPlaylistBySongId` | O(N) | Find + shift |
| `showPlaylist` | O(N) | Builds playlist view |
| `addNext` / `playNext` / `peekNext` | O(1) | Linked queue operations |
| `showUpNext` | O(Q) | Snapshot queue and resolve songs |
| `undo` | O(1) expected per step | Stack pop + single inverse operation |
| `recommend(k)` | O(S^2) | Current insertion sort implementation |

### Space Complexity Highlights

- Library storage: O(S)
- Playlist storage: O(total playlist entries across all playlists)
- Up-next queue: O(Q)
- Undo history: O(total edits across playlists)
- Query/display outputs (`showPlaylist`, `showUpNext`, searches): O(result size)

### Practical Tradeoffs

- Hash-based indexing keeps ID lookups fast and predictable.
- Ordered playlist semantics require array shifts on middle operations; this is expected for the required behavior.
- Recommendation prioritizes simple correctness and deterministic ordering over asymptotic optimality; a heap-based top-k approach would improve performance for very large datasets.

## 3) Testing Strategy and Path Coverage Notes

The test harness in `Main.java` exercises both success and failure branches across required workflows:

- library add/get/remove paths (including invalid and duplicate cases)
- playlist create/delete/add/remove/show paths
- queue add/peek/play/show including empty states
- undo for missing playlist, empty history, multi-step rollback, and 20+ operations
- cross-component side effects (song removal affecting playlists and queue)
- CSV load success, malformed row skip, and invalid header failure
- recommendation ordering behavior

The harness reports:
- `PASS X / Y tests` when all assertions succeed
- `FAIL X / Y tests` with per-test diagnostics showing expected vs actual context

This format makes failures actionable and reproducible.

## 4) Bugs Encountered and Regression Prevention

### Bug 1: Global song removal left stale references

**Issue:** Removing a song from the library did not fully remove every playlist/queue reference in early versions, causing inconsistent state and possible null lookups later.

**Fix:** Implemented cascade cleanup in `removeSong`:
- remove all playlist occurrences of the removed `songId`
- remove all queue occurrences of the removed `songId`
- ensure lookup by removed ID returns null

**Regression tests that now guard this path:**
- `remove song removes all playlist occurrences`
- `remove song updates up-next`
- `remove song removes from library`

### Bug 2: Undo could restore entries for deleted songs

**Issue:** Undo history could replay edits that referenced songs deleted from the global library, reintroducing invalid playlist state.

**Fix:** Added song existence checks during undo and history purging for removed songs.

**Regression tests that now guard this path:**
- `undo success on playlist with history`
- `undo no history failure`
- `undo >=20 step #...`
- `undo fails after history exhausted`

### Bug 3: Title search mismatch with punctuation/case

**Issue:** Early title search behavior was too literal, so equivalent user queries like `"night drive"` vs `"Night-Drive"` could miss valid matches.

**Fix:** Added normalization in search flow:
- lowercase transformation
- punctuation treated as separators
- repeated spaces collapsed
- substring match on normalized values

**Regression tests that now guard this path:**
- `search title success count`
- `search title valid no matches empty`
- `search title invalid request returns null`

## 5) Demo Script (What to Show During Grading)

### Step A: Compile

```bash
javac -d out src/*.java
```

### Step B: Run automated path tests

```bash
java -cp out Main
```

### Expected output (success case)

```text
PASS 140 / 140 tests
```

### Expected output shape (failure case)

If a regression is introduced, output begins with:

```text
FAIL X / Y tests
```

followed by one or more lines containing:
- test name
- expected value/context
- actual value/context

### Step C: Optional interactive CLI demo

```bash
java -cp out Main cli
```

Example commands to type:

```text
ADDSONG s1 "Night Drive" "Echo" "Album" 2024 180 "Pop|Indie" 80
CREATEPLAYLIST p1 "Demo Mix"
ADDPLAYLIST p1 s1
SHOWPLAYLIST p1
QUIT
```

Expected key outputs include `SUCCESS` for valid mutations, playlist rows for `SHOWPLAYLIST`, and `BYE` on quit.

## 6) Remaining Risks and Future Improvements

- Replace insertion-sort recommendation with a more scalable top-k strategy (custom heap).
- Add performance stress tests for very large CSV imports and deep undo histories.
- Add focused CLI parsing edge-case tests (nested quotes/escaping scenarios) for even stronger robustness.
