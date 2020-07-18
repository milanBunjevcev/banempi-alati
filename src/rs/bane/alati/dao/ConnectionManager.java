package rs.bane.alati.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {

    private static Connection issupConnection;
    private static Connection ucinciConnection;

    public static Connection openUcinakConnection() {
        createPropFiles();
        try {
            getPublicIP();

            Class.forName("org.firebirdsql.jdbc.FBDriver");
            Properties connectionProperties;
            connectionProperties = readPropertiesFile("ucinakConnection.properties");
            Connection connection = DriverManager.getConnection("jdbc:firebirdsql:" + connectionProperties.getProperty("server") + "/3050:" + connectionProperties.getProperty("dbpath"), connectionProperties.getProperty("user"), connectionProperties.getProperty("password"));
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("no connection"); // debug
            return null;
        }
    }

    public static Connection openIssupConnection() {
        createPropFiles();
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            Properties connectionProperties;
            connectionProperties = readPropertiesFile("connection.properties");
            Connection connection = DriverManager.getConnection("jdbc:firebirdsql:" + connectionProperties.getProperty("server") + "/3050:" + connectionProperties.getProperty("dbpath"), connectionProperties.getProperty("user"), connectionProperties.getProperty("password"));
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("no connection"); // debug
            return null;
        }
    }

    public static void openConnection() throws Exception {
        createPropFiles();
        Class.forName("org.firebirdsql.jdbc.FBDriver");
        Properties connectionProperties;
        connectionProperties = readPropertiesFile("connection.properties");
        issupConnection = DriverManager.getConnection("jdbc:firebirdsql:" + connectionProperties.getProperty("server") + "/3050:"
                + connectionProperties.getProperty("dbpath_issup"), connectionProperties.getProperty("user"), connectionProperties.getProperty("password"));
        ucinciConnection = DriverManager.getConnection("jdbc:firebirdsql:" + connectionProperties.getProperty("server") + "/3050:"
                + connectionProperties.getProperty("dbpath_ucinci"), connectionProperties.getProperty("user"), connectionProperties.getProperty("password"));
    }

    public static Connection getIssupConnection() throws Exception {
        if (issupConnection == null || issupConnection.isClosed()) {
            throw new Exception("Connection does not exist!");
        }
        return issupConnection;
    }

    public static Connection getUcinciConnection() throws Exception {
        if (ucinciConnection == null || ucinciConnection.isClosed()) {
            throw new Exception("Connection does not exist!");
        }
        return ucinciConnection;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Properties readPropertiesFile(String fileName) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            //input = Connecting.class.getResourceAsStream(fileName);
            input = new java.io.FileInputStream("properties/" + fileName);
            // load a properties file
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return prop;
        }
    }

    private static void savePropToFile(Properties prop, File file) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            prop.store(output, "");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void createPropFiles() { // trebace izbaciti neko obavestenje mozda
        File dir = new File("properties");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File("properties/connection.properties");
        if (!file.exists()) {
            String server = getPublicIP();
            if (server == null || server.equalsIgnoreCase("109.92.28.119")) {
                server = "Banesombor";
            } else {
                server = "109.92.28.119";
            }
            Properties prop = createProperties(server, "c:/Baze/issup.GDB", "c:/Baze/ucinci.GDB");
            savePropToFile(prop, file);
        }
    }

    private static Properties createProperties(String server, String pathIssup, String pathUcinci) {
        Properties prop = new Properties();
        prop.setProperty("dbpath_issup", pathIssup);
        prop.setProperty("dbpath_ucinci", pathUcinci);
        prop.setProperty("server", server);
        prop.setProperty("user", "SYSDBA");
        prop.setProperty("password", "masterkey");
        return prop;
    }

    private static String getPublicIP() {
        // Find public IP address
        String systemIpAddress;
        try {
            URL url_name = new URL("http://bot.whatismyipaddress.com");
            BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()));
            // reads system IPAddress
            systemIpAddress = sc.readLine().trim();
        } catch (Exception e) {
            e.printStackTrace();
            systemIpAddress = null;
        }
        return systemIpAddress;
    }

}
