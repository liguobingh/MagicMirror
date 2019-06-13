package viomi.com.mojingface.mediaplayer;

public class Track {

    private String title;
    private String subTitle;
    private String label;
    private String linkUrl;
    private String imageUrl;
    private String lrcUrl;

    public Track() {

    }

    public Track(String title, String subTitle, String label, String linkUrl, String imageUrl) {
        this.title = title;
        this.subTitle = subTitle;
        this.label = label;
        this.linkUrl = linkUrl;
        this.imageUrl = imageUrl;
    }

    public Track(String title, String subTitle, String label, String linkUrl, String imageUrl, String lrcUrl) {
        this.title = title;
        this.subTitle = subTitle;
        this.label = label;
        this.linkUrl = linkUrl;
        this.imageUrl = imageUrl;
        this.lrcUrl = lrcUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLrcUrl() {
        return lrcUrl;
    }

    public void setLrcUrl(String lrcUrl) {
        this.lrcUrl = lrcUrl;
    }
}
