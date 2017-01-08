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
    <meta charset="utf-8">
    <title>Title</title>
    <link href = "https://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css" rel = "stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>


    <!-- CSS -->
    <style>
        .ui-widget-header,.ui-state-default, ui-button {
            background:#b9cd6d;
            border: 1px solid #b9cd6d;
            color: #FFFFFF;
            font-weight: bold;
        }
    </style>

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

        //удаляем книгу
        function jsDeleteUser(userid) {
            var r = confirm("Удалить пользователя с id="+userid +"?");
            if (r == true) {
                $.get("${pageContext.request.contextPath}/hw/deluser?idDelUser="+userid)
                    .done(function() {
                        location.reload();
                    })
            }
        }

    </script>

</body>
</html>
