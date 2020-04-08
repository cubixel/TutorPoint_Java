package application.controller;

import application.controller.services.MainConnection;
import application.model.Account;
import application.model.managers.SubjectManager;
import application.model.managers.TutorManager;
import application.view.ViewFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainWindowController extends BaseController implements Initializable {

  private SubjectManager subjectManager;
  private TutorManager tutorManager;
  private Account account;
  private static final Logger log = LoggerFactory.getLogger("Client Logger");

  /**
   * .
   * @param viewFactory
   * @param fxmlName
   * @param mainConnection
   * @param account
   */
  public MainWindowController(ViewFactory viewFactory, String fxmlName,
      MainConnection mainConnection, Account account) {
    super(viewFactory, fxmlName, mainConnection);
    subjectManager = new SubjectManager();
    tutorManager = new TutorManager();
    this.account = account;
  }

  /**
   * .
   * @param viewFactory
   * @param fxmlName
   * @param mainConnection
   */
  public MainWindowController(ViewFactory viewFactory, String fxmlName,
      MainConnection mainConnection) {
    super(viewFactory, fxmlName, mainConnection);
    subjectManager = new SubjectManager();
    tutorManager = new TutorManager();
    this.account = null;
  }

  @FXML
  private HBox popUpArea;

  @FXML
  private AnchorPane popUpHolder;

  @FXML
  private TabPane primaryTabPane;

  @FXML
  private TabPane secondaryTabPane;

  @FXML
  private AnchorPane recentAnchorPane;

  @FXML
  private Label usernameLabel;

  @FXML
  private Label tutorStatusLabel;

  @FXML
  private AnchorPane discoverAnchorPane;

  @FXML
  private AnchorPane tutorHubAnchorPane;

  @FXML
  private Button logOutButton;

  BaseController profileWindowController;

  @FXML
  void closePopUp() {
    popUpArea.toBack();
    updateAccountViews();
  }

  @FXML
  void openPopUp() {
    popUpArea.toFront();
  }

  @FXML
  void mediaPlayerButtonAction() {
    Stage stage = (Stage) usernameLabel.getScene().getWindow();
    viewFactory.showMediaPlayerWindow(stage);
  }

  @FXML
  void presentationButtonAction() {
    Stage stage = (Stage) usernameLabel.getScene().getWindow();
    viewFactory.showPresentationWindow(stage);
  }

  @FXML
  void whiteboardButtonAction() {
    Stage stage = (Stage) usernameLabel.getScene().getWindow();
    viewFactory.showWhiteboardWindow(stage);
  }

  @FXML
  void logOutButtonAction() {
    Stage stage = (Stage) usernameLabel.getScene().getWindow();
    viewFactory.showLoginWindow(stage);
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      viewFactory.embedProfileWindow(popUpHolder, this);
      viewFactory.embedDiscoverWindow(discoverAnchorPane, this);
      viewFactory.embedRecentWindow(recentAnchorPane, this);
    } catch (IOException e) {
      e.printStackTrace();
    }

    updateAccountViews();
  }

  private void updateAccountViews() {
    if (account != null) {
      usernameLabel.setText(account.getUsername());

      if (account.getTutorStatus() == 0) {
        tutorStatusLabel.setText("Student Account");
      } else {
        tutorStatusLabel.setText("Tutor Account");
      }
    }

    if (account != null) {
      if (account.getTutorStatus() == 1) {
        try {
          // TODO It is throwing lots of complaints about size of StreamWindow
          // TODO Keeps adding a new tab every time profile popup is displayed
          AnchorPane anchorPaneStream = new AnchorPane();
          Tab tab = new Tab("Stream");
          tab.setContent(anchorPaneStream);
          primaryTabPane.getTabs().add(tab);
          viewFactory.embedStreamWindow(anchorPaneStream, account);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public TabPane getPrimaryTabPane() {
    return primaryTabPane;
  }

  public AnchorPane getDiscoverAnchorPane() {
    return discoverAnchorPane;
  }

  public SubjectManager getSubjectManager() {
    return subjectManager;
  }

  public Account getAccount() {
    return account;
  }

  public TutorManager getTutorManager() {
    return tutorManager;
  }
}
