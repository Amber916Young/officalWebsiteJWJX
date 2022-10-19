package com.web.utils.wx;

import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {
    /**
     * 将xml文件解析为map集合
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
        Map<String,String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        InputStream io = request.getInputStream();
        Document document = reader.read(io);
        Element root = document.getRootElement();
        List<Element> elements = root.elements();
        for (Element element : elements){
            map.put(element.getName(),element.getText());
        }
        io.close();
        return map;
    }
    /**
     * 文本对象转为xml
     * @param textMessage
     * @return
     */
    public static String textMessageToXml(TextMessage textMessage){
        XStream xStream = new XStream();
        //将根节点设为xml
        xStream.alias("xml",textMessage.getClass());
        return xStream.toXML(textMessage);
    }
    public static String textMessageToXml(KefuMessage textMessage){
        XStream xStream = new XStream();
        //将根节点设为xml
        xStream.alias("xml",textMessage.getClass());
        return xStream.toXML(textMessage);
    }
    public static String textMessageToXml(BaseMessage textMessage){
        XStream xStream = new XStream();
        //将根节点设为xml
        xStream.alias("xml",textMessage.getClass());
        return xStream.toXML(textMessage);
    }
}
