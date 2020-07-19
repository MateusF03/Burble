package com.mateus.burble.command.types;

import com.mateus.burble.argument.ArgumentElement;
import com.mateus.burble.command.CommandContent;
import com.mateus.burble.argument.ArgumentType;
import com.mateus.burble.command.annotations.ArgumentAnnotation;
import com.mateus.burble.command.annotations.Command;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Base64;
import java.util.Map;

public class TextCommands {
    @Command(name = "say", description = "The say command")
    @ArgumentAnnotation(name = "msg", type = ArgumentType.STRING, obligatory = true)
    public void say(MessageReceivedEvent event, CommandContent translator) {
        translator.getArgumentMap().keySet().forEach(System.out::println);
        event.getChannel().sendMessage(translator.getArgumentElement("msg").getContent()).queue();
    }

    @Command(name = "b64", description = "General command for Base64 encoding and decoding")
    @ArgumentAnnotation(name = "encode", type = ArgumentType.STRING, obligatory = false) // actually obligatory
    @ArgumentAnnotation(name = "decode", type = ArgumentType.STRING, obligatory = false)
    public void base64(MessageReceivedEvent event, CommandContent content) {
        Map<String, ArgumentElement> argumentMap = content.getArgumentMap();
        MessageChannel channel = event.getChannel();
        if (argumentMap.containsKey("decode")) {
            String str = argumentMap.get("decode").getContent();
            byte[] array = Base64.getDecoder().decode(str);
            channel.sendMessage("**Text decoded:** `" + new String(array) + "`").queue();
        } else if (argumentMap.containsKey("encode")) {
            String str = argumentMap.get("encode").getContent();
            String encoded = Base64.getEncoder().encodeToString(str.getBytes());
            channel.sendMessage("**Text encoded:** `" + encoded + "`").queue();
        } else {
            channel.sendMessage("**Missing arguments:** `Use -decode or -encode`").queue();
        }
    }
}
