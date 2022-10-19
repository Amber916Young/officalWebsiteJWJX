package com.web.mapper;

import com.web.model.IP;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

//@Repository
public interface IPMapper {


    @Select("select * from egt_company where regNO=#{regNO}")
    IP queryByRegNO(String regNO);
}
