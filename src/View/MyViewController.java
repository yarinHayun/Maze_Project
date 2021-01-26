package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Position;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.*;
import java.util.Observable;
import java.util.Observer;

public class MyViewController implements Observer,IView {
    private MyViewModel viewModel;

    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_colsNum;
    public javafx.scene.control.Button id_generateMaze;
    public javafx.scene.control.Button btn_solveMazeNew;
    public javafx.scene.control.MenuItem item_save;
    public MazeDisplayer mazeDisplayer;
    public MediaPlayer mediaPlayer;
    public Media song;

    /**
     * Set the view model
     * @param myViewModel
     */
    public void setViewModel(MyViewModel myViewModel) {
        viewModel = myViewModel;
    }

    /**
     * updating when myViewModel runs the notifyAll method
     * @param observable
     * @param arg
     */
    @Override
    public void update(Observable observable, Object arg) {
        if (observable == viewModel) {
            try {
                displayMaze(viewModel.getMaze(), viewModel.getGoalPosition(),viewModel.getStartPosition());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            id_generateMaze.setDisable(false);
        }
    }

    /**
     * Display the maze on the screen with mazeDisplayer class
     * @param maze
     * @param goalPosition
     * @param startPosition
     * @throws FileNotFoundException
     */
    public void displayMaze(int[][]maze, Position goalPosition, Position startPosition) throws FileNotFoundException {
        mazeDisplayer.setGoalPosition(goalPosition);
        mazeDisplayer.setStartPosition(startPosition);
        mazeDisplayer.setMaze(maze);
        mazeDisplayer.setCharacterPosition(viewModel.getCharacterRowIndex(), viewModel.getCharacterColumnIndex());

        if (viewModel.getCharacterName().equals("Dora"))
            mazeDisplayer.Dora();
        else{
            mazeDisplayer.Diego();
        }
        //if player solve the maze
        if (viewModel.getGoalPosition().getRowIndex() == viewModel.getCharacterRowIndex() && viewModel.getGoalPosition().getColumnIndex() == viewModel.getCharacterColumnIndex())
        {
            mediaPlayer.stop();
            Media song=new Media(getClass().getClassLoader().getResource("Songs/winSong.mp3").toExternalForm());
            mediaPlayer = new MediaPlayer(song);
            mediaPlayer.setVolume(0.8);
            mediaPlayer.play();
            //Alert when player solve the maze
            String content = "Good job - You find Boots!"+"\n"+"Play again or exit" ;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Congratulations!");
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.show();
        }
    }//close display maze

    /**
     * handle play button(in the second scene)that generate new maze
     */
    public void handleGenerateMaze() {
        mediaPlayer.stop();
        int height = Integer.valueOf(txtfld_rowsNum.getText());
        int width = Integer.valueOf(txtfld_colsNum.getText());
        viewModel.generateMaze(height, width);
        id_generateMaze.setDisable(true);
        btn_solveMazeNew.setDisable(false);
        item_save.setDisable(false);
        Media song=new Media(getClass().getClassLoader().getResource("Songs/Dora_Piano_cuting.mp3").toExternalForm());
        mediaPlayer= new MediaPlayer(song);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(javafx.util.Duration.ZERO);
            }
        });
        mediaPlayer.setVolume(0.8);
        mediaPlayer.play();
    }

    /**
     * handle solveMaze button that draws the solution of the maze
     */
    public void handleSolveMaze() {
        viewModel.solveMaze();
        mazeDisplayer.drawSolution(viewModel.getSolPath());
        btn_solveMazeNew.setDisable(true);
    }

    /**
     * handle function-when the player want to focus the zoom on the screen by click press
     * @param keyEvent
     */
    public void handleKeyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
    }


    /**
     * handle function when the player want to save the game
     * @param actionEvent
     */
    public void handleSave(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Saving the maze");
        fc.setInitialDirectory(null);
        File file = fc.showSaveDialog((Stage) mazeDisplayer.getScene().getWindow());
        if (file != null)
            viewModel.save(file);
    }


    /**
     * handle function when the player want to load game
     * @param actionEvent
     */
    public void handleLoad(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog((Stage) mazeDisplayer.getScene().getWindow());
        if (file != null) {
            handleGenerateMaze();
            viewModel.load(file);
            mazeDisplayer.drawMaze();
        }
    }


    /**
     * handle function when the player want to exit
     * @param actionEvent
     */
    public void handelExit(ActionEvent actionEvent) {
        viewModel.exit();
        Platform.exit();
    }


    /**
     * handle function when the player want to get structure and some guidance on the game
     * @param actionEvent
     */
    public void handleHelp(ActionEvent actionEvent) {
        String content = "You can help Dora and Diego to find boots \n" +
                "This is the rules:\n -You can't get out of the frame.\n" +
                "-You can only move up, down, right, left or in diagonals.\n" +
                "-No stepping on the walls.\n" +
                "-The most important - Have fun and tell about us to your friends! :)";
        Alert alert = new  Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    /**
     * handle function when the player want to get information on the game Creators
     **/
    public void handleAbout(ActionEvent actionEvent) {
        String content = "Our names are Yarin Hayun and Dana Chror and we created this amazing Dora&Diego maze.\n" +
                "In this game we used a few algorithms:\n" +
                "First, the algorithm for the generation of the maze is Randomized Prim's algorithm.\n" +
                "Second, the algorithm to solve the maze is Best First Search algorithm.";
        Alert alert = new  Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Little about us..");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }


    /**
     * handle function when the player want to mute the music during the game
     * @param actionEvent
     */
    public void handelMuteMusic(ActionEvent actionEvent) {
        mediaPlayer.stop();
    }

    /**
     * handle function when the player want to get zoom in or out on the game screen
     * @param scroll
     */
    public void handleZoom(ScrollEvent scroll) {
        if(scroll.isControlDown()){
            if(scroll.getDeltaY()>0){
                mazeDisplayer.setZoom(mazeDisplayer.getZoom()*1.1);
            }
            if(scroll.getDeltaY()<0){
                mazeDisplayer.setZoom(mazeDisplayer.getZoom()/1.1);
            }
            scroll.consume();
            mazeDisplayer.drawMaze();
        }
    }

    /**
     * handle function when the player want to get the game properties
     * @param actionEvent
     */
    public void handelProperties(ActionEvent actionEvent) {
        String text = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("Resources/config.properties"));
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line + ",");
                stringBuilder.append(System.lineSeparator());
                line = bufferedReader.readLine();
            }
            text = stringBuilder.toString();
            bufferedReader.close();
        } catch (IOException e) {
        }
        String[] split = text.split(",");
        String content = "";
        content = "The solving maze algorithm is " + splitLine(split[0] + "\n");
        content += "The generator algorithm is " + splitLine(split[1].substring(4) + "\n");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game properties");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    /**
     * help function for handelProperties function
     * @param s
     * @return
     */
    private String splitLine(String s) {
        String[] splitLine = s.split("=");
        return splitLine[1];
    }

    /**
     * Resize the game window, depending on the player's changes
     * @param scene
     */
    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                mazeDisplayer.drawMaze();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                mazeDisplayer.drawMaze();
            }
        });
    }

}//close class
