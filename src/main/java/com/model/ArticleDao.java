package com.model;

import com.model.data.Article;
import com.model.data.ArticlesDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArticleDao {
    private final static Logger logger = LogManager.getLogger();

    public ArticlesDto getAllArticles() {
        List<Article> articles = new ArrayList<>();
        try {
            Connection c = Objects.requireNonNull(ConnectionManager.getConnection());
            PreparedStatement ps = c.prepareStatement("select * from article order by publication_date desc;");
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                articles.add(toArticle(result));
            }
        } catch (SQLException e) {
            logger.error("SQLException-error: ", e);
        }
        return new ArticlesDto(articles);
    }

    public Article getArticleById(int id) {
        try {
            Connection c = Objects.requireNonNull(ConnectionManager.getConnection());
            PreparedStatement ps = c.prepareStatement("select * from article where id=" + id);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                return toArticle(result);
            }
        } catch (SQLException e) {
            logger.error("SQLException-error: ", e);
        }
        return null;
    }

    public ArticlesDto getAllArticles(String keyWord) {
        List<Article> articles = new ArrayList<>();
        try {
            Connection c = Objects.requireNonNull(ConnectionManager.getConnection());
            PreparedStatement ps = c.prepareStatement("select * from article where LOWER(contents) like LOWER('%" + keyWord + "%') order by publication_date desc;");
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                articles.add(toArticle(result));
            }
        } catch (SQLException e) {
            logger.error("SQLException-error: ", e);
        }
        return new ArticlesDto(articles);
    }

    public boolean addArticle(Article articleBody) {
        try {
            Connection c = Objects.requireNonNull(ConnectionManager.getConnection());
            PreparedStatement ps = c.prepareStatement("INSERT INTO article(contents,publication_date,name,author) VALUES(?,?,?,?);");
            Date date = articleBody.getPublicationDateSQL();
            if (articleBody.isNotFull()) {
                return false;
            }
            ps.setString(1, articleBody.getContents());
            ps.setDate(2, date);
            ps.setString(3, articleBody.getName());
            ps.setString(4, articleBody.getAuthor());
            ps.executeUpdate();
            return true;
        }  catch (SQLException e) {
            logger.error("SQLException-error: ", e);
        }
        return false;
    }

    public boolean modifyArticle(Article articleBody) {
        try {
            Connection c = Objects.requireNonNull(ConnectionManager.getConnection());
            if (articleBody.getId() < 0) {
                return false;
            }
            if (articleBody.isEmpty()) {
                return true;
            }
            Article article = getArticleById(articleBody.getId());
            if(article != null){
                article.setContents(getNotNull(articleBody.getContents(), article.getContents()));
                article.setPublicationDate(getNotNull(articleBody.getPublicationDate(), article.getPublicationDate()));
                article.setName(getNotNull(articleBody.getName(), article.getName()));
                article.setAuthor(getNotNull(articleBody.getAuthor(), article.getAuthor()));
                String query = "update article set contents=?,publication_date=?,name=?,author=?,recording_date=? where id= ?;";
                PreparedStatement ps = c.prepareStatement(query);
                ps.setString(1, articleBody.getContents());
                ps.setString(2, articleBody.getPublicationDate());
                ps.setString(3, articleBody.getName());
                ps.setString(4, articleBody.getAuthor());
                ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                ps.setInt(6, articleBody.getId());
                ps.executeUpdate();
            } else {
                return false;
            }
            return true;
        } catch (SQLException e) {
            logger.error("SQLException-error: ", e);
        }
        return false;
    }

    public boolean deleteArticle(int id) {
        try {
            Connection c = Objects.requireNonNull(ConnectionManager.getConnection());
            String query = "delete from article where id= ? ;";
            PreparedStatement ps = c.prepareStatement(query);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("SQLException-error: ", e);
        }
        return false;
    }

    private Article toArticle(ResultSet result) throws SQLException {
        int id = result.getInt("id");
        String contents = result.getString("contents");
        String publication_date = result.getString("publication_date");
        String name = result.getString("name");
        String author = result.getString("author");
        String recording_date = result.getString("recording_date");
        return new Article(id, contents, publication_date, name, author, recording_date);
    }

    private <T> T getNotNull(T o1, T o2){
        return o1 != null ? o1 : o2;
    }
}
