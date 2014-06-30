package kfs.rss.db;

import java.util.ArrayList;
import java.util.Arrays;
import kfs.kfsDbi.kfsDbObject;
import kfs.kfsDbi.kfsDbServerType;
import kfs.kfsDbi.kfsIntAutoInc;
import kfs.kfsDbi.kfsString;

/**
 *
 * @author pavedrim
 */
public class dbUser extends kfsDbObject {

    private final kfsIntAutoInc id;
    private final kfsString name;
    
    dbUser(kfsDbServerType st) {
        super(st, "T_KFS_USER");
        int pos = 0;
        id = new kfsIntAutoInc("ID", "UID", pos++);
        name = new kfsString("NAME", "Name", 128, pos++);
        super.setColumns(id, name);
    }
    
    @Override
    public String[] getCreateTableAddons() {
        ArrayList<String> ret = new ArrayList<String>(Arrays.asList(super.getCreateTableAddons()));
        ret.add("CREATE UNIQUE INDEX IU_"+getName()+"_1 ON " + getName()+" ( " + name.getColumnName()+")");
        return ret.toArray(new String[ret.size()]);
    }
    
}
