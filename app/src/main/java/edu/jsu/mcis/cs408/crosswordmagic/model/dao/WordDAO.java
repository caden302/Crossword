package edu.jsu.mcis.cs408.crosswordmagic.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import edu.jsu.mcis.cs408.crosswordmagic.model.Puzzle;
import edu.jsu.mcis.cs408.crosswordmagic.model.Word;

public class WordDAO {

    private final DAOFactory daoFactory;

    WordDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /* add a new word entry to the database */

    public int create(Word newWord) {

        /* use this method if there is NOT already a SQLiteDatabase open */

        SQLiteDatabase db = daoFactory.getWritableDatabase();
        int result = create(db, newWord);
        db.close();
        return result;

    }

    public int create(SQLiteDatabase db, Word newWord) {

        /* use this method if there IS already a SQLiteDatabase open */

        int key = 0;

        String puzzleid = daoFactory.getProperty("sql_field_puzzleid");
        String row = daoFactory.getProperty("sql_field_row");
        String column = daoFactory.getProperty("sql_field_column");
        String box = daoFactory.getProperty("sql_field_box");
        String direction = daoFactory.getProperty("sql_field_direction");
        String word = daoFactory.getProperty("sql_field_word");
        String clue = daoFactory.getProperty("sql_field_clue");

        ContentValues values = new ContentValues();
        values.put(puzzleid, newWord.getPuzzleid());
        values.put(row, newWord.getRow());
        values.put(column, newWord.getColumn());
        values.put(box, newWord.getBox());
        values.put(direction, newWord.getDirection().ordinal());
        values.put(word, newWord.getWord());
        values.put(clue, newWord.getClue());

        key = (int) db.insert(daoFactory.getProperty("sql_table_words"), null, values);

        return key;

    }

    /* return a list of existing word entries from the database */

    public ArrayList<Word> list(int puzzleid) {

        /* use this method if there is NOT already a SQLiteDatabase open */

        SQLiteDatabase db = daoFactory.getWritableDatabase();
        ArrayList<Word> result = list(db, puzzleid);
        db.close();
        return result;

    }

    public ArrayList<Word> list(SQLiteDatabase db, int puzzleid) {

        ArrayList<Word> result = new ArrayList<>();

        String query = daoFactory.getProperty("sql_get_words");
        Cursor cursor = db.rawQuery(query, new String[]{ String.valueOf(puzzleid) });

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            do {

                HashMap<String, String> params = new HashMap<>();

                /* get data for the next word in the puzzle */

                query = daoFactory.getProperty("sql_get_words");
                cursor = db.rawQuery(query, new String[]{ String.valueOf(puzzleid) });

                if (cursor.moveToFirst()) {

                    do {

                        int wordid = cursor.getInt(0);

                        result.add(find(db, wordid));
                    }
                    while ( cursor.moveToNext() );

                    cursor.close();

                }

            }
            while ( cursor.moveToNext() );

            cursor.close();

        }

        return result;

    }

    /* return an individual word entry from the database */

    public Word find(int id) {

        /* use this method if there is NOT already a SQLiteDatabase open */

        SQLiteDatabase db = daoFactory.getWritableDatabase();
        Word result = find(db, id);
        db.close();
        return result;

    }

    public Word find(SQLiteDatabase db, int id) {

        /* use this method if there IS already a SQLiteDatabase open */

        Word word = null;

        String query = daoFactory.getProperty("sql_get_word");
        Cursor cursor = db.rawQuery(query, new String[]{ String.valueOf(id) });

        if (cursor.moveToFirst()) {

            do {

                HashMap<String, String> params = new HashMap<>();

                cursor.moveToFirst();

                String _id = cursor.getString(0);
                String puzzleid = cursor.getString(1);
                String rowNum = cursor.getString(2);
                String colNum = cursor.getString(3);
                String boxNum = cursor.getString(4);
                String direction = cursor.getString(5);
                String correctWord = cursor.getString(6);
                String hint = cursor.getString(7);

                params.put("_id", _id);
                params.put("puzzleid", puzzleid);
                params.put("row", rowNum);
                params.put("column", colNum);
                params.put("box", boxNum);
                params.put("direction", direction);
                params.put("word", correctWord);
                params.put("clue", hint);

                word = new Word(params);

            }
            while (cursor.moveToNext());

            cursor.close();

        }

        return word;

    }

}