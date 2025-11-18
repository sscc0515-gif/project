<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%
String id = request.getParameter("id");
boolean exist = false;

try {
    Class.forName("org.mariadb.jdbc.Driver");
    Connection conn = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/board", "root", "root");
    PreparedStatement pstmt = conn.prepareStatement("SELECT member_id FROM member WHERE member_id = ?");
    pstmt.setString(1, id);
    ResultSet rs = pstmt.executeQuery();
    if (rs.next()) exist = true;
    rs.close(); pstmt.close(); conn.close();
} catch (Exception e) {
    out.println("DB 오류: " + e.getMessage());
}
%>
<html>
<body>
<%
if (exist) {
%>
  <p style="color:red; font-weight:bold;"> 이미 사용 중인 아이디입니다.</p>
<%
} else {
%>
  <p style="color:green; font-weight:bold;"> 사용 가능한 아이디입니다!</p>
<%
}
%>
</body>
</html>
