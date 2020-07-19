package com.mateus.burble.command.types;

import com.mateus.burble.command.CommandContent;
import com.mateus.burble.command.annotations.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static com.mateus.burble.utils.ImageUtils.*;

public class MemeCommands {

    private BufferedImage iFunnyWatermark;
    private final Color IFUNNY_RECT_COLOR = new Color(24, 25, 29);
    public MemeCommands() {
        try {
            iFunnyWatermark = ImageIO.read(this.getClass().getResourceAsStream("/images/ifunny.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "iFunny", description = "Adds the iFunny watermark in a image")
    public void iFunny(MessageReceivedEvent event, CommandContent commandContent) {
        assert iFunnyWatermark != null;
        InputStream is = commandContent.getMostRecentInputStream(event);
        Message send = event.getChannel().sendMessage("**Processing image**").complete();
        try {
            BufferedImage image = ImageIO.read(is);
            is.close();
            BufferedImage resizedImage = resizeImage(image, Math.max(466, image.getWidth()), image.getHeight());
            BufferedImage imageWithHeight = setHeight(resizedImage, resizedImage.getHeight() + 21);
            if (resizedImage.getWidth() > 466) {
                int width = resizedImage.getWidth() - iFunnyWatermark.getWidth();

                drawRect(imageWithHeight, 0, resizedImage.getHeight(), width, 1, new Color(1, 2, 6));
                drawRect(imageWithHeight, 0, resizedImage.getHeight() + 1, width, 2, IFUNNY_RECT_COLOR);
                drawRect(imageWithHeight, 0, resizedImage.getHeight() + 3, width, 1, new Color(17, 18, 22));
                drawRect(imageWithHeight, 0, resizedImage.getHeight() + 4, width, 1, IFUNNY_RECT_COLOR);
                drawRect(imageWithHeight, 0, resizedImage.getHeight() + 5, width, 1, new Color(19, 20, 24));
                drawRect(imageWithHeight, 0, resizedImage.getHeight() + 6, width, 15, new Color(22, 23, 28));


                drawImage(imageWithHeight, iFunnyWatermark, width, resizedImage.getHeight());
            } else {
                drawImage(imageWithHeight, iFunnyWatermark, 0, resizedImage.getHeight());
            }

            event.getChannel().sendFile(bufferedImageToBytes(imageWithHeight), "ifunny.png").queue(m -> send.delete().queue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
