package com.helinfengxs.request;

import com.alibaba.fastjson.JSONObject;
import com.helinfengxs.utils.Log;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class RequestFuncion {
    /**
     * 发送get请求方法带参数和头部信息
     * @param url  请求地址
     * @param headers 请求头部信息
     * @param params 请求参数
     * @return 返回接口响应信息，hashmap集合
     */
    public static HashMap<String,Object> sendGet(String url,HashMap<String,Object> headers,HashMap<String,Object> params){
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HashMap<String,Object> finalResponse = new HashMap<>(); //用户封装最终接口返回数据。
        Log.info("请求地址："+url);
        Log.info("请求头信息 ： "+headers);
        Log.info("参数信息 ： "+params);
        try {
            //添加请求参数；
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                Set<String> keys = params.keySet();
                //第一种方式添加参数
                /**
                 for(String key:keys){
                 uriBuilder.setParameter(key,String.valueOf(parmas.get(key)));
                 }
                 */
                //第二种添加参数方式
                List<NameValuePair> list = new LinkedList<>();
                for(String key:keys){
                    BasicNameValuePair param = new BasicNameValuePair(key,String.valueOf(params.get(key)));
                    list.add(param);
                }
                uriBuilder.setParameters(list);
            }
            long startTime = System.currentTimeMillis();
            // 根据带参数的URI对象构建GET请求对象
            HttpGet get = new HttpGet(uriBuilder.build());

            if (headers != null) {
                //添加请求头
                Set<String> headKeys = headers.keySet();
                //循环添加请求头
                for(String headerKey:headKeys){
                    get.addHeader(headerKey,String.valueOf(headers.get(headerKey)));
                }
            }
            Log.info("开发发送请求");
            //发送请求得到CloseableHttpResponse响应对象
            response = httpClient.execute(get);
            long ednTime =System.currentTimeMillis();
            Log.info("请求时间: "+(ednTime-startTime)+"ms");
            if (response != null) {
                HttpEntity entity = response.getEntity();
                String inertfaceResponse = EntityUtils.toString(entity, "utf-8");
                Integer statusCode = response.getStatusLine().getStatusCode();
                Log.info(String.valueOf(statusCode));
                Log.info("实际结果： "+ inertfaceResponse);
                finalResponse.put("statusCode",statusCode);
                finalResponse.put("inertfaceResponse",inertfaceResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return finalResponse;

    }

    /**
     * get请求带参数，无头部信息
     * @param url 请求地址
     * @param parmas 参数信息
     * @return 接口返回数据
     */
    public static HashMap<String,Object> sendGet(String url,HashMap<String,Object> parmas){
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;

            //创建HttpGet对象用于发送请求

            try{
                URIBuilder uriBuilder = new URIBuilder(url);
                if (parmas != null) {
                    Set<String> keys = parmas.keySet();
                    //第一种方式添加参数
                    /**
                     for(String key:keys){
                     uriBuilder.setParameter(key,String.valueOf(parmas.get(key)));
                     }
                     */
                    //第二种添加参数方式
                    List<NameValuePair> list = new LinkedList<>();
                    for(String key:keys){
                        BasicNameValuePair param = new BasicNameValuePair(key,String.valueOf(parmas.get(key)));
                        list.add(param);
                    }
                    uriBuilder.setParameters(list);
                }
                // 根据带参数的URI对象构建GET请求对象
                HttpGet get = new HttpGet(uriBuilder.build());
            //发送请求得到CloseableHttpResponse响应对象
            response = httpClient.execute(get);
            if (response != null) {
                StatusLine code = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                String s = EntityUtils.toString(entity);
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * get请求不带参数，无头部信息
     * @param url 请求地址
     * @return 接口返回数据
     */
    public static HashMap<String,Object> sendGet(String url){

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;

        try {
            // 根据带参数的URI对象构建GET请求对象
            HttpGet get = new HttpGet(url);
            //发送请求得到CloseableHttpResponse响应对象
            response = httpClient.execute(get);
            if (response != null) {
                StatusLine code = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                String s = EntityUtils.toString(entity);
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;

    }

    /**
     * 发送post请求方法
     * @param url 请求路径
     * @param headers 请求头
     * @param params 请求参数
     * @param parmasType 参数提交类型，json和表单
     * @return 返回接口数据
     */
    public static HashMap<String,Object>sendPost(String url,HashMap<String,Object> headers,HashMap<String,Object> params,String parmasType){

        HashMap<String,Object> finalResponse = new HashMap<>(); //用户封装最终接口返回数据。
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        Log.info("请求地址："+url);
        Log.info("请求头信息 ： "+headers);
        Log.info("参数信息 ： "+params);
        //添加请求头
        if (headers != null) {
            //添加请求头
            Set<String> headKeys = headers.keySet();
            //循环添加请求头
            for(String headerKey:headKeys){
                httpPost.addHeader(headerKey,String.valueOf(headers.get(headerKey)));
            }
        }
        //判断参数提交方式，分为form表单提交和json提交
        if ("form".equals(parmasType)){
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            for(String key:params.keySet()){
                formparams.add(new BasicNameValuePair(key,String.valueOf(params.get(key))));
            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
            httpPost.setEntity(urlEncodedFormEntity);
        }else {

            httpPost.setHeader("Content-type","application/json");
            JSONObject json = new JSONObject(params);
            StringEntity httpEntity = new StringEntity(json.toString(),"utf-8");


            // 传参
            httpPost.setEntity(httpEntity);


        }


        try {
            long startTime =System.currentTimeMillis();
            response = httpClient.execute(httpPost);
            long ednTime =System.currentTimeMillis();
            Log.info("请求时间: "+(ednTime-startTime)+"ms");
            if (response != null) {
                HttpEntity entity = response.getEntity();
                String inertfaceResponse = EntityUtils.toString(entity, "utf-8");
                Integer statusCode = response.getStatusLine().getStatusCode();
                finalResponse.put("statusCode",statusCode);
                finalResponse.put("inertfaceResponse",inertfaceResponse);
                Log.info(String.valueOf(statusCode));
                Log.info("实际结果： "+ inertfaceResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return finalResponse;
    }
}
