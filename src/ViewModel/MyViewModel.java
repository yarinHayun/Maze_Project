package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Position;
import javafx.scene.input.KeyCode;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel  extends Observable implements Observer {
    private IModel model;
    private int characterRowIndex;
    private int characterColumnIndex;
    private String characterName;

    /**
     * constructor
     * @param model
     */
    public MyViewModel(IModel model) {
        this.model = model;
    }

    /**
     * Generate maze with the parameters
     * @param width
     * @param height
     */
    public void generateMaze(int width, int height) {
        model.clientGenerateMaze(width, height);
    }

    /**
     * calling to client Solve Maze with model and solve the maze
     */
    public void solveMaze() {
        model.clientSolveMaze();
    }

    /**
     * updating when myModel runs notifyAll method
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            characterRowIndex = model.getCharacterPositionRow();
            characterColumnIndex = model.getCharacterPositionColumn();
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Move character with model
     * @param move
     */
    public void moveCharacter(KeyCode move) { model.moveCharacter(move); }

    /**
     * Save file with model saving function
     * @param file
     */
    public void save(File file) {
        model.save(file);
    }

    /**
     * Load file with model loading function
     * @param file
     */
    public void load(File file) {
        try {
            model.load(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * exit with model exit function
     */
    public void exit() {
        model.exit();
    }

    //<editor-fold desc="Getters: getMaze,getSolPath,getStartPosition,getGoalPosition,getCharacterName,getCharacterRowIndex,getCharacterColumnIndex ">
    public int[][] getMaze() {
        return model.getMaze();
    }

    public ArrayList<Position> getSolPath(){
        return model.getSolutionPath();
    }

    public Position getStartPosition() {
        return model.getStartPosition();
    }

    public Position getGoalPosition() {
        return model.getGoalPosition();
    }

    public String getCharacterName() {
        return characterName;
    }

    public int getCharacterRowIndex() {
        return characterRowIndex;
    }

    public int getCharacterColumnIndex() {
        return characterColumnIndex;
    }
    //</editor-fold>

    //<editor-fold desc="Setter: setCharacterName">
    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }
    //</editor-fold>

}