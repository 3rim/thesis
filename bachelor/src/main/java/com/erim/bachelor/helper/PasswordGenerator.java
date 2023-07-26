package com.erim.bachelor.helper;

import java.security.SecureRandom;

public class PasswordGenerator {


    /**
     * Generates 8 Char long String containing only numbers
     * @return 8 Char long String containing only numbers
     */
    public static String generateInitialPassword(){
        return generateInitialPassword(8);
    }

    /**
     * Generate a random String containing only numbers
     * @param len the length of the string to be generated
     * @return random String containing only numbers
     */
    public static String generateInitialPassword(int len){
        final String chars ="0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();


        for(int i=0; i<len; i++){
            int randomIndex = random.nextInt(chars.length());
            builder.append(chars.charAt(randomIndex));
        }
        return builder.toString();
    }
}
