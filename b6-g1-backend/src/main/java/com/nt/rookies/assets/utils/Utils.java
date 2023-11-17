package com.nt.rookies.assets.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Utils {

    public static String IdGenerator(String prefix, int paddingSize, int position) {
        String result = String.format(prefix + "%0" + paddingSize + "d", position);
        return result;
    }

    /**
     * Format LocalDate based on specific pattern
     * @param date
     * @param pattern
     * @return date string with given pattern
     */
    public static String formatLocalDate(String date, String pattern) {
        LocalDate localDate = LocalDate.parse(date);
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        String formattedDate = localDate.format(format);
        return formattedDate;
    }

    /**
     * Generate password with username and date string
     * @param username
     * @param date
     * @return formatted string. Ex: chungbui@01011990
     */
    public static String generatePassword(String username, String date) {
        String formattedDate = Utils.formatLocalDate(date, "ddMMyyyy");
        String str = username + "@" + formattedDate;
        return str;
    }

    /**
     * Generate username from firstName and lastName
     * Ex: Firstname: Vang, Lastname: Do Van. Then return: vangdv
     * @param firstName
     * @param lastName
     * @return generated username
     */
    public static String generateUsername(String firstName, String lastName) {
        StringBuilder builder = new StringBuilder();

        for (String s : firstName.toLowerCase().split(" ")) {
            builder.append(s);
        }

        for (String s : lastName.toLowerCase().split(" ")) {
            builder.append(s.charAt(0));
        }

        String username = builder.toString();
        return username;
    }

    /**
     * Generate username to save on database
     * @param username
     * @return generate username. Ex: vangdv1, vangdv2, vangdv3, etc.
     */
    public static String generateUsernameForDb(String username) {
        StringBuilder usernameForDb = new StringBuilder();

        // Check if username do not have number. Ex: vangdv, chungbv
        // Then insert "1" to the end of username
        if (!Utils.containsNumber(username)) {
            usernameForDb.append(username);
            usernameForDb.append("1");
        } else {
            // If username already has number. Ex: vangdv3, chungbv5
            // Then extract the number and +1 to it
            String[] parts = username.split("(?<=\\D)(?=\\d)");
            Integer newInt = Integer.parseInt(parts[1]) + 1;
            usernameForDb.append(parts[0]);
            usernameForDb.append(Integer.toString(newInt));
        }
        return usernameForDb.toString();
    }

    /**
     * Check if user is under or equal 18 years old
     */
    public static boolean isUserUnder18(String birthDate) {
        LocalDate dob = LocalDate.parse(birthDate);
        LocalDate current = LocalDate.now();
        Long years = ChronoUnit.YEARS.between(dob, current);
        return years < 18;
    }

    /**
     * Check if a string contains number
     */
    public static boolean containsNumber(String aString) {
        return aString.matches(".*\\d.*");
    }

}
