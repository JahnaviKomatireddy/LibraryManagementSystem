/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import db.DatabaseConnector;
import java.sql.*;
public class UserService {
    public void register(String username, String password) {
    try (Connection con = DatabaseConnector.getConnection()) {
        String checkquery= "SELECT * FROM users WHERE username=?";
        PreparedStatement check = con.prepareStatement(checkquery);
        check.setString(1, username);
        ResultSet rs = check.executeQuery();
        if (rs.next()) {
            System.out.println("Username already exists!");
            return;
        }
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, username);
        ps.setString(2, password);
        ps.executeUpdate();
        System.out.println("User registered successfully!");

    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
}
    public boolean login(String username, String password) {
        try (Connection con = DatabaseConnector.getConnection()) {
            String query = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }   
}
