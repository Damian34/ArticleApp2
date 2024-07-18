package com.articleapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.data.Article;

import com.model.data.ArticlesDto;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleControllerTest {
    private static Javalin app;
    private final ArticleDaoExtension dao = new ArticleDaoExtension();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final int port = 8080;

    @BeforeAll
    public static void beforeAll() {
        app = Javalin.create().start(port);
        ArticleController controller = new ArticleController(app);
    }

    @AfterAll
    public static void afterAll(){
        app.stop();
    }

    @Test
    public void getArticlesTest() throws JsonProcessingException{
        // given
        String urlString = createUrl("/article/");

        // when
        String json = dao.getResponseByUrl(urlString);

        // then
        assertNotNull(json);

        // when
        ArticlesDto dto = mapper.readValue(json, ArticlesDto.class);

        // then
        assertFalse(dto.getArticles().isEmpty());
    }

    @Test
    public void getArticleById() {
        // given
        String urlString = createUrl("/article/1");

        // when
        String json = dao.getResponseByUrl(urlString);

        // then
        assertNotNull(json);
    }

    @Test
    public void getArticleByIdFailed() {
        // given
        int maxId = dao.getMaxId() + 1;
        String urlString = createUrl("/article/" + maxId);

        // when
        int code = dao.getDeleteResponseCode(urlString, "GET");

        // then
        assertEquals(code, 404);
    }

    @Test
    public void getArticlesByKeyWord() throws JsonProcessingException {
        // given
        String urlString = createUrl("/articles/Lord");

        // when
        String json = dao.getResponseByUrl(urlString);

        // then
        assertNotNull(json);

        // when
        ArticlesDto dto = mapper.readValue(json, ArticlesDto.class);

        // then
        assertFalse(dto.getArticles().isEmpty());
    }

    @Test
    public void addArticleTest() throws JsonProcessingException {
        // given
        String urlString = createUrl("/add");
        Article article = defaultArticle();
        int maxId = dao.getMaxId();

        // when
        dao.putPostResponseByUrl(urlString, mapper.writeValueAsString(article), "PUT");

        // then
        assertTrue(maxId != dao.getMaxId());
    }

    @Test
    public void modifyArticleTest() throws JsonProcessingException {
        // given
        String urlAdd = createUrl("/add");
        Article article = defaultArticle();
        dao.putPostResponseByUrl(urlAdd, mapper.writeValueAsString(article), "PUT");

        int maxId = dao.getMaxId();
        String url = createUrl("/article/" + maxId);
        Article art = mapper.readValue(dao.getResponseByUrl(url), Article.class);

        // when
        String urlString = createUrl("/modify");
        article.setName("Harry Potter3!");
        dao.putPostResponseByUrl(urlString, mapper.writeValueAsString(article), "POST");

        String url2 = createUrl("/article/" + maxId);
        Article art2 = mapper.readValue(dao.getResponseByUrl(url2), Article.class);

        // then
        assertNotSame(art.getName(), art2.getName());
    }

    @Test
    public void deleteArticleTest() {
        // given
        int maxId = dao.getMaxId();
        String urlString = createUrl("/delete/" + maxId);

        // when
        dao.getDeleteResponseCode(urlString, "DELETE");

        // then
        assertTrue(maxId > dao.getMaxId());
    }

    private String createUrl(String urlSuffix){
        return "http://localhost:" + port + urlSuffix;
    }

    private Article defaultArticle(){
        return new Article(
                -1,
                "Harry Potter is a series of seven fantasy novels written by British author J. K. Rowling. The novels chronicle the lives of a young wizard ...",
                "1997-06-26",
                "Harry Potter",
                "J.K. Rowling",
                null
        );
    }
}
