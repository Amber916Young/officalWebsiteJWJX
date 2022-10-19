package com.web.utils.wx;

import com.fasterxml.jackson.databind.JsonNode;
import com.web.service.NoneMainService;
import com.web.utils.*;
import com.web.utils.database.DataSourceContextHolder;
import java.util.Date;
import java.util.HashMap;
public class GetNewAccessToken {
    private String grant_type ="client_credential";
    private String appid="wx66861739dbbd59b1";
    private String secret="57ea154cd32a3e0bd81f463c75de869b";

    private String JwjxMini_appid="wx43d95d81bbebafae";
    private String JwjxMini_secret="21cdd3c8a676148b30997089eabbf0e6";

    NoneMainService mainService = (NoneMainService) SpringContextUtil.getBean(NoneMainService.class);
    public  String GetToken(String type){
        DataSourceContextHolder.setDbType("dataSourceB");
        HashMap<String, Object> map = null;
        map = mainService.queryWXToken(type);
        String tokenNew = "";
        ResponseModel res = getNewAccessToken(map);
        if(res.getCode()!=0){
            HashMap parans = (HashMap) res.getData();
            tokenNew = parans.get("access_token").toString();
            mainService.updateJwjxWeixin(parans);
        }else {
            HashMap data = (HashMap) res.getData();
            tokenNew = String.valueOf(data.get("access_token"));
        }
        return tokenNew;
    }


    public static void main(String[] args){
        String a = "client_credential";
        String b = "wx66861739dbbd59b1";
        String c = "57ea154cd32a3e0bd81f463c75de869b";

        getAccessToken(a,b,c);

    }
    public String getWebToken(){
//        String redUrl="http://wq5s3f.natappfree.cc/auth_response";
        String redUrl="http%3A%2F%2Fwq5s3f.natappfree.cc%2Fauth_response";
        String url="https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=wx8b62c1db396ad956" +
                "&redirect_uri="+redUrl+"&response_type=code" +
                "&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        return "";

    }
    public String getopenId(){
        String openid="";



        return openid;
    }

    public static void getAccessToken(String a ,String b,String c){
        // 这个接口每天只能调用 2000 次，每个token的存活时间是 7200s
        // 所以可以把这token存入redis，这样就可以减少调用的次数
        // 这里为了代码的简单就不引入redis
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+a + "&appid="+b + "&secret="+c;
        JsonNode jsonNode = JsonUtils.stringToJsonNode(HttpRequest.getwx(url));
        String token = jsonNode.get("access_token").toString();
        String tokenstr = token.substring(1,token.length()-1);
        System.out.println(tokenstr);
    }


    public ResponseModel getNewAccessToken(HashMap map){
        String access_token = map.get("access_token").toString();
        String jsapi_ticket = map.get("jsapi_ticket").toString();
        String appID = map.get("appID").toString();
        String secret = map.get("appSecret").toString();
        String timestamp2 = map.get("timestamp").toString();
        ResponseModel responseModel = new ResponseModel();
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+grant_type + "&appid="+appID + "&secret="+secret;
        long lon = ((new Date()).getTime() / 1000) - Long.parseLong(timestamp2);
        HashMap hashMap = new HashMap();
        hashMap.put("appID",appID);
        if (access_token == null || access_token.equals("") || access_token.equals("error")) {
            JsonNode jsonNode = JsonUtils.stringToJsonNode(HttpRequest.getwx(url));
            access_token = jsonNode.get("access_token").toString();
            access_token = access_token.substring(1,access_token.length()-1);
            jsapi_ticket = updatejsapi_ticket(access_token);
            if (!access_token.equals("error")) {
                hashMap.put("access_token",access_token);
                hashMap.put("jsapi_ticket",jsapi_ticket);
                long timestamp =  (Math.round(new Date().getTime() / 1000));
                hashMap.put("timestamp",timestamp);
                responseModel.setData(hashMap);
                responseModel.setCode(404);
                return responseModel;
            }
        } else if (lon > 7150) {//刷新access_token
            JsonNode jsonNode = JsonUtils.stringToJsonNode(HttpRequest.getwx(url));
            access_token = jsonNode.get("access_token").toString();
            access_token = access_token.substring(1,access_token.length()-1);
            jsapi_ticket = updatejsapi_ticket(access_token);
            long timestamp =  (Math.round(new Date().getTime() / 1000));
            hashMap.put("timestamp",timestamp);
            hashMap.put("access_token",access_token);
            hashMap.put("jsapi_ticket",jsapi_ticket);
            responseModel.setData(hashMap);
            responseModel.setCode(404);
            return responseModel;
        }
        HashMap resdata = new HashMap();
        resdata.put("access_token",access_token);
        resdata.put("jsapi_ticket",jsapi_ticket);
        responseModel.setData(resdata);
        responseModel.setCode(0);
        return responseModel;
    }
    public String updatejsapi_ticket(String access_token){
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi";
        JsonNode jsonNode = JsonUtils.stringToJsonNode(HttpRequest.getwx(url));
        String jsapi_ticket = jsonNode.get("ticket").toString();
        jsapi_ticket = jsapi_ticket.substring(1,jsapi_ticket.length()-1);
        return jsapi_ticket;
    }


}
