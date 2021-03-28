package com.helinfengxs.utils;


import com.vimalselvam.testng.SystemInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Auther: chj
 * @Date: 2019/11/13 17:54
 * @Description:可用于添加系统信息，例如：db的配置信息，人员信息，环境信息等。根据项目实际情况添加
 */
public class ExtentreportConfig implements SystemInfo {
    @Override
    public Map<String, String> getSystemInfo() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("env.properties");
        Properties properties = new Properties();
        Map<String, String> systemInfo = new HashMap<>();
        try {
            properties.load(inputStream);
            systemInfo.put("测试环境", properties.getProperty("Environment"));
            systemInfo.put("测试人员", "xxx");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return systemInfo;
    }
}


