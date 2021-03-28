package com.helinfengxs.utils;

import com.vimalselvam.testng.SystemInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MySystemInfo implements SystemInfo {
    @Override
    public Map<String, String> getSystemInfo() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("env.properties");
        Properties properties = new Properties();
        Map<String, String> systemInfo = new HashMap<>();
        try {
            properties.load(inputStream);
            systemInfo.put("测试环境", properties.getProperty("Environment"));
            systemInfo.put("测试人员", "何林峯");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return systemInfo;
    }

}
