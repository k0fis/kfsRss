package kfs.rss.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import kfs.kfsDbi.kfsADb;
import kfs.kfsDbi.kfsDbServerType;
import kfs.kfsDbi.kfsRowData;

/**
 *
 * @author pavedrim
 */
public class kfsDb extends kfsADb {

    private final dbFeed feed;
    private final dbMsg msg;
    private final dbUser user;
    private final dbUserFeed ufeed;
    private final dbUserMess umsg;

    public kfsDb(Connection con, String schema) throws kfsExRssDb {
        this(kfsDbServerType.kfsDbiPostgre, con, schema);
    }

    private kfsDb(kfsDbServerType st, Connection con, String schema) throws kfsExRssDb {
        super(schema, st, con);
        feed = new dbFeed(st);
        msg = new dbMsg(st);
        user = new dbUser(st);
        ufeed = new dbUserFeed(st);
        umsg = new dbUserMess(st);

        setDboObjects(feed, msg, user, ufeed, umsg);
        try {
            createTables();
        } catch (SQLException ex) {
            throw new kfsExRssDb("Cannot create tables", ex);
        }
    }

    static String readResource(String resName) throws kfsExRssDb {
        InputStream is = kfsDb.class.getResourceAsStream(resName);
        if (is == null) {
            return null;
        }
        StringBuilder ret = new StringBuilder();
        try {
            while (is.available() > 0) {
                ret.appendCodePoint(is.read());
            }
        } catch (IOException ex) {
            throw new kfsExRssDb("Cannot read file form resources: " + resName, ex);
        }
        return ret.toString();
    }

    public void done() throws kfsExRssDb {
        try {
            super.done(true, true);
        } catch (SQLException ex) {
            throw new kfsExRssDb("Error in close DB", ex);
        }
    }

    //feed
    public dbFeed.pojo getFeedByUrl(String url) throws kfsExRssDb {
        ArrayList<kfsRowData> ret = new ArrayList<kfsRowData>();
        try {
            PreparedStatement ps = prepare(feed.sqlFeedByUrl());
            feed.psFeedByUrl(ps, url);
            super.loadCust(ps, ret, feed);
        } catch (SQLException ex) {
            throw new kfsExRssDb("Cannot load feed by url: " + url, ex);
        }
        if (ret.size() <= 0) { // not configured db db
            kfsRowData r = new kfsRowData(feed);
            feed.setUrl(url, r);
            try {
                super.insert(feed, r);
            } catch (SQLException ex) {
                throw new kfsExRssDb("Cannot insert data", ex);
            }
            ret.add(r);
        }
        return feed.getPojo(ret.get(0));
    }

    public int feedUpdate(dbFeed.pojo pj) throws kfsExRssDb {
        try {
            return super.update(feed, pj.kfsGetRow());
        } catch (SQLException ex) {
            throw new kfsExRssDb("Cannot update data", ex);
        }
    }

    public int feedSetRefreshed(dbFeed.pojo pj) throws kfsExRssDb {
        pj.nowRefreshedDate();
        try {
            return super.update(feed, pj.kfsGetRow());
        } catch (SQLException ex) {
            throw new kfsExRssDb("Cannot update data", ex);
        }
    }

    //feed
    public dbMsg.pojo getMsgByGuid(int fid, String guid) throws kfsExRssDb {
        ArrayList<kfsRowData> ret = new ArrayList<kfsRowData>();
        try {
            PreparedStatement ps = prepare(msg.sqlMsgByGuid());
            msg.psMsgByguid(ps, fid, guid);
            super.loadCust(ps, ret, msg);
        } catch (SQLException ex) {
            throw new kfsExRssDb("Cannot load msg by guid: " + guid + ", fid: " + fid, ex);
        }
        if (ret.size() <= 0) { // not configured db db
            kfsRowData r = new kfsRowData(feed);
            msg.setGuid(guid, fid, r);
            try {
                super.insert(msg, r);
            } catch (SQLException ex) {
                throw new kfsExRssDb("Cannot insert data", ex);
            }
            ret.add(r);
        }
        return msg.getPojo(ret.get(0));
    }

    public int msgUpdate(dbMsg.pojo pj) throws kfsExRssDb {
        try {
            return super.update(msg, pj.kfsGetRow());
        } catch (SQLException ex) {
            throw new kfsExRssDb("Cannot update data", ex);
        }
    }
}
