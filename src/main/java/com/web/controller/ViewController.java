package com.web.controller;

import com.github.pagehelper.Page;
import com.web.model.Article;
import com.web.service.ArticleService;
import com.web.utils.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/view")
public class ViewController {
    @Autowired
    ArticleService articleService;

    @ResponseBody
    @RequestMapping("/detail/product/id/{id}")
    public Object APIViewProduct(@PathVariable("id") Integer id){
        try {
            List<Article> article =articleService.queryArticlesByid(id);
            for(Article item:article){
                if(item.getFlag()==0){
                    return item;
                }else {
                    String ftitle = item.getFatherTitle();
                    HashMap map1 = new HashMap();
                    map1.put("category","index");
                    map1.put("status",1);
                    map1.put("flag",1);
                    map1.put("fatherTitle",ftitle);
                    Page<Article> products =  articleService.queryProduct(map1);
                    return products;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    @RequestMapping("/product/id/{id}")
    public Object ViewProduct(@PathVariable("id") Integer id,Model model){
        try {
            List<Article> article =articleService.queryArticlesByid(id);
            for(Article item:article){
                if(item.getFlag()==0){
                    model.addAttribute("article",item);
                    return "/view/product/productDetail";
                }else {
                    String ftitle = item.getFatherTitle();
                    HashMap map1 = new HashMap();
                    map1.put("category","index");
                    map1.put("status",1);
                    map1.put("flag",1);
                    map1.put("fatherTitle",ftitle);
                    Page<Article> products =  articleService.queryProduct(map1);
                    model.addAttribute("articleList",products);
                    return "/view/product/productDetailtab";
                }
            }
            return "/view/product/productDetail";

        }catch (Exception e){
            e.printStackTrace();
        }
        return "/view/product/productDetail";
    }

    @ResponseBody
    @RequestMapping("/detail/news/id/{id}")
    public Object APIViewnews(@PathVariable("id") Integer id){
        ResponseModel responseModel = new ResponseModel();
        try {
            List<Article> article =articleService.queryArticlesByid(id);
            for(Article item:article){
                if(item.getFlag()==0){
                    responseModel.setCode(0);
                    responseModel.setData(item);
                    return responseModel;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }
    @RequestMapping("/news/id/{id}")
    public Object Viewnews(@PathVariable("id") Integer id,Model model){
        try {
            List<Article> article =articleService.queryArticlesByid(id);
            for(Article item:article){
                if(item.getFlag()==0){
                    model.addAttribute("article",item);
                    return "/view/newsArticle";
                }else {
                    String ftitle = item.getFatherTitle();
                    HashMap map1 = new HashMap();
                    map1.put("category","index");
                    map1.put("status",1);
                    map1.put("flag",1);
                    map1.put("fatherTitle",ftitle);
                    Page<Article> products =  articleService.queryProduct(map1);
                    model.addAttribute("articleList",products);
                    return "/view/product/productDetailtab";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "/view/newsArticle";
    }
}
