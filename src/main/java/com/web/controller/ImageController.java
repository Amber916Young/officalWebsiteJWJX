package com.web.controller;

import com.web.model.ImgAll;
import com.web.service.ImgAllService;
import com.web.service.MemberService;
import com.web.utils.FileUtil;
import com.web.utils.HttpRequest;
import com.web.utils.JsonUtils;
import com.web.utils.ResponseModel;
import com.web.utils.database.DataSourceContextHolder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.VideoAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/image")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Resource(name="fileuploadPath")
    private String theSetDir ;  // ="http://119.23.237.197:8888/file/upload";

    @Autowired
    ImgAllService imgAllService;
//    public static void main(String[] args){
//        String url = "http://fileserver.jwjxinfo.com:8081//statics/article_cover/qf1615877465417.jpg";
//        String dir = "article_cover";
//        String imgName = url.substring(url.lastIndexOf("article_cover/"));
//        imgName = imgName.substring(dir.length()+1);
//        System.out.println(imgName);
//    }


//    @RequestMapping("/cover/upload")
//    @ResponseBody
    private ResponseModel uplaodImgAndFiles(@RequestPart("file") MultipartFile[] file){
        ResponseModel responseModel = new ResponseModel();
        DataSourceContextHolder.setDbType("dataSourceA");

        try {
            String url = theSetDir ;
            String directory = "test";
            ResponseModel result = HttpRequest.upload(url,file,directory,5000);
            String imageUrl="";
            ImgAll imgAll = new ImgAll();
            String fileName ="";
            HashMap map = new HashMap();
            if(result.getCode()==0){
                imageUrl = result.getData().toString();
                responseModel.setMsg("ok");
                responseModel.setCode(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setMsg(e.getMessage());
            responseModel.setCode(500);
        }
        return responseModel;
    }

    @ResponseBody
    @RequestMapping(value = "/user/worker/headImg/upload", produces = "application/json; charset=utf-8")
    public Object newUserWorkerHeadImg( @RequestPart("file") MultipartFile file) {
        ResponseModel responseModel = new ResponseModel();
        try {
            String directory = "workerHeadImg";
            ResponseModel res = uplaodImgAndFile(file,directory);
            if(res.getCode()==0){
                HashMap json  = (HashMap) res.getData();
                HashMap map = new HashMap();
                map.put("src",json.get("url").toString());
                responseModel.setCode(0);
                responseModel.setMsg("ok");
                responseModel.setData(map);
            }else {
                responseModel.setCode(500);
                responseModel.setMsg(res.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }


    private ResponseModel uplaodImgAndFile(MultipartFile file,String directory){
        ResponseModel responseModel = new ResponseModel();
        DataSourceContextHolder.setDbType("dataSourceA");

        try {
            String url = theSetDir ;
            String result = HttpRequest.upload(url,file,directory);
            ResponseModel rm = JsonUtils.jsonToPojo(result,ResponseModel.class);
            String imageUrl="";
            ImgAll imgAll = new ImgAll();
            String fileName ="";
            HashMap map = new HashMap();
            if(rm.getCode()==0){
                imageUrl = rm.getData().toString();
                fileName = imageUrl.substring(imageUrl.lastIndexOf(directory+"/"));
                fileName = fileName.substring(directory.length()+1);
                String imageName = file.getOriginalFilename();
                imgAll.setType(directory);
                imgAll.setFileName(fileName);
                imgAll.setFileUrl(imageUrl);
                imgAll.setMimeType(file.getContentType());
                imgAll.setOriginalName(imageName);
                imgAllService.insertImg(imgAll);
                map.put("url",imageUrl);
                map.put("name",imageName);
                responseModel.setData(map);
                responseModel.setMsg("ok");
                responseModel.setCode(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setMsg(e.getMessage());
            responseModel.setCode(500);
        }
        return responseModel;
    }


    @RequestMapping("/chatFile/upload")
    @ResponseBody
    public Object chatFileHandle(@RequestPart("file") MultipartFile file, HttpServletRequest request) {
        ResponseModel responseModel = new ResponseModel();
        try {

            String fileType = "file";
            String directory = "chatFile";
//            ResponseModel res = uplaodImgAndFile(file,directory);
            String filetype = FileUtil.getFileExtension(file).toUpperCase();
            filetype = filetype.substring(1);
            switch (filetype){
                case "MOV":
                    fileType="video";
                    break;
                case "MP4":
                    fileType="video";
                    break;
                case "QUCIKTIME":
                    fileType="video";
                    break;
                case "AVI":
                    fileType="video";
                    break;
                case "WMV":
                    fileType="video";
                    break;
                default:
                    break;
            }
            ResponseModel res =null;
            if(fileType.equals("file")){
                res = uplaodImgAndFile(file,directory);
            }else {
                directory = "chatVideo";
                res = uplaodImgAndFile(file, directory);
            }
            if(res.getCode()==0){
                HashMap json  = (HashMap) res.getData();
                HashMap map = new HashMap();
                map.put("src",json.get("url").toString());
                map.put("name",json.get("name").toString());
                if(!fileType.equals("file")){
                    map.put("fileType",fileType);
                }
                responseModel.setCode(0);
                responseModel.setMsg("ok");
                responseModel.setData(map);
            }else {
                responseModel.setCode(500);
                responseModel.setMsg(res.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }


    @RequestMapping("/chatImg/upload")
    @ResponseBody
    public Object chatImgHandle(@RequestPart("file") MultipartFile file, HttpServletRequest request) {
        ResponseModel responseModel = new ResponseModel();
        try {
            if(file.getSize()>1048576 ){
                file = HandleImg(file);
            }
            String directory = "chatImg";
            ResponseModel res = uplaodImgAndFile(file,directory);
            String imageUrl = "";
            if(res.getCode()==0){
                HashMap json  = (HashMap) res.getData();
                imageUrl = json.get("url").toString();
                HashMap map = new HashMap();
                map.put("src",imageUrl);
                responseModel.setCode(0);
                responseModel.setMsg("ok");
                responseModel.setData(map);
            }else {
                responseModel.setCode(500);
                responseModel.setMsg(res.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
            responseModel.setCode(500);
            responseModel.setMsg(e.getMessage());
        }
        return responseModel;
    }


    //压缩图片
    public MultipartFile HandleImg(MultipartFile file){
        MultipartFile oldMultipartFile = file;
        String fileName = file.getName();
        String contentType = file.getContentType();
        String origFilename = file.getOriginalFilename(); // 图片名
        try {
            File dest = new File("C:/upload/images/" + origFilename); // 保存位置
            System.err.println(file.getSize());
            Thumbnails.of(file.getInputStream()).scale(0.7f).outputQuality(0.25f).toFile(dest);
            FileInputStream fileInputStream = new FileInputStream(dest);
            file = new MockMultipartFile(fileName, origFilename, contentType, fileInputStream);
            fileInputStream.close();
            boolean success = dest.delete();
            logger.info("删除临时file success：{}", success);
        }catch (Exception e){
            logger.error( "压缩图片失败,把MultipartFile赋值为原来的值oldFile,exception：{}", e);
            file = oldMultipartFile;
            e.printStackTrace();
         }

         return  file;
    }

    @RequestMapping("/uploadImage.do")
    @ResponseBody
    public Map<String, String> receiveImage(@RequestPart("upload") MultipartFile file, HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        try {
            if(file.getSize()>1048576 ){
                file = HandleImg(file);
            }
            String directory = "article";
            ResponseModel res = uplaodImgAndFile(file,directory);
            String imageUrl = "";
            if(res.getCode()==0){
                HashMap json  = (HashMap) res.getData();
                imageUrl = json.get("url").toString();
                params.put("uploaded", "true");
                params.put("url", imageUrl);;
            }else {
                params.put("uploaded", "false");
                params.put("errmsg",res.getMsg() );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return params;

    }


    @RequestMapping("/cover/upload")
    @ResponseBody
    public Map<String, String> CoverImage( MultipartFile file, HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        try {
            if(file.getSize()>1048576 ){
                file = HandleImg(file);
            }
            String directory = "article_cover";
            ResponseModel res = uplaodImgAndFile(file,directory);
            String imageUrl = "";
            if(res.getCode()==0){
                HashMap json  = (HashMap) res.getData();
                imageUrl = json.get("url").toString();
                params.put("uploaded", "true");
                params.put("url", imageUrl);;
            }else {
                params.put("uploaded", "false");
                params.put("errmsg",res.getMsg() );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return params;

    }


    @RequestMapping("/message/uploadImage.do")
    @ResponseBody
    public Map<String, String> messageImage(@RequestPart("upload") MultipartFile file, HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        try {
            if(file.getSize()>1048576 ){
                file = HandleImg(file);
            }
            String directory = "emailImg";
            ResponseModel res = uplaodImgAndFile(file,directory);
            String imageUrl = "";
            if(res.getCode()==0){
                HashMap json  = (HashMap) res.getData();
                imageUrl = json.get("url").toString();
                params.put("uploaded", "true");
                params.put("url", imageUrl);;
            }else {
                params.put("uploaded", "false");
                params.put("errmsg",res.getMsg() );
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return params;

    }

}
