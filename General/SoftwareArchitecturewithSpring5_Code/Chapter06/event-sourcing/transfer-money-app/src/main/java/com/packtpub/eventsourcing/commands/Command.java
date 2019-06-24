package com.packtpub.eventsourcing.commands;

import com.packtpub.eventsourcing.commands.persistence.CommandRepository;
import com.packtpub.eventsourcing.events.persistence.EventRepository;
import com.packtpub.eventsourcing.events.EventProcessor;
import org.json.simple.JSONObject;

public abstract class Command {

    protected final JSONObject data;
    protected final CommandRepository commandRepository;
    protected final EventRepository eventRepository;
    protected final EventProcessor eventProcessor;

    public Command(JSONObject data, CommandRepository commandRepository, EventRepository eventRepository, EventProcessor eventProcessor) {
        this.data = data;
        this.commandRepository = commandRepository;
        this.eventRepository = eventRepository;
        this.eventProcessor = eventProcessor;
    }

    public abstract void execute();

    public abstract String getName();

}
