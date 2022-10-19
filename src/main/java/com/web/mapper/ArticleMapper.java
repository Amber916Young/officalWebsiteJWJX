package com.web.mapper;

import com.web.model.Article;
import com.web.utils.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

//@Repository
public interface ArticleMapper {

    @Select("select * from article where status=1 and category='product'")
    List<Article> queryAll();


    int selectPageCount(Page page);

    List<Article> selectPageList(Page page);

    @Insert("insert into article(shortTitle,title,content,detail,url,cover_url,status," +
            "category,flag,fatherTitle,createTime,publishTime,author) values(" +
            "#{shortTitle}," +
            "#{title}," +
            "#{content}," +
            "#{detail}," +
            "#{url}," +
            "#{cover_url}," +
            "#{status}," +
            "#{category}," +
            "#{flag}," +
            "#{fatherTitle}," +
            "#{createTime}," +
            "#{publishTime}," +
            "#{author})")
    void insertArticle(Article article);

    @Delete("delete from article where id=#{id}")
    void deleteArticleByid(Integer id);

    @Select("select * from article where id=#{id}")
    Article queryArticleByid(int id);


    @Select("select * from article where id=#{id}")
    List<Article> queryArticlesByid(Integer id);

    /**
     *
     * UPDATE table_name
     * SET column1=value1,column2=value2,...
     * WHERE some_column=some_value;
     *
     */

    @Update("update article set shortTitle=#{shortTitle}" +
            ",title=#{title} " +
            ",content=#{content} " +
            ",detail=#{detail} " +
            ",cover_url=#{cover_url} " +
            ",status=#{status} " +
            ",category=#{category} " +
            ",createTime=#{createTime} " +
            ",publishTime=#{publishTime} " +
            ",author=#{author} " +
            ",flag=#{flag}" +
            ",fatherTitle=#{fatherTitle}" +
            "  where id=#{id}")
    void updateArticle(Article article);

    List<Article> queryAllByType(HashMap params);

    com.github.pagehelper.Page<Article> queryProduct(HashMap params);

    int queryProductAllNum(HashMap params);

}
