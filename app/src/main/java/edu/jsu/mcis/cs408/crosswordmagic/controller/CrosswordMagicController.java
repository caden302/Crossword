package edu.jsu.mcis.cs408.crosswordmagic.controller;

import android.content.Context;
import android.util.Log;

import edu.jsu.mcis.cs408.crosswordmagic.model.CrosswordMagicModel;
import edu.jsu.mcis.cs408.crosswordmagic.model.PuzzleListItem;
import edu.jsu.mcis.cs408.crosswordmagic.view.AbstractView;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrosswordMagicController extends AbstractController {

    public static final String GRID_LETTERS = "gridLetters";
    public static final String GRID_NUMBERS = "gridNumbers";
    public static final String GRID_DIMENSION = "gridDimension";
    public static final String CLUES_ACROSS = "acrossClues";
    public static final String CLUES_DOWN = "downClues";
    public static final String ANSWER_CORRECT = "solved";
    public static final String PUZZLE_LIST_PROPERTY = "puzzleList";
    public static final String PUZZLE_DOWNLOADED_PROPERTY = "puzzleDownloaded";


    private CrosswordMagicModel model;
    private List<AbstractView> views;

    public CrosswordMagicController(Context context) {
        this.model = new CrosswordMagicModel(context);
        this.views = new ArrayList<>();
    }

    public CrosswordMagicController(Context context, int puzzleid) {
        this.model = new CrosswordMagicModel(context, puzzleid);
        this.views = new ArrayList<>();
    }

    public void addView(AbstractView view) {
        views.add(view);
    }

    private void firePropertyChange(String propertyName, Object oldValue, Object newValue){
        for (AbstractView view : views) {
            view.modelPropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
        }
    }

    public void getGridDimensions(){
        Integer[] dimensions = model.getGridDimensions();
        firePropertyChange(GRID_DIMENSION, null, dimensions);
    }

    public Character[][] getGridLetters(){
        Character[][] letters = model.getGridLetters();
        firePropertyChange(GRID_LETTERS, null, letters);
        return letters;
    }

    public void getGridNumbers(){
        Integer[][] numbers = model.getGridNumbers();
        firePropertyChange(GRID_NUMBERS, null, numbers);
    }

    public void getClues() {
        String across = model.getCluesAcross();
        String down = model.getCluesDown();

        List<String> acrossList = List.of(across.split(System.lineSeparator()));
        List<String> downList = List.of(down.split(System.lineSeparator()));

        firePropertyChange(CLUES_ACROSS, null, acrossList);
        firePropertyChange(CLUES_DOWN, null, downList);
    }

    public void makeGuess(int boxIndex, String guess) {
        Character[][] updatedWord = model.checkWordIsCorrect(boxIndex, guess);
        firePropertyChange(ANSWER_CORRECT, null, updatedWord);
    }

    public void getPuzzleList(){
        PuzzleListItem[] puzzleArray = model.getPuzzleList();
        Log.d("PuzzleList listing", puzzleArray[0].toString());
        firePropertyChange(PUZZLE_LIST_PROPERTY, null, puzzleArray);
    }

    public int callDownload(int puzzleid){
        int databaseID = model.downloadPuzzles(puzzleid);
        firePropertyChange(PUZZLE_DOWNLOADED_PROPERTY, null, databaseID);
        return databaseID;
    }
}
