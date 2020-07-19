package com.mateus.burble.command;

import com.mateus.burble.argument.Argument;
import com.mateus.burble.argument.ArgumentElement;
import com.mateus.burble.command.annotations.ArgumentAnnotation;
import com.mateus.burble.command.annotations.Arguments;
import com.mateus.burble.command.annotations.Command;
import com.mateus.burble.config.CoreConfig;
import com.mateus.burble.exceptions.InvalidCommandException;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class CommandManager {

    private final List<RegisteredCommand> commands = new ArrayList<>();
    private final String PREFIX = CoreConfig.getInstance().getConfig().get("prefix").getAsString();

    private static CommandManager instance;
    private CommandManager() {}
    public static CommandManager getInstance() {
        if (instance == null) {
            synchronized (CommandManager.class) {
                if (instance == null) {
                    instance = new CommandManager();
                }
            }
        }
        return instance;
    }

    public void registerCommands(Object object) {
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        assert methods != null;
        for (Method method : methods) {
            Command command = method.getAnnotation(Command.class);
            ArgumentAnnotation singletonAnnotation = method.getAnnotation(ArgumentAnnotation.class);
            Arguments arguments = method.getAnnotation(Arguments.class);
            if (command == null) continue;
            Class<?>[] params = method.getParameterTypes();
            if (!MessageReceivedEvent.class.isAssignableFrom(params[0])) {
                throw new InvalidCommandException("Command method " + method.getName() + " does not have a message received event as a parameter");
            }
            if (!CommandContent.class.isAssignableFrom(params[1])) {
                throw new InvalidCommandException("Command method " + method.getName() + " does not have a argument translator as a parameter");
            }
            List<Argument> argumentList;
            if (arguments != null) {
                argumentList = new ArrayList<>();
                for (ArgumentAnnotation argumentAnnotation : arguments.value()) {
                    Argument argument = new Argument(argumentAnnotation.name(), argumentAnnotation.type(), argumentAnnotation.obligatory());
                    argumentList.add(argument);
                }
            } else if (singletonAnnotation != null) {
                Argument argument = new Argument(singletonAnnotation.name(), singletonAnnotation.type(), singletonAnnotation.obligatory());
                argumentList = Collections.singletonList(argument);
            } else {
                argumentList = Collections.emptyList();
            }
            RegisteredCommand registeredCommand = new RegisteredCommand(object, method, command, argumentList);
            commands.add(registeredCommand);
        }
    }

    public void handleEvent(MessageReceivedEvent event) {
        String contentRaw = event.getMessage().getContentRaw();
        if (!contentRaw.startsWith(PREFIX)) return;
        User author = event.getAuthor();
        if (author.isBot() || author.isFake()) return;

        String commandName = contentRaw.split(" ")[0].substring(PREFIX.length());
        Optional<RegisteredCommand> commandOptional = commands.stream().filter(cmd -> cmd.getName().equalsIgnoreCase(commandName)).findFirst();
        if (commandOptional.isPresent()) {
            RegisteredCommand command = commandOptional.get();
            CommandContent commandContent = new CommandContent();
            commandContent.translateArguments(contentRaw);

            Map<String, ArgumentElement> translatedArguments = commandContent.getArgumentMap();
            List<Argument> arguments = command.getArguments();
            for (Argument argument : arguments) {
                if (!translatedArguments.containsKey(argument.getArgName()) && argument.isObligatory()) {
                    event.getChannel().sendMessage("**Invalid command:** You are missing the argument: `" + argument.getArgName() + "`").queue();
                    throw new InvalidCommandException("Invalid arguments");
                }  else {
                    ArgumentElement content = translatedArguments.get(argument.getArgName());
                    switch (argument.getType()) {
                        case INT:
                            if (!isInt(content.getContent())) {
                                event.getChannel().sendMessage("**The content of the argument " + argument.getArgName() + " is not a number**").queue();
                                continue;
                            }
                            break;
                        case LONG:
                            if (!isLong(content.getContent())) {
                                event.getChannel().sendMessage("**The content of the argument " + argument.getArgName() + " is not a long**").queue();
                                continue;
                            }
                            break;
                        case BOOLEAN:
                            if (!isBoolean(content.getContent())) {
                                event.getChannel().sendMessage("**The content of the argument " + argument.getArgName() + " is not a boolean**").queue();
                                continue;
                            }
                            break;
                    }
                    argument.setContent(translatedArguments.get(argument.getArgName()));
                    if (translatedArguments.containsKey(argument.getArgName())) {
                        translatedArguments.put(argument.getArgName(), argument.getElement());
                    }
                }
            }
            List<String> invalidKeys = commandContent.getArgumentMap().keySet().stream()
                    .filter(s -> arguments.stream().noneMatch(a -> a.getArgName().equalsIgnoreCase(s))).collect(Collectors.toList());
            invalidKeys.forEach(commandContent.getArgumentMap()::remove);
            command.invoke(event, commandContent);
        }
    }

    private boolean isInt(String content) {
        try {
            Integer.parseInt(content);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isLong(String content) {
        try {
            Long.parseLong(content);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isBoolean(String content) {
        try {
            Boolean.parseBoolean(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
