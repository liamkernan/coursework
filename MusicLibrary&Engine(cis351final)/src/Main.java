import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length > 0 && "cli".equalsIgnoreCase(args[0])) {
            SoundWaveCli cli = new SoundWaveCli(new SoundWaveEngine());
            cli.run(System.in, System.out);
            return;
        }

        TestRunner tests = new TestRunner();
        runAllTests(tests);
        tests.printSummary();
        if (tests.getFailedCount() > 0) {
            System.exit(1);
        }
    }

    private static void runAllTests(TestRunner tests) throws IOException {
        testLibraryAddPaths(tests);
        testRemoveSongWithSideEffects(tests);
        testGetSongPaths(tests);
        testAdditionalInvalidInputPaths(tests);
        testListArtistPaths(tests);
        testSearchTitlePaths(tests);
        testCreateDeletePlaylistPaths(tests);
        testAddSongToPlaylistPaths(tests);
        testRemoveSongFromPlaylistPaths(tests);
        testShowPlaylistPaths(tests);
        testUpNextPaths(tests);
        testUndoPaths(tests);
        testCsvLoadingPaths(tests);
        testRecommendPath(tests);
    }

    private static void testLibraryAddPaths(TestRunner tests) {
        SoundWaveEngine engine = new SoundWaveEngine();
        boolean added = engine.addSong(song("s1", "Night Drive", "Echo", 80));
        tests.assertTrue("add song success", added, true, added);

        boolean duplicate = engine.addSong(song("s1", "Another", "Echo", 50));
        tests.assertFalse("add song duplicate id failure", duplicate, false, duplicate);

        Song invalidPopularity = new Song("s2", "Bad", "Echo", "Album", 2020, 200, new String[]{"Pop"}, 120);
        boolean invalidResult = engine.addSong(invalidPopularity);
        tests.assertFalse("add song invalid field failure", invalidResult, false, invalidResult);
    }

    private static void testRemoveSongWithSideEffects(TestRunner tests) {
        SoundWaveEngine engine = new SoundWaveEngine();
        engine.addSong(song("s1", "Alpha", "A", 70));
        engine.addSong(song("s2", "Beta", "B", 60));
        engine.createPlaylist("p1", "mix");
        engine.addSongToPlaylist("p1", "s1", null);
        engine.addSongToPlaylist("p1", "s2", null);
        engine.addSongToPlaylist("p1", "s1", null);
        engine.addNext("s1");
        engine.addNext("s2");
        engine.addNext("s1");

        boolean removed = engine.removeSong("s1");
        tests.assertTrue("remove song success", removed, true, removed);

        DynamicArray<PlaylistSongView> playlist = engine.showPlaylist("p1");
        tests.assertNotNull("remove song updates playlist list not null", playlist, "non-null", "null");
        tests.assertEqualsInt("remove song removes all playlist occurrences", 1, playlist.size(), "playlist size 1", "playlist size " + playlist.size());
        tests.assertEqualsText("remove song leaves expected playlist song", "s2", playlist.get(0).getSongId(), "s2", playlist.get(0).getSongId());

        DynamicArray<Song> queue = engine.showUpNext();
        tests.assertEqualsInt("remove song updates up-next", 1, queue.size(), "queue size 1", "queue size " + queue.size());
        tests.assertEqualsText("remove song leaves expected up-next song", "s2", queue.get(0).getSongId(), "s2", queue.get(0).getSongId());

        Song missing = engine.getSongById("s1");
        tests.assertNull("remove song removes from library", missing, "null", "non-null");

        boolean missingRemove = engine.removeSong("does-not-exist");
        tests.assertFalse("remove song missing id failure", missingRemove, false, missingRemove);
    }

    private static void testGetSongPaths(TestRunner tests) {
        SoundWaveEngine engine = new SoundWaveEngine();
        engine.addSong(song("id1", "Song One", "Artist X", 40));
        Song found = engine.getSongById("id1");
        tests.assertNotNull("get song by id success non-null", found, "non-null", "null");
        tests.assertEqualsText("get song by id returns matching id", "id1", found.getSongId(), "id1", found.getSongId());
        Song missing = engine.getSongById("missing");
        tests.assertNull("get song by id missing returns null", missing, "null", "non-null");
    }

    private static void testAdditionalInvalidInputPaths(TestRunner tests) {
        SoundWaveEngine engine = new SoundWaveEngine();
        engine.addSong(song("s1", "Any Song", "Any Artist", 33));
        engine.createPlaylist("p1", "Playlist");
        engine.addSongToPlaylist("p1", "s1", null);
        engine.addNext("s1");

        boolean removeBlank = engine.removeSong("   ");
        tests.assertFalse("remove song invalid blank id failure", removeBlank, false, removeBlank);

        Song getBlank = engine.getSongById("   ");
        tests.assertNull("get song invalid blank id returns null", getBlank, "null", "non-null");

        DynamicArray<PlaylistSongView> showBlank = engine.showPlaylist("   ");
        tests.assertNull("show playlist invalid blank id returns null", showBlank, "null", "non-null");

        boolean deleteBlank = engine.deletePlaylist("   ");
        tests.assertFalse("delete playlist invalid blank id failure", deleteBlank, false, deleteBlank);

        boolean addNextBlank = engine.addNext("   ");
        tests.assertFalse("add-next invalid blank id failure", addNextBlank, false, addNextBlank);

        boolean undoBlank = engine.undo("   ");
        tests.assertFalse("undo invalid blank playlist id failure", undoBlank, false, undoBlank);
    }

    private static void testListArtistPaths(TestRunner tests) {
        SoundWaveEngine engine = new SoundWaveEngine();
        engine.addSong(song("a1", "Blue Sky", "The Band", 20));
        engine.addSong(song("a2", "Gray Sky", "The Band", 30));
        engine.addSong(song("a3", "Different", "Other", 40));

        DynamicArray<Song> matches = engine.listSongsByArtist("the band");
        tests.assertNotNull("list artist success non-null", matches, "non-null", "null");
        tests.assertEqualsInt("list artist success count", 2, matches.size(), "2", String.valueOf(matches.size()));

        DynamicArray<Song> noMatches = engine.listSongsByArtist("unknown artist");
        tests.assertNotNull("list artist valid no matches non-null", noMatches, "non-null", "null");
        tests.assertEqualsInt("list artist valid no matches empty", 0, noMatches.size(), "0", String.valueOf(noMatches.size()));

        DynamicArray<Song> invalid = engine.listSongsByArtist("   ");
        tests.assertNull("list artist invalid request returns null", invalid, "null", "non-null");
    }

    private static void testSearchTitlePaths(TestRunner tests) {
        SoundWaveEngine engine = new SoundWaveEngine();
        engine.addSong(song("t1", "Night-Drive", "A", 10));
        engine.addSong(song("t2", "Late Night Drive", "B", 20));
        engine.addSong(song("t3", "Sunrise", "C", 30));

        DynamicArray<Song> matches = engine.searchSongsByTitleKeyword("night drive");
        tests.assertNotNull("search title success non-null", matches, "non-null", "null");
        tests.assertEqualsInt("search title success count", 2, matches.size(), "2", String.valueOf(matches.size()));

        DynamicArray<Song> none = engine.searchSongsByTitleKeyword("volcano");
        tests.assertNotNull("search title valid no matches non-null", none, "non-null", "null");
        tests.assertEqualsInt("search title valid no matches empty", 0, none.size(), "0", String.valueOf(none.size()));

        DynamicArray<Song> invalid = engine.searchSongsByTitleKeyword("   ");
        tests.assertNull("search title invalid request returns null", invalid, "null", "non-null");
    }

    private static void testCreateDeletePlaylistPaths(TestRunner tests) {
        SoundWaveEngine engine = new SoundWaveEngine();
        boolean created = engine.createPlaylist("pl1", "My Playlist");
        tests.assertTrue("create playlist success", created, true, created);

        boolean duplicate = engine.createPlaylist("pl1", "Again");
        tests.assertFalse("create playlist duplicate failure", duplicate, false, duplicate);

        boolean invalid = engine.createPlaylist("   ", "bad");
        tests.assertFalse("create playlist invalid id failure", invalid, false, invalid);

        boolean deleted = engine.deletePlaylist("pl1");
        tests.assertTrue("delete playlist success", deleted, true, deleted);

        boolean missing = engine.deletePlaylist("pl1");
        tests.assertFalse("delete playlist missing failure", missing, false, missing);
    }

    private static void testAddSongToPlaylistPaths(TestRunner tests) {
        SoundWaveEngine engine = new SoundWaveEngine();
        engine.addSong(song("s1", "Song 1", "A", 10));
        engine.addSong(song("s2", "Song 2", "B", 20));
        engine.addSong(song("s3", "Song 3", "C", 30));
        engine.createPlaylist("p", "mix");

        boolean append = engine.addSongToPlaylist("p", "s1", null);
        tests.assertTrue("add playlist append success", append, true, append);

        boolean atZero = engine.addSongToPlaylist("p", "s2", 0);
        tests.assertTrue("add playlist position zero success", atZero, true, atZero);

        boolean atSize = engine.addSongToPlaylist("p", "s3", 2);
        tests.assertTrue("add playlist position size success", atSize, true, atSize);

        DynamicArray<PlaylistSongView> order = engine.showPlaylist("p");
        tests.assertPlaylistOrder("add playlist order check", order, new String[]{"s2", "s1", "s3"});

        boolean missingPlaylist = engine.addSongToPlaylist("missing", "s1", null);
        tests.assertFalse("add playlist missing playlist failure", missingPlaylist, false, missingPlaylist);

        boolean missingSong = engine.addSongToPlaylist("p", "missingSong", null);
        tests.assertFalse("add playlist missing song failure", missingSong, false, missingSong);

        boolean invalidPosition = engine.addSongToPlaylist("p", "s1", -1);
        tests.assertFalse("add playlist invalid position failure", invalidPosition, false, invalidPosition);

        boolean duplicateBehavior = engine.addSongToPlaylist("p", "s1", null);
        tests.assertTrue("add playlist duplicates-policy behavior (duplicates allowed)", duplicateBehavior, true, duplicateBehavior);
    }

    private static void testRemoveSongFromPlaylistPaths(TestRunner tests) {
        SoundWaveEngine engine = new SoundWaveEngine();
        engine.addSong(song("s1", "One", "A", 10));
        engine.addSong(song("s2", "Two", "A", 20));
        engine.createPlaylist("p", "mix");
        engine.addSongToPlaylist("p", "s1", null);
        engine.addSongToPlaylist("p", "s1", null);
        engine.addSongToPlaylist("p", "s2", null);

        boolean removeByPosition = engine.removeSongFromPlaylistByPosition("p", 1);
        tests.assertTrue("remove playlist by position success", removeByPosition, true, removeByPosition);

        boolean removeMissingPlaylist = engine.removeSongFromPlaylistByPosition("missing", 0);
        tests.assertFalse("remove playlist by position missing playlist failure", removeMissingPlaylist, false, removeMissingPlaylist);

        boolean removeInvalidPosition = engine.removeSongFromPlaylistByPosition("p", 99);
        tests.assertFalse("remove playlist by position invalid position failure", removeInvalidPosition, false, removeInvalidPosition);

        engine.addSongToPlaylist("p", "s1", null);
        boolean removeById = engine.removeSongFromPlaylistBySongId("p", "s1");
        tests.assertTrue("remove playlist by song id success", removeById, true, removeById);

        DynamicArray<PlaylistSongView> after = engine.showPlaylist("p");
        tests.assertPlaylistOrder("remove playlist duplicates behavior (first occurrence only)", after, new String[]{"s2", "s1"});

        boolean removeMissingSong = engine.removeSongFromPlaylistBySongId("p", "missing");
        tests.assertFalse("remove playlist by song id missing song failure", removeMissingSong, false, removeMissingSong);

        boolean removeByIdMissingPlaylist = engine.removeSongFromPlaylistBySongId("missing", "s1");
        tests.assertFalse("remove playlist by song id missing playlist failure", removeByIdMissingPlaylist, false, removeByIdMissingPlaylist);
    }

    private static void testShowPlaylistPaths(TestRunner tests) {
        SoundWaveEngine engine = new SoundWaveEngine();
        DynamicArray<PlaylistSongView> missing = engine.showPlaylist("missing");
        tests.assertNull("show playlist missing returns null", missing, "null", "non-null");

        engine.createPlaylist("empty", "Empty");
        DynamicArray<PlaylistSongView> empty = engine.showPlaylist("empty");
        tests.assertNotNull("show playlist empty non-null", empty, "non-null", "null");
        tests.assertEqualsInt("show playlist empty collection size", 0, empty.size(), "0", String.valueOf(empty.size()));

        engine.addSong(song("x1", "X Song", "X", 10));
        engine.addSongToPlaylist("empty", "x1", null);
        DynamicArray<PlaylistSongView> nonEmpty = engine.showPlaylist("empty");
        tests.assertNotNull("show playlist non-empty non-null", nonEmpty, "non-null", "null");
        tests.assertEqualsInt("show playlist non-empty size", 1, nonEmpty.size(), "1", String.valueOf(nonEmpty.size()));
        tests.assertEqualsText("show playlist item includes song id", "x1", nonEmpty.get(0).getSongId(), "x1", nonEmpty.get(0).getSongId());
    }

    private static void testUpNextPaths(TestRunner tests) {
        SoundWaveEngine engine = new SoundWaveEngine();
        engine.addSong(song("u1", "U One", "U", 10));
        engine.addSong(song("u2", "U Two", "U", 20));

        boolean addNextSuccess = engine.addNext("u1");
        tests.assertTrue("add-next success", addNextSuccess, true, addNextSuccess);

        boolean addNextMissing = engine.addNext("missing");
        tests.assertFalse("add-next missing song failure", addNextMissing, false, addNextMissing);

        engine.addNext("u2");
        DynamicArray<Song> showNonEmpty = engine.showUpNext();
        tests.assertNotNull("show up-next non-empty collection non-null", showNonEmpty, "non-null", "null");
        tests.assertEqualsInt("show up-next non-empty size", 2, showNonEmpty.size(), "2", String.valueOf(showNonEmpty.size()));
        tests.assertEqualsText("show up-next preserves queue order first", "u1", showNonEmpty.get(0).getSongId(), "u1", showNonEmpty.get(0).getSongId());
        tests.assertEqualsText("show up-next preserves queue order second", "u2", showNonEmpty.get(1).getSongId(), "u2", showNonEmpty.get(1).getSongId());

        Song peek = engine.peekNext();
        tests.assertNotNull("peek-next success non-null", peek, "non-null", "null");
        tests.assertEqualsText("peek-next returns front song", "u1", peek.getSongId(), "u1", peek.getSongId());

        Song played = engine.playNext();
        tests.assertNotNull("play-next success non-null", played, "non-null", "null");
        tests.assertEqualsText("play-next returns removed front song", "u1", played.getSongId(), "u1", played.getSongId());

        Song second = engine.playNext();
        tests.assertNotNull("play-next second success", second, "non-null", "null");
        tests.assertEqualsText("play-next second returns next", "u2", second.getSongId(), "u2", second.getSongId());

        Song emptyPlay = engine.playNext();
        tests.assertNull("play-next empty queue returns null", emptyPlay, "null", "non-null");

        Song emptyPeek = engine.peekNext();
        tests.assertNull("peek-next empty queue returns null", emptyPeek, "null", "non-null");

        DynamicArray<Song> show = engine.showUpNext();
        tests.assertNotNull("show up-next always non-null", show, "non-null", "null");
        tests.assertEqualsInt("show up-next empty collection when queue empty", 0, show.size(), "0", String.valueOf(show.size()));
    }

    private static void testUndoPaths(TestRunner tests) {
        SoundWaveEngine engine = new SoundWaveEngine();
        engine.addSong(song("s1", "One", "A", 10));
        engine.addSong(song("s2", "Two", "A", 20));
        engine.addSong(song("s3", "Three", "A", 30));

        boolean missingPlaylist = engine.undo("missing");
        tests.assertFalse("undo missing playlist failure", missingPlaylist, false, missingPlaylist);

        engine.createPlaylist("p0", "No History");
        boolean noHistory = engine.undo("p0");
        tests.assertFalse("undo no history failure", noHistory, false, noHistory);

        engine.createPlaylist("p1", "Has History");
        engine.addSongToPlaylist("p1", "s1", null);
        boolean undoSuccess = engine.undo("p1");
        tests.assertTrue("undo success on playlist with history", undoSuccess, true, undoSuccess);
        DynamicArray<PlaylistSongView> afterUndo = engine.showPlaylist("p1");
        tests.assertEqualsInt("undo success reverts playlist state", 0, afterUndo.size(), "0", String.valueOf(afterUndo.size()));

        engine.createPlaylist("p2", "Multi");
        engine.addSongToPlaylist("p2", "s1", null);
        engine.addSongToPlaylist("p2", "s2", null);
        engine.addSongToPlaylist("p2", "s3", null);
        engine.removeSongFromPlaylistByPosition("p2", 1);
        tests.assertPlaylistOrder("undo setup state", engine.showPlaylist("p2"), new String[]{"s1", "s3"});

        boolean undo1 = engine.undo("p2");
        boolean undo2 = engine.undo("p2");
        boolean undo3 = engine.undo("p2");
        boolean undo4 = engine.undo("p2");
        tests.assertTrue("undo multi-step step 1 success", undo1, true, undo1);
        tests.assertTrue("undo multi-step step 2 success", undo2, true, undo2);
        tests.assertTrue("undo multi-step step 3 success", undo3, true, undo3);
        tests.assertTrue("undo multi-step step 4 success", undo4, true, undo4);
        tests.assertEqualsInt("undo multi-step back to original", 0, engine.showPlaylist("p2").size(), "0", String.valueOf(engine.showPlaylist("p2").size()));

        engine.createPlaylist("p3", "Deep Undo");
        for (int i = 0; i < 25; i++) {
            boolean added = engine.addSongToPlaylist("p3", "s1", null);
            tests.assertTrue("undo >=20 setup add #" + i, added, true, added);
        }
        for (int i = 0; i < 25; i++) {
            boolean ok = engine.undo("p3");
            tests.assertTrue("undo >=20 step #" + i, ok, true, ok);
        }
        boolean tooFar = engine.undo("p3");
        tests.assertFalse("undo fails after history exhausted", tooFar, false, tooFar);
        tests.assertEqualsInt("undo >=20 ends at original state", 0, engine.showPlaylist("p3").size(), "0", String.valueOf(engine.showPlaylist("p3").size()));
    }

    private static void testCsvLoadingPaths(TestRunner tests) throws IOException {
        SoundWaveEngine engine = new SoundWaveEngine();
        File csv = File.createTempFile("soundwave", ".csv");
        try (FileWriter writer = new FileWriter(csv)) {
            writer.write("songId,title,artist,album,year,durationSec,genres,popularity\n");
            writer.write("c1,Good Song,Artist A,Album A,2024,180,Pop|Indie,80\n");
            writer.write("c2,Bad Duration,Artist B,Album B,2024,nope,Rock,50\n");
        }
        CsvSongLoader.LoadResult result = CsvSongLoader.load(csv.getAbsolutePath(), engine);
        tests.assertTrue("csv load success path", result.isSuccess(), true, result.isSuccess());
        tests.assertEqualsInt("csv load added count", 1, result.getAddedCount(), "1", String.valueOf(result.getAddedCount()));
        tests.assertEqualsInt("csv load skipped malformed count", 1, result.getSkippedCount(), "1", String.valueOf(result.getSkippedCount()));

        File badHeader = File.createTempFile("soundwave_bad", ".csv");
        try (FileWriter writer = new FileWriter(badHeader)) {
            writer.write("wrong,header\n");
            writer.write("x,y\n");
        }
        CsvSongLoader.LoadResult badResult = CsvSongLoader.load(badHeader.getAbsolutePath(), engine);
        tests.assertFalse("csv load invalid header failure", badResult.isSuccess(), false, badResult.isSuccess());
    }

    private static void testRecommendPath(TestRunner tests) {
        SoundWaveEngine engine = new SoundWaveEngine();
        engine.addSong(song("r1", "R One", "R", 40));
        engine.addSong(song("r2", "R Two", "R", 95));
        engine.addSong(song("r3", "R Three", "R", 60));
        DynamicArray<Song> recommendations = engine.recommend();
        tests.assertNotNull("recommend returns non-null collection", recommendations, "non-null", "null");
        tests.assertEqualsText("recommend returns top popularity first", "r2", recommendations.get(0).getSongId(), "r2", recommendations.get(0).getSongId());
    }

    private static Song song(String id, String title, String artist, int popularity) {
        return new Song(id, title, artist, "Album", 2020, 180, new String[]{"Pop"}, popularity);
    }

    private static class TestRunner {
        private int passed;
        private int failed;
        private final DynamicArray<String> failures;

        private TestRunner() {
            this.passed = 0;
            this.failed = 0;
            this.failures = new DynamicArray<String>();
        }

        void assertTrue(String name, boolean actual, boolean expected, boolean actualForLog) {
            if (actual) {
                passed++;
            } else {
                failed++;
                failures.add(name + " | expected=true | actual=" + actualForLog);
            }
        }

        void assertFalse(String name, boolean actual, boolean expected, boolean actualForLog) {
            if (!actual) {
                passed++;
            } else {
                failed++;
                failures.add(name + " | expected=false | actual=" + actualForLog);
            }
        }

        void assertNull(String name, Object value, String expected, String actual) {
            if (value == null) {
                passed++;
            } else {
                failed++;
                failures.add(name + " | expected=" + expected + " | actual=" + actual);
            }
        }

        void assertNotNull(String name, Object value, String expected, String actual) {
            if (value != null) {
                passed++;
            } else {
                failed++;
                failures.add(name + " | expected=" + expected + " | actual=" + actual);
            }
        }

        void assertEqualsInt(String name, int expected, int actual, String expectedContext, String actualContext) {
            if (expected == actual) {
                passed++;
            } else {
                failed++;
                failures.add(name + " | expected=" + expected + " (" + expectedContext + ") | actual=" + actual + " (" + actualContext + ")");
            }
        }

        void assertEqualsText(String name, String expected, String actual, String expectedContext, String actualContext) {
            boolean ok = expected == null ? actual == null : expected.equals(actual);
            if (ok) {
                passed++;
            } else {
                failed++;
                failures.add(name + " | expected=" + expectedContext + " | actual=" + actualContext);
            }
        }

        void assertPlaylistOrder(String name, DynamicArray<PlaylistSongView> list, String[] expectedSongIds) {
            if (list == null) {
                failed++;
                failures.add(name + " | expected non-null playlist | actual null");
                return;
            }
            if (list.size() != expectedSongIds.length) {
                failed++;
                failures.add(name + " | expected size=" + expectedSongIds.length + " | actual size=" + list.size());
                return;
            }
            for (int i = 0; i < expectedSongIds.length; i++) {
                String actualId = list.get(i).getSongId();
                if (!expectedSongIds[i].equals(actualId)) {
                    failed++;
                    failures.add(name + " | expected id at " + i + "=" + expectedSongIds[i] + " | actual=" + actualId);
                    return;
                }
            }
            passed++;
        }

        int getFailedCount() {
            return failed;
        }

        void printSummary() {
            int total = passed + failed;
            if (failed == 0) {
                System.out.println("PASS " + passed + " / " + total + " tests");
            } else {
                System.out.println("FAIL " + passed + " / " + total + " tests");
                for (int i = 0; i < failures.size(); i++) {
                    System.out.println(" - " + failures.get(i));
                }
            }
        }
    }
}
