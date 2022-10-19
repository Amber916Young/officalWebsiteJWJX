package com.web.service;

import com.web.mapper.ArticleMapper;
import com.web.mapper.ImgAllMapper;
import com.web.model.Article;
import com.web.model.ImgAll;
import com.web.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImgAllService {
    @Autowired
    ImgAllMapper imgAllMapper;

    public void insertImg(ImgAll imgAll) {
        imgAllMapper.insertImg(imgAll);
    }
}
