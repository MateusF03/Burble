package com.mateus.burble.command;

import com.mateus.burble.argument.Argument;
import com.mateus.burble.command.annotations.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.lang.reflect.Method;
import java.util.List;

public class RegisteredCommand {

    private final Command annotation;
    private final Object object;
    private final List<Argument> arguments;
    private final Method method;

    public RegisteredCommand(Object object, Method method, Command annotation, List<Argument> arguments) {
        this.object = object;
        this.annotation = annotation;
        this.arguments = arguments;
        this.method = method;
    }

    public String getName() {
        return annotation.name();
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public void invoke(MessageReceivedEvent event, CommandContent commandContent) {
        try {
            method.invoke(object, event, commandContent);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
