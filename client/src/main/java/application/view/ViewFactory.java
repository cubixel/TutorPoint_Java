package application.view;

import application.controller.BaseController;
import application.controller.DiscoverWindowController;
import application.controller.LoginWindowController;
import application.controller.MainWindowController;
import application.controller.MediaPlayerController;
import application.controller.PresentationWindowController;
import application.controller.ProfileWindowController;
import application.controller.RecentWindowController;
import application.controller.RegisterWindowController;
import application.controller.StreamWindowController;
import application.controller.SubjectWindowContoller;
import application.controller.TextChatWindowController;
import application.controller.TextWindowController;
import application.controller.WebcamWindowController;
import application.controller.WhiteboardWindowController;
import application.controller.services.MainConnection;
import application.model.Account;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to generate and the views that
 * the user will see. It contains the methods used to generate
 * a 'Controller' to be applied to the supplied Stage for
 * particular FXMLScene. It also generates Controllers for
 * embedded FXML Scenes.
 *
 * @author James Gardner
 * @see    ViewInitialiser
 * @see    BaseController
 */
public class ViewFactory {

  private MainConnection mainConnection;
  private ViewInitialiser viewInitialiser;
  private static final Logger log = LoggerFactory.getLogger("ViewFactory");

  /**
   * Initialises a newly created {@code ViewFactory} object. Needs
   * access to the main client-server connection in order to
   * distribute this to the controllers. This constructor makes
   * creates a new {@code ViewInitialiser} and then calls the
   * other constructor created for testing purposes.
   *
   * @param mainConnection
   *        This is the main connection to the server, established on startup
   *
   */
  public ViewFactory(MainConnection mainConnection) {
    /* Makes a call to the other constructor that has been created for testing. */
    this(mainConnection, new ViewInitialiser());
  }

  /**
   * This constructor is used for testing the {@code ViewFactory}
   * by providing access to all fields so that Mockito
   * versions of those objects can be provided.
   *
   * @param mainConnection
   *        This is the main connection to the server, established on startup
   *
   * @param viewInitialiser
   *        The ViewInitialiser to connect Controllers with Scenes
   *
   */
  public ViewFactory(MainConnection mainConnection, ViewInitialiser viewInitialiser) {
    this.mainConnection = mainConnection;
    this.viewInitialiser = viewInitialiser;
  }

  /**
   * Creates a LoginWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Stage to the ViewInitialiser for setup.
   *
   * @param stage
   *        The Stage to contain the new Scene
   */
  public void showLoginWindow(Stage stage) {
    /* Each window needs a controller specific to it. This
     * is creating a new LoginWindowController using the
     * Abstract class BaseController. The LoginWindow.fxml
     * is passed in as an argument. */
    BaseController loginWindowController =
        new LoginWindowController(this, "fxml/LoginWindow.fxml", mainConnection);
    viewInitialiser.initialiseStage(loginWindowController, stage);
    log.info("ViewFactory: Login Window Setup");
  }

  /**
   * Creates a MainWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Stage to the ViewInitialiser for setup.
   *
   * @param stage
   *        The Stage to contain the new Scene
   */
  public void showMainWindow(Stage stage, Account account) {
    /* The MainWindowController takes the MainWindow.fxml
     * as its argument. These fxml files must be placed in
     * the correct folder: resources -> view -> fxml */
    BaseController mainWindowController =
        new MainWindowController(this, "fxml/MainWindow.fxml", mainConnection, account);
    viewInitialiser.initialiseStage(mainWindowController, stage);
    log.info("ViewFactory: Main Window Setup");
  }

  /**
   * Creates a RegisterWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Stage to the ViewInitialiser for setup.
   *
   * @param stage
   *        The Stage to contain the new Scene
   */
  public void showRegisterWindow(Stage stage) {
    BaseController registerWindowController =
        new RegisterWindowController(this, "fxml/RegisterWindow.fxml", mainConnection);
    viewInitialiser.initialiseStage(registerWindowController, stage);
    log.info("ViewFactory: Registration Window Setup");
  }

