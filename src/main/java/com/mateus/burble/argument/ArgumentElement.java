package com.mateus.burble.argument;

import com.mateus.burble.utils.MessageParser;
import com.mateus.burble.utils.TimeFormatter;
import net.dv8tion.jda.api.entities.User;

import java.util.Date;
import java.util.List;

public class ArgumentElement {

    private final String content;

    public ArgumentElement(String content) {
        this.content = content;
    }

    public int getAsInt() {
        return Integer.parseInt(content);
    }

    public long getAsLong() {
        return Long.parseLong(content);
    }

    public boolean getAsBoolean() {
        return Boolean.parseBoolean(content);
    }

    public Date getAsDateToNow() {
        long millis = System.currentTimeMillis() + TimeFormatter.getTimeAsMillis(content);
        return new Date(millis);
    }

    public Date getAsDate() {
        return new Date(TimeFormatter.getTimeAsMillis(content));
    }

    public List<User> getAsUsers() {
        MessageParser messageParser = new MessageParser(content);
        return messageParser.getUsers();
    }

    public String getContent() {
        return content;
    }
}
