import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class SoundWaveCli {
    private final SoundWaveEngine engine;

    public SoundWaveCli(SoundWaveEngine engine) {
        this.engine = engine;
    }

    public void run(InputStream in, PrintStream out) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            String[] tokens = splitCommand(trimmed);
            String command = tokens[0].toUpperCase();

            if ("QUIT".equals(command)) {
                out.println("BYE");
                return;
            }

            handleCommand(command, tokens, out);
        }
    }

    private void handleCommand(String command, String[] tokens, PrintStream out) {
        try {
            if ("LOAD".equals(command)) {
                handleLoad(tokens, out);
            } else if ("ADDSONG".equals(command)) {
                handleAddSong(tokens, out);
            } else if ("REMOVESONG".equals(command)) {
                printMutation(out, engine.removeSong(required(tokens, 1)));
            } else if ("GETSONG".equals(command)) {
                Song song = engine.getSongById(required(tokens, 1));
                if (song == null) {
                    out.println("FAILED: song not found");
                } else {
                    out.println(song.toString());
                }
            } else if ("LISTARTIST".equals(command)) {
                DynamicArray<Song> songs = engine.listSongsByArtist(required(tokens, 1));
                printSongsQuery(out, songs);
            } else if ("SEARCHTITLE".equals(command)) {
                DynamicArray<Song> songs = engine.searchSongsByTitleKeyword(required(tokens, 1));
                printSongsQuery(out, songs);
            } else if ("CREATEPLAYLIST".equals(command)) {
                String name = tokens.length > 2 ? tokens[2] : "";
                printMutation(out, engine.createPlaylist(required(tokens, 1), name));
            } else if ("DELETEPLAYLIST".equals(command)) {
                printMutation(out, engine.deletePlaylist(required(tokens, 1)));
            } else if ("ADDPLAYLIST".equals(command)) {
                handleAddPlaylist(tokens, out);
            } else if ("REMOVEPLAYLIST".equals(command)) {
                handleRemovePlaylist(tokens, out);
            } else if ("SHOWPLAYLIST".equals(command)) {
                DynamicArray<PlaylistSongView> view = engine.showPlaylist(required(tokens, 1));
                if (view == null) {
                    out.println("FAILED: missing playlist");
                } else if (view.isEmpty()) {
                    out.println("EMPTY PLAYLIST");
                } else {
                    for (int i = 0; i < view.size(); i++) {
                        out.println(view.get(i).toString());
                    }
                }
            } else if ("ADDNEXT".equals(command)) {
                printMutation(out, engine.addNext(required(tokens, 1)));
            } else if ("PLAYNEXT".equals(command)) {
                Song song = engine.playNext();
                if (song == null) {
                    out.println("EMPTY QUEUE");
                } else {
                    out.println(song.toString());
                }
            } else if ("PEEKNEXT".equals(command)) {
                Song song = engine.peekNext();
                if (song == null) {
                    out.println("EMPTY QUEUE");
                } else {
                    out.println(song.toString());
                }
            } else if ("SHOWUPNEXT".equals(command)) {
                DynamicArray<Song> songs = engine.showUpNext();
                if (songs.isEmpty()) {
                    out.println("EMPTY QUEUE");
                } else {
                    for (int i = 0; i < songs.size(); i++) {
                        out.println(songs.get(i).toString());
                    }
                }
            } else if ("UNDO".equals(command)) {
                printMutation(out, engine.undo(required(tokens, 1)));
            } else if ("RECOMMEND".equals(command)) {
                DynamicArray<Song> songs = engine.recommend();
                if (songs.isEmpty()) {
                    out.println("EMPTY QUERY");
                } else {
                    for (int i = 0; i < songs.size(); i++) {
                        out.println(songs.get(i).toString());
                    }
                }
            } else {
                out.println("FAILED: unknown command");
            }
        } catch (IllegalArgumentException ex) {
            out.println("FAILED: " + ex.getMessage());
        }
    }

    private void handleLoad(String[] tokens, PrintStream out) {
        CsvSongLoader.LoadResult result = CsvSongLoader.load(required(tokens, 1), engine);
        if (!result.isSuccess()) {
            out.println("FAILED: " + result.getMessage());
            return;
        }
        out.println("SUCCESS: added=" + result.getAddedCount() + " skipped=" + result.getSkippedCount());
    }

    private void handleAddSong(String[] tokens, PrintStream out) {
        if (tokens.length < 9) {
            throw new IllegalArgumentException("ADDSONG requires 8 arguments");
        }
        String songId = tokens[1];
        String title = tokens[2];
        String artist = tokens[3];
        String album = tokens[4];
        int year = parseYear(tokens[5]);
        int durationSec = parseInt(tokens[6]);
        String[] genres = SoundWaveEngine.parseGenres(tokens[7]);
        int popularity = parseInt(tokens[8]);
        Song song = new Song(songId, title, artist, album, year, durationSec, genres, popularity);
        printMutation(out, engine.addSong(song));
    }

    private void handleAddPlaylist(String[] tokens, PrintStream out) {
        if (tokens.length < 3) {
            throw new IllegalArgumentException("ADDPLAYLIST requires playlistId and songId");
        }
        Integer position = null;
        if (tokens.length > 3) {
            position = parseInt(tokens[3]);
        }
        boolean success = engine.addSongToPlaylist(tokens[1], tokens[2], position);
        printMutation(out, success);
    }

    private void handleRemovePlaylist(String[] tokens, PrintStream out) {
        if (tokens.length < 3) {
            throw new IllegalArgumentException("REMOVEPLAYLIST requires playlistId and songId|position");
        }
        String target = tokens[2];
        boolean parsedAsInteger = isInteger(target);
        boolean success;
        if (parsedAsInteger) {
            success = engine.removeSongFromPlaylistByPosition(tokens[1], Integer.parseInt(target));
        } else {
            success = engine.removeSongFromPlaylistBySongId(tokens[1], target);
        }
        printMutation(out, success);
    }

    private String required(String[] tokens, int index) {
        if (tokens.length <= index || tokens[index] == null || tokens[index].trim().isEmpty()) {
            throw new IllegalArgumentException("missing argument");
        }
        return tokens[index];
    }

    private void printMutation(PrintStream out, boolean success) {
        out.println(success ? "SUCCESS" : "FAILED");
    }

    private void printSongsQuery(PrintStream out, DynamicArray<Song> songs) {
        if (songs == null) {
            out.println("FAILED: invalid request");
        } else if (songs.isEmpty()) {
            out.println("EMPTY QUERY");
        } else {
            for (int i = 0; i < songs.size(); i++) {
                out.println(songs.get(i).toString());
            }
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("invalid integer: " + value);
        }
    }

    private int parseYear(String value) {
        if ("?".equals(value) || "unknown".equalsIgnoreCase(value)) {
            return -1;
        }
        return parseInt(value);
    }

    private boolean isInteger(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        int start = value.charAt(0) == '-' ? 1 : 0;
        if (start == 1 && value.length() == 1) {
            return false;
        }
        for (int i = start; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private String[] splitCommand(String line) {
        DynamicArray<String> tokens = new DynamicArray<String>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (Character.isWhitespace(c) && !inQuotes) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current = new StringBuilder();
                }
            } else {
                current.append(c);
            }
        }

        if (current.length() > 0) {
            tokens.add(current.toString());
        }

        String[] out = new String[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            out[i] = tokens.get(i);
        }
        return out;
    }
}
