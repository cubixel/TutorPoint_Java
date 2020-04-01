package application.view;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import application.controller.services.MainConnection;
import application.model.Account;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

public class ViewFactoryTest {

  private ViewFactory viewFactory;

  @Mock
  private Stage stageMock;

  @Mock
  private MainConnection mainConnectionMock;
  
  @Mock
  private ViewInitialiser viewInitialiserMock;

  @Mock
  private Account accountMock;

  @Mock
  private Logger log;

  @BeforeEach
  public void setUp() {
    initMocks(this);
    viewFactory = new ViewFactory(mainConnectionMock, viewInitialiserMock, log);
  }

  @Test
  public void testShowLoginWindow() {
    viewFactory.showLoginWindow(stageMock);
    verify(viewInitialiserMock).initialiseStage(any(), any());
  }

  @Test
  public void testShowRegisterWindow() {
    viewFactory.showRegisterWindow(stageMock);
    verify(viewInitialiserMock).initialiseStage(any(), any());
  }

  @Test
  public void testShowMainWindow() {
    viewFactory.showMainWindow(stageMock, accountMock);
    verify(viewInitialiserMock).initialiseStage(any(), any());
  }

  @Test
  public void testShowWhiteboardWindow() {
    viewFactory.showWhiteboardWindow(stageMock);
    verify(viewInitialiserMock).initialiseStage(any(), any());
  }

  @Test
  public void testShowMediaPlayerWindow() {
    viewFactory.showMediaPlayerWindow(stageMock);
    verify(viewInitialiserMock).initialiseStage(any(), any());
  }

  @Test
  public void testShowWebcamWindow() {
    viewFactory.showWebcamWindow(stageMock);
    verify(viewInitialiserMock).initialiseStage(any(), any());
  }

}
