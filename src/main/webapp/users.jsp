<%--
  Created by IntelliJ IDEA.
  User: duzer
  Date: 21.12.2016
  Time: 1:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript"
            src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
</head>
<body>

    <table>
        <tr><td><a href="books.jsp">Книги</a></td><td><a href="users.jsp">Пользователи</a></td></tr>
    </table>

    <button type="button">Добавить пользователя</button>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Пользователь</th>
                <th></th>
            </tr>
        </thead>
        <tbody></tbody>
    </table>

    <script type="text/javascript" language="javascript">

        var getUsersUrl = "${pageContext.request.contextPath}/hw/getusers"

        $(document).ready(function () {
            $("<tbody></tbody>").insertAfter("tbody:last").load(getUsersUrl);
        });

    </script>

</body>
</html>
