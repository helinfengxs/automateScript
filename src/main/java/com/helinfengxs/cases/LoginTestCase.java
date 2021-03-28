package com.helinfengxs.cases;


import com.alibaba.fastjson.JSONObject;
import com.helinfengxs.request.RequestFuncion;
import com.helinfengxs.utils.Common;
import com.helinfengxs.utils.Log;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import java.util.HashMap;


public class LoginTestCase  {

    //构造出用户登陆参数的二维数组，传参到登陆测试方法中
    @DataProvider(name="loginDta")
    public Object[][]getData(){
        Object loginInterface = Common.data.get("loginInterface");
        return Common.getArray(loginInterface.toString());
    }


    public void login() {

    }

    /**
     * 测试登陆接口用例方法
     * @param arr 读取excel中login接口用例集对象（所有子集）
     */

    @Test(dataProvider="loginDta",description = "loginCase")

    public void testLogin(Object arr) {

//        ExtentTest node = Common.parentTest.get().createNode("11111111111111");
//        Common.test.set(node);
        //利用fastjson反序列化出jsonobject对象
        JSONObject jsonObject = JSONObject.parseObject(arr.toString());
        Integer caseNum = jsonObject.getInteger("caseNum");
        String caseName = jsonObject.getString("caseName");
        Log.startTestCase(caseNum,caseName);
        //状态码
        Integer code = Integer.parseInt(jsonObject.get("statusCode").toString());
        //预期结果
        JSONObject expectedResults = JSONObject.parseObject(jsonObject.get("expectedResults").toString());
        //拼接成完整url地址
        String url = Common.host+jsonObject.get("requestAddr");

        HashMap<String, Object> headers = null;
        HashMap<String, Object> params = null;

        /**
         *

        //判断头部信息是否为空，不为空则反序列化出Hashmap集合
        if(header!=null){
            headers = JSONObject.parseObject(header.toString(),
                new TypeReference<HashMap<String, Object>>(){});
        }
        //断参数信息是否为空，不为空则反序列化出Hashmap集合
        Object param = jsonObject.get("params");
        if(param!=null){
            params = JSONObject.parseObject(param.toString(),
                    new TypeReference<HashMap<String, Object>>(){});
        }
         */
        //提取参数信息
        params = Common.getMap(jsonObject.get("params"));
        //提取头部信息以及参数信息
        headers = Common.getMap(jsonObject.get("headers"));
        //提取参数提交方式
        String paramType = jsonObject.getString("parmsType");
        //发送请求，得到接口返回数据以及
        HashMap<String, Object> stringObjectHashMap = RequestFuncion.sendPost(url, headers, params, paramType);
        JSONObject finalResponse = JSONObject.parseObject(stringObjectHashMap.get("inertfaceResponse").toString());
        Integer statusCode = (int)stringObjectHashMap.get("statusCode");
        Log.info("预期结果： " +jsonObject.getString("expectedResults"));
        Log.endTestCase(caseName);
        Log.info("\n"+"\n");
        //断言
        Assert.assertEquals(statusCode,code);
        Assert.assertEquals(finalResponse.get("success"),expectedResults.get("success"));
        Assert.assertEquals(finalResponse.get("code"),expectedResults.get("code"));
    }
}
