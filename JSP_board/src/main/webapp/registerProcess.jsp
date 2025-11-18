<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%
request.setCharacterEncoding("UTF-8");

String id = request.getParameter("member_id");
String pw = request.getParameter("password");
String name = request.getParameter("name");
String phone = request.getParameter("phone");

try {
    Class.forName("org.mariadb.jdbc.Driver");
    Connection conn = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/board", "root", "root");
    String sql = "INSERT INTO member (member_id, password, name, phone) VALUES (?, ?, ?, ?)";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setString(1, id);
    pstmt.setString(2, pw);
    pstmt.setString(3, name);
    pstmt.setString(4, phone);

    int result = pstmt.executeUpdate();

    if (result > 0) {
        response.sendRedirect("login.jsp");
    } else {
        out.println("<script>alert('회원가입 실패'); history.back();</script>");
    }

    pstmt.close();
    conn.close();
} catch (Exception e) {
    out.println("오류: " + e.getMessage());
}
%>
