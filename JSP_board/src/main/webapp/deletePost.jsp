<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%
String userId = (String) session.getAttribute("userId");
String role = (String) session.getAttribute("userRole");

int boardId = Integer.parseInt(request.getParameter("id"));

Class.forName("org.mariadb.jdbc.Driver");
Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/board?useUnicode=true&characterEncoding=UTF-8", "root", "root");

PreparedStatement check = conn.prepareStatement("SELECT writer FROM board WHERE board_id = ?");
check.setInt(1, boardId);
ResultSet rs = check.executeQuery();
if (rs.next()) {
  String writer = rs.getString("writer");
  if (!userId.equals(writer) && !"admin".equals(role)) {
    out.println("<script>alert('권한이 없습니다.'); history.back();</script>");
    return;
  }
}

PreparedStatement deleteFile = conn.prepareStatement("DELETE FROM file WHERE board_id = ?");
deleteFile.setInt(1, boardId);
deleteFile.executeUpdate();

PreparedStatement deletePost = conn.prepareStatement("DELETE FROM board WHERE board_id = ?");
deletePost.setInt(1, boardId);
deletePost.executeUpdate();

deleteFile.close(); deletePost.close(); conn.close();
response.sendRedirect("main.jsp");
%>
