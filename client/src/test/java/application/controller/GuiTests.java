package application.controller;

import static org.mockito.MockitoAnnotations.initMocks;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;

public class GuiTests {

    @BeforeAll
    public static void setUpToolkit(){
        /* This method starts the JavaFX runtime. The specified Runnable will then be
         * called on the JavaFX Application Thread. */
        Platform.startup(() -> System.out.println("Toolkit initialized ..."));
    }

    @Nested
    class Login extends LoginWindowControllerTest{

        @BeforeEach
        public void setUp(){
            /* Initializes objects annotated with Mockito annotations, e.g. @Mock. */
            initMocks(this);
            usernameField = new TextField();
            passwordField = new PasswordField();
            errorLabel = new Label();

            loginWindowController = new LoginWindowController(viewFactoryMock,null, mainConnectionMock,
                                                            usernameField, passwordField, errorLabel, loginServiceMock);
        }

        @Test
        public void doTestFieldsValidation(){testFieldsValidation();}

        @Test
        public void doTestLoginAction(){testLoginAction();}

    }

    @Nested
    class Register extends RegisterWindowControllerTest{
        
        @BeforeEach
        public void setUp(){
            /* Initializes objects annotated with Mockito annotations, e.g. @Mock. */
            initMocks(this);
            usernameField = new TextField();
            passwordField = new PasswordField();
            errorLabel = new Label();
            isTutorCheckBox = new CheckBox();

            registerWindowController = new RegisterWindowController(viewFactoryMock,null, mainConnectionMock,
                    usernameField, passwordField, errorLabel, isTutorCheckBox, registerServiceMock);
        }

        @Test
        public void doTestFieldsValidation(){testFieldsValidation();}

        @Test
        public void doTestRegisterAction(){testRegisterAction();}
    }

    @Nested
    class Whiteboard extends WhiteboardWindowControllerTest {

        @BeforeEach
        public void setUp() {
            initMocks(this);

            /*
            canvas = new Canvas();
            widthSlider = new Slider();
            colorPicker = new ColorPicker();

            whiteboardWindowController = new WhiteboardWindowController(viewFactoryMock, "WhiteboardWindow", mainConnectionMock, canvas, widthSlider, colorPicker);
             */

            whiteboardWindowController = new WhiteboardWindowController(viewFactoryMock, "WhiteboardWindow", mainConnectionMock);
        }

        @Test
        public void doTestWhiteboardInitialisation(){testWhiteboardInitialisation();}

        @Test
        public void doTestSelectTool(){testSelectTool();}

        @Test
        public void doTestChangeColor(){testChangeColor();}

        @Test
        public void doTestChangeWidth(){testChangeWidth();}
    }
    
}