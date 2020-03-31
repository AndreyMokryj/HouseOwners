<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${type}</title>
</head>
<body bgcolor="#fafad2">
    <h2>List of ${type}:</h2>
    <ul>
        <c:forEach var="item" items="${items}">
            <li><a href="/${type}/${item.getId()}">${item.toString()}</a></li>
        </c:forEach>
    </ul>
    <br>
    <p style="color: red">${message}</p>
    <br><br>
    <div style="position: absolute; width: 10%; height: 30%; background-color: aqua">
        <br>
            <c:forEach var="el" items="${list}">
                ${el} <br />
            </c:forEach>
    </div>
    <div style="position: absolute; background-color: aqua; left: 10%; width: 90%; height: 30%">
        <br>
        <form action="/${type}/" method="post">
            <c:forEach var="el" items="${list}">
                <input type="text" name="${el}" required><br />
            </c:forEach>
            <br>
            <button type="submit">Add new</button> <button onclick="location.href='/'">Back</button>
        </form>
    </div>
</body>
</html>
