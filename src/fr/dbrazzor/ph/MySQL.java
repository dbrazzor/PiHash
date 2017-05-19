package fr.dbrazzor.ph;

import java.sql.*;

/**
 * Created by Thomas on 12/05/2014.
 */
class MySQL {

    private Connection connection;

    MySQL(String ip, int port, String userName, String password, String database) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + database + "?user=" + userName + "&password=" + password + "&useSSL=false");

    }

    boolean tableExists() {

        DatabaseMetaData dbm;

        try {
            dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "Passwords", null);
            if (tables.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

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

            statement.executeUpdate("CREATE TABLE `" + tableName + "` (\nID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," + update +
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

    String getLastPass() {

        PreparedStatement statement;
        String pass = "a";

        try {

            statement = connection.prepareStatement("");
            ResultSet resultSet = statement.executeQuery("SELECT Password FROM Passwords ORDER BY ID DESC LIMIT 1");

            while (resultSet.next()) {

                pass = resultSet.getString("Password");

            }

            return pass;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    int getLastId() {

        PreparedStatement statement;
        int id = 1;

        try {

            statement = connection.prepareStatement("");
            ResultSet resultSet = statement.executeQuery("SELECT ID FROM Passwords ORDER BY ID DESC LIMIT 1");

            while (resultSet.next()) {

                id = resultSet.getInt("ID");

            }

            return id;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1;

    }

}
