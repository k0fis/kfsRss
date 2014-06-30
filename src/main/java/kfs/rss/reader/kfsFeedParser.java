package kfs.rss.reader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

/**
 *
 * @author pavedrim
 */
public class kfsFeedParser {

    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LINK = "link";
    static final String ITEM = "item";
    static final String GUID = "guid";
    static final String URL = "url";

    public static kfsFeed readFeed(String url) throws IOException {
        try {
            return readFeed(new URL(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static kfsFeed readFeed(URL iurl) throws IOException {
        kfsFeed feed = null;
        try {
            boolean isFeedHeader = true;
            // Set header values intial to the empty string
            String description = "";
            String title = "";
            String link = "";
            String guid = "";
            String url = "";

            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader

            InputStream in = iurl.openStream();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName().getLocalPart().toLowerCase();
                    if (localPart.equals(ITEM)) {
                        if (isFeedHeader) {
                            isFeedHeader = false;
                            feed = new kfsFeed(title, link, description, url);
                        }
                    }
                    if (localPart.equals(TITLE)) {
                        title = getCharacterData(event, eventReader);
                    }
                    if (localPart.equals(DESCRIPTION)) {
                        description = getCharacterData(event, eventReader);
                    }
                    if (localPart.equals(LINK)) {
                        link = getCharacterData(event, eventReader);
                    }
                    if (localPart.equals(GUID)) {
                        guid = getCharacterData(event, eventReader);
                    }
                    if (localPart.equals(URL)) {
                        url = getCharacterData(event, eventReader);
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart().equals(ITEM)) {
                        if (feed != null) {
                            feed.addMessage(new kfsMessage(title, description, link, guid));
                        }
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return feed;
    }

    private static String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    public static void writeFeed(kfsFeed rssfeed, String outputFilename) throws Exception {
        writeFeed(rssfeed, new FileOutputStream(outputFilename));
    }

    public static void writeFeed(kfsFeed rssfeed, OutputStream outStr) throws Exception {

        // Create a XMLOutputFactory
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        // Create XMLEventWriter
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(outStr);

        // Create a EventFactory
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");

        // Create and write Start Tag
        StartDocument startDocument = eventFactory.createStartDocument();

        eventWriter.add(startDocument);

        // Create open tag
        eventWriter.add(end);

        StartElement rssStart = eventFactory.createStartElement("", "", "rss");
        eventWriter.add(rssStart);
        eventWriter.add(eventFactory.createAttribute("version", "2.0"));
        eventWriter.add(end);

        eventWriter.add(eventFactory.createStartElement("", "", "channel"));
        eventWriter.add(end);

        // Write the different nodes
        createNode(eventWriter, TITLE, rssfeed.getTitle());
        createNode(eventWriter, LINK, rssfeed.getLink());
        createNode(eventWriter, DESCRIPTION, rssfeed.getDescription());

        for (kfsMessage entry : rssfeed.getMessages()) {
            eventWriter.add(eventFactory.createStartElement("", "", ITEM));
            eventWriter.add(end);
            createNode(eventWriter, TITLE, entry.getTitle());
            createNode(eventWriter, DESCRIPTION, entry.getDescription());
            createNode(eventWriter, LINK, entry.getLink());
            createNode(eventWriter, GUID, entry.getGuid());
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndElement("", "", ITEM));
            eventWriter.add(end);

        }

        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndElement("", "", "channel"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndElement("", "", "rss"));

        eventWriter.add(end);

        eventWriter.add(eventFactory.createEndDocument());

        eventWriter.close();
    }

    private static void createNode(XMLEventWriter eventWriter, String name,
            String value) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // Create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // Create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // Create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }
}
