/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author khg09
 */
import java.sql.*;
import java.io.*;

public class DB_MAN {
    String strDriver = "oracle.jdbc.OracleDriver";
    String strURL = "jdbc:oracle:thin:@34.22.68.5:1521:FREE";
    String strUser = 
    String strPWD = 
    
    Connection con;
    Statement stmt;
    ResultSet rs;
    
    public void dbOpen() throws IOException{
        try{
            Class.forName(strDriver);
            
            con = DriverManager.getConnection(strURL, strUser, strPWD);
            stmt = con.createStatement();
        } catch(Exception e){
          System.out.println("SQLException : " + e.getMessage());
        }
    }
    
    public void dbClose() throws IOException{
        try{
            stmt.close();
            con.close();
        } catch(SQLException e){
            System.out.println("SQLException : " + e.getMessage());
        }
    }
}   

    
