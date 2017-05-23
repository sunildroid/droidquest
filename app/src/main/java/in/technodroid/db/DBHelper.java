package in.technodroid.db;

/**
 * Created by IBM_ADMIN on 9/20/2015.
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import in.technodroid.model.QAModel;
import in.technodroid.model.UpdatesModel;
import in.technodroid.swap.Updates;

public class DBHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DBHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DROIDQAManager";

    // Table Names
    // private static final String TABLE_QA = "QATable";

    //private static final String TABLE_TODO_TAG = "todo_tags";
    private static DBHelper mInstance = null;
    Context context;

    public static DBHelper getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new DBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    //  column names for QA TABLE
    private interface QA_TABLE {
        public static final String TABLE_NAME = "QATable";

        public static final String KEY_ID = "id";
        public static final String KEY_QUESTION = "question";
        public static final String KEY_ANSWER = "answer";
        public static final String KEY_BY = "by";
        public static final String KEY_TOPIC = "topic";
        public static final String KEY_SUBTOPIC = "subtopic";
        public static final String KEY_RATING = "rating";
        public static final String KEY_ASKEDIN = "asked_in";
        public static final String KEY_URL = "img_url";
        public static final String KEY_LEVEL = "level";
        public static final String KEY_FAV = "isFAV";
    }

    // column names for updates
    private interface UPDATES_TABLE {
        public static final String KEY_ID = "id";
        public static final String TABLE_NAME = "UpdatesTable";
        public static final String KEY_TITLE = "title";
        public static final String KEY_DESC = "desc";
        public static final String KEY_CATEGORY = "category";
        public static final String KEY_IMGPATH = "imgpath";
        public static final String KEY_TAG = "tag";
        public static final String KEY_FAV = "isFAV";
    }


    // Table Create Statements
    // QA table create statement
    private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + QA_TABLE.TABLE_NAME + "("
             +QA_TABLE.KEY_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
            +QA_TABLE.KEY_QUESTION + " TEXT," +
            QA_TABLE.KEY_ANSWER + " TEXT," +
            QA_TABLE.KEY_ASKEDIN + " TEXT," +
            QA_TABLE.KEY_BY + " TEXT," +
            QA_TABLE.KEY_TOPIC + " TEXT," +
            QA_TABLE.KEY_SUBTOPIC + " TEXT," +
            QA_TABLE.KEY_URL + " TEXT," +
            QA_TABLE.KEY_LEVEL + " INTEGER," +
            QA_TABLE.KEY_FAV + " BOOL," +
            QA_TABLE.KEY_RATING + " REAL" + ")";

    // Updates table create statement
    private static final String CREATE_TABLE_UPDATES = "CREATE TABLE " + UPDATES_TABLE.TABLE_NAME
            + "(" + UPDATES_TABLE.KEY_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT," +
            UPDATES_TABLE.KEY_TITLE + " TEXT," +
            UPDATES_TABLE.KEY_CATEGORY + " TEXT," +
            UPDATES_TABLE.KEY_DESC + " TEXT," +
            UPDATES_TABLE.KEY_IMGPATH + " TEXT," +
            UPDATES_TABLE.KEY_TAG + " TEXT," +
            QA_TABLE.KEY_FAV + " BOOL" +
            ")";

    private DBHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_TODO);
        db.execSQL(CREATE_TABLE_UPDATES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + QA_TABLE.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UPDATES_TABLE.TABLE_NAME);
        // create new tables
        onCreate(db);
    }

    // ------------------------ "qa TABLE" table methods ----------------//

    /**
     * Creating a QA
     */
    public void insertQA(List<QAModel> qamodels) {
        for(QAModel qamodel:qamodels) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(QA_TABLE.KEY_QUESTION, qamodel.get_Question());
            values.put(QA_TABLE.KEY_ANSWER, qamodel.get_Answer());
            values.put(QA_TABLE.KEY_ASKEDIN, qamodel.get_AskedIn());
            values.put(QA_TABLE.KEY_BY, qamodel.get_By());
            //values.put(QA_TABLE.KEY_ID, qamodel.get_id());
            // values.put(QA_TABLE.KEY_LEVEL, 0);
            //  values.put(QA_TABLE.KEY_RATING, 0);
            values.put(QA_TABLE.KEY_SUBTOPIC, qamodel.get_SubTopic());
            values.put(QA_TABLE.KEY_TOPIC, qamodel.get_Topic());
            values.put(QA_TABLE.KEY_URL, qamodel.get_imgURL());
            values.put(QA_TABLE.KEY_FAV, qamodel.isFav() == true ? 1 : 0);
            db.insert(QA_TABLE.TABLE_NAME, null, values);
        }

    }

    public void insertQA(QAModel qamodel) {
        //for(QAModel qamodel:qamodels) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(QA_TABLE.KEY_QUESTION, qamodel.get_Question());
            values.put(QA_TABLE.KEY_ANSWER, qamodel.get_Answer());
            values.put(QA_TABLE.KEY_ASKEDIN, qamodel.get_AskedIn());
            values.put(QA_TABLE.KEY_BY, qamodel.get_By());
            //values.put(QA_TABLE.KEY_ID, qamodel.get_id());
            // values.put(QA_TABLE.KEY_LEVEL, 0);
            //  values.put(QA_TABLE.KEY_RATING, 0);
            values.put(QA_TABLE.KEY_SUBTOPIC, qamodel.get_SubTopic());
            values.put(QA_TABLE.KEY_TOPIC, qamodel.get_Topic());
            values.put(QA_TABLE.KEY_URL, qamodel.get_imgURL());
            values.put(QA_TABLE.KEY_FAV, qamodel.isFav() == true ? 1 : 0);
            db.insert(QA_TABLE.TABLE_NAME, null, values);
       // }

    }

    /**
     * get list of Favourite QA
     */
    public ArrayList<QAModel> getFavQA(boolean isFav) {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + QA_TABLE.TABLE_NAME + " WHERE "
                + QA_TABLE.KEY_FAV + " == " + 1;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<QAModel> arQA = new ArrayList<QAModel>();
        if (c.moveToFirst()) {
            do {
                QAModel td = new QAModel(
                        c.getString(c.getColumnIndex(QA_TABLE.KEY_QUESTION)),
                        c.getString(c.getColumnIndex(QA_TABLE.KEY_ANSWER)),
                        c.getString(c.getColumnIndex(QA_TABLE.KEY_URL)),
                        c.getString(c.getColumnIndex(QA_TABLE.KEY_BY))


                );
                td.set_id(c.getInt((c.getColumnIndex(QA_TABLE.KEY_ID))));
                //td.set_Level(c.getInt((c.getColumnIndex(QA_TABLE.KEY_LEVEL))));
                //td.set_rating(c.getFloat((c.getColumnIndex(QA_TABLE.KEY_RATING))));
                td.set_Topic((c.getString(c.getColumnIndex(QA_TABLE.KEY_TOPIC))));
                td.set_SubTopic((c.getString(c.getColumnIndex(QA_TABLE.KEY_SUBTOPIC))));
                td.set_AskedIn((c.getString(c.getColumnIndex(QA_TABLE.KEY_ASKEDIN))));
                td.setIsFav(c.getInt(c.getColumnIndex(QA_TABLE.KEY_FAV))==1 ?true:false);
                // adding to todo list
                arQA.add(td);
            } while (c.moveToNext());
        }
        return arQA;
    }

    /**
     * getting all QA List
     */
    public List<QAModel> getAllQA() {
        List<QAModel> arQA = new ArrayList<QAModel>();
        String selectQuery = "SELECT  * FROM " + QA_TABLE.TABLE_NAME;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                QAModel td = new QAModel(
                        c.getString(c.getColumnIndex(QA_TABLE.KEY_QUESTION)),
                        c.getString(c.getColumnIndex(QA_TABLE.KEY_ANSWER)),
                        c.getString(c.getColumnIndex(QA_TABLE.KEY_URL)),
                        c.getString(c.getColumnIndex(QA_TABLE.KEY_BY))
                );
                td.set_id(c.getInt((c.getColumnIndex(QA_TABLE.KEY_ID))));
                // td.set_Level(c.getInt((c.getColumnIndex(QA_TABLE.KEY_LEVEL))));
                td.set_rating(c.getInt((c.getColumnIndex(QA_TABLE.KEY_RATING))));
                td.set_Topic((c.getString(c.getColumnIndex(QA_TABLE.KEY_TOPIC))));
                td.set_SubTopic((c.getString(c.getColumnIndex(QA_TABLE.KEY_SUBTOPIC))));
                td.set_AskedIn((c.getString(c.getColumnIndex(QA_TABLE.KEY_ASKEDIN))));
                td.setIsFav(c.getInt((c.getColumnIndex(QA_TABLE.KEY_FAV)))== 1?true : false);
                // adding to todo list
                arQA.add(td);
            } while (c.moveToNext());
        }
        return arQA;
    }

    /**
     * getting QA count
     */
    public int getQACount() {
        String countQuery = "SELECT  * FROM " + QA_TABLE.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a QA item
     */
    public int updateQAItem(QAModel qamodel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QA_TABLE.KEY_QUESTION, qamodel.get_Question());
        values.put(QA_TABLE.KEY_ANSWER, qamodel.get_Answer());
        values.put(QA_TABLE.KEY_ASKEDIN, qamodel.get_AskedIn());
        values.put(QA_TABLE.KEY_BY, qamodel.get_By());
       values.put(QA_TABLE.KEY_ID, qamodel.get_id());//todo
        //values.put(QA_TABLE.KEY_LEVEL, qamodel.get_Level());
       // values.put(QA_TABLE.KEY_RATING, qamodel.get_rating());//todo
        values.put(QA_TABLE.KEY_SUBTOPIC, qamodel.get_SubTopic());
        values.put(QA_TABLE.KEY_TOPIC, qamodel.get_Topic());
        values.put(QA_TABLE.KEY_URL, qamodel.get_imgURL());
        values.put(QA_TABLE.KEY_FAV, qamodel.isFav()==true ?1:0);
        // updating row
        return db.update(QA_TABLE.TABLE_NAME, values, QA_TABLE.KEY_ID + " = ?",
                new String[]{String.valueOf(qamodel.get_id())});
    }

    /**
     * Deleting a QA item
     */
    public void deleteQA() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + QA_TABLE.TABLE_NAME);
    }


    //***************************** Methods for Updates Table*****************************************
    // ------------------------ "qa TABLE" table methods ----------------//

    /**
     *  Insert to Updates
     */
    public void insertUpdates(UpdatesModel uModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UPDATES_TABLE.KEY_CATEGORY, uModel.getCategory());
        values.put(UPDATES_TABLE.KEY_DESC, uModel.getDesc());
        values.put(UPDATES_TABLE.KEY_FAV, uModel.isFav());
       // values.put(UPDATES_TABLE.KEY_ID, uModel.getId());
        values.put(UPDATES_TABLE.KEY_TAG,uModel.getTag());
        values.put(UPDATES_TABLE.KEY_TITLE, uModel.getTitle());
        values.put(UPDATES_TABLE.KEY_IMGPATH, uModel.getImgPath());
        values.put(UPDATES_TABLE.KEY_FAV, uModel.isFav()==true ?1:0);
        db.insert(UPDATES_TABLE.TABLE_NAME, null, values);

    }

    /**
     *  Insert to Updates
     */
    public void insertUpdates(List<UpdatesModel> uModels) {
        for (UpdatesModel uModel : uModels) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(UPDATES_TABLE.KEY_CATEGORY, uModel.getCategory());
            values.put(UPDATES_TABLE.KEY_DESC, uModel.getDesc());
            values.put(UPDATES_TABLE.KEY_FAV, uModel.isFav());
            // values.put(UPDATES_TABLE.KEY_ID, uModel.getId());
            values.put(UPDATES_TABLE.KEY_TAG, uModel.getTag());
            values.put(UPDATES_TABLE.KEY_TITLE, uModel.getTitle());
            values.put(UPDATES_TABLE.KEY_IMGPATH, uModel.getImgPath());
            values.put(UPDATES_TABLE.KEY_FAV, uModel.isFav() == true ? 1 : 0);
            db.insert(UPDATES_TABLE.TABLE_NAME, null, values);
        }
    }



    /**
     * get list of Favourite Updates
     */
    public ArrayList<UpdatesModel> getFavUpdates(boolean isFav) {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + UPDATES_TABLE.TABLE_NAME + " WHERE "
                + UPDATES_TABLE.KEY_FAV + " = " + isFav;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<UpdatesModel> arQA = new ArrayList<UpdatesModel>();
        if (c.moveToFirst()) {
            do {
                UpdatesModel td = new UpdatesModel(
                        c.getString(c.getColumnIndex(UPDATES_TABLE.KEY_CATEGORY))  ,
                        c.getString(c.getColumnIndex(UPDATES_TABLE.KEY_TITLE)),
                        c.getString(c.getColumnIndex(UPDATES_TABLE.KEY_DESC)),
                        c.getString(c.getColumnIndex(UPDATES_TABLE.KEY_IMGPATH)),
                        c.getString(c.getColumnIndex(UPDATES_TABLE.KEY_TAG))
                );
                td.setId(c.getInt((c.getColumnIndex(UPDATES_TABLE.KEY_ID))));
                td.setIsFav(c.getInt((c.getColumnIndex(UPDATES_TABLE.KEY_FAV)))== 1?true : false);

                // adding to Updates list
                arQA.add(td);
            } while (c.moveToNext());
        }
        return arQA;
    }

    /**
     * getting all UPdates List
     * */
    public List<UpdatesModel> getAllUpdates() {
        List<QAModel> arQA = new ArrayList<QAModel>();
        String selectQuery = "SELECT  * FROM " + UPDATES_TABLE.TABLE_NAME;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<UpdatesModel> arUpdates = new ArrayList<UpdatesModel>();
        if (c.moveToFirst()) {
            do {
                UpdatesModel td = new UpdatesModel(
                        c.getString(c.getColumnIndex(UPDATES_TABLE.KEY_CATEGORY))  ,
                        c.getString(c.getColumnIndex(UPDATES_TABLE.KEY_TITLE)),
                        c.getString(c.getColumnIndex(UPDATES_TABLE.KEY_DESC)),
                        c.getString(c.getColumnIndex(UPDATES_TABLE.KEY_IMGPATH)),
                        c.getString(c.getColumnIndex(UPDATES_TABLE.KEY_TAG))
                );
                td.setId(c.getInt((c.getColumnIndex(UPDATES_TABLE.KEY_ID))));
                td.setIsFav(c.getInt((c.getColumnIndex(UPDATES_TABLE.KEY_FAV)))== 1?true : false);

                // adding to Updates list
                arUpdates.add(td);
            } while (c.moveToNext());
        }
        return arUpdates;
    }

    /**
     * getting QA count
     */
    public int getUpdatesCount() {
        String countQuery = "SELECT  * FROM " + UPDATES_TABLE.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a Updates item
     */
    public int updateTipItem(UpdatesModel uModel) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UPDATES_TABLE.KEY_IMGPATH, uModel.getImgPath());
        values.put(UPDATES_TABLE.KEY_TITLE, uModel.getTitle());
        values.put(UPDATES_TABLE.KEY_TAG, uModel.getTag());
        //values.put(UPDATES_TABLE.KEY_ID, uModel.getId());
        values.put(UPDATES_TABLE.KEY_DESC,uModel.getDesc());//todo
        values.put(UPDATES_TABLE.KEY_FAV, uModel.isFav());
        values.put(UPDATES_TABLE.KEY_CATEGORY, uModel.getCategory());
        // updating row
        return db.update(UPDATES_TABLE.TABLE_NAME, values, QA_TABLE.KEY_ID + " = ?",
                new String[] { String.valueOf(uModel.getId()) });
    }

    /**
     * Deleting a UPDATE item
     */
    public void deleteUpdates() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + UPDATES_TABLE.TABLE_NAME);
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
