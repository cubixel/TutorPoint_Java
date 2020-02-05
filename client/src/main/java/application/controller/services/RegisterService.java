package application.controller.services;

import application.model.account.Account;
import com.google.gson.Gson;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;

public class RegisterService extends Service<AccountRegisterResult> {

    Account account;
    MainConnection connection;

    public RegisterService(Account account, MainConnection connection) {
        this.account = account;
        this.connection = connection;
    }

    public void setAccount(Account account){
        this.account = account;
    }

    private AccountRegisterResult register() {
        // check username no other users
        // wait for response from server as to success of registering
        // respond with AccountRegisterResults for each case.
        try {
            connection.sendString(connection.packageClass(this.account));
            String serverReply = connection.listenForString();
            return new Gson().fromJson(serverReply, AccountRegisterResult.class);
        } catch (IOException e){
            e.printStackTrace();
            return AccountRegisterResult.FAILED_BY_NETWORK;
        } catch (Exception e){
            e.printStackTrace();
            return  AccountRegisterResult.FAILED_BY_UNEXPECTED_ERROR;
        }
    }

    @Override
    protected Task<AccountRegisterResult> createTask() {
        return new Task<AccountRegisterResult>() {
            @Override
            protected AccountRegisterResult call() throws Exception {
                return register();
            }
        };
    }


}
