package fr.dbrazzor.ph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 16/07/2016.
 */
enum HashType {

    MD5("MD5"),
    BCRYPT("BCrypt"),
    CRC32("CRC32"),
    SHA1("SHA-1"),
    SHA256("SHA-256");

    private static List<HashType> hashTypes = new ArrayList<HashType>();
    private String name;

    HashType(String name) {
        this.name = name;
    }

    public static HashType getHash(String hashName) {

        for (HashType HashType : values()) {
            if (HashType.name.equalsIgnoreCase(hashName)) return HashType;
        }

        return null;

    }

    public static void addHash(HashType hashType) {
        hashTypes.add(hashType);
    }

    public static HashType[] getHashs() {
        return hashTypes.toArray(new HashType[hashTypes.size()]);
    }

    public String getName() {
        return name;
    }

}
