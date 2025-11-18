<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>계정 찾기</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="form-container">
    <h2>아이디 / 비밀번호 찾기</h2>

    <form method="post" action="findId.jsp">
      <label>이름</label>
      <input type="text" name="name" required>
      <label>휴대전화</label>
      <input type="text" name="phone" required>
      <button type="submit">아이디 찾기</button>
    </form>

    <form method="post" action="findPw.jsp" style="margin-top: 30px;">
      <label>아이디</label>
      <input type="text" name="member_id" required>
      <label>이름</label>
      <input type="text" name="name" required>
      <label>휴대전화</label>
      <input type="text" name="phone" required>
      <button type="submit">비밀번호 찾기</button>
    </form>
  </div>
</body>
</html>
