package Model;

import algorithms.mazeGenerators.Position;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface IModel {

    void clientGenerateMaze(int row, int col);
    void clientSolveMaze();
    int[][] getMaze();
    Position getGoalPosition();
    Position getStartPosition();
    ArrayList<Position> getSolutionPath();
    int getCharacterPositionColumn();
    int getCharacterPositionRow();
    void moveCharacter(KeyCode move);
    void save(File file);
    void exit();
    void load(File chosen) throws FileNotFoundException;

}


