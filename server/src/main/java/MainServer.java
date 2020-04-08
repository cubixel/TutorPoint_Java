import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sql.MySql;
import sql.MySqlFactory;

/**
 * CLASS DESCRIPTION: ################# MainServer is the top level server
 * class. It runs on a separate thread to Main........
 *
 * @author CUBIXEL
 *
 */
public class MainServer extends Thread {

  private ServerSocket serverSocket;
  private ServerSocket dataServerSocket;
  private SocketAcceptor serverAcceptor;
  private SocketAcceptor dataAcceptor;
  private Socket socket;
  private Socket dataSocket;
  private String databaseName;

  private DataInputStream dis;
  private DataOutputStream dos;
  private DataInputStream dataIn;
  private DataOutputStream dataOut;

  private int clientToken = 0;

  private ArrayList<WhiteboardHandler> activeSessions;
  private HashMap<Integer, ClientHandler> activeClients;

  private MySqlFactory mySqlFactory;
  private MySql sqlConnection;

  /*
   * Logger used by Server. Prints to both the console and to a file 'logFile.log'
   * saved under resources/logs. All classes in Server should create a Logger of
   * the same name.
   */
  private static final Logger log = LoggerFactory.getLogger("Server Logger");

  /**
   * Constructor that creates a serverSocket on a specific Port Number.
   *
   * @param port Port Number.
   */
  public MainServer(int port) throws IOException {
    databaseName = "tutorpointnew";
    mySqlFactory = new MySqlFactory(databaseName);
    activeClients = new HashMap<>();

    // This should probably be synchronized
    activeSessions = new ArrayList<>();

    serverSocket = new ServerSocket(port);
    dataServerSocket = new ServerSocket(port + 1);
  }

  /**
   * CONSTRUCTOR DESCRIPTION.
   *
   * @param port         DESCRIPTION
   * @param databaseName DESCRIPTION
   */
  public MainServer(int port, String databaseName) {
    this.databaseName = databaseName;
    mySqlFactory = new MySqlFactory(databaseName);
    activeClients = new HashMap<>();
    // This should probably be synchronized
    activeSessions = new ArrayList<>();

    try {
      serverSocket = new ServerSocket(port);
      dataServerSocket = new ServerSocket(port + 1);
      // serverSocket.setSoTimeout(2000);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * CONSTRUCTOR DESCRIPTION.
   *
   * @param port         DESCRIPTION
   * @param mySqlFactory DESCRIPTION
   * @param databaseName DESCRIPTION
   */
  public MainServer(int port, MySqlFactory mySqlFactory, String databaseName) {
    this.databaseName = databaseName;
    this.mySqlFactory = mySqlFactory;
    activeClients = new HashMap<>();
    // This should probably be synchronized
    activeSessions = new ArrayList<>();

    try {
      serverSocket = new ServerSocket(port);
      dataServerSocket = new ServerSocket(port + 1);
      // serverSocket.setSoTimeout(2000);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void run() {
    serverAcceptor = new SocketAcceptor(serverSocket, socket, "Client");
    dataAcceptor = new SocketAcceptor(dataServerSocket, dataSocket, "Data");

    serverAcceptor.start();
    dataAcceptor.start();

    /* Main server should sit in this loop waiting for clients */
    while (true) {
      try {
        if (socket != null) {
          log.info("New Client Accepted: Token " + clientToken);

          dis = new DataInputStream(socket.getInputStream());
          dos = new DataOutputStream(socket.getOutputStream());

          sqlConnection = mySqlFactory.createConnection();

          ClientHandler ch = new ClientHandler(dis, dos, clientToken, sqlConnection,
              activeSessions);

          Thread t = new Thread(ch);

          activeClients.put(clientToken, ch);

          t.start();

          clientToken++;
          socket = null;
        }

        if (dataSocket != null) {
          log.info("New Data Connection Accepted");

          dataIn = new DataInputStream(dataSocket.getInputStream());
          dataOut = new DataOutputStream(dataSocket.getOutputStream());

          Integer incomingToken = dataIn.readInt();
          activeClients.get(incomingToken);

          dataSocket = null;
        }

      } catch (IOException e) {
        log.error("Failed to create DataInput/OutputStreams", e);
      } catch (SQLException e) {
        log.error("Failed to connect to MySQL database", e);
      }
    }
  }

  /**
   * METHOD DESCRIPTION.
   */
  public ClientHandler getClientHandler() {
    /*
     * ###################################################
     * Would you ever need to select from other clients
     * This is just client 0 atm.
     * ###################################################
     */
    return this.activeClients.get(0);
  }

  public HashMap<Integer, ClientHandler> getActiveClients() {
    return activeClients;
  }


  public boolean isSocketClosed() {
    return this.serverSocket.isClosed();
  }

  /* Getter method for binding state of serverSocket.
    * Returns true if the ServerSocket is successfully
    * bound to an address.
    * */
  public boolean isBound() {
    return serverSocket.isBound();
  }

  /**
   * Main entry point for program.
   */
  public static void main(String[] args) {
    MainServer main = null;
    try {
      main = new MainServer(5000);
      main.start();
      log.info("Server started successfully");
    } catch (IOException e) {
      log.error("Could not start the server", e);
    }

  }
}
