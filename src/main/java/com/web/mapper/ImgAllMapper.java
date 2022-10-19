package com.web.mapper;

import com.web.model.Article;
import com.web.model.ImgAll;
import com.web.utils.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ImgAllMapper {

/***
 *     private int id;
 *     private int aid;
 *     private String fileName;
 *     private String originalName;
 *     private String mimeType;
 *     private String fileUrl;
 *     private String type;
 * */
    @Insert("insert into img_all(fileName,originalName,mimeType,fileUrl,type) values(" +
            " #{fileName}" +
            ",#{originalName}" +
            ",#{mimeType}" +
            ",#{fileUrl}" +
            ",#{type})")
    void insertImg(ImgAll imgAll);
}
