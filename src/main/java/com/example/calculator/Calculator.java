package com.example.calculator;

public class Calculator {
    public static double calculate(double number1, double number2, char operator) {
        switch (operator) {
            case '+':
                return number1 + number2;
            case '-':
                return number1 - number2;
            case '*':
                return number1 * number2;
            case '/':
                if (number2 != 0) {
                    return number1 / number2;
                } else {
                    throw new ArithmeticException("Division by zero is not allowed");
                }
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }
}