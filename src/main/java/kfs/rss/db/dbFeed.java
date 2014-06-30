package kfs.rss.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import kfs.kfsDbi.*;

/**
 *
 * @author pavedrim
 */
public class dbFeed extends kfsDbObject {

    public kfsLongAutoInc id;
    public kfsString title;
    public kfsString url;
    public kfsString urlImg;
    public kfsString desc;
    public kfsDate refreshDate;

    dbFeed(kfsDbServerType st) {
        super(st, "T_KFS_FEED");
        int pos = 0;
        id = new kfsLongAutoInc("C_ID", pos++);
        url = new kfsString("C_URL", "URL", 2048, pos++);
        urlImg = new kfsString("C_URL_IMG", "URL Logo", 2048, pos++);
        desc = new kfsString("C_DESC", "Description", 2048, pos++);
        title = new kfsString("C_TITLE", "Title", 32, pos++);
        refreshDate = new kfsDate("REFRESH_DATE", "Refresh Date", pos++);

        super.setColumns(id, url, urlImg, desc, title, refreshDate);
        super.setIdsColumns(id);
        super.setUpdateColumns(refreshDate);
    }

    void setUrl(String pUrl, kfsRowData rd) {
        url.setData(pUrl, rd);
    }

    public String sqlFeedByUrl() {
        if (serverType == kfsDbServerType.kfsDbiPostgre) {
            return getSelect("get_feed(?)", getColumns(), null, false);
        } else {
            return getSelect(getName(), getColumns(), url);
        }
    }

    public void psFeedByUrl(PreparedStatement ps, String pUrl) throws SQLException {
        ps.setString(1, pUrl);
    }

    @Override
    public String[] getCreateTableAddons() {
        ArrayList<String> ret = new ArrayList<String>(Arrays.asList(super.getCreateTableAddons()));
        ret.add("CREATE UNIQUE INDEX IU_" + getName() + "_1 ON " + getName() + " ( " + url.getColumnName() + ")");
        if (serverType == kfsDbServerType.kfsDbiPostgre) {
            String s;
            try {
                s = kfsDb.readResource("/sql/pg_feed.sql");
            } catch (kfsExRssDb ex) {
                s = null;
            }
            if ((s != null) && (s.length() > 0)) {
                ret.add(s);
            }
        }
        return ret.toArray(new String[ret.size()]);
    }

    @Override
    public pojo getPojo(kfsRowData row) {
        return new pojo(row);
    }

    public class pojo extends kfsPojoObj<dbFeed> {

        pojo(kfsRowData rd) {
            super(dbFeed.this, rd);
        }

        public long getId() {
            return inx.id.getData(rd);
        }

        public String getUrl() {
            return inx.url.getData(rd);
        }

        public String getUrlImage() {
            return inx.urlImg.getData(rd);
        }

        public String getDescription() {
            return inx.desc.getData(rd);
        }

        public String getTitle() {
            return inx.title.getData(rd);
        }

        public Date getRefreshedDate() {
            return inx.refreshDate.getData(rd);
        }

        public void setNowRefreshedDate() {
            inx.refreshDate.setData(new Date(), rd);
        }
    }
}
