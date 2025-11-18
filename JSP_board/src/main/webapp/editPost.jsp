<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
request.setCharacterEncoding("UTF-8");

String userId = (String) session.getAttribute("userId");
if (userId == null) {
    response.sendRedirect("login.jsp");
    return;
}

int id = Integer.parseInt(request.getParameter("id"));
Class.forName("org.mariadb.jdbc.Driver");
Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/board?useUnicode=true&characterEncoding=UTF-8", "root", "root");

PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM board WHERE board_id = ?");
pstmt.setInt(1, id);
ResultSet rs = pstmt.executeQuery();

if (rs.next()) {
  String writer = rs.getString("writer");
  String role = (String) session.getAttribute("userRole");
  if (!userId.equals(writer) && !"admin".equals(role)) {
      out.println("<script>alert('권한이 없습니다.'); history.back();</script>");
      return;
  }
%>
<html>
<head>
  <meta charset="UTF-8">
  <title>게시글 수정</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="form-container">
    <h2>게시글 수정</h2>
    <form method="post" action="editProcess.jsp">
      <input type="hidden" name="board_id" value="<%= id %>">
      <label>제목</label>
      <input type="text" name="title" value="<%= rs.getString("title") %>" required>

      <label>내용</label>
      <textarea name="content" rows="10" required><%= rs.getString("content") %></textarea>

      <div class="btn-group">
        <button type="submit">수정하기</button>
        <button type="button" onclick="history.back()">취소</button>
      </div>
    </form>
  </div>
</body>
</html>
<%
}
rs.close(); pstmt.close(); conn.close();
%>
