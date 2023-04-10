package Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class Controller
{
  @FXML private ImageView imageChose1;
  @FXML private ImageView imageChose2;
  @FXML private GridPane table;
  @FXML private HBox player1Turn;
  @FXML private HBox player2Turn;
  @FXML private HBox field;

  private static boolean player1IsCircle = true;
  private static int currPlayer = 1;
  private Image cross= new Image(Objects.requireNonNull(
      getClass().getResourceAsStream("../img/cross.png")));
  private Image circle = new Image(Objects.requireNonNull(
      getClass().getResourceAsStream("../img/circle.png")));

  private int[][] matrix = new int[3][3];


  public void printMatrix()
  {
    for(int i = 0; i < matrix.length; i++)
    {
      for (int j = 0; j < matrix[matrix.length - 1].length; j++)
      {
        System.out.print(matrix[i][j] + ", ");
      }
      System.out.println();
    }
    System.out.println();
    System.out.println();
    System.out.println();
  }


  public int findWhoWon()
  {
    int res = 0;


    for (int i = 0; i < matrix.length; i++)
    {
      if(matrix[i][0] == matrix[i][1] && matrix[i][0] == matrix[i][2] && matrix[i][0] != 0){
        return matrix[i][0];
      }
    }

    for (int i = 0; i < matrix.length; i++)
    {
      if(matrix[0][i] == matrix[1][i] && matrix[0][i] == matrix[2][i] && matrix[0][i] != 0){
        return matrix[0][i];
      }
    }

    for(int i = 0; i < matrix.length - 1; i++)
    {
      if(matrix[i][i] == matrix[i + 1][i + 1])
      {
        res = matrix[i][i];
      }
      else{
        res = 0;
        break;
      }
    }

    if(res != 0)
      return res;

    for(int i = 0, j = matrix.length - 1; i < matrix.length - 1; i++, j--)
    {
      if(matrix[i][j] == matrix[i + 1][j - 1])
      {
        res = matrix[i][j];
      }
      else{
        res = 0;
        break;
      }
    }

    if(res != 0)
      return res;


    for (int i = 0; i < matrix.length; i++)
    {
      for (int j = 0; j < matrix[matrix.length - 1].length; j++)
      {
        if(matrix[i][j] == 0)
        {
          return -1;
        }
      }
    }

    return res;
  }

  public void handleOnClick(MouseEvent e)
  {

    if(e.getSource() == imageChose1 || e.getSource() == imageChose2)
    {

      player1IsCircle = !player1IsCircle;

      if(player1IsCircle)
      {
        imageChose1.setImage(circle);
        imageChose2.setImage(cross);
        imageChose1.setId("Circle");
        imageChose2.setId("Cross");
      }
      else
      {
        imageChose1.setImage(cross);
        imageChose2.setImage(circle);
        imageChose2.setId("Circle");
        imageChose1.setId("Cross");
      }
    }
    else if(table.getChildren().contains(e.getSource()))
    {


      imageChose1.setDisable(true);
      imageChose2.setDisable(true);
      ImageView imageView = (ImageView) e.getSource();
      if(currPlayer % 2 == 0)
      {
        field.setAlignment(Pos.BASELINE_CENTER);
        player1Turn.setAlignment(Pos.CENTER);
        player1Turn.setVisible(true);
        player1Turn.setManaged(true);
        player2Turn.setVisible(false);
        player2Turn.setManaged(false);

        imageView.setImage(imageChose2.getImage());
        imageView.setId(imageChose2.getId());
      }
      else{
        field.setAlignment(Pos.BASELINE_CENTER);
        player2Turn.setAlignment(Pos.CENTER);
        player2Turn.setVisible(true);
        player2Turn.setManaged(true);
        player1Turn.setVisible(false);
        player1Turn.setManaged(false);

        imageView.setImage(imageChose1.getImage());
        imageView.setId(imageChose1.getId());
      }
      currPlayer++;
      imageView.setDisable(true);

      int index = table.getChildren().indexOf(e.getSource());
      int row = index / 3;
      int col = index % 3;
      matrix[row][col] = ((ImageView)e.getSource()).getId().equals("Circle")? 1 : ((ImageView)e.getSource()).getId().equals("Cross")? 2 : 0;

      printMatrix();
      int whoWon = findWhoWon();
      if(whoWon == 1)
      {
        String str = "Congratulations, " + (imageChose1.getId().equals("Circle")? "Player 1" : "Player 2") + " won!";
        printAlert("Circle won!", str);
      }
      else if(whoWon == 2)
      {
        String str = "Congratulations, " + (imageChose1.getId().equals("Cross")? "Player 1" : "Player 2") + " won!";
        printAlert("Cross won!", str);
      }
      else if(whoWon == 0){
        printAlert("Tie!", "TIE!");
      }
    }
  }

  private void loadNewGame()
  {

    ObservableList<Node> images = table.getChildren();
    for (Node node:images)
    {
      try
      {
        ImageView imageView = ((ImageView) node);
        imageView.setImage(null);
        imageView.setDisable(false);
      }
      catch (ClassCastException e)
      {
      }
    }

    player1Turn.setManaged(true);
    player1Turn.setVisible(true);
    player1Turn.setDisable(false);
    imageChose1.setDisable(false);

    player2Turn.setVisible(true);
    player2Turn.setManaged(true);
    player2Turn.setDisable(false);
    imageChose2.setDisable(false);

    matrix = new int[3][3];
    currPlayer = 1;
    player1IsCircle = true;
  }

  private void printAlert(String title, String contextText)
  {
    ButtonType close = new ButtonType("Close game", ButtonBar.ButtonData.CANCEL_CLOSE);
    ButtonType playAgain = new ButtonType("PLAY AGAIN!", ButtonBar.ButtonData.OK_DONE);
    Alert alert = new Alert(Alert.AlertType.INFORMATION, contextText, close, playAgain);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.showAndWait();
    if(alert.getResult() == close)
      System.exit(0);
    else{
      loadNewGame();
    }
  }

}
