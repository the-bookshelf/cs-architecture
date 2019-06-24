package com.packtpub.bankingclient.balance.ui;

import com.packtpub.bankingapp.api.BankingApi;
import com.packtpub.bankingapp.client.BankClient;
import com.packtpub.bankingapplication.balance.domain.BalanceInformation;
import com.packtpub.bankingclient.security.ui.LoginController;
import com.packtpub.bankingclient.ui.NavigableController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceController extends NavigableController {

    @FXML
    Text balance;

    public void queryBalance() {
        BankingApi api = BankClient.getRetrofit().create(BankingApi.class);
        Call<BalanceInformation> call = api.queryBalance(LoginController.JWT_TOKEN);
        call.enqueue(new Callback<BalanceInformation>() {

            @Override
            public void onResponse(Call<BalanceInformation> call, Response<BalanceInformation> response) {
                BalanceInformation balanceInformation = response.body();
                balance.setText(String.format("Hi %s, your balance is %d", balanceInformation.getCustomer(), balanceInformation.getBalance()));
            }

            @Override
            public void onFailure(Call<BalanceInformation> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void exit() {
        Platform.exit();
    }
}
