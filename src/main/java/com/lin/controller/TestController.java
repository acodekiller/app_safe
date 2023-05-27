package com.lin.controller;

import com.lin.pojo.User;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * description:
 * author: Code_Lin
 * date:2023/5/20 15:40
 */
@RestController
public class TestController {

    @GetMapping("/testCommand")
    public String login(@RequestParam String str) {
        return str;
    }

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
    private final static String PASSWORD = "root";

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

}
//命令注入输入参数：C:%26calc