package com.mateus.burble.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A formatter for time written in text
 * It's disgusting, but it works
 * @author Mateus F
 * @version 1.0
 * @since 2020-07-19
 */
public class TimeFormatter {

    private static final Pattern TIME_PATTERN = Pattern.compile("([:/]?\\d+[/:]?\\d*[/]?\\d*)([hmsd]?)");
    private static final Pattern HOUR_PATTERN = Pattern.compile("(\\d{2}):(\\d{2})");
    private static final SimpleDateFormat SECONDS_FORMAT = new SimpleDateFormat("ss");
    private static final SimpleDateFormat MINUTES_FORMAT = new SimpleDateFormat("mm");
    private static final long HOURS_IN_MINUTES = TimeUnit.HOURS.toMinutes(1L);
    private static final long DAYS_IN_HOURS = TimeUnit.DAYS.toHours(1L);

    /**
     * Formats the string in a date, then returns it as a long millis
     * @param content The string to format
     * @return The time in millis
     */
    public static long getTimeAsMillis(String content) {
        Matcher matcher = TIME_PATTERN.matcher(content);
        long millis = 0L;
        while (matcher.find()) {
            if (matcher.groupCount() < 2) continue;
            System.out.println(matcher.group(2));
            switch (matcher.group(2)) {
                case "h":
                    Matcher hourMatcher = HOUR_PATTERN.matcher(matcher.group(1));
                    if (hourMatcher.matches()) {
                        long hours = Long.parseLong(hourMatcher.group(1));
                        long minutes = Long.parseLong(hourMatcher.group(2));
                        millis += TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes);
                    } else {
                        millis += TimeUnit.HOURS.toMillis(Long.parseLong(matcher.group(1)));
                    }
                    break;
                case "s":
                    millis += TimeUnit.SECONDS.toMillis(Long.parseLong(matcher.group(1)));
                    break;
                case "m":
                    millis += TimeUnit.MINUTES.toMillis(Long.parseLong(matcher.group(1)));
                    break;
                case "d":
                    millis += TimeUnit.DAYS.toMillis(Long.parseLong(matcher.group(1)));
                    break;
            }
        }
        return millis;
    }

    /**
     * Formats the millis to a string
     */
    public static String formatMillis(long millis) {
        //System.out.println(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        StringBuilder stringBuilder = new StringBuilder();
        if (minutes >= HOURS_IN_MINUTES && hours < DAYS_IN_HOURS) {
            Date date = new Date(millis);
            String minutesString = MINUTES_FORMAT.format(date);
            String secondsString = SECONDS_FORMAT.format(date);
            stringBuilder.append(hours).append(" hours, ");
            if (!minutesString.equals("00")) {
                stringBuilder.append(minutesString).append(" minutes, ");
            }
            if (!secondsString.equals("00")) {
                stringBuilder.append(" seconds, ");
            }
        } else if (hours >= DAYS_IN_HOURS) {
            long days = TimeUnit.HOURS.toDays(hours);
            Date date = new Date(millis);
            String minutesString = MINUTES_FORMAT.format(date);
            String secondsString = SECONDS_FORMAT.format(date);
            stringBuilder.append(days).append(" days, ");
            if (!minutesString.equals("00")) {
                stringBuilder.append(minutesString).append(" minutes, ");
            }
            if (!secondsString.equals("00")) {
                stringBuilder.append(secondsString).append(" seconds, ");
            }
        } else if (hours == 0) {
            String secondsString = SECONDS_FORMAT.format(new Date(millis));
            stringBuilder.append(minutes).append(" minutes, ");
            if (!secondsString.equals("00")) {
                stringBuilder.append(secondsString).append(" seconds, ");
            }
        }
        return stringBuilder.toString().substring(0, stringBuilder.length() - ", ".length());
    }
}
