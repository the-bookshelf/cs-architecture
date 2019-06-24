package com.packtpub.eventsourcing.command;

import com.packtpub.eventsourcing.commands.Command;
import com.packtpub.eventsourcing.commands.CommandInvoker;
import org.junit.Test;
import org.mockito.Mockito;

public class CommandInvokerTest {

    @Test
    public void givenACommandTheInvokerCallTheExecuteCommandAction() throws Exception {
        Command command = Mockito.mock(Command.class);
        CommandInvoker commandInvoker = new CommandInvoker();

        commandInvoker.invoke(command);

        Mockito.verify(command, Mockito.times(1)).execute();
    }
}
