package kfs.rss.db;

import kfs.kfsDbi.*;

/**
 *
 * @author pavedrim
 */
public class dbUserMess extends kfsDbObject{

    // id, fid, uid, readed
    private final kfsIntAutoInc id;
    private final kfsInt fid;
    private final kfsInt uid;
    private final kfsDate readed;
    
    dbUserMess(kfsDbServerType st) {
        super(st, "T_KFS_USER_MSG");
        int pos = 0;
        id = new kfsIntAutoInc("ID", "Id", pos++);
        fid = new kfsInt("FID", "FID", kfsIntAutoInc.idMaxLen, pos++, false);
        uid = new kfsInt("UID", "UID", kfsIntAutoInc.idMaxLen, pos++, false);
        readed = new kfsDate("READED", "READED", pos++);
        super.setColumns(id, fid, uid, readed);
    }
}
