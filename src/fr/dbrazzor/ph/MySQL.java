package fr.dbrazzor.ph;

import java.sql.*;

/**
 * Created by Thomas on 12/05/2014.
 */
public class MySQL {

    private Connection connection;

    MySQL(String ip, int port, String userName, String password, String database) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + database + "?user=" + userName + "&password=" + password + "&useSSL=false");

    }

    boolean createTable() {

        String tableName = "Passwords";

        try {

            PreparedStatement statement = connection.prepareStatement("");
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet res = meta.getTables(null, null, null, new String[]{"TABLE"});
            while (res.next()) {

                if (res.getString("TABLE_NAME").equalsIgnoreCase(tableName)) {
                    statement.executeUpdate("DROP TABLE `" + tableName + "`");
                    break;
                }

            }
            String update = "`Password` VARCHAR ( 255 ) ,\n";

            HashType[] hashTypes = HashType.getHashs();

            for (int i = 0; i < hashTypes.length; i++) {
                update += "`" + hashTypes[i].getName() + "` VARCHAR( 255 )" + (i != hashTypes.length - 1 ? " ,\n" : "\n");
            }

            statement.executeUpdate("CREATE TABLE `" + tableName + "` (\n" + update +
                    ") ENGINE=InnoDB CHARACTER SET=utf8");
            System.out.println("MySQL Table created!");


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    boolean addHash(String password, String... hashs) {

        String tableName = "Passwords";

        try {
            PreparedStatement statement = connection.prepareStatement("");

            HashType[] hashTypes = HashType.getHashs();

            String update = "INSERT INTO `" + tableName + "` (`Password`, ";

            for (int i = 0; i < hashTypes.length; i++) {
                update += "`" + hashTypes[i].getName() + "`" + (i != hashTypes.length - 1 ? ", " : "");
            }

            update += ") VALUES ('" + password.replaceAll("\\\\", "\\\\\\\\") + "', ";

            for (int i = 0; i < hashTypes.length; i++) {
                update += "'" + hashs[i] + "'" + (i != hashTypes.length - 1 ? ", " : "");
            }

            update += ")";

            statement.executeUpdate(update);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

}
