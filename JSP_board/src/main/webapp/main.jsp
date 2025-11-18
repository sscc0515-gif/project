<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, java.util.*" %>
<%@ page session="true" %>


<%
request.setCharacterEncoding("UTF-8");

String type = request.getParameter("type");
String keyword = request.getParameter("keyword");
int pages = 1;
if (request.getParameter("pages") != null) {
    pages = Integer.parseInt(request.getParameter("pages"));
}
int pageSize = 10;
int offset = (pages - 1) * pageSize;

String where = "";
if (type != null && keyword != null && !keyword.trim().isEmpty()) {
    if ("title".equals(type) || "writer".equals(type)) {
        where = " WHERE " + type + " LIKE ?";
    }
}
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>메인 게시판</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
<main>
  <nav>
    <ul class="menu">
      <li><a href="main.jsp">게시판</a></li>
      <li class="right">
        <% if (session.getAttribute("userId") == null) { %>
          <a href="login.jsp">로그인</a>
          <a href="register.jsp">회원가입</a>
        <% } else { %>
          <span><%= session.getAttribute("userName") %> 님 반갑습니다!</span>
          <a href="logout.jsp">로그아웃</a>
        <% } %>
      </li>
    </ul>
  </nav>

  <br> <h2>게시판</h2>

  <div class="board-header">
    <%
      int totalCount = 0;
      try {
        Class.forName("org.mariadb.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/board?useUnicode=true&characterEncoding=UTF-8", "root", "root");

        String countSql = "SELECT COUNT(*) FROM board b JOIN member m ON b.writer = m.member_id" + where;
        PreparedStatement countStmt = conn.prepareStatement(countSql);
        if (!where.isEmpty()) countStmt.setString(1, "%" + keyword + "%");
        ResultSet countRs = countStmt.executeQuery();
        if (countRs.next()) totalCount = countRs.getInt(1);

        countRs.close();
        countStmt.close();
        conn.close();
      } catch(Exception e) {
        out.println("총 게시글 수 오류: " + e.getMessage());
      }

      int totalPage = (int)Math.ceil((double)totalCount / pageSize);
    %>
    <span>총 <%= totalCount %>건의 게시글</span>
    <div class="search-box">
      <form method="get" action="main.jsp">
        <select name="type">
          <option value="title" <%= "title".equals(type) ? "selected" : "" %>>제목</option>
          <option value="writer" <%= "writer".equals(type) ? "selected" : "" %>>작성자</option>
        </select>
        <input type="text" name="keyword" value="<%= keyword != null ? keyword : "" %>" placeholder="검색어 입력">
        <button type="submit">검색</button>
      </form>
    </div>
  </div>

  <table class="board-table">
    <thead>
      <tr>
        <th>번호</th><th>제목</th><th>작성자</th><th>작성일</th><th>첨부</th>
      </tr>
    </thead>
    <tbody>
      <%
        try {
          Class.forName("org.mariadb.jdbc.Driver");
          Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/board?useUnicode=true&characterEncoding=UTF-8", "root", "root");

          String sql = "SELECT b.board_id, b.title, m.name AS writer, b.created_at, " +
                       "EXISTS (SELECT 1 FROM file WHERE board_id = b.board_id) AS has_file " +
                       "FROM board b JOIN member m ON b.writer = m.member_id" +
                       where +
                       " ORDER BY b.board_id DESC LIMIT ?, ?";

          PreparedStatement pstmt = conn.prepareStatement(sql);
          int paramIndex = 1;
          if (!where.isEmpty()) pstmt.setString(paramIndex++, "%" + keyword + "%");
          pstmt.setInt(paramIndex++, offset);
          pstmt.setInt(paramIndex, pageSize);

          ResultSet rs = pstmt.executeQuery();
          while (rs.next()) {
      %>
      <tr>
        <td><%= rs.getInt("board_id") %></td>
		<td>
		<%
  		if (session.getAttribute("userId") != null) {
		%>
    		<a href="viewPost.jsp?id=<%= rs.getInt("board_id") %>"><%= rs.getString("title") %></a>
		<%
  		} else {
		%>
   		 <a href="login.jsp" onclick="return confirm('로그인이 필요합니다. 로그인하시겠습니까?')">
     		 <%= rs.getString("title") %>
  		  </a>
		<%
		  }
		%>
		</td>
        <td><%= rs.getString("writer") %></td>
        <td><%= rs.getTimestamp("created_at").toLocalDateTime().toLocalDate() %></td>
        <td><%= rs.getBoolean("has_file") ? "📎" : "" %></td>
      </tr>
      <%
          }
          rs.close(); pstmt.close(); conn.close();
        } catch (Exception e) {
          out.println("<tr><td colspan='5'>데이터 오류: " + e.getMessage() + "</td></tr>");
        }
      %>
    </tbody>
  </table>

  <div class="pagination">
    <%
      for (int i = 1; i <= totalPage; i++) {
    %>
      <a href="main.jsp?pages=<%= i %><% if (type != null) out.print("&type=" + type); if (keyword != null) out.print("&keyword=" + keyword); %>"
         <%= (i == pages) ? "class='active'" : "" %>><%= i %></a>
    <%
      }
    %>
  </div>

  <% if (session.getAttribute("userId") != null) { %>
    <button class="write-btn" onclick="location.href='writePost.jsp'">글쓰기</button>
  <% } %>
  
  
  
</main>
</body>
</html>
