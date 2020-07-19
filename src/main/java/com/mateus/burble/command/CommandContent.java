package com.mateus.burble.command;

import com.mateus.burble.argument.ArgumentElement;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandContent {

    private final Pattern ARGUMENT_PATTERN;
    private final Map<String, ArgumentElement> argumentMap;
    private final Pattern URL_PATTERN;

    public CommandContent() {
        this.ARGUMENT_PATTERN =  Pattern.compile("(?:-{1,2}|/)(\\w+)(?:[=:]?|\\s+)([^-\\s\"][^\"]*?|\"[^\"]*\")?(?=\\s+[-/]|$)");
        this.argumentMap = new HashMap<>();
        this.URL_PATTERN = Pattern.compile("((([A-Za-z]{3,9}:(?://)?)(?:[-;:&=+$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=+$,\\w]+@)[A-Za-z0-9.-]+)((?:/[+~%/.\\w-_]*)?\\??(?:[-+=&;%@.\\w_]*)#?(?:[\\w]*))?)");
    }

    public void translateArguments(String rawMessage) {
        Matcher matcher = ARGUMENT_PATTERN.matcher(rawMessage);

        while (matcher.find()) {
            if (matcher.groupCount() < 2) continue;

            argumentMap.put(matcher.group(1), new ArgumentElement(matcher.group(2)));
        }
    }

    public InputStream getMostRecentInputStream(MessageReceivedEvent event) {
        InputStream inputStream = getInputStreamFromMessage(event.getMessage());
        if (inputStream != null) {
            return inputStream;
        }
        for (Message m : event.getChannel().getHistory().retrievePast(50).complete()) {
            InputStream is = getInputStreamFromMessage(m);
            if (is != null) {
                return is;
            }
        }
        return null;
    }

    private InputStream getInputStreamFromMessage(Message message) {
        List<Message.Attachment> attachments = message.getAttachments();
        if (!attachments.isEmpty()) {
            try {
                return attachments.get(0).retrieveInputStream().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        String content = message.getContentRaw();
        Matcher matcher = URL_PATTERN.matcher(content);
        while (matcher.find()) {
            String url = matcher.group(1);
            try {
                return new URL(url).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Map<String, ArgumentElement> getArgumentMap() {
        return argumentMap;
    }

    public ArgumentElement getArgumentElement(String argument) {
        return argumentMap.get(argument);
    }

}
