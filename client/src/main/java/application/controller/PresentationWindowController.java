package application.controller;

import application.controller.presentation.ImageHandler;
import application.controller.presentation.VideoHandler;
import application.controller.services.MainConnection;
import application.view.ViewFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class PresentationWindowController extends BaseController implements Initializable {

  @FXML
  private StackPane pane;

  @FXML
  private Canvas canvas;

  @FXML
  private Button draw1;

  @FXML
  private Button remove1;

  @FXML
  private Button draw2;

  @FXML
  private Button remove2;


  private ImageHandler imageHandler = null;
  private VideoHandler videoHandler = null;

  public PresentationWindowController(ViewFactory viewFactory, String fxmlName,
      MainConnection mainConnection) {
    super(viewFactory, fxmlName, mainConnection);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.imageHandler = new ImageHandler(pane);
    this.videoHandler = new VideoHandler(pane);
    imageHandler.register("https://homepages.cae.wisc.edu/~ece533/images/cat.png", "0:0", 200, 20, 200, 200);
    imageHandler.register("https://homepages.cae.wisc.edu/~ece533/images/tulips.png", "0:1", 20, 200, 300, 100);
    videoHandler.register("http://cubixel.ddns.net:52677/hls/Upload/test/1080p.m3u8", "0:3");
  }

  @FXML
  void drawImage1(ActionEvent event) {
    imageHandler.draw("0:0");
  }

  @FXML
  void removeImage1(ActionEvent event) {
    imageHandler.undraw("0:0");
  }

  @FXML
  void drawImage2(ActionEvent event) {
    videoHandler.play("0:1");
  }

  @FXML
  void removeImage2(ActionEvent event) {
    videoHandler.stop("0:1");
  }
}
