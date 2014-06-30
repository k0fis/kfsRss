package kfs.rss.reader;

/**
 *
 * @author pavedrim
 */
public class kfsMessage {

    private final String title;
    private final String description;
    private final String link;
    private final String guid;

    public kfsMessage(String title, String description, String link, String guid) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.guid = guid;
        
    }
    
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getGuid() {
        return guid;
    }

    @Override
    public String toString() {
        return "MSG {title=" + title + ", description=" + description
                + ", link=" + link + ", guid=" + guid + "}";
    }
}
