package application.controller;

import application.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;

public class OptionsWindowController extends BaseController {

    public OptionsWindowController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
    }

    @FXML
    private Slider fontSizePicker;

    @FXML
    private ChoiceBox<?> themePicker;

    @FXML
    void applyButtonAction() {

    }
    @FXML
    void cancelButtonAction() {

    }


}