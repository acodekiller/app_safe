package com.lin.controller;

import com.lin.mapper.UserMapper;
import com.lin.pojo.User;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * description:
 * author: Code_Lin
 * date:2023/5/20 15:40
 */
@RestController
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/testXSQL")
    public String testXSQL(@RequestBody UserDTO userDTO){
        User user1 = userMapper.selectUser2(userDTO.getUsername(), userDTO.getPassword());
        if(user1 != null){
            return "【提示】登录成功！" + user1;
        }
        return "【错误】登录失败！";
    }
    //admin' or '1' = '1' limit 1 #

    @GetMapping("/testXSQL2")
    public String testXSQL2(@RequestBody UserDTO userDTO){
        User user1 = userMapper.selectUser(userDTO.getUsername(), userDTO.getPassword());
        if(user1 != null){
            return "【提示】登录成功！" + user1;
        }
        return "【错误】登录失败！";
    }

//    @GetMapping("/testCommand")
//    public String login(@RequestParam String str) {
//        return str;
//    }

    @GetMapping("/linux/codeInject")
    public String codeInjectLinux(@RequestParam String filePath) {
//        if(!valid_command(filePath)){
//            return "输入参数不合法!";
//        }
        String[] cmdList = new String[]{"sh", "-c", "ls -la" + filePath};
        ProcessBuilder builder = new ProcessBuilder(cmdList);
        //如果此属性为true，则随后由该对象的start()方法启动的子进程生成的任何错误输出将与标准输出合并，
        //以便可以使用Process.getInputStream()方法读取两者。
        builder.redirectErrorStream(true);
        Process proces = null;
        String res = "";
        try {
            proces = builder.start();
            res = IOUtils.toString(proces.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @GetMapping("/win/codeInject")
    public String codeInjectWin(@RequestParam String filePath) {
//        if(!valid_command(filePath)){
//            return "输入参数不合法!";
//        }
        String[] cmdList = new String[]{"cmd", "/C", "dir " + filePath};
        ProcessBuilder builder = new ProcessBuilder(cmdList);
        //如果此属性为true，则随后由该对象的start()方法启动的子进程生成的任何错误输出将与标准输出合并，
        //以便可以使用Process.getInputStream()方法读取两者。
        builder.redirectErrorStream(true);
        Process proces = null;
        String res = "";
        try {
            proces = builder.start();
            res = IOUtils.toString(proces.getInputStream(), "GBK");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }


    private static boolean valid_command(String s){
        if(s.indexOf("&") > 0) return false;
        if(s.indexOf("|") > 0) return false;
        if(s.indexOf(";") > 0) return false;
        if(s.indexOf(">") > 0) return false;
        if(s.indexOf("<") > 0) return false;
        if(s.indexOf("'") > 0) return false;
        if(s.indexOf("\"") > 0) return false;
        if(s.indexOf("!") > 0) return false;
        if(s.indexOf("`") > 0) return false;
        if(s.indexOf("%") > 0) return false;
        return true;
    }

    private final static String USERNAME = "admin";
    private final static String PASSWORD = "123";

    @PostMapping("/login")
    public void login(HttpServletRequest request,HttpServletResponse response) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
        DocumentBuilder builder;
        String result = "";
        try{
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(request.getInputStream());
            String username = getValueByTagName(document,"username");
            String password = getValueByTagName(document,"password");
            if(username.equals(USERNAME) && password.equals(PASSWORD)){
                result = String.format("<result><code>%d</code><msg>%s</msg></result>",1,username);
            }else{
                result = String.format("<result><code>%d</code><msg>%s</msg></result>",0,username);
            }
        }catch (Exception e){
            e.printStackTrace();
            result = String.format("<result><code>%d</code><msg>%s</msg></result>",3,e.getMessage());
        }
        response.setContentType("text/xml;charset=UTF-8");
        try {
            response.getWriter().append(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getValueByTagName(Document doc, String tagName){
        if(doc == null || tagName.equals(null)){
            return "";
        }
        NodeList pl = doc.getElementsByTagName(tagName);
        if(pl != null && pl.getLength() > 0){
            return pl.item(0).getTextContent();
        }
        return "";
    }

    private Boolean isValidFolder(String folder){
        if(folder.contains("folder1") || folder.contains("folder2")){
            return true;
        }
//        Set<String> set = new HashSet<String>(){{
//            add("folder1");
//            add("folder2");
//        }};
//        if(set.contains(folder)){
//            return true;
//        }
        return false;
    }

    @PostMapping("/upLoad")
    public String upLoadFile(@RequestParam MultipartFile multipartFile,@RequestParam String folder){
        if (isValidFolder(folder) && !multipartFile.isEmpty()){
            try {
                //上传的文件需要保存的路径和文件名称，路径需要存在，否则报错
                multipartFile.transferTo(new File("D:/test/" + folder + "/"+ multipartFile.getOriginalFilename()));
            } catch (IllegalStateException | IOException e){
                e.printStackTrace();
                return "上传失败";
            }
        } else {
            return "请选择文件并且输入合法的目录：folder1 或 folder2";
        }
        return "上传成功";
    }

    @PostMapping("/upLoadWithCanonicalPath")
    public String upLoadFileWithCanonicalPath(@RequestParam MultipartFile multipartFile,@RequestParam String folder) throws IOException {
        File file = new File("D:/test/" + folder + "/" + multipartFile.getOriginalFilename());
        //对文件路径进行归一化
        String canonicalPath = file.getCanonicalPath();
        if (isValidFolder(canonicalPath) && !multipartFile.isEmpty()){
            try {
                //上传的文件需要保存的路径和文件名称，路径需要存在，否则报错
                multipartFile.transferTo(file);
            } catch (IllegalStateException | IOException e){
                e.printStackTrace();
                return "上传失败";
            }
        } else {
            return "请选择文件并且输入合法的目录：folder1 或 folder2";
        }
        return "上传成功";
    }


    public static class UserDTO{
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "UserDTO{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

}





//命令注入输入参数：C:%26calc




//content-type为 multipart/form-data 或 multipart/mixed stream