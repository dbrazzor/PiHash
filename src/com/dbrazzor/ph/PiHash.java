package com.dbrazzor.ph;

import com.sun.deploy.util.StringUtils;

/**
 * Created by Thomas GRIVET (aka dbrazzor) on 02/05/2016.
 */
public class PiHash {

    public static char[] keys = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '&', '\'', '(', '§', '!', ')', '=', '+', '?', '>', '<', '^', '$', '*', '€', '%', '£', '`',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static void main(String[] args) throws InterruptedException {

        if(args.length<1) return;

        String string = "a";
        int toChange = 0;
        char charToChange = keys[0];
        boolean were9;
        boolean haveFound;

        int u = 0;

        while(true){

            System.out.println(string+" : "+u+" : "+md5(string));
            haveFound = false;
            int position;
            char charAt;

            if(string.endsWith("9")){

                were9 = true;

                for(int i = 0; i<string.length(); i++){

                    position = string.length()-1-i;
                    charAt = string.charAt(position);

                    if(charAt!='9'){
                        toChange = position;
                        charToChange = getNextChar(charAt);
                        haveFound = true;
                        break;
                    }
                }

            } else were9 = false;

            if(were9){

                if(!haveFound) {

                        string = string.replaceAll(".", "a")+"a";

                }
                else{

                    string = changeCharInPosition(toChange, charToChange, string);

                    for(int i = string.length()-1; i>toChange; i--) {

                        string = changeCharInPosition(i, 'a', string);

                    }

                }

            }

            else string = changeCharInPosition(string.length()-1, getNextChar(string.charAt(string.length()-1)), string);
            u++;

        }

    }

    private static char getNextChar(char character){

        for(int i = 0; i<keys.length; i++){

            if(keys[i] == character) return keys[i+1];

        }

        return 'i';

    }

    private static String changeCharInPosition(int position, char ch, String str){
        char[] charArray = str.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }

    private static String replaceAll(String str){

        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++){
            sb.append('a');
        }
        return sb.toString();

    }

    private static String md5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {
        }
        return null;
    }

}
