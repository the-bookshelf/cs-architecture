package com.packtpub.bankingmobile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.packtpub.bankingapp.api.BankingApi;
import com.packtpub.bankingapp.client.BankClient;
import com.packtpub.bankingapplication.balance.domain.BalanceInformation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceActivity extends Activity {

    private TextView balanceTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        balanceTextView = findViewById(R.id.fullscreen_content);
        String token = getIntent().getStringExtra(LoginActivity.JWT_TOKEN);

        BankingApi api = BankClient.getRetrofit().create(BankingApi.class);
        Call<BalanceInformation> call = api.queryBalance(token);
        call.enqueue(new Callback<BalanceInformation>() {
            @Override
            public void onResponse(Call<BalanceInformation> call, Response<BalanceInformation> response) {
                BalanceInformation balanceInformation = response.body();
                balanceTextView.setText(String.format("Hi %s, your balance is %d", balanceInformation.getCustomer(), balanceInformation.getBalance()));
            }

            @Override
            public void onFailure(Call<BalanceInformation> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }


}
