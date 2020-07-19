package com.mateus.burble.utils;

import com.mateus.burble.Burble;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Parses the message entity from JDA
 * @author Mateus F
 * @version 1.0
 * @since 2020-07-19
 */
public class MessageParser {

    private static final JDA jda = Burble.getJDA();
    private static final Pattern ID = Pattern.compile("(\\d{17,20})");
    private static final Pattern RAW_USER_MENTION = Pattern.compile("<@(\\d{17,20})!?>");
    private static final Pattern RAW_CHANNEL_MENTION = Pattern.compile("<#(\\d{17,20})");
    private static final Pattern USER_REF = Pattern.compile("(.{1,32})#(\\d{4})");
    private final String message;


    public MessageParser(Message message) {
        this.message = message.getContentRaw();
    }

    public MessageParser(String message) {
        this.message = message;
    }

    /**
     * Gets the users in the message
     * @return The users in the message
     */
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        Matcher userMention = RAW_USER_MENTION.matcher(message);
        Matcher userRef = USER_REF.matcher(message);
        Matcher userId = ID.matcher(message);

        while (userMention.find()) {
            String id = userMention.group(1);
            User user = jda.getUserById(id);
            if (user != null) users.add(user);
        }
        while (userRef.find()) {
            if (userRef.groupCount() < 2) continue;
            String userName = userRef.group(1);
            String discriminator = userRef.group(2);
            jda.getUsers().stream().filter(u -> u.getName().equals(userName) && u.getDiscriminator().equals(discriminator))
                    .findFirst().ifPresent(users::add);
        }
        while (userId.find()) {
            String id = userId.group(1);
            User user = jda.getUserById(id);
            if (user != null) users.add(user);
        }

        return users.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Gets the channels from the message
     * @return The channels from the message
     */

    public List<TextChannel> getChannels() {
        List<TextChannel> textChannels = new ArrayList<>();
        Matcher channelMention = RAW_CHANNEL_MENTION.matcher(message);

        while(channelMention.find()) {
            String id = channelMention.group(1);
            textChannels.add(jda.getTextChannelById(id));
        }
        return textChannels.stream().distinct().collect(Collectors.toList());
    }
}
