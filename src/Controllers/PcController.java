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

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class PcController
{

  @FXML private ImageView computerChose;
  @FXML private ImageView playerChose;
  @FXML private GridPane gameTable;
  @FXML private HBox computerBox;
  @FXML private HBox playerBox;
  @FXML private HBox playersBox;

  private static boolean playerHasCircle = true;
  private Image cross= new Image(Objects.requireNonNull(
      getClass().getResourceAsStream("../img/cross.png")));
  private Image circle = new Image(Objects.requireNonNull(
      getClass().getResourceAsStream("../img/circle.png")));

  private int[][] gameInMatrix = new int[3][3];


  public void printMatrix()
  {
    for(int i = 0; i < gameInMatrix.length; i++)
    {
      for (int j = 0; j < gameInMatrix[gameInMatrix.length - 1].length; j++)
      {
        System.out.print(gameInMatrix[i][j] + ", ");
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


    for (int i = 0; i < gameInMatrix.length; i++)
    {
      if(gameInMatrix[i][0] == gameInMatrix[i][1] && gameInMatrix[i][0] == gameInMatrix[i][2] && gameInMatrix[i][0] != 0){
        return gameInMatrix[i][0];
      }
    }

    for (int i = 0; i < gameInMatrix.length; i++)
    {
      if(gameInMatrix[0][i] == gameInMatrix[1][i] && gameInMatrix[0][i] == gameInMatrix[2][i] && gameInMatrix[0][i] != 0){
        return gameInMatrix[0][i];
      }
    }

    for(int i = 0; i < gameInMatrix.length - 1; i++)
    {
      if(gameInMatrix[i][i] == gameInMatrix[i + 1][i + 1])
      {
        res = gameInMatrix[i][i];
      }
      else{
        res = 0;
        break;
      }
    }

    if(res != 0)
      return res;

    for(int i = 0, j = gameInMatrix.length - 1; i < gameInMatrix.length - 1; i++, j--)
    {
      if(gameInMatrix[i][j] == gameInMatrix[i + 1][j - 1])
      {
        res = gameInMatrix[i][j];
      }
      else{
        res = 0;
        break;
      }
    }

    if(res != 0)
      return res;


    for (int i = 0; i < gameInMatrix.length; i++)
    {
      for (int j = 0; j < gameInMatrix[gameInMatrix.length - 1].length; j++)
      {
        if(gameInMatrix[i][j] == 0)
        {
          return -1;
        }
      }
    }

    return res;
  }

  public void handleOnClick(MouseEvent e) throws InterruptedException
  {

    if(e.getSource() == computerChose || e.getSource() == playerChose)
    {

      playerHasCircle = !playerHasCircle;

      if(!playerHasCircle)
      {
        computerChose.setImage(circle);
        playerChose.setImage(cross);
        computerChose.setId("Circle");
        playerChose.setId("Cross");
      }
      else
      {
        computerChose.setImage(cross);
        playerChose.setImage(circle);
        playerChose.setId("Circle");
        computerChose.setId("Cross");
      }
    }
    else if(gameTable.getChildren().contains(e.getSource()))
    {

      computerChose.setDisable(true);
      playerChose.setDisable(true);
      ImageView imageView = (ImageView) e.getSource();

      playerBox.setAlignment(Pos.CENTER);
      computerBox.setVisible(false);
      computerBox.setManaged(false);
      playerBox.setVisible(true);
      playerBox.setManaged(true);

      imageView.setImage(playerChose.getImage());
      imageView.setId(playerChose.getId());
      imageView.setDisable(true);

      int index = gameTable.getChildren().indexOf(e.getSource());
      int row = index / 3;
      int col = index % 3;
      gameInMatrix[row][col] = ((ImageView)e.getSource()).getId().equals("Circle")? 1 : ((ImageView)e.getSource()).getId().equals("Cross")? 2 : 0;

      //printMatrix();

      computerTurn();

      //printMatrix();

      int whoWon = findWhoWon();
      if(whoWon == 1)
      {
        String str = (playerChose.getId().equals("Circle")? "Congratulations, Player" : "Computer") + " won!";
        printAlert("Circle won!", str);
      }
      else if(whoWon == 2)
      {
        String str = (playerChose.getId().equals("Cross")? "Congratulations, Player" : "Computer") + " won!";
        printAlert("Cross won!", str);
      }
      else if(whoWon == 0){
        printAlert("Tie!", "TIE!");
      }
    }
  }

  private void loadNewGame()
  {

    ObservableList<Node> images = gameTable.getChildren();
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

    computerBox.setManaged(true);
    computerBox.setVisible(true);
    computerBox.setDisable(false);
    computerChose.setDisable(false);

    playerBox.setVisible(true);
    playerBox.setManaged(true);
    playerBox.setDisable(false);
    playerChose.setDisable(false);

    gameInMatrix = new int[3][3];
    playerHasCircle = true;
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

  private void computerTurn() throws InterruptedException
  {

    ArrayList<Integer> possibleTurns = new ArrayList<>();
    for (int i = 0; i < gameInMatrix.length; i++)
    {
      for (int j = 0; j < gameInMatrix[gameInMatrix.length - 1].length; j++)
      {
        if(gameInMatrix[i][j] == 0)
        {

          possibleTurns.add(i * 3 + j);
        }
      }
    }
    if(possibleTurns.size() == 0)
      return;

    int turn = computerChose.getId().equals("Circle")? 1 : 2;


    int countNotZero = 0;
    boolean isOnlyOneZero = false;
    int zeroPositionI = 0;
    int zeroPositionJ = 0;
    int temp = 0;

    for (int i = 0; i < gameInMatrix.length; i++)
    {
      for(int j = 0; j < 3; j++)
      {
        if(gameInMatrix[i][j] != 0)
        {
          if(countNotZero == 0)
          {
            temp = gameInMatrix[i][j];
          }
          else {
            if(gameInMatrix[i][j] != temp)
            {
              break;
            }
          }

          if(countNotZero == 2)
          {
            break;
          }
          countNotZero++;
        }
        else{
          isOnlyOneZero = true;
          zeroPositionI = i;
          zeroPositionJ = j;
        }

      }
      if(isOnlyOneZero && countNotZero == 2)
      {
        gameInMatrix[zeroPositionI][zeroPositionJ] = turn;
        ImageView imageView = (ImageView) gameTable.getChildren().get(zeroPositionI * 3 + zeroPositionJ);
        imageView.setImage(computerChose.getImage());
        imageView.setId(computerChose.getId());
        imageView.setDisable(true);
        return;
      }
      countNotZero = 0;
      isOnlyOneZero = false;
    }

    countNotZero = 0;
    isOnlyOneZero = false;
    zeroPositionI = 0;
    zeroPositionJ = 0;
    temp = 0;

    for (int i = 0; i < gameInMatrix.length; i++)
    {

      for(int j = 0; j < 3; j++)
      {
        if(gameInMatrix[j][i] != 0)
        {
          if(countNotZero == 0)
          {
            temp = gameInMatrix[j][i];
          }
          else {
            if(gameInMatrix[j][i] != temp)
            {
              break;
            }
          }

          if(countNotZero == 2)
          {
            break;
          }
          countNotZero++;
        }
        else{
          isOnlyOneZero = true;
          zeroPositionI = i;
          zeroPositionJ = j;
        }

      }
      if(isOnlyOneZero && countNotZero == 2)
      {
        gameInMatrix[zeroPositionJ][zeroPositionI] = turn;
        ImageView imageView = (ImageView) gameTable.getChildren().get(zeroPositionJ * 3 + zeroPositionI);
        imageView.setImage(computerChose.getImage());
        imageView.setId(computerChose.getId());
        imageView.setDisable(true);
        return;
      }
      countNotZero = 0;
      isOnlyOneZero = false;
    }

    countNotZero = 0;
    isOnlyOneZero = false;
    temp = 0;
    zeroPositionI = 0;


    for (int i = 0; i < gameInMatrix.length; i++)
    {
      if(gameInMatrix[i][i] != 0)
      {
        if(countNotZero == 0)
        {
          temp = gameInMatrix[i][i];
        }
        else {
          if(gameInMatrix[i][i] != temp)
          {
            break;
          }
        }
        countNotZero++;
      }
      else{
        isOnlyOneZero = true;
        zeroPositionI = i;
      }

      if(i == gameInMatrix.length - 1 && countNotZero == 2 && isOnlyOneZero)
      {
        gameInMatrix[zeroPositionI][zeroPositionI] = turn;
        ImageView imageView = (ImageView) gameTable.getChildren().get(zeroPositionI * 3 + zeroPositionI);
        imageView.setImage(computerChose.getImage());
        imageView.setId(computerChose.getId());
        imageView.setDisable(true);
        return;
      }
    }


    countNotZero = 0;
    isOnlyOneZero = false;
    temp = 0;
    zeroPositionI = 0;
    zeroPositionJ = 0;

    for (int i = 0, j = gameInMatrix.length - 1; i < gameInMatrix.length; i++, j--)
    {
      if(gameInMatrix[i][j] != 0)
      {
        if(countNotZero == 0)
        {
          temp = gameInMatrix[i][j];
        }
        else {
          if(gameInMatrix[i][j] != temp)
          {
            break;
          }
        }

        countNotZero++;
      }
      else{
        isOnlyOneZero = true;
        zeroPositionI = i;
        zeroPositionJ = j;
      }

      if(i == gameInMatrix.length - 1 && countNotZero == 2 && isOnlyOneZero)
      {
        gameInMatrix[zeroPositionI][zeroPositionJ] = turn;
        ImageView imageView = (ImageView) gameTable.getChildren().get(zeroPositionI * 3 + zeroPositionJ);
        imageView.setImage(computerChose.getImage());
        imageView.setId(computerChose.getId());
        imageView.setDisable(true);
        return;
      }
    }

    Random random = new Random();
    int computerTurnIndex = possibleTurns.get(random.nextInt(possibleTurns.size()));

    int row = computerTurnIndex / 3;
    int col = computerTurnIndex % 3;
    gameInMatrix[row][col] = turn;
    ImageView imageView = (ImageView) gameTable.getChildren().get(computerTurnIndex);
    imageView.setImage(computerChose.getImage());
    imageView.setId(computerChose.getId());
    imageView.setDisable(true);
  }
}
