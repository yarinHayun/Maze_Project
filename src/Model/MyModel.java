package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import Client.*;
import java.util.Observable;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;


public class MyModel  extends Observable implements IModel {
    private Maze maze;
    private ArrayList<Position> solutionPath;
    private int characterRowIdx;
    private int characterColIdx;
    private static Server mazeGeneratorServer;
    private static Server mazeSolveServer;
    private ServerStrategySolveSearchProblem solveStrategy;
    private ServerStrategyGenerateMaze generateStrategy;

    /**
     * constructor
     */

    public MyModel() {
        maze=new Maze(0,0,new Position(0,0),new Position(0,0));
        solutionPath=new ArrayList<>();
    }

    public  void startServer(){
        generateStrategy=new ServerStrategyGenerateMaze();
        solveStrategy=new ServerStrategySolveSearchProblem();
        mazeGeneratorServer = new Server(5400,1000, generateStrategy);
        mazeSolveServer = new Server(5401,1000, solveStrategy);
        mazeGeneratorServer.start();
        mazeSolveServer.start();
    }

    /**
     * This function communicates with the server that generate a maze
     * @param row
     * @param col
     */
    public void clientGenerateMaze(int row, int col){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[10+ (row+2)*(col+2)]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
            characterRowIdx=maze.getStartPosition().getRowIndex();
            characterColIdx=maze.getStartPosition().getColumnIndex();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }

    /**
     * This function communicates with the server that solve the maze
     */
    public void clientSolveMaze (){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        maze.setM_startPosition(new Position(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex()));
                        toServer.writeObject(maze);
                        toServer.flush();
                        Solution mazeSolution = (Solution)fromServer.readObject();
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        solutionPath.clear();

                        for(int i = 0; i < mazeSolutionSteps.size(); ++i) {
                            AState state= (AState) mazeSolution.getSolutionPath().get(i);
                            String [] position = state.getState().split(",");
                            int row = Integer.parseInt(position[0].substring(1));
                            int column = Integer.parseInt(position[1].substring(0,position[1].length() - 1));
                            solutionPath.add(new Position(row, column));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers();
    }

    /**
     * This function saves the current maze to a file on disk
     * @param file
     */
    public void save(File file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            maze.setM_startPosition(new Position(characterRowIdx,characterColIdx));
            out.writeObject(maze);
            out.flush();
            out.close();
        } catch (IOException ex) { }
    }

    /**
     * load file and create maze according to the file
     * @param file
     */
    public void load(File file) {
        try {
            FileInputStream in= new FileInputStream(file);
            ObjectInputStream inputStream=new ObjectInputStream(in);
            Maze tempMaze=(Maze) inputStream.readObject();
            characterRowIdx = tempMaze.getStartPosition().getRowIndex();
            characterColIdx = tempMaze.getStartPosition().getColumnIndex();
            maze=tempMaze;

            setChanged();
            notifyObservers();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * move the player on the maze - up, down, right, left and diagonals
     * @param movement
     */
    @Override
    public void moveCharacter(KeyCode movement) {
        switch (movement) {
            // case NUMPAD8: //up
            case UP:
                if (maze.getValPositon(characterRowIdx - 1,characterColIdx) != 1)
                    characterRowIdx--;
                break;
           //  case NUMPAD2://down
            case DOWN:
                if (maze.getValPositon(characterRowIdx + 1,characterColIdx) != 1)
                    characterRowIdx++;
                break;
           //  case NUMPAD6: //right
            case RIGHT:
                if (maze.getValPositon(characterRowIdx,characterColIdx + 1) != 1)
                    characterColIdx++;
                break;
            // case NUMPAD4: //left
            case LEFT:
                if (maze.getValPositon(characterRowIdx,characterColIdx - 1) != 1)
                    characterColIdx--;
                break;
            case NUMPAD9: //up and right
                if (maze.getValPositon(characterRowIdx - 1,characterColIdx + 1) != 1 &&
                        (maze.getValPositon(characterRowIdx - 1,characterColIdx) == 0 ||
                                maze.getValPositon(characterRowIdx,characterColIdx + 1) == 0)){
                    characterRowIdx--;
                    characterColIdx++;
                }
                break;
            case NUMPAD3: //right and down
                if (maze.getValPositon(characterRowIdx + 1,characterColIdx + 1) != 1 &&
                        (maze.getValPositon(characterRowIdx + 1,characterColIdx) == 0 ||
                                maze.getValPositon(characterRowIdx,characterColIdx + 1) == 0)) {
                    characterRowIdx++;
                    characterColIdx++;
                }
                break;
            case NUMPAD1:
                if (maze.getValPositon(characterRowIdx + 1,characterColIdx - 1) != 1 &&
                        (maze.getValPositon(characterRowIdx + 1,characterColIdx) == 0 ||
                                maze.getValPositon(characterRowIdx,characterColIdx - 1) == 0)){
                    characterRowIdx++;
                    characterColIdx--;
                }
                break;

            case NUMPAD7:
                if (maze.getValPositon(characterRowIdx - 1,characterColIdx - 1) != 1 &&
                        (maze.getValPositon(characterRowIdx - 1,characterColIdx) == 0 ||
                                maze.getValPositon(characterRowIdx,characterColIdx - 1) == 0)) {
                    characterColIdx--;
                    characterRowIdx--;
                }
                break;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * exit from the game
     */
    @Override
    public void exit() {
        mazeGeneratorServer.stop();
        mazeSolveServer.stop();
        System.exit(0);
    }

    //<editor-fold desc="Getters:getMaze,getGoalPosition,getStartPosition,getSolutionPath,getCharacterPositionRow,getCharacterPositionColumn">
    public int[][] getMaze(){
        return maze.getMaze();
    }

    public Position getGoalPosition(){return maze.getGoalPosition();}

    public Position getStartPosition(){
        return maze.getStartPosition();
    }

    public ArrayList<Position> getSolutionPath() {
        return solutionPath;
    }

    public int getCharacterPositionRow() { return characterRowIdx; }

    public int getCharacterPositionColumn() { return characterColIdx; }
    //</editor-fold>
}//close class

