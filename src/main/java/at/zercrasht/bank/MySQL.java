package at.zercrasht.bank;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class MySQL {

        public static String username;
        public static String password;
        public static String database;
        public static String host;
        public static String port;
        public static Connection con;

        public MySQL(String user, String pass, String host2, String dB) {}

        public static void connect()
        {
            if (!isConnected()) {
                try
                {
                    con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password=" + password + "&autoReconnect=true");
                    Bukkit.getConsoleSender().sendMessage("§aMySQL verbunden");
                }
                catch (SQLException e)
                {
                    Bukkit.getConsoleSender().sendMessage("§cEine verbindung zur MySQL konnte nicht hergestellt werden!");
                }
            }
        }

        public static void close()
        {
            if (isConnected()) {
                try
                {
                    con.close();
                    Bukkit.getConsoleSender().sendMessage("§cMySQL verbindung getrent");
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        public static boolean isConnected()
        {
            return con != null;
        }

        public static void createTable()
        {
            if (isConnected()) {
                try
                {
                    con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Bank (UUID VARCHAR(100), COINS int)");
                    Bukkit.getConsoleSender().sendMessage("§aMySQL Table created");
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        public static void update(String qry)
        {
            if (isConnected()) {
                try
                {
                    con.createStatement().executeUpdate(qry);
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        public static ResultSet getResult(String qry)
        {
            ResultSet rs = null;
            try
            {
                Statement st = con.createStatement();
                rs = st.executeQuery(qry);
            }
            catch (SQLException e)
            {
                connect();
                System.err.println(e);
            }
            return rs;
        }

        public static File getMySQLFile()
        {
            return new File("plugins/BankSystem", "MySQL.yml");
        }

        public static FileConfiguration getMySQLFileConfiguration()
        {
            return YamlConfiguration.loadConfiguration(getMySQLFile());
        }

        public static void setStandardMySQL()
        {
            FileConfiguration cfg = getMySQLFileConfiguration();

            cfg.options().copyDefaults(true);
            cfg.addDefault("username", "root");
            cfg.addDefault("password", "password");
            cfg.addDefault("database", "BankSystem");
            cfg.addDefault("host", "localhost");
            cfg.addDefault("port", "3306");
            try
            {
                cfg.save(getMySQLFile());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        public static void readMySQL()
        {
            FileConfiguration cfg = getMySQLFileConfiguration();
            username = cfg.getString("username");
            password = cfg.getString("password");
            database = cfg.getString("database");
            host = cfg.getString("host");
            port = cfg.getString("port");
        }

        public static void CheckConnection()
        {

            try {
                if(!con.isValid(0) || con.isClosed() || con == null)
                {


                    close();
                    connect();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

