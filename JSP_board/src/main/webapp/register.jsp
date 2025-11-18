<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회원가입</title>
  <link rel="stylesheet" href="style.css">
  <script>
    function checkId() {
      const id = document.getElementById("member_id").value;
      if (!id) {
        alert("아이디를 입력하세요.");
        return;
      }
      window.open("idCheck.jsp?id=" + id, "idCheck", "width=400,height=200");
    }
  </script>
</head>
<body>
  <div class="form-container">
    <h2>회원가입</h2>
    <form method="post" action="registerProcess.jsp">
      
      <div class="id-check">
      <label>아이디 *</label>
        <input type="text" id="member_id" name="member_id" required>
        <button type="button" onclick="checkId()">중복확인</button>
      </div>

      <label>비밀번호 *</label>
      <input type="password" name="password" required>
<br>
      <label>이름 *</label>
      <input type="text" name="name" required>
<br>
      <label>휴대전화 *</label>
      <input type="text" name="phone" required>
<br>
      <div class="btn-group">
        <button type="submit">회원가입</button>
      </div>
    </form>
  </div>
</body>
</html>
