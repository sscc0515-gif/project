<%@ page import="java.io.*" %>
<%
String filename = request.getParameter("filename");
String path = application.getRealPath("/upload/" + filename);
File file = new File(path);

response.setHeader("Content-Disposition", "attachment;filename=" + filename);
FileInputStream in = new FileInputStream(file);
ServletOutputStream outStream = response.getOutputStream();

byte[] buf = new byte[4096];
int len;
while ((len = in.read(buf)) != -1) {
  outStream.write(buf, 0, len);
}
in.close();
outStream.close();
%>
