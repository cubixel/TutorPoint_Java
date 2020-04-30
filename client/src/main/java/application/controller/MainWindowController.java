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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainWindowController extends BaseController implements Initializable {

  private final SubjectManager subjectManager;
  private final TutorManager tutorManager;
  private final TutorManager liveTutorManager;
  private final Account account;

  private static final Logger log = LoggerFactory.getLogger("MainWindowController");

  /**
   * .
   *
   * @param viewFactory    .
   * @param fxmlName       .
   * @param mainConnection .
   * @param account        .
   */
  public MainWindowController(ViewFactory viewFactory, String fxmlName,
      MainConnection mainConnection, Account account) {
    super(viewFactory, fxmlName, mainConnection);
    subjectManager = new SubjectManager();
    tutorManager = new TutorManager();
    liveTutorManager = new TutorManager();
    this.account = account;
  }

  /**
   * .
   *
   * @param viewFactory    .
   * @param fxmlName       .
   * @param mainConnection .
   */
  public MainWindowController(ViewFactory viewFactory, String fxmlName,
      MainConnection mainConnection) {
    super(viewFactory, fxmlName, mainConnection);
    subjectManager = new SubjectManager();
    tutorManager = new TutorManager();
    liveTutorManager = new TutorManager();
    this.account = null;
  }

  @FXML
  private TabPane navbar;

  @FXML
  private AnchorPane homeWindow;

  @FXML
  private AnchorPane subscriptionsWindow;

  @FXML
  private AnchorPane discoverWindow;

  @FXML
  private AnchorPane profileWindow;

  @FXML
  private AnchorPane streamWindow;

  @FXML
  private Button logOutButton;

  BaseController profileWindowController;


  @FXML
  void mediaPlayerButtonAction() {
    Stage stage = (Stage) navbar.getScene().getWindow();
    viewFactory.showMediaPlayerWindow(stage);
  }

  @FXML
  void presentationButtonAction() {
    Stage stage = (Stage) navbar.getScene().getWindow();
    if (account.getTutorStatus() == 1) {
      viewFactory.showPresentationWindow(stage, true);
    } else {
      viewFactory.showPresentationWindow(stage, false);
    }
    
  }

  @FXML
  void whiteboardButtonAction() {
    Stage stage = (Stage) navbar.getScene().getWindow();
    viewFactory.showWhiteboardWindow(stage);
  }

  @FXML
  void logOutButtonAction() {
    try {
      getMainConnection().sendString("Logout");
    } catch (IOException e) {
      log.error("Failed to tell server to logout", e);
    }
    log.info("Logging Out");
    // Stop current presentation if one exists
    if (getMainConnection().getListener().hasPresentationWindowController()) {
      log.info("Clearing presentation on exit");
      getMainConnection().getListener().getPresentationWindowController().clearPresentation();
      getMainConnection().getListener().setPresentationWindowController(null);
    }
    
    // TODO Remove the users remember me details
    getMainConnection().setUserID(-1);
    Stage stage = (Stage) navbar.getScene().getWindow();
    viewFactory.showLoginWindow(stage);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      viewFactory.embedHomeWindow(homeWindow, this);
      viewFactory.embedDiscoverWindow(discoverWindow, this);
      viewFactory.embedProfileWindow(profileWindow, this);
      viewFactory.embedSubscriptionsWindow(subscriptionsWindow, this);

      /*profilePane.setOnMouseEntered(e -> {
        Thread.currentThread().interrupt();
        new Thread(() -> {
          while (profilePane.getWidth() < 200) {
            if (Thread.interrupted()) {
              System.out.println("Left Stopped");
              break;
            }
            profilePane.setPrefWidth(profilePane.getWidth() + 10);
          }
          System.out.println("Left Done");
        }).start();
      });

      profilePane.setOnMouseExited(e -> {
        Thread.currentThread().interrupt();
        new Thread(() -> {
          while (profilePane.getWidth() > 20) {
            try {
              if (Thread.interrupted()) {
                System.out.println("Right Stopped");
                throw new InterruptedException();
              }
              profilePane.setPrefWidth(profilePane.getWidth() - 10);
            } catch (InterruptedException ie)  {
              break;
            }
          }
          System.out.println("Right Done");
        }).start();
      });*/

    } catch (IOException e) {
      e.printStackTrace();
    }

    /* TODO Set Up Screen
     * Request from server the top set of subjects.
     * with each one get the server to send the thumbnail too.
     * Fill out the display with the subjects and the thumbnails
     *
     * */
    //downloadSubjects();

    if (account.getTutorStatus() == 1) {
      try {
        viewFactory.embedStreamWindow(streamWindow, account, account.getUserID(), true);
      } catch (IOException e) {
        log.error("Failed to embed Stream Window Controller", e);
      }
    } else {
      navbar.getTabs().remove(4);
    }

  }

  public TabPane getPrimaryTabPane() {
    return navbar;
  }

  public AnchorPane getDiscoverAnchorPane() {
    return discoverWindow;
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

  public TutorManager getLiveTutorManager() {
    return liveTutorManager;
  }

  public TabPane getNavbar() {
    return navbar;
  }

  public Tab getHomeTab() {
    return navbar.getTabs().get(0);
  }

  public Tab getSubscriptionsTab() {
    return navbar.getTabs().get(1);
  }

  public Tab getDiscoverTab() {
    return navbar.getTabs().get(2);
  }

  public Tab getProfileTab() {
    return navbar.getTabs().get(3);
  }

  public Tab getStreamTab() {
    return navbar.getTabs().get(4);
  }


/*Task<Void> moveSidePaneLeft = new Task<Void>() {
    @Override
    protected Void call() throws Exception {
      while (profilePane.getWidth() < 200) {
        if (Thread.currentThread().isInterrupted()) {
          System.out.println("Left Stopped");
          return null;
        }
        profilePane.setPrefWidth(profilePane.getWidth() + 10);
      }
      System.out.println("Left Done");
      return null;
    }
  };

  Task<Void> moveSidePaneRight = new Task<Void>() {
    @Override
    protected Void call() throws Exception {
      while (profilePane.getWidth() > 20) {
        if (Thread.currentThread().isInterrupted()) {
          System.out.println("Right Stopped");
          return null;
        }
        profilePane.setPrefWidth(profilePane.getWidth() - 10);
      }
      System.out.println("Right Done");
      return null;
    }
  };*/
}
