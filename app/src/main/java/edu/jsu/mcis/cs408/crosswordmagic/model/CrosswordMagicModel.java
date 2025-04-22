package edu.jsu.mcis.cs408.crosswordmagic.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import edu.jsu.mcis.cs408.crosswordmagic.controller.CrosswordMagicController;
import edu.jsu.mcis.cs408.crosswordmagic.model.dao.*;


public class CrosswordMagicModel extends AbstractModel {

    private final int DEFAULT_PUZZLE_ID = 1;

    private Puzzle puzzle;

    private PuzzleDAO puzzleDAO;

    private Character[][] letters;
    private Integer[][] numbers;

    private Integer[] dimensions = new Integer[2];

    private int crossHeight, crossWidth;

    public CrosswordMagicModel(Context context){
        DAOFactory daoFactory = new DAOFactory(context);
        this.puzzleDAO = daoFactory.getPuzzleDAO();

        this.puzzle = puzzleDAO.find(DEFAULT_PUZZLE_ID);
        if (puzzle != null) {
            setGridLetters(puzzle.getLetters());
            setGridNumbers(puzzle.getNumbers());
            setGridDimensions(puzzle.getHeight(), puzzle.getWidth());
        }
    }
    public CrosswordMagicModel(Context context, int puzzleid){
        DAOFactory daoFactory = new DAOFactory(context);
        this.puzzleDAO = daoFactory.getPuzzleDAO();

        this.puzzle = puzzleDAO.find(DEFAULT_PUZZLE_ID);
        if (puzzle != null) {
            setGridLetters(puzzle.getLetters());
            setGridNumbers(puzzle.getNumbers());
            setGridDimensions(puzzle.getHeight(), puzzle.getWidth());
        }
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
        Log.d("checkIfCOrrect", direction.toString());
        if (!direction.toString().isBlank()) {
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
}