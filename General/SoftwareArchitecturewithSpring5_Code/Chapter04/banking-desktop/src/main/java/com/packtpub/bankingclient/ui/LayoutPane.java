package com.packtpub.bankingclient.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LayoutPane extends BorderPane {

    private Map<String, Node> windows;

    public LayoutPane() {
        this.windows = new HashMap<>();
    }

    public void loadScreen(String screenName, URL fxmlRepresentation) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlRepresentation);
        Parent screen = fxmlLoader.load();

        NavigableController navigableController = fxmlLoader.getController();
        navigableController.setLayout(this);

        windows.put(screenName, screen);
    }

    public void showScreen(String screenName) {
        this.setCenter(windows.get(screenName));
    }

}
