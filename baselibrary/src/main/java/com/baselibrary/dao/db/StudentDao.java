package com.baselibrary.dao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.baselibrary.pojo.Student;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "STUDENT".
*/
public class StudentDao extends AbstractDao<Student, Long> {

    public static final String TABLENAME = "STUDENT";

    /**
     * Properties of entity Student.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property SId = new Property(0, Long.class, "sId", true, "_id");
        public final static Property StudentNo = new Property(1, int.class, "studentNo", false, "STUDENT_NO");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Address = new Property(3, String.class, "address", false, "ADDRESS");
    }


    public StudentDao(DaoConfig config) {
        super(config);
    }
    
    public StudentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"STUDENT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: sId
                "\"STUDENT_NO\" INTEGER NOT NULL UNIQUE ," + // 1: studentNo
                "\"NAME\" TEXT," + // 2: name
                "\"ADDRESS\" TEXT);"); // 3: address
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STUDENT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Student entity) {
        stmt.clearBindings();
 
        Long sId = entity.getSId();
        if (sId != null) {
            stmt.bindLong(1, sId);
        }
        stmt.bindLong(2, entity.getStudentNo());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(4, address);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Student entity) {
        stmt.clearBindings();
 
        Long sId = entity.getSId();
        if (sId != null) {
            stmt.bindLong(1, sId);
        }
        stmt.bindLong(2, entity.getStudentNo());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(4, address);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Student readEntity(Cursor cursor, int offset) {
        Student entity = new Student( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // sId
            cursor.getInt(offset + 1), // studentNo
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // address
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Student entity, int offset) {
        entity.setSId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStudentNo(cursor.getInt(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAddress(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Student entity, long rowId) {
        entity.setSId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Student entity) {
        if(entity != null) {
            return entity.getSId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Student entity) {
        return entity.getSId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
