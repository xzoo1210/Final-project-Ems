package com.ems.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactFormat {

    public static final Pattern VALID_VIETNAM_PHONE_REGEX =
            Pattern.compile("^(84|0[3|5|7|8|9])+([0-9]{8})\\b$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_NAME_REGEX =
            Pattern.compile("^[A-Z]+([ '-][a-zA-Z]+)*$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_IMAGE_URL_REGEX =
            Pattern.compile("^(http(s?):/)(/[^/]+)+\" + \"\\.(?:jpg|gif|png)$", Pattern.CASE_INSENSITIVE);

    public static boolean validatePhone(String phoneNumber) {
        Matcher matcher = VALID_VIETNAM_PHONE_REGEX.matcher(phoneNumber);
        return matcher.find();
    }

    public static boolean validateFirstName(String firstName) {
        Matcher matcher = VALID_NAME_REGEX.matcher(firstName);
        return matcher.find();
    }

    public static boolean validateMiddleName(String middleName) {
        Matcher matcher = VALID_NAME_REGEX.matcher(middleName);
        return matcher.find();
    }

    public static boolean validateLastName(String lastName) {
        Matcher matcher = VALID_NAME_REGEX.matcher(lastName);
        return matcher.find();
    }

    public static boolean validateImageURL(String imageURL) {
        Matcher matcher = VALID_IMAGE_URL_REGEX.matcher(imageURL);
        return matcher.find();
    }

}
