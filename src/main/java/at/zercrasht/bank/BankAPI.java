package at.zercrasht.bank;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BankAPI {

    public static boolean playerExists(String uuid) {
        try {
            ResultSet rs = MySQL.getResult("SELECT * FROM Bank WHERE UUID='" + uuid + "'");
            if (rs.next()) {
                return (rs.getString("UUID") != null);
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }
    public static void createPlayer(String uuid) {
        if (!playerExists(uuid)) {
            MySQL.update("INSERT INTO Bank (UUID, COINS) VALUES ('" + uuid + "', '0');");
        }
    }

    public static Integer getCoins(String uuid) {
        Integer i = Integer.valueOf(0);
        if (playerExists(uuid)) {
            try {
                ResultSet rs = MySQL.getResult("SELECT * FROM Bank WHERE UUID='" + uuid + "'");
                if (rs.next()) {
                    Integer.valueOf(rs.getInt("COINS"));
                }
                i = Integer.valueOf(rs.getInt("COINS"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            createPlayer(uuid);
            getCoins(uuid);
        }
        return i;
    }


    public static void setCoins(String uuid, Integer Coins) {
        if (playerExists(uuid)) {
            MySQL.update("UPDATE Bank SET Coins='" + Coins + "' WHERE UUID='" + uuid + "'");
        } else {
            createPlayer(uuid);
            setCoins(uuid, Coins);
        }
    }

    public static void addCoins(String uuid, Integer Coins) {
        if (playerExists(uuid)) {
            setCoins(uuid, Integer.valueOf(getCoins(uuid).intValue() + Coins.intValue()));
        } else {
            createPlayer(uuid);
            addCoins(uuid, Coins);
        }
    }


    public static void removeCoins(String uuid, Integer Coins) {
        if (playerExists(uuid)) {
            setCoins(uuid, Integer.valueOf(getCoins(uuid).intValue() - Coins.intValue()));
        } else {
            createPlayer(uuid);
            removeCoins(uuid, Coins);
        }
    }

}
