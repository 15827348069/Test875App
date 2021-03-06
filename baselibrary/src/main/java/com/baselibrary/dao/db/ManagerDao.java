package com.baselibrary.dao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.baselibrary.pojo.Manager;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MANAGER".
*/
public class ManagerDao extends AbstractDao<Manager, Long> {

    public static final String TABLENAME = "MANAGER";

    /**
     * Properties of entity Manager.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property MId = new Property(0, Long.class, "mId", true, "_id");
        public final static Property Manage_pw = new Property(1, String.class, "manage_pw", false, "manage_pw");
    }


    public ManagerDao(DaoConfig config) {
        super(config);
    }
    
    public ManagerDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MANAGER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: mId
                "\"manage_pw\" TEXT);"); // 1: manage_pw
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MANAGER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Manager entity) {
        stmt.clearBindings();
 
        Long mId = entity.getMId();
        if (mId != null) {
            stmt.bindLong(1, mId);
        }
 
        String manage_pw = entity.getManage_pw();
        if (manage_pw != null) {
            stmt.bindString(2, manage_pw);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Manager entity) {
        stmt.clearBindings();
 
        Long mId = entity.getMId();
        if (mId != null) {
            stmt.bindLong(1, mId);
        }
 
        String manage_pw = entity.getManage_pw();
        if (manage_pw != null) {
            stmt.bindString(2, manage_pw);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Manager readEntity(Cursor cursor, int offset) {
        Manager entity = new Manager( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // mId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // manage_pw
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Manager entity, int offset) {
        entity.setMId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setManage_pw(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Manager entity, long rowId) {
        entity.setMId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Manager entity) {
        if(entity != null) {
            return entity.getMId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Manager entity) {
        return entity.getMId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
