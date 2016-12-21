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
        $(document).ready(function () {
            $("#driver").click(function (event) {
                $('#ins_place').insertAfter('thead').load('${pageContext.request.contextPath}/hw/getbooks');
            });
        });
    </script>

</head>
<body>

    <table>
        <tr><td><a href="books.jsp">Книги</a></td><td><a href="users.jsp">Пользователи</a></td></tr>
    </table>
    <br>
    <input type="button" id="driver" value="Load Books"/>
    <br>
    <table>
        <thead>
            <tr><th>ID</th><th>NameBook</th><th>ISBNBook</th><th>Book Taker</th></tr>
        </thead>
        <tbody id="ins_place">
        </tbody>
    </table>

</body>
</html>
