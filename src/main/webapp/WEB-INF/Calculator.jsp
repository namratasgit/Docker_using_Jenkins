<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Calculator</title>
</head>
<body>
    <h1>Simple Calculator</h1>
    <form action="calculator" method="post">
        Enter first number: <input type="text" name="number1"><br>
        Enter second number: <input type="text" name="number2"><br>
        Choose an operator: <input type="text" name="operator"><br>
        <input type="submit" value="Calculate">
    </form>
    <br>
    <c:if test="${not empty requestScope.result}">
        Result: ${requestScope.result}
    </c:if>
</body>
</html>
