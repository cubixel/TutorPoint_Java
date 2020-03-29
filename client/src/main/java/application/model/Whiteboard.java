package application.model;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

public class Whiteboard {

  private Canvas canvas, canvasTemp;
  private GraphicsContext gc, gcTemp;
  private String selectedTool;
  private Line line = new Line();
  private Rectangle rect = new Rectangle();

  /**
   * @param canvas
   */
  public Whiteboard(Canvas canvas, Canvas canvasTemp) {
    this.canvas = canvas;
    this.canvasTemp = canvasTemp;

    gc = canvas.getGraphicsContext2D();
    gcTemp = canvasTemp.getGraphicsContext2D();

    // Set the shape of the stroke
    gc.setLineCap(StrokeLineCap.ROUND);
    gc.setMiterLimit(1);

    // Set the color and stroke width of the tool.
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(10);

    // Set the tool type.
    selectedTool = "pen";

    // Set the canvas height and width.
    canvas.setHeight(790);
    canvas.setWidth(1200);
    canvasTemp.setHeight(790);
    canvasTemp.setWidth(1200);
  }

  /**
   * Begins a new graphics context path when the primary mouse button is pressed.
   * Updates the state of the mouse to 'pressed'.
   */
  public void createNewStroke() {
    gc.beginPath();

    System.out.println("Start of new stroke.");
  }

  /**
   * Continues the new graphics context path when the primary mouse button is dragged.
   * Updates the state of the mouse to 'dragged'.
   */
  public void draw(MouseEvent mouseEvent) {
    gc.lineTo(mouseEvent.getX(), mouseEvent.getY());
    gc.stroke();

    System.out.println("Stroke xPos: " + mouseEvent.getX() + ", yPos: " + mouseEvent.getY());
  }

  /**
   * Draws an opaque line onto the canvas
   */
  public void highlight() {
    gcTemp.clearRect(0,0,1200,790);
    // Sets opacity to 40%
    gc.setStroke(Color.color(getStrokeColor().getRed(), getStrokeColor().getGreen(), getStrokeColor().getBlue(), 0.4));
    gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
  }

  /**
   * Continues the new graphics context path with a white stroke colour when the primary mouse button is dragged.
   * Updates the state of the mouse to 'dragged'.
   */
  public void erase(MouseEvent mouseEvent) {
    gc.setStroke(Color.WHITE);
    gc.lineTo(mouseEvent.getX(), mouseEvent.getY());
    gc.stroke();

    System.out.println("Stroke xPos: " + mouseEvent.getX() + ", yPos: " + mouseEvent.getY());
  }

  /**
   * Sets the start coordinates for a new rectangle.
   */
  public void startRect(MouseEvent mouseEvent) {
    rect.setX(mouseEvent.getX());
    rect.setY(mouseEvent.getY());
  }

  /**
   * Sets the start coordinates for a new line.
   */
  public void startLine(MouseEvent mouseEvent) {
    line.setStartX(mouseEvent.getX());
    line.setStartY(mouseEvent.getY());
  }

  /**
   * Sets the width and height for a new rectangle.
   */
  public void endRect(MouseEvent mouseEvent) {
    rect.setWidth(Math.abs((mouseEvent.getX()-rect.getX())));
    rect.setHeight(Math.abs((mouseEvent.getY()-rect.getY())));
    
    // TODO – Try to implement rectangle preview for drawing squares backwards.
//    if(rect.getX() > mouseEvent.getX())
//    {
//      rect.setWidth(Math.abs(-(mouseEvent.getX()-rect.getX())));
//    }
//    if(rect.getY() > mouseEvent.getY())
//    {
//      rect.setY(mouseEvent.getY());
//    }
  }

  /**
   * Sets the end coordinates for a new line.
   */
  public void endLine(MouseEvent mouseEvent) {
    line.setEndX(mouseEvent.getX());
    line.setEndY(mouseEvent.getY());
  }

  /**
   * Draws the new rectangle using the start coordinates, height and width.
   */
  public void drawRect(MouseEvent mouseEvent) {
    gcTemp.clearRect(0,0,1200,790);
    endRect(mouseEvent);
    gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
  }

  /**
   * Draws the new line using the start and end coordinates.
   */
  public void drawLine() {
    gcTemp.clearRect(0,0,1200,790);
    // Sets opacity to 0%
    gc.setStroke(Color.color(getStrokeColor().getRed(), getStrokeColor().getGreen(), getStrokeColor().getBlue(), 1));
    gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
  }

  public void drawRectEffect(MouseEvent mouseEvent) {
    gcTemp.setLineCap(StrokeLineCap.ROUND);
    gcTemp.setLineWidth(getStrokeWidth());
    gcTemp.setStroke(getStrokeColor());
    gcTemp.clearRect(0,0,1200,790);
    gcTemp.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    endRect(mouseEvent);
  }

  /**
   * Draws a preview line onto a temp canvas
   */
  public void drawLineEffect(MouseEvent mouseEvent) {
    gcTemp.setLineCap(StrokeLineCap.ROUND);
    gcTemp.setLineWidth(getStrokeWidth());
    // Sets opacity to 0%
    gcTemp.setStroke(Color.color(getStrokeColor().getRed(), getStrokeColor().getGreen(), getStrokeColor().getBlue(), 1));
    gcTemp.clearRect(0,0,1200,790);
    gcTemp.strokeLine(line.getStartX(), line.getStartY(), mouseEvent.getX(), mouseEvent.getY());
  }

  /**
   * Draws a preview opaque line onto a temp canvas
   */
  public void highlightEffect(MouseEvent mouseEvent) {
    gcTemp.setLineCap(StrokeLineCap.ROUND);
    gcTemp.setLineWidth(getStrokeWidth());
    // Sets opacity to 40%
    gcTemp.setStroke(Color.color(getStrokeColor().getRed(), getStrokeColor().getGreen(), getStrokeColor().getBlue(), 0.4));
    gcTemp.clearRect(0,0,1200,790);
    gcTemp.strokeLine(line.getStartX(), line.getStartY(), mouseEvent.getX(), mouseEvent.getY());
  }

  /**
   * Ends the new graphics context path when the primary mouse button is released.
   * Updates the state of the mouse to 'released'.
   */
  public void endNewStroke() {
    gc.closePath();

    System.out.println("End of new stroke.");
  }

  public void setStrokeColor(Color color) {
    gc.setStroke(color);

    System.out.println("Stroke colour changed to: " + color);
  }

  public Color getStrokeColor() {
    return (Color) gc.getStroke();
  }

  public void setStrokeWidth(double width) {
    gc.setLineWidth(width);

    System.out.println("Stroke width changed to: " + width);
  }

  public int getStrokeWidth() {
    return (int) gc.getLineWidth();
  }

  public void setStrokeTool(String tool) {
    selectedTool = tool;

    System.out.println("Whiteboard tool changed to: " + tool);
  }

  public String getStrokeTool() {
    return selectedTool;
  }

  public Canvas getCanvas() {
    return canvas;
  }
}
