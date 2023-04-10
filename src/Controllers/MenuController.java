package Controllers;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MenuController
{
  @FXML private Button playerVsPlayer;
  @FXML private Button playerVsComputer;
  @FXML private Button about;
  @FXML private Button menuButton;

  public void handleOnClick(ActionEvent e) throws IOException
  {
    if(e.getSource() == playerVsPlayer)
    {
//      Stage window = new Stage();
      Stage window = (Stage) playerVsPlayer.getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
          "../FXML/tic-tac-toe.fxml"));
      Scene scene = new Scene(loader.load());
      window.setScene(scene);
      window.show();
    }
    else if(e.getSource() == playerVsComputer)
    {
      //      Stage window = new Stage();
      Stage window = (Stage) playerVsComputer.getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
          "../FXML/tic-tac-toe_PC.fxml"));
      Scene scene = new Scene(loader.load());
      window.setScene(scene);
      window.show();
    }
    else if (e.getSource() == about)
    {
      Stage window = (Stage) about.getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
          "../FXML/about.fxml"));
      Scene scene = new Scene(loader.load());
      window.setScene(scene);
      window.show();
    }
  }

  public void handleAboutButton(ActionEvent e) throws IOException
  {
    Stage window = (Stage) menuButton.getScene().getWindow();;
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
        "../FXML/start-menu.fxml"));
    Scene scene = new Scene(loader.load());
    window.setScene(scene);
    window.show();
  }

  public void handleOnHover(MouseEvent e)
  {
    Button button = (Button) e.getSource();
    Timeline timeline = new Timeline();
    timeline.getKeyFrames().addAll(
        new KeyFrame(Duration.millis(250),
            new KeyValue(button.scaleXProperty(), 1.1),
            new KeyValue(button.scaleYProperty(), 1.1),
            new KeyValue(button.styleProperty(), "-fx-background-color: #800080;-fx-text-fill: #ffffff;")
        ));
    timeline.play();
  }

  public void handleOnLeave(MouseEvent e)
  {
    Button button = (Button) e.getSource();
    Timeline timeline = new Timeline();
    timeline.getKeyFrames().addAll(
        new KeyFrame(Duration.millis(250),
            new KeyValue(button.styleProperty(), "-fx-background-color: #ffffff;-fx-text-fill: #800080;"),
            new KeyValue(button.scaleXProperty(), 1.0),
            new KeyValue(button.scaleYProperty(), 1.0)
        ));
    timeline.play();
  }
}
