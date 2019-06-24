package com.packtpub.bankingclient;

import com.packtpub.bankingclient.balance.ui.BalanceController;
import com.packtpub.bankingclient.security.ui.LoginController;
import com.packtpub.bankingclient.ui.LayoutPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BankingClientApplication extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        LayoutPane layoutPane = new LayoutPane();
        layoutPane.loadScreen("balance", BalanceController.class.getResource("Balance.fxml"));
        layoutPane.loadScreen("login", LoginController.class.getResource("Login.fxml"));

        layoutPane.showScreen("login");

        Scene scene = new Scene(layoutPane);
        scene.getStylesheets().add("bootstrapfx.css");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
}
