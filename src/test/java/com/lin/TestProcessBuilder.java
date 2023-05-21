package com.lin;

import com.lin.utils.SynchronousLocalShellCommand;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * description:
 * author: Code_Lin
 * date:2023/5/21 19:27
 */
@SpringBootTest
public class TestProcessBuilder {

    @Test
    public void testProcessBuilder(){

        SynchronousLocalShellCommand.doCommand("ping www.baidu.com");

    }

}
