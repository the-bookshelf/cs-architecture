package com.packtpub.bankingclient.security.ui;

import com.packtpub.bankingapp.api.SecurityApi;
import com.packtpub.bankingapp.client.BankClient;
import com.packtpub.bankingapplication.balance.domain.Credentials;
import com.packtpub.bankingclient.ui.NavigableController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;


public class LoginController extends NavigableController {

    public static String JWT_TOKEN = null;

    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML
    Text error;

    public void login() {
        SecurityApi api = BankClient.getRetrofit().create(SecurityApi.class);
        Call<String> call = api.login(new Credentials(username.getText(), password.getText()));
        try {
            Response<String> response = call.execute();
            if(response.isSuccessful()){
                JWT_TOKEN = response.body();
                layout.showScreen("balance");
            }else {
                error.setText("An error happened, try again later.");
            }
        } catch (IOException e) {
            error.setText("An error happened, try again later.");
            e.printStackTrace();
        }

    }

    public void cancel() {
        Platform.exit();
    }
}
