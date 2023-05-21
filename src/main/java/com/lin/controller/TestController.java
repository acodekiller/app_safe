package com.lin.controller;

import com.lin.pojo.User;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.InputStream;
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

    @GetMapping("/codeInject")
    public String codeInject(String filePath) {
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


}
