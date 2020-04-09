package application.model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Whiteboard {

  private Canvas canvas;
  private GraphicsContext gc;
  private GraphicsContext gcTemp;
  private Point2D mouseStart;
  private Point2D mouseEnd;
  private Line line = new Line();
  private static final Logger log = LoggerFactory.getLogger("Whiteboard");

  /**
   * Model for the whiteboard screen.
   * @param canvas Main canvas that content is drawn to.
   * @param canvasTemp Canvas that content is drawn to before the main canvas.
   */
  public Whiteboard(Canvas canvas, Canvas canvasTemp) {
    this.canvas = canvas;

    // Initialise the main and temp graphics context.
    gc = canvas.getGraphicsContext2D();
    gcTemp = canvasTemp.getGraphicsContext2D();

    // Set the shape of the stroke
    gc.setLineCap(StrokeLineCap.ROUND);
    gc.setMiterLimit(1);
    gcTemp.setLineCap(StrokeLineCap.ROUND);
    gcTemp.setMiterLimit(1);

    // Set the color and stroke width of the tool.
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(10);
    gcTemp.setStroke(Color.BLACK);
    gcTemp.setLineWidth(10);

    // Set the canvas and temp canvas height and width.
    canvas.setHeight(790);
    canvas.setWidth(1200);
    canvasTemp.setHeight(790);
    canvasTemp.setWidth(1200);

    line = new Line();
    mouseStart = new Point2D(0,0);
    mouseEnd = new Point2D(0,0);
  }

  /**
   * Begins a new graphics context path when the primary mouse button is pressed.
   * Updates the state of the mouse to 'pressed'.
   */
  public void createNewStroke() {
    gc.beginPath();
    log.info("Start of new stroke.");
  }

  /**
   * Ends the new graphics context path when the primary mouse button is released.
   * Updates the state of the mouse to 'released'.
   */
  public void endNewStroke() {
    gc.closePath();
    log.info("End of new stroke.");
  }

  /**
   * Continues the new graphics context path when the primary mouse button is dragged.
   * Updates the state of the mouse to 'dragged'.
   */
  public void draw(MouseEvent mouseEvent) {
    gc.lineTo(mouseEvent.getX(), mouseEvent.getY());
    gc.stroke();
  }

  /**
   * Draws an opaque line onto the canvas.
   */
  public void highlight() {
    gcTemp.clearRect(0,0,1200,790);
    // Set opacity to 40%
    gc.setStroke(Color.color(getStrokeColor().getRed(), getStrokeColor().getGreen(),
        getStrokeColor().getBlue(), 0.4));
    gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
  }

  /**
   * Continues the new graphics context path with a white stroke colour when the
   *  primary mouse button is dragged.
   * Updates the state of the mouse to 'dragged'.
   */
  public void erase(MouseEvent mouseEvent) {
    gc.lineTo(mouseEvent.getX(), mouseEvent.getY());
    gc.stroke();
  }

  /**
   * Sets the start coordinates for a new line.
   */
  public void startLine(MouseEvent mouseEvent) {
    line.setStartX(mouseEvent.getX());
    line.setStartY(mouseEvent.getY());
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
    mouseEnd = new Point2D(mouseEvent.getX(), mouseEvent.getY());
    gc.strokeRect(Math.min(mouseStart.getX(), mouseEnd.getX()),
        Math.min(mouseStart.getY(), mouseEnd.getY()),
        Math.abs(mouseStart.getX() - mouseEnd.getX()),
        Math.abs(mouseStart.getY() - mouseEnd.getY()));
  }

  /**
   * Draws the new circle using the start coordinates and radius.
   */
  public void drawCirc(MouseEvent mouseEvent) {
    gcTemp.clearRect(0,0,1200,790);
    mouseEnd = new Point2D(mouseEvent.getX(), mouseEvent.getY());
    gc.strokeOval(Math.min(mouseStart.getX(), mouseEnd.getX()),
        Math.min(mouseStart.getY(), mouseEnd.getY()),
        Math.abs(mouseStart.getX() - mouseEnd.getX()),
        Math.abs(mouseStart.getY() - mouseEnd.getY()));
  }

  /**
   * Draws the new line using the start and end coordinates.
   */
  public void drawLine() {
    gcTemp.clearRect(0,0,1200,790);
    // Set opacity to 0%
    gc.setStroke(Color.color(getStrokeColor().getRed(), getStrokeColor().getGreen(),
        getStrokeColor().getBlue(), 1));
    gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
  }

  /**
   * Draws a new text object using the start and end coordinates.
   */
  public void drawText(String text, MouseEvent mouseEvent) {
    gcTemp.clearRect(0,0,1200,790);
    mouseEnd = new Point2D(mouseEvent.getX(), mouseEvent.getY());
    gc.setFont(Font.font(Math.sqrt((Math.pow((mouseEnd.getX() - mouseStart.getX()), 2))
        + Math.pow((mouseEnd.getY() - mouseStart.getY()), 2)) / 2));
    gc.setFill(getStrokeColor());
    gc.setStroke(getStrokeColor());
    gc.fillText(text, mouseStart.getX(), mouseStart.getY());
    //gc.strokeText(text.getText(), textStart.getX(), textStart.getY());
  }

  /**
   * Draws a preview opaque line onto a temp canvas.
   */
  public void highlightEffect(MouseEvent mouseEvent) {
    gcTemp.setLineCap(StrokeLineCap.ROUND);
    gcTemp.setLineWidth(getStrokeWidth());
    // Set opacity to 40%
    gcTemp.setStroke(Color.color(getStrokeColor().getRed(), getStrokeColor().getGreen(),
        getStrokeColor().getBlue(), 0.4));
    gcTemp.clearRect(0,0,1200,790);
    gcTemp.strokeLine(line.getStartX(), line.getStartY(), mouseEvent.getX(), mouseEvent.getY());
  }

  /**
   * Function to show the rectangle size and position before placing on canvas.
   * @param mouseEvent User input.
   */
  public void drawRectEffect(MouseEvent mouseEvent) {
    gcTemp.setLineWidth(getStrokeWidth());
    gcTemp.setStroke(getStrokeColor());
    gcTemp.clearRect(0,0,1200,790);
    mouseEnd = new Point2D(mouseEvent.getX(), mouseEvent.getY());
    gcTemp.strokeRect(Math.min(mouseStart.getX(), mouseEnd.getX()),
        Math.min(mouseStart.getY(), mouseEnd.getY()),
        Math.abs(mouseStart.getX() - mouseEnd.getX()),
        Math.abs(mouseStart.getY() - mouseEnd.getY()));
  }

  /**
   * Function to show the circle size and position before placing on canvas.
   * @param mouseEvent User input.
   */
  public void drawCircEffect(MouseEvent mouseEvent) {
    gcTemp.setLineCap(StrokeLineCap.ROUND);
    gcTemp.setLineWidth(getStrokeWidth());
    gcTemp.setStroke(getStrokeColor());
    gcTemp.clearRect(0,0,1200,790);
    mouseEnd = new Point2D(mouseEvent.getX(), mouseEvent.getY());
    gcTemp.strokeOval(Math.min(mouseStart.getX(), mouseEnd.getX()),
        Math.min(mouseStart.getY(), mouseEnd.getY()),
        Math.abs(mouseStart.getX() - mouseEnd.getX()),
        Math.abs(mouseStart.getY() - mouseEnd.getY()));
  }

  /**
   * Draws a preview line onto a temp canvas.
   */
  public void drawLineEffect(MouseEvent mouseEvent) {
    gcTemp.setLineCap(StrokeLineCap.ROUND);
    gcTemp.setLineWidth(getStrokeWidth());
    // Sets opacity to 100%
    gcTemp.setStroke(Color.color(getStrokeColor().getRed(), getStrokeColor().getGreen(),
        getStrokeColor().getBlue(), 1));
    gcTemp.clearRect(0,0,1200,790);
    gcTemp.strokeLine(line.getStartX(), line.getStartY(), mouseEvent.getX(), mouseEvent.getY());
  }

  /**
   * Draws a preview text object onto a temp canvas.
   */
  public void drawTextEffect(String text, MouseEvent mouseEvent) {
    mouseEnd = new Point2D(mouseEvent.getX(), mouseEvent.getY());
    gcTemp.setFont(Font.font(Math.sqrt((Math.pow((mouseEnd.getX() - mouseStart.getX()), 2))
        + Math.pow((mouseEnd.getY() - mouseStart.getY()), 2)) / 2));
    gcTemp.setFill(getStrokeColor());
    gcTemp.setStroke(getStrokeColor());
    gcTemp.clearRect(0,0,1200,790);
    gcTemp.fillText(text, mouseStart.getX(), mouseStart.getY());
    //gcTemp.strokeText(text.getText(), textStart.getX(), textStart.getY());
  }

  public void setStrokeColor(Color color) {
    gc.setStroke(color);
  }

  public Color getStrokeColor() {
    return (Color) gc.getStroke();
  }

  public void setStrokeWidth(double width) {
    gc.setLineWidth(width);
  }

  public int getStrokeWidth() {
    return (int) gc.getLineWidth();
  }

  /**
   * Sets the start coordinates for a new rectangle.
   */
  public void setStartPosition(MouseEvent mouseEvent) {
    mouseStart = new Point2D(mouseEvent.getX(), mouseEvent.getY());
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public GraphicsContext getGraphicsContext() {
    return gc;
  }
}
