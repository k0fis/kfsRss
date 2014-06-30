package kfs.rss.reader;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    public void testFeedParserIdnes() throws IOException {
        //kfsFeed feed = kfsFeedParser.readFeed("http://servis.idnes.cz/rss.asp");//"http://kofis.eu/rss.xml");
        kfsFeed feed = kfsFeedParser.readFeed(AppTest.class.getResource("/test2_rss.xml"));
        System.out.println(feed);
        if (feed != null) {
            for (kfsMessage message : feed.getMessages()) {
                System.out.println(message);

            }
        }
    }

    public void testFeedParserKofisEu() throws IOException {
        //kfsFeed feed = kfsFeedParser.readFeed("http://kofis.eu/rss.xml");//"http://kofis.eu/rss.xml");
        kfsFeed feed = kfsFeedParser.readFeed(AppTest.class.getResource("/test_rss.xml"));
        System.out.println(feed);
        if (feed != null) {
            for (kfsMessage message : feed.getMessages()) {
                System.out.println(message);

            }
        }
    }
}
