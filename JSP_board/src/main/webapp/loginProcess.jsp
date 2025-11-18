<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%
request.setCharacterEncoding("UTF-8");
String id = request.getParameter("member_id");
String pw = request.getParameter("password");

try {
    Class.forName("org.mariadb.jdbc.Driver");
    Connection conn = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/board", "root", "root");

    String sql = "SELECT * FROM member WHERE member_id = ? AND password = ?";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setString(1, id);
    pstmt.setString(2, pw);

    ResultSet rs = pstmt.executeQuery();
    if (rs.next()) {
        session.setAttribute("userId", rs.getString("member_id"));
        session.setAttribute("userName", rs.getString("name"));
        session.setAttribute("userRole", rs.getString("role"));
        response.sendRedirect("main.jsp");
    } else {
%>
<script>
  alert("아이디 또는 비밀번호가 잘못되었습니다.");
  history.back();
</script>
<%
    }
    rs.close(); pstmt.close(); conn.close();
} catch (Exception e) {
    out.println("오류: " + e.getMessage());
}
%>
