<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%
request.setCharacterEncoding("UTF-8");
String userId = (String) session.getAttribute("userId");
String role = (String) session.getAttribute("userRole");

int boardId = Integer.parseInt(request.getParameter("board_id"));
String title = request.getParameter("title");
String content = request.getParameter("content");

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

PreparedStatement pstmt = conn.prepareStatement("UPDATE board SET title=?, content=? WHERE board_id=?");
pstmt.setString(1, title);
pstmt.setString(2, content);
pstmt.setInt(3, boardId);
pstmt.executeUpdate();

pstmt.close(); conn.close();
response.sendRedirect("viewPost.jsp?id=" + boardId);
%>
