package com.web.utils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.xerces.util.XMLSymbols.PREFIX_XML;

public class HTMLSpirit{
    public static void main(String[] args){
        String text = "<p>\n    <span style=\" font - size:14 px; font - family:微软雅黑, &quot; Microsoft YaHei&quot;"+
       " \">&nbsp; &nbsp; 大多数企业生产管理环节中对辅材的管理是相对薄弱的环节，系统合理的区分不同辅材的出库，真实反应当前材料的消耗情况，" +
                "及时追溯辅材的源头（包括供应商、批次等），对材料渠道做到有据可查的客观评价。</span><span style=\" font - family:宋体;font - size:14 px;\"><br/></span>\n</p>";
//        System.err.println(delHTMLTag(text));
    }
    public   String delHTMLTag(String htmlStr){
        if(htmlStr==null||htmlStr.length()<=0){

        }else{
            String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
            String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
            String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

            Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
            Matcher m_script=p_script.matcher(htmlStr);
            htmlStr=m_script.replaceAll(""); //过滤script标签

            Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
            Matcher m_style=p_style.matcher(htmlStr);
            htmlStr=m_style.replaceAll(""); //过滤style标签

            Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            Matcher m_html=p_html.matcher(htmlStr);
            htmlStr=m_html.replaceAll(""); //过滤html标签
            htmlStr=htmlStr.replaceAll("&nbsp;", "");
            htmlStr=htmlStr.replaceAll("//s*|/t|/r|/n","");//去除字符串中的空格,回车,换行符,制表符
        }

        return htmlStr.trim(); //返回文本字符串
    }
    private static final String PREFIX_XML = "<xml>";
    private static final String SUFFIX_XML = "</xml>";
    private static final String PREFIX_CDATA = "<![CDATA[";
    private static final String SUFFIX_CDATA = "]]>";
    public static String mapToXml(Map<Object, Object> parm, boolean isAddCDATA) {
        StringBuffer strbuff = new StringBuffer(PREFIX_XML);
        if (null != parm) {
            for (Map.Entry<Object, Object> entry : parm.entrySet()) {
                strbuff.append("<").append(entry.getKey()).append(">");
                if (isAddCDATA) {
                    strbuff.append(PREFIX_CDATA);
                    if (null != entry.getValue()) {
                        strbuff.append(entry.getValue());
                    }
                    strbuff.append(SUFFIX_CDATA);
                } else {
                    if (null != entry.getValue()) {
                        strbuff.append(entry.getValue());
                    }
                }
                strbuff.append("</").append(entry.getKey()).append(">");
            }
        }
        return strbuff.append(SUFFIX_XML).toString();
    }

    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        Map<String, String> data = new HashMap<>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
        org.w3c.dom.Document doc = documentBuilder.parse(stream);
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        for(int i = 0;i<nodeList.getLength();i++){
            Node node = nodeList.item(i);
            if(node.getNodeType()==Node.ELEMENT_NODE){
                org.w3c.dom.Element element = (org.w3c.dom.Element )node;
                data.put(element.getNodeName(),element.getTextContent());
            }
        }
        try {
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;

    }


}