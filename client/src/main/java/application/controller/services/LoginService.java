package application.controller.services;

import application.model.account.Account;
import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;

// extends the javafx implementation of multithreading (service)
// this should connect to the server, send over the user details and confirm
// the result
public class LoginService extends Service<AccountLoginResult> {

    Account account;
    MainConnection connection;

    public LoginService(Account account, MainConnection mainConnection) {
        this.account = account;
        this.connection = mainConnection;
    }

    public void setAccount(Account account){
        this.account = account;
    }

    private AccountLoginResult login(){
        String jsonAccount = connection.packageClass(this.account);
        try {
            connection.sendString(jsonAccount);
            String serverReply = connection.listenForString();
            return new Gson().fromJson(serverReply, AccountLoginResult.class);
        } catch (IOException e){
            e.printStackTrace();
            return AccountLoginResult.FAILED_BY_NETWORK;
        }catch (Exception e){
            e.printStackTrace();
            return AccountLoginResult.FAILED_BY_UNEXPECTED_ERROR;
        }
    }

    @Override
    protected Task<AccountLoginResult> createTask() {
        return new Task<AccountLoginResult>() {
            @Override
            protected AccountLoginResult call() throws Exception {
                return login();
            }
        };
    }

}

