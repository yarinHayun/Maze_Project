package View;

import algorithms.mazeGenerators.Position;
import java.io.FileNotFoundException;

public interface IView {

    void displayMaze(int [][] maze, Position goalPosition,Position startPositin) throws FileNotFoundException;

}
