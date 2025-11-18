<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%
String id = request.getParameter("member_id");
String name = request.getParameter("name");
String phone = request.getParameter("phone");

try {
    Class.forName("org.mariadb.jdbc.Driver");
    Connection conn = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/board", "root", "root");

    String sql = "SELECT * FROM member WHERE member_id = ? AND name = ? AND phone = ?";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setString(1, id);
    pstmt.setString(2, name);
    pstmt.setString(3, phone);
    ResultSet rs = pstmt.executeQuery();

    if (rs.next()) {
        session.setAttribute("resetUserId", id);
        response.sendRedirect("resetPassword.jsp");
    } else {
        out.println("<script>alert('정보가 일치하지 않습니다.'); history.back();</script>");
    }

    rs.close(); pstmt.close(); conn.close();
} catch (Exception e) {
    out.println("오류: " + e.getMessage());
}
%>
