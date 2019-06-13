package viomi.com.mojingface.mediaplayer;

/**
 * <p>descript：歌单实体<p>
 * <p>author：randysu<p>
 * <p>create time：2018/12/10<p>
 * <p>update time：2018/12/10<p>
 * <p>version：1<p>
 */
public class MiguMusicSheetBean {

    private long columnId;
    private String columnName;
    private long[] playlistIds;

    public long getColumnId() {
        return columnId;
    }

    public void setColumnId(long columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public long[] getPlaylistIds() {
        return playlistIds;
    }

    public void setPlaylistIds(long[] playlistIds) {
        this.playlistIds = playlistIds;
    }
}
