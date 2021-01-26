package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.File;
import java.util.Optional;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        MyModel model = new MyModel();
        model.startServer();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        //---------------------
        primaryStage.setTitle("Dora & Diego Maze");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("OpenWindow.fxml").openStream());
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        OpenController viewController = fxmlLoader.getController();
        Media song=new Media(getClass().getClassLoader().getResource("Songs/Dora_Karaoke_Version.mp3").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(song);
        mediaPlayer.setVolume(0.8);
        viewController.setMediaPlayer(mediaPlayer);
        viewController.setSong(song);
        //-------------------------
        viewController.setViewModel(viewModel);
        //repeat music
        viewController.mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                viewController.mediaPlayer.seek(javafx.util.Duration.ZERO);
            }
        });
        viewController.mediaPlayer.play();
        SetStageCloseEvent(primaryStage, model);
        primaryStage.show();
    }

    /**
     * exit from game
     * @param primaryStage
     * @param myModel
     */
    private void SetStageCloseEvent(Stage primaryStage, MyModel myModel) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                String str="Are you sure you want to exit?";
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("exit");
                alert.setContentText(str);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    myModel.exit();
                    primaryStage.close();
                } else {
                    windowEvent.consume();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
