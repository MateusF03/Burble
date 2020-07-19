package com.mateus.burble;

import com.mateus.burble.command.CommandManager;
import com.mateus.burble.command.types.MemeCommands;
import com.mateus.burble.command.types.TextCommands;
import com.mateus.burble.config.CoreConfig;
import com.mateus.burble.listener.MainListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.io.File;

/**
 * A Discord bot, it uses JDA, it doesn’t have a specific reason to exist
 * if I find something interesting, I’ll implement it
 * @author Mateus F
 * @version 1.0
 * @since 2020-07-19
 */
public class Burble extends ListenerAdapter {

    private static JDA jda;

    public static void main(String[] args) throws LoginException {
        CoreConfig coreConfig = CoreConfig.getInstance();
        coreConfig.setup();
        String token = coreConfig.getConfig().get("token").getAsString();
        if (token.equals("<default-token>")) {
            System.err.println("Set the token");
            return;
        }
        JDA jda = JDABuilder.createDefault(token).build();
        jda.addEventListener(new MainListener(), new Burble());
        CommandManager.getInstance().registerCommands(new TextCommands());
        CommandManager.getInstance().registerCommands(new MemeCommands());
    }

    /**
     * Returns the folder that will be used for data storage
     * @return The data folder
     */
    public static File getDataFolder() {
        File folder = new File(System.getProperty("user.dir") + "/data");
        if (!folder.exists()) folder.mkdirs();
        return folder;
    }


    /**
     * Returns the main JDA instance
     * @return Main JDA instance
     */
    public static JDA getJDA() {
        return jda;
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        jda = event.getJDA();
    }
}
