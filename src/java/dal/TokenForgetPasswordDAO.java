/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.TokenForgetPassword;
import model.User;

/**
 *
 * @author MinHeee
 */
public class TokenForgetPasswordDAO extends DBContext {

    public String createToken(int userId) {
        String token = UUID.randomUUID().toString();
        String sql = "INSERT INTO TokenForgetPassword (Token, ExpiryTime, IsUsed, UserId) VALUES (?, DATEADD(MINUTE, 15, GETDATE()), 0, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setInt(2, userId);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return token;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TokenForgetPasswordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public TokenForgetPassword getTokenByToken(String token) {
        String sql = "SELECT t.*, u.UserID, u.Username, u.Email " +
                    "FROM TokenForgetPassword t " +
                    "INNER JOIN Users u ON t.UserId = u.UserID " +
                    "WHERE t.Token = ? AND t.IsUsed = 0 AND t.ExpiryTime > GETDATE()";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TokenForgetPassword tokenObj = new TokenForgetPassword();
                tokenObj.setId(rs.getInt("Id"));
                tokenObj.setToken(rs.getString("Token"));
                tokenObj.setExpiryTime(rs.getString("ExpiryTime"));
                tokenObj.setIsUsed(rs.getBoolean("IsUsed"));
                
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setUserName(rs.getString("Username"));
                user.setEmail(rs.getString("Email"));
                tokenObj.setUser(user);
                
                return tokenObj;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TokenForgetPasswordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean markTokenAsUsed(String token) {
        String sql = "UPDATE TokenForgetPassword SET IsUsed = 1 WHERE Token = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(TokenForgetPasswordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void cleanupExpiredTokens() {
        String sql = "DELETE FROM TokenForgetPassword WHERE ExpiryTime < GETDATE() OR IsUsed = 1";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TokenForgetPasswordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}