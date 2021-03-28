package com.helinfengxs.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.helinfengxs.Excel.ExcelEntity;
import com.helinfengxs.Excel.ExcelListener;
import com.helinfengxs.Excel.SaveData;
import com.helinfengxs.request.RequestFuncion;
import lombok.Data;

import java.io.*;
import java.util.HashMap;
import java.util.List;

@Data
public class Common {
    //设置静态变量保存读取到excel的数据
    public static HashMap<String, Object> data;
    public static String host;
    public static String token;

    /**
     * 静态代码块，用于读取excel数据
     */
    static {
        String path = System.getProperty("user.dir");

        String fileName =path+ "\\testCase.xlsx";
        SaveData saveData = new SaveData();
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, ExcelEntity.class, new ExcelListener(saveData)).sheet().doRead();
        data = saveData.getData();

        String filePath = path+"\\host.txt";
        FileInputStream fin = null;
        BufferedReader buffReader = null;
        try {
            fin = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(fin);
            buffReader = new BufferedReader(reader);
            host = buffReader.readLine();

        } catch (Exception e) {
            e.printStackTrace();
            if (buffReader != null) {
                try {
                    buffReader.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        String url = host+"ucenter-member/login";
        HashMap<String,Object> map = new HashMap<>();
        map.put("mobile","13700000011");
        map.put("password","111111");
        HashMap<String, Object> json = RequestFuncion.sendPost(url, null, map, "json");
        JSONObject inertfaceResponse = JSONObject.parseObject(json.get("inertfaceResponse").toString());
        Object mes = inertfaceResponse.get("data");
        JSONObject jsonObject = JSONObject.parseObject(mes.toString());
        token = (String) jsonObject.get("token");

    }

    /**
     * 用户构造参数二位数组
     *
     * @param str 请求参数
     * @return 参数二维数组
     */
    public static Object[][] getArray(String str) {

        JSONArray objects = JSONArray.parseArray(str);
        List jsonArray = JSONObject.parseArray(objects.toString());
        Object[][] arry = new Object[jsonArray.size()][];
        for (int i = 0; i < objects.size(); i++) {
            arry[i] = new Object[]{jsonArray.get(i)};
        }

        return arry;
    }

    /**
     * Object对象转换为HashMan
     * @param object json格式的对象
     * @return hashmap集合
     */
    public static HashMap<String, Object> getMap(Object object) {
        HashMap<String, Object> headers = new HashMap<>();

        if (object != null) {
            headers = JSONObject.parseObject(object.toString(),
                    new TypeReference<HashMap<String, Object>>() {
                    });
        }
        return headers;
    }


}
