package com.web.service;

import com.web.mapper.ArticleMapper;
import com.web.model.Article;
import com.web.model.Member;
import com.web.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    ArticleMapper articleMapper;

    public List<Article> queryAll() {
        return articleMapper.queryAll();
    }


    public int selectPageCount(Page page) {
        return articleMapper.selectPageCount(page);
    }

    public List<Article> selectPageList(Page page) {
        List<Article> list =  articleMapper.selectPageList(page);
        return list;
    }

    public void insertArticle(Article article) {
        articleMapper.insertArticle(article);
    }

    public void deleteArticleByid(Integer id) {
        articleMapper.deleteArticleByid(id);
    }

    public Article queryArticleByid(int id) {
        return  articleMapper.queryArticleByid(id);
    }

    public void updateArticle(Article article) {
        articleMapper.updateArticle(article);
    }

    public List<Article> queryAllByType(HashMap params) {
        return articleMapper.queryAllByType(params);
    }

    public com.github.pagehelper.Page<Article> queryProduct(HashMap params) {
        return articleMapper.queryProduct(params);

    }

    public int queryProductAllNum(HashMap params) {
        return articleMapper.queryProductAllNum(params);
    }

    public List<Article> queryArticlesByid(Integer id) {
        return  articleMapper.queryArticlesByid(id);

    }
}
