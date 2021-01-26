package View;

import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import java.io.FileNotFoundException;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.io.FileInputStream;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private Position goalPosition;
    private Position startPosition;
    private Image Wall;
    private Image star;
    private Image ImageBoots;
    private Image myPlayer;
    private int m_characterRow;
    private int m_characterColumn;
    private StringProperty WallImage = new SimpleStringProperty();
    private StringProperty imageSol = new SimpleStringProperty();
    private StringProperty boots = new SimpleStringProperty();
    private StringProperty dora = new SimpleStringProperty();
    private StringProperty diego = new SimpleStringProperty();
    private double zoom=1;
    private double canvasHeight;
    private double canvasWidth;
    private double cellHeight;
    private double cellWidth;

    /**
     * Draws the maze board and the characters on it
     */
    public void drawMaze(){
        try{

            Wall = new Image(ClassLoader.getSystemResourceAsStream("Images/imageWall.jpg"));
            ImageBoots=new Image(ClassLoader.getSystemResourceAsStream("Images/boots.jpg"));
            canvasHeight = getWidth();
            canvasWidth = getHeight();
            cellHeight = canvasHeight / maze[0].length * zoom;
            cellWidth = canvasWidth / maze.length * zoom;
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());
            //Draw Maze
            for (int i = 0; i < maze[0].length; i++) {
                for (int j = 0; j < maze.length; j++) {
                    if (maze[j][i] == 1)
                        gc.drawImage(Wall, i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                    else{
                        gc.setFill(Color.SILVER);
                        gc.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                    }
                    if (j == goalPosition.getRowIndex() && i == goalPosition.getColumnIndex())
                        gc.drawImage(ImageBoots, i * (cellHeight), j * (cellWidth), cellHeight, cellWidth);
                }
            }
            gc.drawImage(myPlayer, getCharacterPositionColumn() * cellHeight, getCharacterPositionRow() * cellWidth, cellHeight, cellWidth);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Draws the solution from the start position to the end position
     * @param solPath
     */
    public void drawSolution(ArrayList<Position>solPath){
        try{
            star =new Image(ClassLoader.getSystemResourceAsStream("Images/star.jpg"));
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());
            drawMaze();
            for (int i = 1; i < solPath.size() - 1; i++)
                gc.drawImage(star, solPath.get(i).getColumnIndex() * cellHeight, solPath.get(i).getRowIndex() * cellWidth, cellHeight, cellWidth);
        }catch (Exception e){
        }

    }

    /**
     * Set the characters positions
     * Call the function that draw the maze to draw it with the position of the character
     * @param characterRow
     * @param characterColumn
     */
    public void setCharacterPosition(int characterRow,int characterColumn){
        m_characterRow=characterRow;
        m_characterColumn=characterColumn;
        drawMaze();
    }

    /**
     * set Dora as the player chosen by the user
     * call the function that draw the maze to draw it with the Dora chosen by the user
     */
    public void Dora(){
        myPlayer=new Image(ClassLoader.getSystemResourceAsStream("Images/doraPlayer.jpg"));
        drawMaze();
    }

    /**
     * set Diego as the player chosen by the user
     * call the function that draw the maze to draw it with the Diego chosen by the user
     */
    public void Diego(){
        myPlayer=new Image(ClassLoader.getSystemResourceAsStream("Images/diegoPlayer.jpg"));
        drawMaze();
    }

    //region Getters:
    public int getCharacterPositionRow() {
        return m_characterRow;
    }

    public int getCharacterPositionColumn() {
        return m_characterColumn;
    }

    public String getWallImage() {
        return WallImage.get();
    }

    public double getZoom() {
        return zoom;
    }

    public String getImageSol() {
        return imageSol.get();
    }

    public String getImageSolProperty() {
        return imageSol.get();
    }

    public String getBoots() {
        return boots.get();
    }

    public StringProperty getImageBootsProperty() {
        return boots;
    }

    public String getDora() {
        return dora.get();
    }
    public String getDiego() {
        return diego.get();
    }
    //endregion

    //region Setters:
    public void setMaze(int[][] maze) throws FileNotFoundException {
        this.maze = maze;
        drawMaze();
    }
    public void setGoalPosition(Position goalPosition) {
        this.goalPosition = goalPosition;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public void setBoots(String boots) {
        this.boots.set(boots);
    }

    public void setImageSol(String imageSol) {
        this.imageSol.set(imageSol);
    }

    public void setCharacterRow(int characterRow) {
        this.m_characterRow= characterRow;
    }

    public void setCharacterColumn(int characterColumn) {
        this.m_characterColumn = characterColumn;
    }

    public void setWallImage(String imageWall) {
        this.WallImage.set(imageWall);
    }

    public void setDora(String imageStart) {
        dora.set(imageStart);
    }

    public void setDiego(String imageStart) {
        diego.set(imageStart);
    }
    //endregion









}