  /**
   * Creates a WhiteboardWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Stage to the ViewInitialiser for setup.
   *
   * @param stage
   *        The Stage to contain the new Scene
   */
  public void showWhiteboardWindow(Stage stage) {
    BaseController whiteboardWindowController =
        new WhiteboardWindowController(this, "fxml/WhiteboardWindow.fxml", mainConnection, "userID-000");
    viewInitialiser.initialiseStage(whiteboardWindowController, stage);
  }


  /**
   * DESCRIPTION.
   */
  public void showTextWindow(Stage stage) {
    BaseController TextWindowController =
        new TextWindowController(this, "fxml/TextWindow.fxml", mainConnection);
    viewInitialiser.initialiseStage(TextWindowController, stage);
  }

  /**
   * Creates a PresentationWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Stage to the ViewInitialiser for setup.
   *
   * @param stage
   *        The Stage to contain the new Scene
   */
  public void showPresentationWindow(Stage stage) {
    BaseController controller =
        new PresentationWindowController(this, "fxml/PresentationWindow.fxml", mainConnection);
    viewInitialiser.initialiseStage(controller, stage);
  }

  /**
   * Creates a MediaPlayerController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Stage to the ViewInitialiser for setup.
   *
   * @param stage
   *        The Stage to contain the new Scene
   */
  public void showMediaPlayerWindow(Stage stage) {
    BaseController mediaPlayerController =
        new MediaPlayerController(this, "fxml/MediaPlayerWindow.fxml", mainConnection);
    viewInitialiser.initialiseStage(mediaPlayerController, stage);
  }

  /**
   * Creates a WebcamWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Stage to the ViewInitialiser for setup.
   *
   * @param stage
   *        The Stage to contain the new Scene
   */
  public void showWebcamWindow(Stage stage) {
    BaseController webcamWindowController =
        new WebcamWindowController(this, "fxml/WebcamWindow.fxml", mainConnection);
    viewInitialiser.initialiseStage(webcamWindowController, stage);
  }

  /**
   * Creates a StreamWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Stage to the ViewInitialiser for setup.
   *
   * @param stage
   *        The Stage to contain the new Scene
   */
  public void showStreamWindow(Stage stage, Account account) {
    BaseController controller =
        new StreamWindowController(this, "fxml/StreamWindow.fxml", mainConnection, account);
    viewInitialiser.initialiseStage(controller, stage);
  }

  public void showTextChatWindow(Stage stage) {
    BaseController controller =
        new TextChatWindowController(this, "fxml/TextChatWindow.fxml", mainConnection);
    viewInitialiser.initialiseStage(controller, stage);
  }

  /**
   * Creates a ProfileWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Anchor Pane to the ViewInitialiser for setup.
   *
   * @param  anchorPane
   *         The Anchor Pane to contain the new Scene
   *
   * @param  mainWindowController
   *         ######################
   *
   * @throws IOException
   *         Thrown if the FXML file supplied with the Controller can't be found
   */
  public void embedProfileWindow(AnchorPane anchorPane, MainWindowController mainWindowController) throws IOException {
    BaseController profileWindowController = new ProfileWindowController(this,
        "fxml/ProfileWindow.fxml", mainConnection, mainWindowController);
    viewInitialiser.initialiseEmbeddedStage(profileWindowController, anchorPane);
  }

  /**
   * Creates a StreamWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Anchor Pane to the ViewInitialiser for setup.
   *
   * @param  anchorPane
   *         The Anchor Pane to contain the new Scene
   *
   * @param  account
   *         The user Account created once past the login stage
   *
   * @throws IOException
   *         Thrown if the FXML file supplied with the Controller can't be found
   */
  public void embedStreamWindow(AnchorPane anchorPane, Account account) throws IOException {
    BaseController streamWindowController = new StreamWindowController(this,
        "fxml/StreamWindow.fxml", mainConnection, account);
    viewInitialiser.initialiseEmbeddedStage(streamWindowController, anchorPane);
  }

  /**
   * Creates a DiscoverWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Anchor Pane to the ViewInitialiser for setup.
   *
   * @param  anchorPane
   *         The Anchor Pane to contain the new Scene
   *
   * @param  mainWindowController
   *         ######################
   *
   * @throws IOException
   *         Thrown if the FXML file supplied with the Controller can't be found
   */
  public void embedDiscoverWindow(AnchorPane anchorPane,
      MainWindowController mainWindowController) throws IOException {
    BaseController discoverWindowController = new DiscoverWindowController(this,
        "fxml/DiscoverWindow.fxml", mainConnection, mainWindowController);
    viewInitialiser.initialiseEmbeddedStage(discoverWindowController, anchorPane);
  }

