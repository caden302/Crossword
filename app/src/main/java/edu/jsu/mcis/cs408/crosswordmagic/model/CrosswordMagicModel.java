package edu.jsu.mcis.cs408.crosswordmagic.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.model.dao.*;


public class CrosswordMagicModel extends AbstractModel {

    private final int DEFAULT_PUZZLE_ID = 1;
    private Puzzle puzzle;
    private PuzzleDAO puzzleDAO;
    private WordDAO wordDAO;
    private WebServiceDAO webServiceDAO;
    private Character[][] letters;
    private Integer[][] numbers;
    private Integer[] dimensions = new Integer[2];

    private int crossHeight, crossWidth;

    public CrosswordMagicModel(Context context){
        DAOFactory daoFactory = new DAOFactory(context);
        this.puzzleDAO = daoFactory.getPuzzleDAO();
        this.webServiceDAO = daoFactory.getWebServiceDAO();
        this.wordDAO = daoFactory.getWordDAO();
        this.puzzle = puzzleDAO.find(DEFAULT_PUZZLE_ID);
        Log.d("Name", puzzle.getName());
        Log.d("Letters", Arrays.deepToString(puzzle.getLetters()));
        Log.d("Numbers", Arrays.deepToString(puzzle.getNumbers()));
        Log.d("Dimensions", Integer.toString(puzzle.getHeight())  + "x" + Integer.toString(puzzle.getWidth()));
        setGridLetters(puzzle.getLetters());
        setGridNumbers(puzzle.getNumbers());
        setGridDimensions(puzzle.getHeight(), puzzle.getWidth());

    }
    public CrosswordMagicModel(Context context, int puzzleid){
        DAOFactory daoFactory = new DAOFactory(context);
        this.puzzleDAO = daoFactory.getPuzzleDAO();
        this.webServiceDAO = daoFactory.getWebServiceDAO();
        this.wordDAO = daoFactory.getWordDAO();
        this.puzzle = puzzleDAO.find(puzzleid);

        Log.d("Name", puzzle.getName());
        Log.d("Letters", Arrays.deepToString(puzzle.getLetters()));
        Log.d("Numbers", Arrays.deepToString(puzzle.getNumbers()));
        Log.d("Dimensions", Integer.toString(puzzle.getHeight())  + "x" + Integer.toString(puzzle.getWidth()));

        setGridLetters(puzzle.getLetters());
        setGridNumbers(puzzle.getNumbers());
        setGridDimensions(puzzle.getHeight(), puzzle.getWidth());

    }

    public void setGridLetters(Character[][] letters){
        this.letters = letters;
        firePropertyChange(CrosswordMagicController.GRID_LETTERS, null, letters);
    }

    public void setGridNumbers(Integer[][] numbers){
        this.numbers = numbers;
        firePropertyChange(CrosswordMagicController.GRID_NUMBERS, null, numbers);
    }

    public void setGridDimensions(int crossHeight, int crossWidth){
        this.crossHeight = crossHeight;
        this.crossWidth = crossWidth;
        dimensions[0] = this.crossHeight;
        dimensions[1] = this.crossWidth;
        firePropertyChange(CrosswordMagicController.GRID_DIMENSION, null, dimensions);
    }

    public String getCluesAcross(){
        return puzzle.getCluesAcross();
    }

    public String getCluesDown(){
        return puzzle.getCluesDown();
    }

    public Character[][] getGridLetters(){
        return this.letters;
    }

    public Integer[][] getGridNumbers(){
        return this.numbers;
    }

    public Integer[] getGridDimensions(){
        return this.dimensions;
    }

    public Character[][] checkWordIsCorrect(int boxNum, String guess){
        WordDirection direction = puzzle.checkGuess(boxNum, guess.toUpperCase());
        if (direction != null && !direction.toString().isBlank()) {
            puzzle.addWordToGuessed(Integer.toString(boxNum) + direction);
            Log.d("checkIfCOrrect", puzzle.getWord(Integer.toString(boxNum) + direction).toString());
            return puzzle.getLetters();
        }
        return null;
    }

    public PuzzleListItem[] getPuzzleList(){
        return puzzleDAO.list();
    }

    public ArrayList<String> puzzleNames(){
        PuzzleListItem[] puzzles = puzzleDAO.list();
        ArrayList<String> names = new ArrayList<>();
        for(PuzzleListItem puzzle : puzzles){
            names.add(puzzle.toString());
        }
        return names;
    }

    public int downloadPuzzles(int puzzleid){
        try {
            int puzzleID;
            JSONObject jsonObj = webServiceDAO.list(puzzleid);
            JSONArray jsonArray = jsonObj.getJSONArray("puzzle");
            HashMap <String,String> puzzleParams = new HashMap<>();
            String name = jsonObj.getString("name");
            String description = jsonObj.getString("description");
            String width = jsonObj.getString("width");
            String height = jsonObj.getString("height");

            puzzleParams.put("name", name);
            puzzleParams.put("description", description);
            puzzleParams.put("width", width);
            puzzleParams.put("height", height);

            puzzle = new Puzzle(puzzleParams);
            puzzleID = puzzleDAO.create(puzzle);

            for(int i = 0; i < jsonArray.length(); i++){
                HashMap<String,String> wordParams = new HashMap<>();
                String jsonString = jsonArray.getString(i);
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String clue = jsonObject.getString("clue");
                String column = jsonObject.getString("column");
                String box = jsonObject.getString("box");
                String row = jsonObject.getString("row");
                String word = jsonObject.getString("word");
                String direction = jsonObject.getString("direction");

                wordParams.put("_id", Integer.toString(i));
                wordParams.put("puzzleid", Integer.toString(puzzleID));
                wordParams.put("clue", clue);
                wordParams.put("column", column);
                wordParams.put("box", box);
                wordParams.put("row", row);
                wordParams.put("word", word);
                wordParams.put("direction", direction);

                Word w = new Word(wordParams);
                wordDAO.create(w);
            }
            puzzle = puzzleDAO.find(puzzleID);
            Log.d("Testing", Integer.toString(puzzleID));
        } catch (Exception e){
            Log.d("Exception", e.toString());
        }
        return puzzleid;
    }

}