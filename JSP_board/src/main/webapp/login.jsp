<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>로그인</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="form-container">
    <h2>로그인</h2>
    <form method="post" action="loginProcess.jsp">
      <label>아이디</label>
      <input type="text" name="member_id" required>

      <label>비밀번호</label>
      <input type="password" name="password" required>

      <div class="btn-group">
        <button type="submit">로그인</button><br>
        <a href="findAccount.jsp" class="cancel">아이디/비밀번호 찾기</a> 
        
      </div>
      
    </form>
 <a href = "register.jsp">회원가입</a><br>
    
  </div>
</body>
</html>
