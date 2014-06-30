package kfs.rss.reader;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pavedrim
 */
public class kfsFeed {

    final String title;
    final String link;
    final String description;
    final List<kfsMessage> entries = new ArrayList<kfsMessage>();

    public kfsFeed(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
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

    @Override
    public String toString() {
        return "FEED {description=" + description+ ", link=" + link + ", title=" + title+"}" ;
    }
}
