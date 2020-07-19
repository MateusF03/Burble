package com.mateus.burble.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Utilities for image editing
 * @author Mateus F
 * @version 1.0
 * @since 2020-07-19
 */
public class ImageUtils {

    /**
     * Resizes the image
     * @param image The original image
     * @param width Width to resize
     * @param height Height to resize
     * @return The resized image
     */
    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();

        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return resizedImage;
    }

    /**
     * Sets the height, without resizing the original image
     * @param image The original image
     * @param height The new height
     * @return The processed image
     */
    public static BufferedImage setHeight(BufferedImage image, int height) {
        BufferedImage resizedImage = new BufferedImage(image.getWidth(), height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();

        graphics2D.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        graphics2D.dispose();
        return resizedImage;
    }

    /**
     * Draws a rectangle in the image
     * @param image The image
     * @param startX The start X to draw the rectangle
     * @param startY The start Y to draw the rectangle
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @param color The color of the rectangle
     */
    public static void drawRect(BufferedImage image, int startX, int startY, int width, int height, Color color) {
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(color);
        Rectangle rectangle = new Rectangle(startX, startY, width, height);
        graphics2D.fill(rectangle);
        graphics2D.dispose();
    }

    /**
     * Converts the image in a array of bytes
     * @param image The image
     * @return The image as a array of bytes
     * @throws IOException If the writing fails
     */
    public static byte[] bufferedImageToBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        byte[] array = os.toByteArray();
        os.close();
        return array;
    }

    /**
     * Draws a image over another image
     * @param image The image
     * @param imageToDraw The image to draw
     * @param startX The start X to draw the image
     * @param startY The start Y to draw the image
     */
    public static void drawImage(BufferedImage image, BufferedImage imageToDraw, int startX, int startY) {
        drawImage(image, imageToDraw, startX, startY, imageToDraw.getWidth(), imageToDraw.getHeight());
    }

    /**
     * Draws a image over another image
     * @param image The image
     * @param imageToDraw The image to draw
     * @param startX The start X to draw the image
     * @param startY The start Y to draw the image
     * @param width The width of the drawn image
     * @param height The height of the drawn image
     */
    public static void drawImage(BufferedImage image, BufferedImage imageToDraw, int startX, int startY, int width, int height) {
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.drawImage(imageToDraw, startX, startY, width, height, null);
        graphics2D.dispose();
    }
}