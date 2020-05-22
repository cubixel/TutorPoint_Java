package application.controller.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import application.controller.enums.StreamingStatusUpdateResult;
import java.io.IOException;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * Test class for the UpdateStreamingStatusService. Tests all return values
 * that can be received from the Server and also checks the error
 * handling from Network errors.
 *
 * @author James Gardner
 * @see UpdateStreamingStatusService
 */
public class UpdateStreamingStatusServiceTest {

  private UpdateStreamingStatusService updateStreamingStatusService;
  private String returnedString;
  volatile boolean threadDone;

  @Mock
  private MainConnection mainConnectionMock;

  /**
   * Sets up the JavaFX Toolkit for running JavaFX processes on.
   */
  @BeforeAll
  public static void setUpToolkit() {
    /* This method starts the JavaFX runtime. The specified Runnable will then be
     * called on the JavaFX Application Thread. */
    Platform.startup(() -> System.out.println("Toolkit initialized ..."));
  }

  /**
   * Initialises Mocks and creates a FollowSubjectRequestService instance to test on.
   */
  @BeforeEach
  public void setUp() {
    initMocks(this);

    updateStreamingStatusService = new UpdateStreamingStatusService(mainConnectionMock, true);

    threadDone = false;
  }

  @Test
  public void claimingConnectionTest() {
    try {
      returnedString = String.valueOf(StreamingStatusUpdateResult.STATUS_UPDATE_SUCCESS);
      doReturn(false).when(mainConnectionMock).claim();
      when(mainConnectionMock.listenForString()).thenReturn(returnedString);
    } catch (IOException e) {
      fail(e);
    }

    Platform.runLater(() -> {
      updateStreamingStatusService.start();
      long start = System.currentTimeMillis();
      long end = start + 2000;
      /*
       * The service should remain in a loop whilst the connection is not
       * free to use.
       */
      while (System.currentTimeMillis() < end) {
        StreamingStatusUpdateResult result = updateStreamingStatusService.getValue();
        assertNull(result);
        assertFalse(updateStreamingStatusService.isFinished());
      }
      doReturn(true).when(mainConnectionMock).claim();
      updateStreamingStatusService.setOnSucceeded(event -> {

        try {
          verify(mainConnectionMock, times(1)).sendString(any());
          verify(mainConnectionMock, times(1)).release();
        } catch (IOException e) {
          fail();
        }

        StreamingStatusUpdateResult result = updateStreamingStatusService.getValue();

        assertEquals(StreamingStatusUpdateResult.STATUS_UPDATE_SUCCESS, result);
        // Thread under test complete
        threadDone = true;
      });
    });

    while (!threadDone) {
      // This thread must wait until the thread under test is complete
      Thread.onSpinWait();
    }
  }

  @Test
  public void successfulResultTest() {
    // Setting Mock return value.
    try {
      returnedString = String.valueOf(StreamingStatusUpdateResult.STATUS_UPDATE_SUCCESS);
      when(mainConnectionMock.listenForString()).thenReturn(returnedString);
      when(mainConnectionMock.claim()).thenReturn(true);
    } catch (IOException e) {
      fail(e);
    }
    Platform.runLater(() -> {
      updateStreamingStatusService.start();
      updateStreamingStatusService.setOnSucceeded(event -> {

        try {
          verify(mainConnectionMock, times(1)).sendString(any());
          verify(mainConnectionMock, times(1)).release();
        } catch (IOException e) {
          fail();
        }

        StreamingStatusUpdateResult result = updateStreamingStatusService.getValue();

        assertEquals(StreamingStatusUpdateResult.STATUS_UPDATE_SUCCESS, result);
        threadDone = true;
      });
    });

    while (!threadDone) {
      Thread.onSpinWait();
    }
  }

  @Test
  public void failedAccessingDatabaseTest() {
    // Setting Mock return value.
    try {
      returnedString = String.valueOf(StreamingStatusUpdateResult.FAILED_ACCESSING_DATABASE);
      when(mainConnectionMock.listenForString()).thenReturn(returnedString);
      when(mainConnectionMock.claim()).thenReturn(true);
    } catch (IOException e) {
      fail(e);
    }

    Platform.runLater(() -> {
      updateStreamingStatusService.start();
      updateStreamingStatusService.setOnSucceeded(event -> {

        try {
          verify(mainConnectionMock, times(1)).sendString(any());
          verify(mainConnectionMock, times(1)).release();
        } catch (IOException e) {
          fail();
        }

        StreamingStatusUpdateResult result = updateStreamingStatusService.getValue();

        assertEquals(StreamingStatusUpdateResult.FAILED_ACCESSING_DATABASE, result);
        threadDone = true;
      });
    });

    while (!threadDone) {
      Thread.onSpinWait();
    }
  }

  @Test
  public void failingByNetworkTest() {
    // Setting Mock return value.
    try {
      when(mainConnectionMock.claim()).thenReturn(true);
      when(mainConnectionMock.listenForString()).thenReturn(returnedString);
      when(mainConnectionMock.claim()).thenReturn(true);
      doThrow(IOException.class).when(mainConnectionMock).sendString(any());
    } catch (IOException e) {
      fail(e);
    }

    Platform.runLater(() -> {
      updateStreamingStatusService.start();
      updateStreamingStatusService.setOnSucceeded(event -> {

        try {
          verify(mainConnectionMock, times(1)).sendString(any());
        } catch (IOException e) {
          fail();
        }

        StreamingStatusUpdateResult result = updateStreamingStatusService.getValue();

        assertEquals(StreamingStatusUpdateResult.FAILED_BY_NETWORK, result);
        threadDone = true;
      });
    });

    while (!threadDone) {
      Thread.onSpinWait();
    }
  }
}