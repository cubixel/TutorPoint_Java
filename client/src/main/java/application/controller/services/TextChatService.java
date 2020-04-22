package application.controller.services;

import application.controller.enums.TextChatMessageResult;
import application.controller.enums.TextChatRequestResult;
import application.model.Message;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextChatService extends Thread {

  private MainConnection connection;
  private Message message;
  private TextChatSession sessionPackage;
  private static final Logger log = LoggerFactory.getLogger("TexChatService");

  public TextChatService(Message message, MainConnection connection, String userID, String sessionID) {
    this.message = message;
    this.connection = connection;
    this.sessionPackage = new TextChatSession(userID, sessionID, message);
  }

  /**
   * Method for sending message object and waiting on server reply.
   */
  public TextChatMessageResult send() {
    // Send message to server and waits reply.
    try {
      connection.sendString(connection.packageClass(this.message));
      String serverReply = connection.listenForString();
      return new Gson().fromJson(serverReply, TextChatMessageResult.class);
    } catch (IOException e) {
      e.printStackTrace();
      return TextChatMessageResult.FAILED_BY_NETWORK;
    } catch (Exception e) {
      e.printStackTrace();
      return TextChatMessageResult.FAILED_BY_UNEXPECTED_ERROR;
    }
  }

  public void setMessage(Message message) {
    this.message = message;
  }

  public Message getMessage() {
    return message;
  }

  protected Task<TextChatRequestResult> createTask() {
    return new Task<TextChatRequestResult>() {
      @Override
      protected TextChatRequestResult call() throws Exception {
        return send();
      }
    };
  }

  /**
   * Creates and sends a session package for the
   * local text chat to the server text chat handler.
   *
   *
   */
  public void sendSessionUpdates() {

    // Create session package to send to server.
    sessionPackage.setMessage(this.message);

    // Send package to server
    TextChatMessageResult result = send();
    switch (result) {
      case TEXT_CHAT_MESSAGE_SUCCESS:
        log.info("TextChat Session Package - Received.");
        break;
      case FAILED_BY_INCORRECT_USER_ID:
        log.warn("TextChat Session Package - Wrong user ID.");
        break;
      case FAILED_BY_UNEXPECTED_ERROR:
        log.warn("TextChat Session Package - Unexpected error.");
        break;
      case FAILED_BY_NETWORK:
        log.warn("TextChat Session Package - Network error.");
        send();
        break;
      default:
        log.warn("TextChat Session Package - Unknown error.");
    }
  }

  /**
   * Method to update the client text chat model using the
   * received session package.
   *
   * @param sessionPackage Received session package.
   */
  public void updateTextChatSession(JsonObject sessionPackage) {

    String SessionMessageString = sessionPackage.get("message").getAsString();
    Message sessionMessage =

    // Update the text chat
    setMessage();

    this.message = SessionMessage;

    log.debug(sessionPackage.toString());

  }
}