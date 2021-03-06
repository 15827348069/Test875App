package com.baselibrary.dao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.identityscope.IdentityScopeType;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * Master of DAO (schema version 6): knows all DAOs.
 */
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 6;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(Database db, boolean ifNotExists) {
        ArcFaceDao.createTable(db, ifNotExists);
        ArcFeMaleFaceDao.createTable(db, ifNotExists);
        ArcMaleFaceDao.createTable(db, ifNotExists);
        DepartmentDao.createTable(db, ifNotExists);
        FaceDao.createTable(db, ifNotExists);
        Finger3Dao.createTable(db, ifNotExists);
        Finger6Dao.createTable(db, ifNotExists);
        IdCardDao.createTable(db, ifNotExists);
        ManagerDao.createTable(db, ifNotExists);
        PwDao.createTable(db, ifNotExists);
        StudentDao.createTable(db, ifNotExists);
        UserDao.createTable(db, ifNotExists);
        TestIdCardDao.createTable(db, ifNotExists);
        TestPersonDao.createTable(db, ifNotExists);
    }

    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(Database db, boolean ifExists) {
        ArcFaceDao.dropTable(db, ifExists);
        ArcFeMaleFaceDao.dropTable(db, ifExists);
        ArcMaleFaceDao.dropTable(db, ifExists);
        DepartmentDao.dropTable(db, ifExists);
        FaceDao.dropTable(db, ifExists);
        Finger3Dao.dropTable(db, ifExists);
        Finger6Dao.dropTable(db, ifExists);
        IdCardDao.dropTable(db, ifExists);
        ManagerDao.dropTable(db, ifExists);
        PwDao.dropTable(db, ifExists);
        StudentDao.dropTable(db, ifExists);
        UserDao.dropTable(db, ifExists);
        TestIdCardDao.dropTable(db, ifExists);
        TestPersonDao.dropTable(db, ifExists);
    }

    /**
     * WARNING: Drops all table on Upgrade! Use only during development.
     * Convenience method using a {@link DevOpenHelper}.
     */
    public static DaoSession newDevSession(Context context, String name) {
        Database db = new DevOpenHelper(context, name).getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }

    public DaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }

    public DaoMaster(Database db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(ArcFaceDao.class);
        registerDaoClass(ArcFeMaleFaceDao.class);
        registerDaoClass(ArcMaleFaceDao.class);
        registerDaoClass(DepartmentDao.class);
        registerDaoClass(FaceDao.class);
        registerDaoClass(Finger3Dao.class);
        registerDaoClass(Finger6Dao.class);
        registerDaoClass(IdCardDao.class);
        registerDaoClass(ManagerDao.class);
        registerDaoClass(PwDao.class);
        registerDaoClass(StudentDao.class);
        registerDaoClass(UserDao.class);
        registerDaoClass(TestIdCardDao.class);
        registerDaoClass(TestPersonDao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

    /**
     * Calls {@link #createAllTables(Database, boolean)} in {@link #onCreate(Database)} -
     */
    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

}
