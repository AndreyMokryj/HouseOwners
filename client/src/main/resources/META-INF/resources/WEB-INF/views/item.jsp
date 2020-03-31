<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Item</title>
</head>
<body bgcolor="#fafad2">
    <h2>Details:</h2>
        <div style="position: absolute; width: 10%; height: 40%; background-color: aqua">
            <br>
            <ul>
                <li>id: </li><br>
                <c:forEach var="key" items="${map.keySet()}">
                    <li>${key}</li><br>

                </c:forEach>
            </ul>
        </div>
        <div style="position: absolute; background-color: aqua; left: 10%; width: 90%; height: 40%">
            <br>
            <form action="/${type}/update/${item.getId()}"method="post">
                <br>${item.getId()}<br>
                <c:forEach var="key" items="${map.keySet()}">
                    <textarea name="${key}" required>${map.get(key)}</textarea><br>

                </c:forEach>
                <button type="submit">Edit</button>
            </form><br>
            <button onclick="location.href='/${type}/'">Back</button>
            <button onclick="location.href='/${type}/delete/${item.getId()}'">Delete</button>
            <br><br><br>
        </div>
    <br>



</body>
</html>