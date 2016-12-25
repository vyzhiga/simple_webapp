<%--
  Created by IntelliJ IDEA.
  User: duzer
  Date: 21.12.2016
  Time: 0:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript"
            src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

    <script type="text/javascript" language="javascript">
        var numPage = 1;
        var recPerPage = 5;
        //		recPerPage = document.getElementById("recqnt").value;
        $(document).ready(function () {
            $("#load").click(function () {
                $("#tabrow_div").load('${pageContext.request.contextPath}/hw/getbooks?page='+numPage+'&recPerPage='+recPerPage);
                numPage = numPage+1;
            });
        });
    </script>

</head>
<body>

<table>
    <tr><td><a href="books.jsp">Книги</a></td><td><a href="users.jsp">Пользователи</a></td></tr>
</table>
<br>
<button type="button" id="load">Load Books</button>
<input id="recqnt" type="number" min="1" value="5">
<br>
<div id="tabhead_div">
    <table>
        <thead>
        <tr><th>ID</th><th>NameBook</th><th>ISBNBook</th><th>Book Taker</th></tr>
        </thead>
    </table>
</div>
<div id="tabrow_div">
</div>

</body>
</html>
