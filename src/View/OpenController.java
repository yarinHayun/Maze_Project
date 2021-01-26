package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OpenController extends Canvas  implements Initializable {

    private MyViewModel viewModel;
    public javafx.scene.control.Button playGame;
    public ComboBox<String> comboBox;
    public MediaPlayer mediaPlayer;
    public Media song;


    public void setViewModel(MyViewModel myViewModel) {
        viewModel = myViewModel;
    }

    /**
     * Handle the button "play game" and replace the current scene to the second scene
     * @param event
     * @throws IOException
     */
    public void handlePlayGame (ActionEvent event) throws IOException {
        mediaPlayer.stop();
        Media song=new Media(getClass().getClassLoader().getResource("Songs/Dora_Piano_cuting.mp3").toExternalForm());
        MediaPlayer newMediaPlayer = new MediaPlayer(song);
        newMediaPlayer.setVolume(0.8);
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent New = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene sceneNew = new Scene(New, 1050, 600);
        MyViewController myViewController = fxmlLoader.getController();
        myViewController.mediaPlayer = newMediaPlayer;
        //repeat music
        myViewController.mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                myViewController.mediaPlayer.seek(javafx.util.Duration.ZERO);
            }
        });
        myViewController.mediaPlayer.play();
        myViewController.setViewModel(viewModel);
        viewModel.addObserver(myViewController);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(sceneNew);
        window.show();
        myViewController.setResizeEvent(sceneNew);
    }

    /**
     * Handle the button "choose player" and choose between Dora and Diego players
     * @param actionEvent
     */
    public void handleChoosePlayer(ActionEvent actionEvent) {

        if (comboBox.getValue().equals("Dora"))
            viewModel.setCharacterName("Dora");
        else
            viewModel.setCharacterName("Diego");
        playGame.setDisable(false);
    }

    /**
     * Initialize the comboBox "choose player" -insert Dora and diego players to the comboBox
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBox.getItems().addAll("Dora", "Diego");
        comboBox.setDisable(false);
    }

    //region Setters: setMediaPlayer,setSong
    public void setMediaPlayer(MediaPlayer mediaPlayer) { this.mediaPlayer = mediaPlayer; }

    public void setSong(Media song) { this.song = song; }
    //endregion

}//close class
