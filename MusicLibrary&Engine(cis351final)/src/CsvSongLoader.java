import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvSongLoader {
    public static class LoadResult {
        private final boolean success;
        private final int addedCount;
        private final int skippedCount;
        private final String message;

        public LoadResult(boolean success, int addedCount, int skippedCount, String message) {
            this.success = success;
            this.addedCount = addedCount;
            this.skippedCount = skippedCount;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public int getAddedCount() {
            return addedCount;
        }

        public int getSkippedCount() {
            return skippedCount;
        }

        public String getMessage() {
            return message;
        }
    }

    public static LoadResult load(String filePath, SoundWaveEngine engine) {
        if (filePath == null || filePath.trim().isEmpty() || engine == null) {
            return new LoadResult(false, 0, 0, "invalid arguments");
        }

        int added = 0;
        int skipped = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String header = reader.readLine();
            if (header == null) {
                return new LoadResult(false, 0, 0, "empty file");
            }
            if (!isExpectedHeader(header)) {
                return new LoadResult(false, 0, 0, "invalid csv header");
            }

            String line;
            int lineNo = 1;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (line.trim().isEmpty()) {
                    skipped++;
                    continue;
                }
                Song song = parseLine(line);
                if (song == null) {
                    skipped++;
                    continue;
                }
                if (engine.addSong(song)) {
                    added++;
                } else {
                    skipped++;
                }
            }
            return new LoadResult(true, added, skipped, "loaded");
        } catch (IOException ex) {
            return new LoadResult(false, added, skipped, ex.getMessage());
        }
    }

    private static boolean isExpectedHeader(String header) {
        String normalized = header.trim().toLowerCase();
        return normalized.equals("songid,title,artist,album,year,durationsec,genres,popularity");
    }

    private static Song parseLine(String line) {
        String[] fields = splitCsvLine(line);
        if (fields == null || fields.length != 8) {
            return null;
        }
        String songId = clean(fields[0]);
        String title = clean(fields[1]);
        String artist = clean(fields[2]);
        String album = clean(fields[3]);
        Integer year = parseYear(clean(fields[4]));
        Integer durationSec = parseInt(clean(fields[5]));
        String[] genres = SoundWaveEngine.parseGenres(clean(fields[6]));
        Integer popularity = parseInt(clean(fields[7]));

        if (year == null || durationSec == null || popularity == null) {
            return null;
        }
        if (!Song.isValid(songId, title, artist, album, year, durationSec, genres, popularity)) {
            return null;
        }
        return new Song(songId, title, artist, album, year, durationSec, genres, popularity);
    }

    private static String clean(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed.trim();
    }

    private static Integer parseInt(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static Integer parseYear(String value) {
        if (value == null || value.isEmpty() || value.equals("?") || value.equalsIgnoreCase("unknown")) {
            return -1;
        }
        return parseInt(value);
    }

    private static String[] splitCsvLine(String line) {
        DynamicArray<String> parts = new DynamicArray<String>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                parts.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        if (inQuotes) {
            return null;
        }
        parts.add(current.toString());

        String[] out = new String[parts.size()];
        for (int i = 0; i < parts.size(); i++) {
            out[i] = parts.get(i);
        }
        return out;
    }
}
