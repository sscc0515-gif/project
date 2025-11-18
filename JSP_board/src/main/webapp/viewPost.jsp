<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, java.util.*" %>


<%
request.setCharacterEncoding("UTF-8");
String userId = (String) session.getAttribute("userId");
String userRole = (String) session.getAttribute("userRole");
boolean isAdmin = "admin".equals(userRole);

int id = Integer.parseInt(request.getParameter("id"));
Class.forName("org.mariadb.jdbc.Driver");
Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/board", "root", "root");

PreparedStatement pstmt = conn.prepareStatement(
  "SELECT b.*, m.name FROM board b JOIN member m ON b.writer = m.member_id WHERE board_id = ?");
pstmt.setInt(1, id);
ResultSet rs = pstmt.executeQuery();

if (rs.next()) {
    String writerId = rs.getString("writer");
%>
  <h2><%= rs.getString("title") %></h2>
  <p>작성자: <%= rs.getString("name") %></p>
  <p>작성일: <%= rs.getTimestamp("created_at") %></p>
  <p>작성내용</p>
  <div><pre><%= rs.getString("content") %></pre></div>

  <h4>첨부파일</h4>
  <ul>
  <%
    PreparedStatement fps = conn.prepareStatement("SELECT * FROM file WHERE board_id = ?");
    fps.setInt(1, id);
    ResultSet frs = fps.executeQuery();
    while (frs.next()) {
  %>
    <li>
      <a href="download.jsp?filename=<%= frs.getString("stored_name") %>">
        <%= frs.getString("original_name") %>
      </a>
    </li>
  <%
    }
    frs.close(); fps.close();
  %>
  </ul>
<% if (userId != null && (userId.equals(writerId) || isAdmin)) { %>
    <a href="editPost.jsp?id=<%= id %>"> 수정</a>
    <a href="deletePost.jsp?id=<%= id %>" onclick="return confirm('정말 삭제하시겠습니까?')"> 삭제</a>
  <% } %>
  <a href="main.jsp">메인으로</a>
  
<%
}
rs.close(); pstmt.close(); conn.close();
%>
