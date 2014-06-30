package kfs.rss.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import kfs.kfsDbi.*;

/**
 *
 * @author pavedrim
 */
public class dbMsg extends kfsDbObject {

    // id, Fid
    private final kfsLongAutoInc id;
    private final kfsInt fid;
    private final kfsString title;
    private final kfsString description;
    private final kfsString link;
    private final kfsString guid;
    private final kfsDate importDate;

    dbMsg(kfsDbServerType st) {
        super(st, "T_KFS_MSG");
        int pos = 0;
        id = new kfsLongAutoInc("ID", pos++);
        fid = new kfsInt("FID", "FID", kfsIntAutoInc.idMaxLen, pos++, false);
        title = new kfsString("C_TITLE", "Title", 2048, pos++);
        description = new kfsString("C_DESC", "Description", 2048, pos++);
        link = new kfsString("C_LINK", "Link", 2048, pos++);
        guid = new kfsString("G_GUID", "Guid", 2048, pos++);
        importDate = new kfsDate("IMPORT_DATE", "Import Date", pos++);

        setColumns(id, fid, title, description, link, guid, importDate);
    }
    
    public void setGuid(String p_guid, int p_fid, kfsRowData rd){
        guid.setData(p_guid, rd);
        fid.setData(p_fid, rd);
    }
    
    public String sqlMsgByGuid() {
        if (serverType == kfsDbServerType.kfsDbiPostgre) {
            return getSelect("get_msg(?, ?)", getColumns(), null, false);
        } else {
            return getSelect(getName(), getColumns(), fid, guid);
        }
    }
    
    public void psMsgByguid(PreparedStatement ps, int fid, String guid) throws SQLException {
        ps.setInt(1, fid);
        ps.setString(2, guid);
    }
    
    @Override
    public String[] getCreateTableAddons() {
        ArrayList<String> ret = new ArrayList<String>(Arrays.asList(super.getCreateTableAddons()));
        ret.add("CREATE UNIQUE INDEX IU_" + getName() + "_1 ON " + getName() + " ( " + fid.getColumnName() + ", " + guid.getColumnName() + ")");
        if (serverType == kfsDbServerType.kfsDbiPostgre) {
            String s;
            try {
                s = kfsDb.readResource("/sql/pg_msg.sql");
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

    public class pojo extends kfsPojoObj<dbMsg> {

        pojo(kfsRowData rd) {
            super(dbMsg.this, rd);
        }
    }
}