  /**
   * Creates a SubjectWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Anchor Pane to the ViewInitialiser for setup.
   *
   * @param  anchorPane
   *         The Anchor Pane to contain the new Scene
   *
   *
   * @throws IOException
   *         Thrown if the FXML file supplied with the Controller can't be found
   */
  public void embedSubjectWindow(AnchorPane anchorPane, MainWindowController mainWindowController, int subject) throws IOException {
    BaseController subjectWindowContoller = new SubjectWindowContoller(this,
        "fxml/discoverWindows/SubjectWindow.fxml", mainConnection, mainWindowController, subject, anchorPane);
    viewInitialiser.initialiseEmbeddedStage(subjectWindowContoller, anchorPane);
  }

  /**
   * Creates a MediaPlayerController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Anchor Pane to the ViewInitialiser for setup.
   *
   * @param  anchorPane
   *         The Anchor Pane to contain the new Scene
   *
   * @throws IOException
   *         Thrown if the FXML file supplied with the Controller can't be found
   */
  public void embedMediaPlayerWindow(AnchorPane anchorPane) throws IOException {
    BaseController mediaPlayerController = new MediaPlayerController(this,
        "fxml/MediaPlayerWindow.fxml", mainConnection);
    viewInitialiser.initialiseEmbeddedStage(mediaPlayerController, anchorPane);
  }

  /**
   * Creates a WhiteboardWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Anchor Pane to the ViewInitialiser for setup.
   *
   * @param  anchorPane
   *         The Anchor Pane to contain the new Scene
   *
   * @throws IOException
   *         Thrown if the FXML file supplied with the Controller can't be found
   */
  public void embedWhiteboardWindow(AnchorPane anchorPane) throws IOException {
    BaseController whiteboardWindowController = new WhiteboardWindowController(this,
        "fxml/WhiteboardWindow.fxml", mainConnection, "userID-000");
    viewInitialiser.initialiseEmbeddedStage(whiteboardWindowController, anchorPane);
  }

  /**
   * Creates a PresentationWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Anchor Pane to the ViewInitialiser for setup.
   *
   * @param  anchorPane
   *         The Anchor Pane to contain the new Scene
   *
   * @throws IOException
   *         Thrown if the FXML file supplied with the Controller can't be found
   */
  public void embedPresentationWindow(AnchorPane anchorPane) throws IOException {
    BaseController presentationWindowController = new PresentationWindowController(this,
        "fxml/PresentationWindow.fxml", mainConnection);
    viewInitialiser.initialiseEmbeddedStage(presentationWindowController, anchorPane);
  }

  /**
   * Creates a TextChatWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Anchor Pane to the ViewInitialiser for setup.
   *
   * @param  anchorPane
   *         The Anchor Pane to contain the new Scene
   *
   * @throws IOException
   *         Thrown if the FXML file supplied with the Controller can't be found
   */
  public void embedTextChatWindow(AnchorPane anchorPane) throws IOException {
    BaseController textChatWindowController = new TextChatWindowController(this,
        "fxml/TextChatWindow.fxml", mainConnection);
    viewInitialiser.initialiseEmbeddedStage(textChatWindowController, anchorPane);
  }

  /**
   * Creates a TextChatWindowController, connect it to the
   * associated FXML file and sends this along with the
   * supplied Anchor Pane to the ViewInitialiser for setup.
   *
   * @param anchorPane
   *        The Anchor Pane to contain the new Scene
   *
   * @param  mainWindowController
   *         ######################
   *
   * @throws IOException
   *         Thrown if the FXML file supplied with the Controller can't be found
   */
  public void embedRecentWindow(AnchorPane anchorPane,
      MainWindowController mainWindowController) throws IOException {
    BaseController recentWindowController = new RecentWindowController(this,
        "fxml/homeWindows/RecentWindow.fxml", mainConnection, mainWindowController);
    viewInitialiser.initialiseEmbeddedStage(recentWindowController, anchorPane);
  }
}
