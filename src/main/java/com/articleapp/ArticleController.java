package com.articleapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.data.Article;
import com.model.ArticleDao;
import com.model.data.ArticlesDto;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class ArticleController {

    private final ArticleDao articleDao = new ArticleDao();
    private final ObjectMapper mapper = new ObjectMapper();

    public ArticleController(Javalin app) {
        app.get("/article", getArticles);
        app.get("/article/{id}", getArticle);
        app.get("/articles/{keyWord}", getArticlesByKeyWord);
        app.put("/add", addArticle);
        app.post("/modify", modifyArticle);
        app.delete("/delete/{id}", deleteArticle);
    }

    public Handler getArticles = ctx -> {
        ArticlesDto articles = articleDao.getAllArticles();
        ctx.json(mapper.writeValueAsString(articles));
    };

    public Handler getArticle = ctx -> {
        String nonConvertedId = ctx.pathParam("id");
        try {
            int id = Integer.parseInt(nonConvertedId);
            Article article = articleDao.getArticleById(id);
            if (article == null) {
                ctx.status(404);
                ctx.html("Not Found!");
            } else {
                ctx.json(mapper.writeValueAsString(article));
            }
        } catch (NumberFormatException e) {
            ctx.status(404);
            ctx.html("Not Found!");
        }
    };

    public Handler getArticlesByKeyWord = ctx -> {
        String keyWord = ctx.pathParam("keyWord");
        ArticlesDto articles = articleDao.getAllArticles(keyWord);
        ctx.json(mapper.writeValueAsString(articles));
    };

    public Handler addArticle = ctx -> {
        Article articleBody = mapper.readValue(ctx.body(), Article.class);
        if(!articleDao.addArticle(articleBody)){
            ctx.html("Incorrect Format!");
        }
    };

    public Handler modifyArticle = ctx -> {
        Article articleBody = mapper.readValue(ctx.body(), Article.class);
        if(!articleDao.modifyArticle(articleBody)){
            ctx.html("Incorrect Format!");
        }
    };

    public Handler deleteArticle = ctx -> {
        String nonConvertedId = ctx.pathParam("id");
        try {
            int id = Integer.parseInt(nonConvertedId);
            boolean result = articleDao.deleteArticle(id);
            if (result) {
                ctx.html("Deleted!");
            } else {
                ctx.html("Not Found!");
            }
        } catch (NumberFormatException e) {
            ctx.status(404);
            ctx.html("Not Found!");
        }
    };
}
