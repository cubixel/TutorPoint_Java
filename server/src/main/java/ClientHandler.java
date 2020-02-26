import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import services.AccountLoginResult;
import services.AccountRegisterResult;
import sql.MySQL;

public class ClientHandler extends Thread {

    private int token;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private MySQL sqlConnection;
    private long lastHeartbeat;
    private boolean loggedIn;

    // int numberOfSubjectSent

    public ClientHandler(DataInputStream dis, DataOutputStream dos, int token, MySQL sqlConnection){
        setDaemon(true);
        this.dis = dis;
        this.dos = dos;
        this.token = token;
        this.sqlConnection = sqlConnection;
        this.lastHeartbeat = System.currentTimeMillis();
        this.loggedIn = true;
    }

    @Override
    public void run() {
        // Does the client need to know its number?
        //writeString("Token#" + token);
        String received = null;

        while (lastHeartbeat > (System.currentTimeMillis() - 10000) & loggedIn){
            // Do stuff with this client in this thread
            // when client disconnects then close it down.

            try {

                while(dis.available() > 0) {
                    received = dis.readUTF();
                }

                if (received != null){
                    try {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(received, JsonObject.class);
                        String action = jsonObject.get("Class").getAsString();
                        System.out.println(action);

                        if (action.equals("Account")) {
                            if (jsonObject.get("isRegister").getAsInt() == 1) {
                                createNewUser(jsonObject.get("username").getAsString(), jsonObject.get("hashedpw").getAsString(),
                                        jsonObject.get("tutorStatus").getAsInt());
                            }else {
                                loginUser(jsonObject.get("username").getAsString(), jsonObject.get("hashedpw").getAsString());
                            }
                        }

                        if (action.equals("SubjectManager")){
                            getSubjects();
                        }

                    } catch (JsonSyntaxException e){
                        if (received.equals("Heartbeat")) {
                            lastHeartbeat = System.currentTimeMillis();
                            System.out.println("Recieved Heartbeat from client " + token + " at " + lastHeartbeat);
                        } else {
                            System.out.println("Recieved string: " + received);
                            writeString(received);
                        }
                    }
                    received = null;
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }


        }

        System.out.println("Client " + token + " disconnected");
    }

    public String readString(){
        try {
            return dis.readUTF();
        } catch (IOException e){
            System.out.println(e);
            return null;
        }
    }

    public void writeString(String msg){
        try {
            dos.writeUTF(msg);
        } catch (IOException IOE){
            IOE.printStackTrace();
        }
    }

    public void loginUser(String username, String password) throws SQLException, IOException {
        Gson gson = new Gson();
        if (sqlConnection.checkUserDetails(username, password) == false){
            JsonElement jsonElement = gson.toJsonTree(AccountLoginResult.FAILED_BY_CREDENTIALS);
            dos.writeUTF(gson.toJson(jsonElement));
            System.out.println(gson.toJson(jsonElement));
        }else{
            JsonElement jsonElement = gson.toJsonTree(AccountLoginResult.SUCCESS);
            dos.writeUTF(gson.toJson(jsonElement));
            System.out.println(gson.toJson(jsonElement));
        }
    }

    public void createNewUser(String username, String password, int isTutor) throws IOException {
        Gson gson = new Gson();
        if (sqlConnection.getUserDetails(username) == false){
            if (sqlConnection.createAccount(username, password, isTutor)){

                JsonElement jsonElement = gson.toJsonTree(AccountRegisterResult.SUCCESS);
                dos.writeUTF(gson.toJson(jsonElement));
            } else {
                JsonElement jsonElement = gson.toJsonTree(AccountRegisterResult.FAILED_BY_UNEXPECTED_ERROR);
                dos.writeUTF(gson.toJson(jsonElement));
            }
        }else {
            JsonElement jsonElement = gson.toJsonTree(AccountRegisterResult.FAILED_BY_CREDENTIALS);
            dos.writeUTF(gson.toJson(jsonElement));
        }
    }

    public void getSubjects(){

    }

    public String toString()
    {
        return "This is client " + token;
    }

    public void logOff(){
        this.loggedIn = false;
    }
}
