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
    </head>

    <body>
        <table>
            <tr><td><a href="books.jsp">Книги</a></td><td><a href="users.jsp">Пользователи</a></td></tr>
        </table>
        <br>
        <input id="load" type="button" value="Load Books">
        <input id="recqnt" type="number" min="1" defaultValue="5" value="5">
        <br>
        <table>
            <thead>
                <tr><th>ID</th><th>NameBook</th><th>ISBNBook</th><th>Book Taker</th></tr>
            </thead>
            <tbody>
            </tbody>
        </table>

        <script type="text/javascript" language="javascript">
            var numPage = 1;
            var recPerPage = 5;
            $(document).ready(function () {
                recPerPage = $("#recqnt").val();
                console.log("recpp", recPerPage);
                $("<tbody></tbody>").insertAfter("tbody:last").load('${pageContext.request.contextPath}/hw/getbooks?page='+numPage+'&recPerPage='+recPerPage);
                numPage = numPage+1;
                $("#recqnt").click(function() {
                    recPerPage = $("#recqnt").val();
                    console.log("recpp", recPerPage);
                });
                $("#load").click(function () {
                    $("<tbody></tbody>").insertAfter("tbody:last").load('${pageContext.request.contextPath}/hw/getbooks?page='+numPage+'&recPerPage='+recPerPage);
                    numPage = numPage+1;
                });
            });
        </script>

    </body>
</html>
