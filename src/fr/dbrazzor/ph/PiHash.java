package fr.dbrazzor.ph;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.dbrazzor.ph.Hash.*;

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

    private static String mysql_Password;

    private static char[] keys = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            ' ', '!', '\"', '#', '$', '%', '&', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static Thread thread = null;

    private static MySQL mySQL;
    private static boolean use_mysql;

    public static void main(String[] args) throws InterruptedException, IOException {

        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));

        if (args.length < 1) {

            System.out.println("Thanks for using dbrazzor's hashing algorithm, you will be able to generate salted passwords based on the hashing method(s) you want to use.\n\n" +
                    "Here are the currently available hash functions :\n");

            for (HashType hashType : HashType.values()) {
                System.out.println("- " + hashType.getName());
            }

            System.out.println("\nIf you want to connect and store passwords in a mysql database, use the argument \"use-mysql\", you will be asked for your database details the first time, those will then be stored in an external file called PiHash-mysql.txt. You password won't be stored.\n\n" +
                    "If you need a graphical interface, use the argument \"use-ui\".");

            return;

        }

        boolean pass = false;

        if (argsList.contains("use-mysql")) {

            use_mysql = true;
            mysql_Password = new String(System.console().readPassword("Please enter your MySQL password: "));

        }

        for (int i = 0; i < args.length; i++) {

            if (args[i].equalsIgnoreCase("use-ui")) break;
            if (args[i].equalsIgnoreCase("use-mysql")) continue;

            if (args[i].equalsIgnoreCase("all")) {
                for (HashType hashType : HashType.values()) HashType.addHash(hashType);
                break;
            }

            HashType hashType = HashType.getHash(args[i]);

            if (hashType == null) {
                System.out.println("The hash funtion \"" + args[i] + "\" doesn't exists." + (i == args.length - 1 && !pass ? " Exiting." : ""));
                System.exit(0);
            }

            pass = true;
            HashType.addHash(hashType);

        }

        if (use_mysql) {

            try {

                mySQL = new MySQL("localhost", 3306, "root", mysql_Password, "Hashed_Passwords");
                System.out.println("Connected to mysql server!");
                if (!mySQL.tableExists()) {
                    mySQL.createTable();
                }

                u = mySQL.getLastId();

            } catch (ClassNotFoundException | SQLException e) {

                System.out.println("Could not connect to MySQL server. Exiting.");

            }

        }

        for (int i = 3; i > 0; i--) {

            System.out.print("Starting in " + i + " second" + (i > 1 ? "s" : " ") + "\r");
            Thread.sleep(1000);

        }

        HashType[] hashTypes = HashType.getHashs();

        String s = "Starting password hashing with functions : ";
        for (int i = 0; i < hashTypes.length; i++)
            s += hashTypes[i].getName() + (i != hashTypes.length - 1 ? ", " : ".");

        System.out.println(s);

        string = use_mysql ? mySQL.getLastPass() : "a";
        toChange = 0;
        charToChange = keys[0];

        File file;

        String[] print = new String[]{"Hashed Passwords - by dbrazzor - Page " + t + " [" + u + " ; " + (u + 100000) + "]\n\nPassword : Combinations : "};

        for (int i = 0; i < hashTypes.length; i++) {
            print[0] += hashTypes[i].getName() + (i != hashTypes.length - 1 ? " : " : "\n");
        }

        file = new File("/home/thomas/Hash/" + t + ".txt");

        if (!file.exists() && !use_mysql) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        printWriter = new PrintWriter("/home/thomas/Hash/" + t + ".txt");

        printWriter.println(print[0]);

        new Thread(() -> {

            for (; ; ) {
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

            for (; ; ) {

                if (u < 0 || !canWrite) continue;

                if (j >= 100000 && !use_mysql) {

                    try {

                        /*System.out.println("[!] Starting paste...");

                        PasteBuilder pasteBuilder = factory.createPaste();

                        pasteBuilder.setTitle("Hashed Passwords - by dbrazzor - Page " + t + " [" + (u - 10000) + " ; " + u + "]");

                        String header = "Hashed Passwords - by dbrazzor - Page " + t + " [" + (u - 10000) + " ; " + u + "]\n\nPassword : Combinations : ";

                        for (int i = 0; i < hashTypes.length; i++) {
                            header += hashTypes[i].getName() + (i != hashTypes.length - 1 ? " : " : "\n");
                        }

                        pasteBuilder.setRaw(header+"\n\n"+pasteStr);

                        pasteBuilder.setMachineFriendlyLanguage("text");

                        pasteBuilder.setVisiblity(PasteVisiblity.Public);

                        pasteBuilder.setExpire(PasteExpire.Never);

                        Paste paste = pasteBuilder.build();

                        Response<String> postResult = pastebin.post(paste, userKey);

                        if (postResult.hasError()) {
                            System.out.println("Pastebin error: " + postResult.getError());
                            return;
                        }

                        System.out.println("Paste created! Url: " + postResult.get());

                        pasteStr = "";*/

                        t++;
                        printWriter.close();
                        printWriter = new PrintWriter("/home/thomas/Hash/" + t + ".txt");
                        print[0] = "Hashed Passwords - by dbrazzor - Page " + t + " [" + u + " ; " + (u + 100000) + "]\n\nPassword : Combinations : ";

                        for (int i = 0; i < hashTypes.length; i++) {
                            print[0] += hashTypes[i].getName() + (i != hashTypes.length - 1 ? " : " : "\n");
                        }

                        printWriter.println(print[0]);

                        j = 0;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String hashs = "";
                List<String> hashList = new ArrayList<>();

                for (int i = 0; i < hashTypes.length; i++) {

                    String hash = "";

                    switch (hashTypes[i]) {

                        case BCRYPT:
                            hash = BCrypt.hashpw(string, BCrypt.gensalt());
                            break;
                        case MD5:
                            hash = md5(string);
                            break;
                        case CRC32:
                            hash = "" + CRC32(string);
                            break;
                        case SHA1:
                            hash = SHA1(string);
                            break;
                        case SHA256:
                            hash = SHA256(string);
                            break;

                    }

                    hashList.add(hash);
                    hashs += hash + (i != hashTypes.length - 1 ? " : " : "");

                }

                String data = string + " : " + u + " : " + hashs;
                System.out.println(data);
                //pasteStr += "\n"+data;
                if (!use_mysql) printWriter.println(data);
                if (mySQL != null) mySQL.addHash(string, hashList.toArray(new String[hashList.size()]));

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

}
