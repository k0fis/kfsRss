package kfs.rss.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author pavedrim
 */
public class kfsFeed {

    private final String title;
    private final String link;
    private final String description;
    private final String imageUrl;
    private final List<kfsMessage> entries = new ArrayList<kfsMessage>();

    public kfsFeed(String title, String link, String description, String imageUrl) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public void addMessage(kfsMessage msg) {
        entries.add(msg);
    }

    public List<kfsMessage> getMessages() {
        return entries;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    
    @Override
    public String toString() {
        return Arrays.toString(new Object[]{"FEED {description", description, "link", link,
            "title", title, "IMG", imageUrl, "}"});
    }
}
