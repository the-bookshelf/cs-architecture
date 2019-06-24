package com.packtpub.eventsourcing.commands;

public class CommandInvoker {

    public void invoke(Command command) {
        command.execute();
    }
}
