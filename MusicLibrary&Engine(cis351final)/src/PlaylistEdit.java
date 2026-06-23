public class PlaylistEdit {
    public static final int TYPE_ADD = 1;
    public static final int TYPE_REMOVE = 2;

    private final int type;
    private final String songId;
    private final int position;

    public PlaylistEdit(int type, String songId, int position) {
        this.type = type;
        this.songId = songId;
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public String getSongId() {
        return songId;
    }

    public int getPosition() {
        return position;
    }
}
