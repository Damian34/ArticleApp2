package com.articleapp;

import io.javalin.Javalin;

public class DriverJavelin {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);
        ArticleController articles = new ArticleController(app);
    }
}
