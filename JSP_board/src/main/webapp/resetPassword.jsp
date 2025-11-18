<%@ page contentType="text/html;charset=UTF-8" %>
<%
String id = (String) session.getAttribute("resetUserId");
if (id == null) {
  response.sendRedirect("login.jsp");
  return;
}
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>비밀번호 재설정</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="form-container">
    <h2>비밀번호 재설정</h2>
    <form action="resetPasswordProcess.jsp" method="post">
      <label>새 비밀번호</label>
      <input type="password" name="newPassword" required>
      <button type="submit">비밀번호 변경</button>
    </form>
  </div>
</body>
</html>
