package com.web.mapper;

import com.web.model.Certification;
import com.web.model.Member;
import com.web.model.Servers;
import com.web.utils.Page;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @author amber
 */
public interface CertificationMapper {

    @Select("select id,name,version,category,type,DATE_FORMAT(createTime,'%Y-%m-%d') as createTime  from certification where status=1 order by id asc")
    List<Certification> queryAll(Page page);

    @Select("select * from jwjxinfo.server_user where state=0 and type=0  and username != Administrator limit 1 ")
    Servers queryAllservers();
}
