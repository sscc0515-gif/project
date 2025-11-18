<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>글쓰기</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
<div class="form-container">
  <h2>글쓰기</h2>
  <form method="post" action="writeProcess.jsp" enctype="multipart/form-data">
      <label>파일 첨부 (최대 5개)</label><br>
    <% for (int i = 1; i <= 5; i++) { %>
    <input type="file" name="file<%= i %>" accept="*/*"><br>
  <% } %>
    
<br> 
    <label>제목</label>&nbsp;
    <input type="text" name="title" required>
<br><br>
    <label>내용</label>&nbsp;
<br>
    <textarea name="content" cols ="40" rows = "10"required></textarea>
<br>
    

    <div class="btn-group">
      <button type="submit">등록</button>&nbsp;
      <button type="button" onclick="history.back()">취소</button>
    </div>
  </form>
</div>
</body>
</html>
