package helper;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ListManager;
import model.User;

import java.sql.*;

public class databaseAccess {

    String sql = "select * from users";

    PreparedStatement ps;

    {
        try {
            ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                User newUser = new User(userId, userName, password);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}

