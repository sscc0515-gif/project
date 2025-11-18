
import java.io.IOException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


public class PersonalInfo {
    DB_MAN DBM = new DB_MAN();
    
    String id;
    String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean checkIDPW(String id, String password){
        String strQuery = "select * from sign_info";
        
        try{
            DBM.dbOpen();
            DBM.rs = DBM.stmt.executeQuery(strQuery);
        
            while(DBM.rs.next()){
                if(DBM.rs.getString(1).equals(id)){     // 아이디가 있는지 확인하고 일치하는지 확인
                    if(DBM.rs.getString(2).equals(password))    // 비밀번호가 일치하는지 확인되면 true 반환
                        return true;
                }
            }
            return false;       // id가 없고 pwd가 일치하지않으면 false 반환
        } catch(Exception e){
            System.out.println("SQLException : " + e.getMessage());
            return false;
        }
    }
    
    public boolean checkDuplication(String id){
        String strQuery = "select id from sign_info";
        
        try{
            DBM.dbOpen();
            DBM.rs = DBM.stmt.executeQuery(strQuery);
        
            while(DBM.rs.next()){
                if(DBM.rs.getString(1).equals(id)){     // 아이디가 있는지 확인하고 이미 존재하는지 확인
                        return true;
                }
            }
            setId(id);  // id 저장하기
            return false;       // id가 없으면 false 반환
        } catch(Exception e){
            System.out.println("SQLException : " + e.getMessage());
            return false;
        }
    }
    
    public boolean checkSignUp(String pw, String pwConfirm){
        boolean check = pw.equals(pwConfirm);   // 비밀번호 일치 여부 확인
        
        try{     
            if(check){
                setPassword(pw);    // 비밀번호 저장
                DBM.dbOpen();
                DBM.rs = DBM.stmt.executeQuery("insert into sign_info values('" + getId() + "', '" + getPassword() + "')");    // DB에 계정 저장
                DBM.dbClose();
                return true;
            }
        } catch(Exception e){
            System.out.println("SQLException : " + e.getMessage());
            return false;
        }
        return false;
    }
}
