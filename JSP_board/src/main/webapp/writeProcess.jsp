<%@ page import="java.io.*, java.sql.*, java.util.*" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%
request.setCharacterEncoding("UTF-8");
String path = application.getRealPath("/upload");
File dir = new File(path);
if (!dir.exists()) dir.mkdirs();

MultipartRequest multi = new MultipartRequest(request, path, 50*1024*1024, "UTF-8", new DefaultFileRenamePolicy());

String title = multi.getParameter("title");
String content = multi.getParameter("content");
String writer = (String) session.getAttribute("userId");

if (writer == null) {
  out.println("<script>alert('로그인이 필요합니다.'); location.href='login.jsp';</script>");
  return;
}

try {
  Class.forName("org.mariadb.jdbc.Driver");
  Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/board", "root", "root");

  String sql = "INSERT INTO board (title, content, writer) VALUES (?, ?, ?)";
  PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
  pstmt.setString(1, title);
  pstmt.setString(2, content);
  pstmt.setString(3, writer);
  pstmt.executeUpdate();

  ResultSet rs = pstmt.getGeneratedKeys();
  int boardId = 0;
  if (rs.next()) boardId = rs.getInt(1);

  
  // 파일 업로드 처리
  for (int i = 1; i <= 5; i++) {
  String param = "file" + i;
  String original = multi.getOriginalFileName(param);
  String saved = multi.getFilesystemName(param);
  if (original != null) {
    PreparedStatement fp = conn.prepareStatement(
      "INSERT INTO file (board_id, original_name, stored_name, file_path) VALUES (?, ?, ?, ?)");
    fp.setInt(1, boardId);
    fp.setString(2, original);
    fp.setString(3, saved);
    fp.setString(4, path + "/" + saved);
    fp.executeUpdate();
    fp.close();
    
  }
}

  rs.close(); pstmt.close(); conn.close();
  response.sendRedirect("main.jsp");

} catch (Exception e) {
  out.println("오류: " + e.getMessage());
}
%>
