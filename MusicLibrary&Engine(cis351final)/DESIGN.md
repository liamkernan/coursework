# SoundWave Design Document

## 1) Class / Module Plan

- `Song`
  - Immutable song model with required fields and validation helper.
- `SoundWaveEngine`
  - Main backend API implementing all required operations.
  - Owns library, playlists, queue, and recommendation logic.
- `Playlist`
  - Ordered song IDs plus per-playlist undo stack.
  - Handles add/remove/undo behavior.
- `CsvSongLoader`
  - CSV parsing and import into `SoundWaveEngine`.
- `SoundWaveCli`
  - Command parser/dispatcher for required CLI commands.
- `Main`
  - Automated path-testing harness (and optional CLI entry mode).

### Custom Data Structures (no built-in collections)
- `DynamicArray<T>`: resizable array for ordered collections.
- `SimpleHashMap<K,V>`: separate-chaining hash table.
- `SimpleStack<T>`: linked-list stack (undo history).
- `SongQueue`: linked-list FIFO queue (up-next line).

## 2) UML-Style Relationship Overview

- `SoundWaveEngine`
  - has `SimpleHashMap<String, Song>` (library)
  - has `SimpleHashMap<String, Playlist>` (playlists)
  - has `SongQueue` (up-next)
- `Playlist`
  - has `DynamicArray<String>` (ordered song IDs)
  - has `SimpleStack<PlaylistEdit>` (undo history)
- `PlaylistEdit`
  - stores operation type + songId + position
- `SoundWaveCli`
  - depends on `SoundWaveEngine` + `CsvSongLoader`
- `Main`
  - executes tests against `SoundWaveEngine` / `CsvSongLoader`

```mermaid
classDiagram
    direction TB

    %% ─── Domain Models ───

    class Song {
      -songId : String
      -title : String
      -artist : String
      -album : String
      -year : int
      -durationSec : int
      -genres : DynamicArray~String~
      -popularity : int
      +getSongId() String
      +getTitle() String
      +getArtist() String
      +getAlbum() String
      +getYear() int
      +getDurationSec() int
      +getGenres() DynamicArray~String~
      +getPopularity() int
      +isValid()$ boolean
    }

    class PlaylistEdit {
      -type : EditType
      -songId : String
      -position : int
      +getType() EditType
      +getSongId() String
      +getPosition() int
    }

    class PlaylistSongView {
      -position : int
      -songId : String
      -title : String
      -artist : String
    }

    %% ─── Core Engine ───

    class SoundWaveEngine {
      -songsById : SimpleHashMap~String, Song~
      -playlistsById : SimpleHashMap~String, Playlist~
      -upNextQueue : SongQueue
      +addSong(song : Song) boolean
      +removeSong(songId : String) boolean
      +getSongById(songId : String) Song
      +listSongsByArtist(artist : String) DynamicArray~Song~
      +searchSongsByTitleKeyword(kw : String) DynamicArray~Song~
      +createPlaylist(id : String, name : String) boolean
      +deletePlaylist(id : String) boolean
      +addSongToPlaylist(plId : String, songId : String, pos : int) boolean
      +removeSongFromPlaylistByPosition(plId : String, pos : int) boolean
      +removeSongFromPlaylistBySongId(plId : String, songId : String) boolean
      +showPlaylist(plId : String) DynamicArray~PlaylistSongView~
      +addNext(songId : String) boolean
      +playNext() Song
      +peekNext() Song
      +showUpNext() DynamicArray~Song~
      +undo(plId : String) boolean
      +recommend(limit : int) DynamicArray~Song~
    }

    class Playlist {
      -playlistId : String
      -displayName : String
      -songIds : DynamicArray~String~
      -history : SimpleStack~PlaylistEdit~
      +addSong(songId : String, pos : int) boolean
      +removeSongByPosition(pos : int) PlaylistEdit
      +removeSongBySongId(songId : String) PlaylistEdit
      +getSongIds() DynamicArray~String~
      +undo() boolean
      +purgeReferences(songId : String) void
    }

    %% ─── I/O Layer ───

    class CsvSongLoader {
      +load(filePath : String, engine : SoundWaveEngine) int
      -parseLine(line : String) Song
    }

    class SoundWaveCli {
      -engine : SoundWaveEngine
      -loader : CsvSongLoader
      +run() void
      -dispatch(command : String) void
    }

    class Main {
      +main(args : String[])$ void
      -runAllTests()$ void
      -assertEqual(expected, actual, label)$ void
    }

    %% ─── Custom Data Structures ───

    class DynamicArray~T~ {
      -data : T[]
      -size : int
      -capacity : int
      +add(item : T) void
      +insertAt(index : int, item : T) void
      +removeAt(index : int) T
      +get(index : int) T
      +size() int
      +contains(item : T) boolean
    }

    class SimpleHashMap~K, V~ {
      -buckets : DynamicArray~Entry~[]
      -size : int
      +put(key : K, value : V) void
      +get(key : K) V
      +remove(key : K) V
      +containsKey(key : K) boolean
      +size() int
      +values() DynamicArray~V~
    }

    class SimpleStack~T~ {
      -top : Node~T~
      -size : int
      +push(item : T) void
      +pop() T
      +peek() T
      +isEmpty() boolean
      +size() int
    }

    class SongQueue {
      -head : Node~String~
      -tail : Node~String~
      -size : int
      +enqueue(songId : String) void
      +dequeue() String
      +peek() String
      +isEmpty() boolean
      +removeAll(songId : String) void
      +toArray() DynamicArray~String~
    }

    %% ─── Composition (owns / lifecycle-bound) ───

    SoundWaveEngine *-- "1" SongQueue : upNextQueue
    SoundWaveEngine *-- "0..*" Song : songsById
    SoundWaveEngine *-- "0..*" Playlist : playlistsById
    Playlist *-- "0..*" PlaylistEdit : history

    %% ─── Internal usage (data structures) ───

    SoundWaveEngine ..> SimpleHashMap : uses
    Playlist ..> DynamicArray : songIds
    Playlist ..> SimpleStack : history
    Song ..> DynamicArray : genres

    %% ─── Produces ───

    SoundWaveEngine ..> PlaylistSongView : produces

    %% ─── Dependencies (I/O layer → engine) ───

    SoundWaveCli ..> SoundWaveEngine : delegates to
    SoundWaveCli ..> CsvSongLoader : uses
    CsvSongLoader ..> SoundWaveEngine : loads into
    Main ..> SoundWaveEngine : tests
    Main ..> CsvSongLoader : tests
```

