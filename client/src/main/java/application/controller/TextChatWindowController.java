package application.controller;

import application.controller.enums.TextChatRequestResult;
import application.controller.services.MainConnection;
import application.controller.services.TextChatRequestService;
import application.controller.services.TextChatService;
import application.model.Message;
import application.model.managers.MessageManager;
import application.view.ViewFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CLASS DESCRIPTION: This class is used to generate an interactive text chat which allows multiple
 * users to send and receive messages.
 *
 * @author Oli Clarke
 */

public class TextChatWindowController extends BaseController implements Initializable {

  private TextChatService textChatService;                  // Service for text chat.
  private TextChatRequestService textChatRequestService;    // Request service for text chat.
  private MainConnection connection;                        // Connection to server
  private Integer userID;                                   // Client User ID
  private Integer sessionID;                                // Connected Text Chat Session ID
  private Message message;                                  // Most recent message in current chat.
  private MessageManager messageManager;                    // Manager for all messages in session.

  private static final Logger log = LoggerFactory.getLogger("TextChatWindowController");

  @FXML
  private TextField textChatInput;

  @FXML
  private Button textChatSendButton;

  @FXML
  private VBox textChatVBox;

  @FXML
  void sendMsgButton() {
    sendMsgText();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.message = new Message(userID, sessionID, "init message");
    this.messageManager = new MessageManager(textChatVBox);
    startService();
    this.textChatRequestService = new TextChatRequestService(connection, userID, sessionID);
    sendRequest();
    addActionListeners();
    log.info("Text Chat Initialised.");
  }

  /**
   * Main class constructor.
   */
  public TextChatWindowController(ViewFactory viewFactory, String fxmlName,
      MainConnection mainConnection, Integer userID, Integer sessionID) {
    super(viewFactory, fxmlName, mainConnection);
    this.message = new Message(userID, sessionID, "init message");
    this.connection = mainConnection;
    this.userID = userID;
    this.sessionID = sessionID;
  }


  /**
   * Test class constructor.
   */
  public TextChatWindowController(ViewFactory viewFactory, String fxmlName,
      MainConnection mainConnection, Integer userID, Integer sessionID,
      TextField textChatInput, Button textChatSendButton) {
    super(viewFactory, fxmlName, mainConnection);
    this.message = new Message(userID, sessionID, "init message");
    this.connection = mainConnection;
    this.userID = userID;
    this.sessionID = sessionID;
    this.textChatInput = textChatInput;
    this.textChatSendButton = textChatSendButton;
  }


  /**
   * Method to send request for text chat.
   */
  private void sendRequest() {
    if (!textChatRequestService.isRunning()) {
      textChatRequestService.reset();
      textChatRequestService.start();
    }

    textChatRequestService.setOnSucceeded(event -> {
      TextChatRequestResult result = textChatRequestService.getValue();
      switch (result) {
        case SESSION_REQUEST_TRUE:
          log.info("Text Chat Session Request - True.");
          break;
        case SESSION_REQUEST_FALSE:
          log.info("Text Chat Session Request - False.");
          log.info("New Text Chat Session Created - Session ID: " + sessionID);
          break;
        case FAILED_BY_NETWORK:
          log.warn("Text Chat Session Request - Network error.");
          break;
        default:
          log.warn("Text Chat Session Request - Unknown error.");
      }
    });
  }

  /**
   * Method to start service for text chat.
   */
  private void startService() {
    this.textChatService = new TextChatService(this.message, this.messageManager, this.connection,
        this.userID,
        this.sessionID);
    this.connection.getListener().setTextChatService(textChatService);
    this.textChatService.start();
  }

  /**
   * Method to initialise the main text chat UI action listeners.
   */
  private void addActionListeners() {

    // 'ENTER' key to send message from text field.
    textChatInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent key) {
        if (key.getCode().equals(KeyCode.ENTER)) {
          sendMsgText();
        }
      }
    });

  }

  /**
   * Method to send session update of client's typed message.
   */
  private void sendMsgText() {
    if (!textChatInput.getText().isEmpty()) {

      // Update Message Contents of controller.
      this.message.setMsg(textChatInput.getText());
      this.message.setUserID(this.userID);
      this.message.setSessionID(this.sessionID);

      // Pass local message to service.
      this.textChatService.sendSessionUpdates(this.message);

      textChatInput.clear();
    }
  }

  /**
   * GETTERS & SETTERS.
   **/

  public Message getMessage() {
    return message;
  }

}