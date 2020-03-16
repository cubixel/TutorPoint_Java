package application.controller;

import application.controller.services.MainConnection;
import application.controller.services.SubjectRenderer;
import application.model.managers.SubjectManager;
import application.view.ViewFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainWindowController extends BaseController implements Initializable {

  private SubjectRenderer subjectRenderer;
  private SubjectManager subjectManager;

  public MainWindowController(ViewFactory viewFactory, String fxmlName,
      MainConnection mainConnection) {
    super(viewFactory, fxmlName, mainConnection);
    subjectManager = new SubjectManager();
  }

  @FXML
  private TabPane primaryTabPane;

  @FXML
  private TabPane secondaryTabPane;

  @FXML
  private Label profileNameField;

  @FXML
  private ScrollBar scrollBar;

  @FXML
  private HBox hboxOne;

  @FXML
  void profileButtonAction() {

  }


  @FXML
  void tempButton() throws IOException, InterruptedException {
    setUpSubjectView();
  }

  @FXML
  void mediaPlayerButtonAction() {
    Stage stage = (Stage) profileNameField.getScene().getWindow();
    viewFactory.showMediaPlayerWindow(stage);
  }

  @FXML
  void presentationButtonAction() {
    Stage stage = (Stage) profileNameField.getScene().getWindow();
    viewFactory.showPresentationWindow(stage);
  }

  @FXML
  void whiteboardButtonAction() {
    Stage stage = (Stage) profileNameField.getScene().getWindow();
    viewFactory.showWhiteboardWindow(stage);
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    /* TODO Set Up Screen
     * Request from server the top set of subjects.
     * with each one get the server to send the thumbnail too.
     * Fill out the display with the subjects and the thumbnails
     *
     * */
    //setUpSubjectView();
    //setUpTutorView();
  }

  private void setUpSubjectView() throws InterruptedException {
    subjectRenderer = new SubjectRenderer(getMainConnection(), hboxOne, subjectManager);
    subjectRenderer.start();
  }

  //private void setUpTutorView() {
  //}

}