## 3) Internal Design Choices and Rationale

- **Song storage**: hash map keyed by `songId`.
  - O(1) expected lookups/inserts/removals.
- **Playlist storage**: hash map keyed by `playlistId`.
  - O(1) expected create/delete/find.
- **Playlist order**: dynamic array of `songId`.
  - O(1) append; O(n) insert/remove in middle due shifting.
  - Matches required ordered behavior.
- **Up-next queue**: linked FIFO queue.
  - O(1) enqueue/dequeue/peek.
- **Undo**: per-playlist stack of edits.
  - O(1) push/pop for undoable playlist operations.
  - Per-playlist isolation simplifies correctness and scope.

### Policy Decisions
- Playlist duplicates: **allowed**.
- Remove-by-songId: removes **first occurrence**.
- Unknown year representation: `-1`.
- Malformed CSV lines: **skip with warning-level behavior** (counted as skipped).
- Remove-song side effect policy:
  - remove from library by exact `songId`
  - remove **all occurrences** from every playlist
  - remove **all occurrences** from up-next queue
  - purge undo history entries referencing removed song IDs

### Query Normalization Rules
- `LISTARTIST`: trimmed, case-insensitive exact match.
- `SEARCHTITLE`: lowercased; non-alphanumeric characters normalized to spaces; repeated spaces collapsed; substring match.

## 4) Complexity Expectations

- `addSong`: O(1) expected
- `removeSong`: O(P + total playlist entries + Q) worst-case scan for side-effects
- `getSongById`: O(1) expected
- `listSongsByArtist`: O(S)
- `searchSongsByTitleKeyword`: O(S * T) (T = title normalization/contains cost)
- `createPlaylist` / `deletePlaylist`: O(1) expected
- `addSongToPlaylist`:
  - append O(1) amortized
  - positioned insert O(n) for shift
- `removeSongFromPlaylistByPosition`: O(n)
- `removeSongFromPlaylistBySongId`: O(n)
- `showPlaylist`: O(n)
- `addNext` / `playNext` / `peekNext`: O(1)
- `showUpNext`: O(Q)
- `undo`: O(1) expected per step
- `recommend(k)`: O(S^2) with insertion sort in current implementation

## 5) Testing Strategy (Main.java)

`Main.java` automatically executes path-based tests with PASS/FAIL summary and detailed failure diagnostics.

Coverage includes:
- All required success/failure branches for library operations.
- Playlist creation/deletion/add/remove/show including boundary positions and duplicate policy behavior.
- Up-next queue success/failure/empty-state behavior.
- Undo success/no-history/missing-playlist/multi-step/20+ steps.
- Cross-component side effects (song removal updates playlists and queue).
- CSV load success and malformed/failure paths.

The test harness is deterministic and executable with one command:
```bash
java -cp src Main
```
