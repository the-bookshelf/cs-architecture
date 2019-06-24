package com.packtpub.bankingapp.api;

import com.packtpub.bankingapplication.balance.domain.BalanceInformation;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface BankingApi {

    @GET("/api/secure/balance")
    Call<BalanceInformation> queryBalance(@Header("x-auth-token") String token);
}
