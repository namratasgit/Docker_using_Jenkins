package com.example.calculator;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/calculator")
    public class CalculatorServlet extends HttpServlet {
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            double number1 = Double.parseDouble(request.getParameter("number1"));
            double number2 = Double.parseDouble(request.getParameter("number2"));
            char operator = request.getParameter("operator").charAt(0);

            double result = Calculator.calculate(number1, number2, operator);

            request.setAttribute("result", result);
            request.getRequestDispatcher("calculator.jsp").forward(request, response);
        }
    }


