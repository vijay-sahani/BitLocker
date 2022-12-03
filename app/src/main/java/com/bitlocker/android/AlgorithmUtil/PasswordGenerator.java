package com.bitlocker.android.AlgorithmUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class PasswordGenerator {

    private static final Character[] ALPHA_UPPER_CHARACTERS = {'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final Character[] ALPHA_LOWER_CHARACTERS = {'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final Character[] NUMERIC_CHARACTERS = {'0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9'};
    private static final Character[] SPECIAL_CHARACTERS = {'@', '#',
            '$', '%', '^', '&', '*', '?'};
    List<Character[]> charSets;

    public PasswordGenerator() {
        charSets = new ArrayList<>();
        charSets.add(ALPHA_LOWER_CHARACTERS);
    }

    public String getPassword(int len, boolean capital, boolean numbers, boolean special) {
        StringBuilder passBuilder = new StringBuilder();
        int specialCharacterPercentage, numberPercentage;

        boolean isCapitalPresent = charSets.contains(ALPHA_UPPER_CHARACTERS);
        if (capital && !isCapitalPresent) charSets.add(ALPHA_UPPER_CHARACTERS);
        else if (!capital && isCapitalPresent) charSets.remove(ALPHA_UPPER_CHARACTERS);

        if (special) {
            specialCharacterPercentage = (20 * len) / 100;
            addExtraCharacters(passBuilder, specialCharacterPercentage, SPECIAL_CHARACTERS);
        } else specialCharacterPercentage = 0;

        if (numbers) {
            numberPercentage = (20 * len) / 100;
            addExtraCharacters(passBuilder, numberPercentage, NUMERIC_CHARACTERS);
        } else numberPercentage = 0;

        final int charSetLen = charSets.size();
        Random rand = new Random();
        for (int i = 0; i < len - specialCharacterPercentage - numberPercentage; i++) {
            int randomLength = rand.nextInt(charSetLen);
            Character[] newAlpha = charSets.get(randomLength);
            int randomSetLen = newAlpha.length;
            int randomAlphaLen = rand.nextInt(randomSetLen);
            passBuilder.append(newAlpha[randomAlphaLen]);
        }
        return scramble(rand, passBuilder.toString());
    }

    private String scramble(Random random, String inputString) {
        // Convert your string into a simple char array:
        char[] output = inputString.toCharArray();
        // Scramble the letters using the standard Fisher-Yates shuffle,
        for (int i = 0; i < output.length; i++) {
            int j = random.nextInt(output.length);
            // Swap letters
            char temp = output[i];
            output[i] = output[j];
            output[j] = temp;
        }

        return String.valueOf(output);
    }

    private void addExtraCharacters(StringBuilder output, int len, Character[] chars) {
        for (int i = 0; i < len; i++) {
            int randomLen = new Random().nextInt(chars.length - 1);
            output.append(chars[randomLen]);
        }
    }

    public int passwordStrength(String password) {
        int n = password.length();
        boolean hasUpper = false,
                hasDigit = false, specialChar = false;
        if (Pattern.compile("(?s).*[A-Z].*").matcher(password).matches()) hasUpper = true;
        if (Pattern.compile("(?=.*\\d)").matcher(password).find()) hasDigit = true;
        if (Pattern.compile("(?=.*[@#$%^&+=])").matcher(password).find()) specialChar = true;
        if (hasDigit && hasUpper && specialChar && n >= 12)
            return 2;// strong;
        else if (hasUpper || hasDigit || specialChar)
            return 1; //medium
        return 0;// week;
    }
}