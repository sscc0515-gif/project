<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%
String id = (String) session.getAttribute("resetUserId");
String pw = request.getParameter("newPassword");

try {
    Class.forName("org.mariadb.jdbc.Driver");
    Connection conn = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/board", "root", "root");

    String sql = "UPDATE member SET password = ? WHERE member_id = ?";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setString(1, pw);
    pstmt.setString(2, id);
    int result = pstmt.executeUpdate();

    if (result > 0) {
        session.removeAttribute("resetUserId");
        out.println("<script>alert('비밀번호가 변경되었습니다.'); location.href='login.jsp';</script>");
    } else {
        out.println("<script>alert('비밀번호 변경 실패'); history.back();</script>");
    }

    pstmt.close(); conn.close();
} catch (Exception e) {
    out.println("오류: " + e.getMessage());
}
%>
