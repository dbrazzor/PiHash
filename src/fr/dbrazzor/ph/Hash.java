package fr.dbrazzor.ph;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Created by Thomas on 17/07/2016.
 */
class Hash {

    static String md5(String string) {

        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(string.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {
        }
        return null;
    }

    static long CRC32(String string) {

        byte[] bytes = string.getBytes();
        Checksum checksum = new CRC32();

        checksum.update(bytes, 0, bytes.length);

        return checksum.getValue();

    }

    static String SHA1(String string) {

        String sha1 = "";
        try {

            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sha1;
    }

    static String SHA256(String string) {

        String sha2 = "";
        try {

            MessageDigest crypt = MessageDigest.getInstance("SHA-256");
            crypt.reset();
            crypt.update(string.getBytes("UTF-8"));
            sha2 = byteToHex(crypt.digest());

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sha2;
    }

    private static String byteToHex(final byte[] hash) {

        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}
