package com.lin.utils;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SynchronousLocalShellCommand {


  /**
   * 执行命令并返回结果
   *
   * @return 命令执行结果
   */
  public static String doCommand(String command) {
//    String res = "cmd.exe -c " + command;

    String res = "powershell.exe Start-Process " + "cmd.exe -c " + command+ " -verb RunAs";
    System.out.println(res);
    ProcessBuilder processBuilder = new ProcessBuilder(res);
    try {
      // 将错误输出流转移到标准输出流中,但使用Runtime不可以
      processBuilder.redirectErrorStream(true);
      Process process = processBuilder.start();
      String dataMsg = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
      int rsp = process.waitFor();
      System.out.println("run command "+ command+", response " + rsp);
      return dataMsg;
    } catch (IOException | InterruptedException e) {
      System.out.println("run command "+ command+", exception " + e);
    }

    return null;


  }

}
