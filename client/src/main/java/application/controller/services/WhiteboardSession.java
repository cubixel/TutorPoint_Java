package application.controller.services;

import java.util.UUID;
import javafx.scene.paint.Color;

public class WhiteboardSession {

  private String sessionID;
  private String tutorID;
  private String mouseState;
  private boolean tutorOnlyAccess;
  private Color strokeColor;
  private int strokeWidth;
  private double strokeXPosition;
  private double strokeYPosition;


  public WhiteboardSession(String tutorID) {
    this.sessionID = UUID.randomUUID().toString();
    this.tutorID = tutorID;
    this.mouseState = "idle";
    this.tutorOnlyAccess = true;
    this.strokeColor = Color.BLACK;
    this.strokeWidth = -1;
    this.strokeXPosition = -1;
    this.strokeYPosition = -1;

  }

  public void setTutorOnlyAccess(boolean tutorOnlyAccess) {
    this.tutorOnlyAccess = tutorOnlyAccess;
  }

  public void setStrokeColor(Color strokeColor) {
    this.strokeColor = strokeColor;
  }

  public void setStrokeWidth(int strokeWidth) {
    this.strokeWidth = strokeWidth;
  }

  public void setStrokePosition(double xPos, double yPos) {
    this.strokeXPosition = xPos;
    this.strokeYPosition = yPos;
  }

  public void setMouseState(String mouseState) {
    this.mouseState = mouseState;
  }
}
