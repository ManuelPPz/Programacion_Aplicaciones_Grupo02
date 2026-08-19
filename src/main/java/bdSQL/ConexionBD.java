/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bdSQL;

/**
 *
 * @author manuelpalumbo
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    private static final String URL = "jdbc:mysql://localhost:3306/edEXT_db?useSSl=false&serverTimezone=UTC";
    //hay que cambiarlo y crear un perfil exclusivo para la bd actualmente tiene mis claves xD
    private static final String USER = "root";
    private static final String PASSWORD = "ghiuliana";
    
    public static Connection getConexion() throws SQLException {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                throw new SQLException("No se encontro el driver de MySQL: " + e.getMessage());
                }
    return DriverManager.getConnection(URL, USER, PASSWORD);
   }
    
 }
