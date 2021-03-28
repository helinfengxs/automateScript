package com.helinfengxs.cases;

import com.alibaba.fastjson.JSONObject;
import com.helinfengxs.request.RequestFuncion;
import com.helinfengxs.utils.Common;
import com.helinfengxs.utils.Log;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;

public class LoginFormCase {

    @DataProvider(name = "loginFormData")
    public Object[][] getData(){
        return  Common.getArray(Common.data.get("loginForm").toString());
    }

    @Test(dataProvider = "loginFormData",description = "表单登陆用例")
    public void loginForm(Object object){
        JSONObject jsonObject = JSONObject.parseObject(object.toString());
        Integer caseNum = jsonObject.getInteger("caseNum");
        String caseName = jsonObject.getString("caseName");
        Log.startTestCase(caseNum,caseName);
        String url =  Common.host+(String)jsonObject.get("requestAddr");
        //状态码
        Integer code = Integer.parseInt(jsonObject.get("statusCode").toString());
        //预期结果
        JSONObject expectedResults = JSONObject.parseObject(jsonObject.get("expectedResults").toString());
        HashMap<String, Object> headers = Common.getMap(jsonObject.get("headers"));
        HashMap<String, Object> params = Common.getMap(jsonObject.get("params"));
        if (headers!=null){
            headers.put("token",Common.token);
        }else{
            headers = new HashMap<>();
            headers.put("token",Common.token);
        }
        String paramsType = (String) jsonObject.get("paramsType");
        HashMap<String, Object> stringObjectHashMap = RequestFuncion.sendPost(url, headers, params, paramsType);
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
