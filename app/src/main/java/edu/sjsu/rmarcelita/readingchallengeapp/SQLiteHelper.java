package edu.sjsu.rmarcelita.readingchallengeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by riri on 11/3/17.
 */

public class SQLiteHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "readingChallenge";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createUsersInfoTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + createUsersInfoTable());
        onCreate(sqLiteDatabase);
    }

    public String createUsersInfoTable() {
        final String TABLE_NAME = "usersInfo";
        final String NAME = "name";
        final String USERNAME = "username";
        final String EMAIL = "email";

        final String CREATE_USERS_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                NAME + " TEXT, " +
                USERNAME + " TEXT unique, " +
                EMAIL + " TEXT unique);";

        return CREATE_USERS_INFO_TABLE;
    }

    public void insertUsersInfoTable(String name, String username, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("username", username);
        cv.put("email", email);
        db.insert("usersInfo", null, cv);
        db.close();
    }

    public void createUsersChallengeTable() {
        final String TABLE_NAME = "usersChallenge";
        final String USERNAME = "username";
        final String BOOKS = "books";
        final String PAGES_READ = "pagesRead";
        final String COMPLETED = "completed";

        final String CREATE_USERS_CHALLENGE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                USERNAME + " TEXT unique, " +
                BOOKS + " TEXT, " +
                PAGES_READ + " INTEGER, " +
                COMPLETED + " INTEGER);";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_USERS_CHALLENGE_TABLE);
        db.close();
    }

    public void insertUsersChallengeTable(String username, String books, int pagesRead,
                                          int completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("books", books);
        cv.put("pagesRead", pagesRead);
        cv.put("completed", completed);
        db.insert("usersChallenge", null, cv);
        db.close();
    }

    public void updateUsersChallengeTable(String username, String books, int pagesRead,
                                          int goal, int completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE usersChallenge SET username='" + username + "', books='" +
                books + "', pagesRead=" + pagesRead + ", completed=" + completed + ";";
        db.execSQL(query);
        db.close();
    }

    public void createBooksInfoTable() {
        final String TABLE_NAME = "booksInfo";
        final String TITLE = "title";
        final String AUTHOR = "author";
        final String GENRE = "genre";
        final String SYNOPSIS = "synopsis";
        final String PAGES = "pages";
        final String COVER = "cover";
        final String STARS = "stars";

        final String CREATE_BOOKS_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                TITLE + " TEXT unique, " +
                AUTHOR + " TEXT, " +
                GENRE + " TEXT, " +
                SYNOPSIS + " TEXT, " +
                PAGES + " INTEGER, " +
                COVER + " TEXT, " +
                STARS + " DOUBLE);";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_BOOKS_INFO_TABLE);
        db.close();
    }

    public void insertBooksInfoTable(String title, String author, String genre, String synopsis,
                                     int pages, String cover, double stars) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("author", author);
        cv.put("genre", genre);
        cv.put("synopsis", synopsis);
        cv.put("pages", pages);
        cv.put("cover", cover);
        cv.put("stars", stars);
        db.insert("booksInfo", null, cv);
        db.close();
    }

    public void updateSynopsis(String title, String synopsis) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE booksInfo SET synopsis='" + synopsis + "' WHERE title='" + title + "';";
        db.execSQL(query);
        db.close();
    }

    public Books getRandomBook(int id) {
        Books book;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM booksInfo" +
                " WHERE ID =" + id + ";", null);
        res.moveToFirst();
        String title = res.getString(res.getColumnIndex("title"));
        String author = res.getString(res.getColumnIndex("author"));
        String bookGenre = res.getString(res.getColumnIndex("genre"));
        String bookCover = res.getString(res.getColumnIndex("cover"));
        String bookSynopsis = res.getString(res.getColumnIndex("synopsis"));
        int pages = res.getInt(res.getColumnIndex("pages"));
        double stars = res.getDouble(res.getColumnIndex("stars"));
        book = new Books(title, author, bookGenre, bookSynopsis, bookCover, pages, stars);
        db.close();
        return book;
    }

    public Books getBookByTitle(String tableName, String title) {
        Books book;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + tableName +
                " WHERE title = '" + title + "';", null);
        res.moveToFirst();
        String bookTitle = res.getString(res.getColumnIndex("title"));
        String author = res.getString(res.getColumnIndex("author"));
        String bookGenre = res.getString(res.getColumnIndex("genre"));
        String bookCover = res.getString(res.getColumnIndex("cover"));
        String bookSynopsis = res.getString(res.getColumnIndex("synopsis"));
        int pages = res.getInt(res.getColumnIndex("pages"));
        double stars = res.getDouble(res.getColumnIndex("stars"));
        book = new Books(bookTitle, author, bookGenre, bookSynopsis, bookCover, pages, stars);
        db.close();
        return book;
    }

    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM usersInfo";
        db.execSQL(query);
        db.close();
    }

    public String getUserInfo(String infoToGet) {
        SQLiteDatabase db = this.getReadableDatabase();
        String info = "";
        Cursor res = db.rawQuery("SELECT " + infoToGet + " FROM usersInfo;", null);
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            info = res.getString(0);
            res.moveToNext();
        }
        db.close();
        return info;
    }

    public void createCurrentBooksTable() {
        final String TABLE_NAME = "currentBooks";
        final String TITLE = "title";
        final String PAGES = "pages";
        final String COVER = "cover";

        final String CREATE_CURRENT_BOOKS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                TITLE + " TEXT unique, " +
                PAGES + " INTEGER, " +
                COVER + " TEXT);";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_CURRENT_BOOKS_TABLE);
        db.close();
    }

    public void insertCurrentBooksTable(String title, String pages, String cover) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("pages", pages);
        cv.put("cover", cover);
        db.insert("currentBooks", null, cv);
        db.close();
    }

    public void createChallengeTable() {
        final String TABLE_NAME = "challenges";
        final String BOOK_ONE = "bookOne";
        final String BOOK_TWO = "bookTwo";
        final String BOOK_THREE = "bookThree";
        final String MONEY_ONE = "moneyOne";
        final String MONEY_TWO = "moneyTwo";
        final String MONEY_THREE = "moneyThree";

        final String CREATE_CHALLENGE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                BOOK_ONE + " INTEGER, " +
                BOOK_TWO + " INTEGER, " +
                BOOK_THREE + " INTEGER, " +
                MONEY_ONE + " REAL, " +
                MONEY_TWO + " REAL, " +
                MONEY_THREE + " REAL);";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_CHALLENGE_TABLE);
        db.close();
    }

    public void insertChallengeTable(int bookOne, int bookTwo, int bookThree, double moneyOne,
                                     double moneyTwo, double moneyThree) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("bookOne", bookOne);
        cv.put("bookTwo", bookTwo);
        cv.put("bookThree", bookThree);
        cv.put("moneyOne", moneyOne);
        cv.put("moneyTwo", moneyTwo);
        cv.put("moneyThree", moneyThree);
        db.insert("challenges", null, cv);
        db.close();
    }

    public ArrayList<Integer> getChallengesBooksInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> info = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM challenges WHERE ID = 1;", null);
        res.moveToFirst();
        info.add(res.getInt(res.getColumnIndex("bookOne")));
        info.add(res.getInt(res.getColumnIndex("bookTwo")));
        info.add(res.getInt(res.getColumnIndex("bookThree")));
        db.close();
        return info;
    }

    public ArrayList<Double> getChallengesMoneyInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Double> info = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM challenges WHERE ID = 1;", null);
        res.moveToFirst();
        info.add(res.getDouble(res.getColumnIndex("moneyOne")));
        info.add(res.getDouble(res.getColumnIndex("moneyTwo")));
        info.add(res.getDouble(res.getColumnIndex("moneyThree")));
        db.close();
        return info;
    }

    public void updateChallengeInfo(int bookOne, int bookTwo, int bookThree,
                                    double moneyOne, double moneyTwo, double moneyThree) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE challenges SET bookOne =" + bookOne + ", bookTwo =" + bookTwo +
                ", bookThree =" + bookThree + ", moneyOne = " + moneyOne + ", moneyTwo = " + moneyTwo +
                ", moneyThree = " + moneyThree + ";";
        db.execSQL(query);
        db.close();
    }

    public void createReadBooksTable() {
        final String TABLE_NAME = "readBooks";
        final String TITLE = "title";
        final String AUTHOR = "author";
        final String GENRE = "genre";
        final String SYNOPSIS = "synopsis";
        final String PAGES = "pages";
        final String COVER = "cover";
        final String STARS = "stars";

        final String CREATE_BOOKS_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                TITLE + " TEXT unique, " +
                AUTHOR + " TEXT, " +
                GENRE + " TEXT, " +
                SYNOPSIS + " TEXT, " +
                PAGES + " INTEGER, " +
                COVER + " TEXT, " +
                STARS + " DOUBLE);";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_BOOKS_INFO_TABLE);
        db.close();
    }

    public void insertReadBooksTable(String title, String author, String genre, String synopsis,
                                     int pages, String cover, double stars) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("author", author);
        cv.put("genre", genre);
        cv.put("synopsis", synopsis);
        cv.put("pages", pages);
        cv.put("cover", cover);
        cv.put("stars", stars);
        db.insert("readBooks", null, cv);
        db.close();
    }

    public void updateReadBooksSynopsis(String title, String synopsis) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE readBooks SET synopsis='" + synopsis + "' WHERE title='" + title + "';";
        db.execSQL(query);
        db.close();
    }

    public ArrayList<Books> getAllReadBooks() {
        ArrayList<Books> readBooksList = new ArrayList<>();
        Books book;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM readBooks;", null);
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            String title = res.getString(res.getColumnIndex("title"));
            String author = res.getString(res.getColumnIndex("author"));
            String bookGenre = res.getString(res.getColumnIndex("genre"));
            String bookCover = res.getString(res.getColumnIndex("cover"));
            String bookSynopsis = res.getString(res.getColumnIndex("synopsis"));
            int pages = res.getInt(res.getColumnIndex("pages"));
            double stars = res.getDouble(res.getColumnIndex("stars"));
            book = new Books(title, author, bookGenre, bookSynopsis, bookCover, pages, stars);
            readBooksList.add(book);
            res.moveToNext();
        }
        db.close();
        return readBooksList;
    }

    public boolean checkEmptyDatabase(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + tableName, null);
        boolean dbEmpty;

        if (mCursor.moveToFirst())
        {
            dbEmpty = false;

        } else
        {
            dbEmpty = true;
        }
        return dbEmpty;
    }
}
