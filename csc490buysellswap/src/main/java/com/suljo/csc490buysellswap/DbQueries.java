package com.suljo.csc490buysellswap;

public class DbQueries {
    public static String loginQuery = """
               SELECT *
               FROM poc_connectivity.[user]
               WHERE userName = ? AND pWord = ?;
               """;
}
