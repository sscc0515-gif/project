<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%
String name = request.getParameter("name");
String phone = request.getParameter("phone");

try {
    Class.forName("org.mariadb.jdbc.Driver");
    Connection conn = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/board", "root", "root");

    String sql = "SELECT member_id FROM member WHERE name = ? AND phone = ?";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setString(1, name);
    pstmt.setString(2, phone);
    ResultSet rs = pstmt.executeQuery();

    if (rs.next()) {
        String foundId = rs.getString("member_id");
        out.println("<script>alert('아이디는 " + foundId + " 입니다.'); location.href='login.jsp';</script>");
    } else {
        out.println("<script>alert('일치하는 회원이 없습니다.'); history.back();</script>");
    }

    rs.close(); pstmt.close(); conn.close();
} catch (Exception e) {
    out.println("오류: " + e.getMessage());
}
%>
