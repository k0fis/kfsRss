package kfs.rss.db;

import java.util.ArrayList;
import java.util.Arrays;
import kfs.kfsDbi.*;

/**
 *
 * @author pavedrim
 */
public class dbUserFeed extends kfsDbObject {

    private final kfsIntAutoInc id;
    private final kfsInt uid;
    private final kfsInt fid;
    private final kfsString category;
    private final kfsInt sortPos;
    
    public dbUserFeed(kfsDbServerType st) {
        super(st, "T_KFS_USER_FEED");
        int pos = 0;
        id = new kfsIntAutoInc("ID", "ID", pos++);
        uid = new kfsInt("UID", "UID", kfsIntAutoInc.idMaxLen, pos++, false);
        fid = new kfsInt("FID", "FID", kfsIntAutoInc.idMaxLen, pos++, false);
        category = new kfsString("CATEGORY", "Category", 128, pos++);
        sortPos = new kfsInt("SORTING_POS", "Sort pos", 4, pos++, false);
        super.setColumns(id, uid, fid, category, sortPos);
    }

        @Override
    public String[] getCreateTableAddons() {
        ArrayList<String> ret = new ArrayList<String>(Arrays.asList(super.getCreateTableAddons()));
        ret.add("CREATE UNIQUE INDEX IU_"+getName()+"_1 ON " + getName()+" ( " + uid.getColumnName()+", "+fid.getColumnName()+")");
        return ret.toArray(new String[ret.size()]);
    }

    
}
