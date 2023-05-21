package com.lin.utils;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * description:
 * author: Code_Lin
 * date:2023/5/21 19:42
 */
public class Test22 {

    /** 命令信息 */
    private final String command;

    public Test22(String command) {
        this.command = command;
    }

    /**
     * 执行命令并返回结果
     *
     * @return 命令执行结果
     */
    public String doCommand() {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            // 将错误输出流转移到标准输出流中,但使用Runtime不可以
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            String dataMsg = IOUtils.toString(process.getInputStream());
            int rsp = process.waitFor();
            System.out.println("re         p+"+rsp);
            return dataMsg;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        Test22 test22 = new Test22("cmd.exe /c ping www.baidu.com");
        test22.doCommand();
    }

}

