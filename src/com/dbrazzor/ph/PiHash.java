package com.dbrazzor.ph;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Thomas GRIVET (aka dbrazzor) on 02/05/2016.
 */
public class PiHash {

    private static String string;
    private static int toChange;
    private static char charToChange;
    private static boolean were9;
    private static boolean haveFound;

    private static long u = 0;

    private static PrintWriter printWriter;

    private static int j = 0, t = 1;

    private static boolean canWrite = true;

    private static char[] keys = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            ' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static Thread thread = null;

    public static void main(String[] args) throws InterruptedException, IOException {

        if (args.length < 1) return;

        string = "a";
        toChange = 0;
        charToChange = keys[0];

        printWriter = new PrintWriter("/media/Hashed/MD5/" + t + ".txt");
        printWriter.println("Hashed Passwords - MD5 - by dbrazzor - Page " + t + " [" + u + " ; " + (u + 100000) + "]\n\nPassword : Combinations : Hash\n");

        new Thread(() -> {

            while (true) {
                System.out.println(string);
                if (thread != null) {
                    if (!thread.isAlive()) {
                        thread.start();
                    }
                }

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        thread = new Thread(() -> {

            while (true) {

                if (u < 0 || !canWrite) continue;

                if (j >= 100000) {

                    try {
                        t++;
                        printWriter.close();
                        printWriter = new PrintWriter("/media/Hashed/MD5/" + t + ".txt");
                        printWriter.println("Hashed Passwords - MD5 - by dbrazzor - Page " + t + " [" + u + " ; " + (u + 100000) + "]\n\nPassword : Combinations : Hash\n");
                        j = 0;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String md5 = md5(string);
                String data = string + " : " + u + " : " + md5;
                System.out.println(data);

                printWriter.println(data);

                haveFound = false;
                int position;
                char charAt;

                if (string.endsWith("9")) {

                    were9 = true;

                    for (int i = 0; i < string.length(); i++) {

                        position = string.length() - 1 - i;
                        charAt = string.charAt(position);

                        if (charAt != '9') {
                            toChange = position;
                            charToChange = getNextChar(charAt);
                            haveFound = true;
                            break;
                        }
                    }

                } else were9 = false;

                if (were9) {

                    if (!haveFound) {

                        string = string.replaceAll(".", "a") + "a";

                    } else {

                        string = changeCharInPosition(toChange, charToChange, string);

                        for (int i = string.length() - 1; i > toChange; i--) {

                            string = changeCharInPosition(i, 'a', string);

                        }

                    }

                } else
                    string = changeCharInPosition(string.length() - 1, getNextChar(string.charAt(string.length() - 1)), string);
                u++;
                j++;


            }

        }) {{
            start();
        }};

    }

    private static char getNextChar(char character) {

        for (int i = 0; i < keys.length; i++) {

            if (keys[i] == character) return keys[i + 1];

        }

        return 'i';

    }

    private static String changeCharInPosition(int position, char ch, String str) {
        char[] charArray = str.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }

    private static String replaceAll(String str) {

        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
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
