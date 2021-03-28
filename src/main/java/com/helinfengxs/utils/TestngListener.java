package com.helinfengxs.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.util.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.TestAttribute;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import org.testng.xml.XmlTest;


/**
 * 测试报告模板.
 */
public class TestngListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.createInstance("extent.html");
    //private static ThreadLocal parentTest = new ThreadLocal();
    //private static ThreadLocal test = new ThreadLocal();
    //不造为嘛，代码苦手表示用上面的代码就是编译报错，所以就随便改了改，如下，就通过了。。
    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal();

    @Override
    public synchronized void onStart(ITestContext context) {
//        ExtentTest parent = extent.createTest(getClass().getName());
//        System.out.println(context.toString());
//        parentTest.set(parent);

    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        extent.flush();
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        Object[] parameters = result.getParameters();
        JSONObject jsonObject = JSONObject.parseObject(parameters[0].toString());
        String caseName = jsonObject.getString("caseName");
        Integer caseNum = jsonObject.getInteger("caseNum");
        String requestAddr = jsonObject.getString("requestAddr");
        String requestType = jsonObject.getString("requestType");
        String headers = jsonObject.getString("headers");
        String params = jsonObject.getString("params");
        String expectedResults = jsonObject.getString("expectedResults");
        String finalResult = "请求地址： "+requestAddr+"\n"+"  请求方式： "+requestType+"\n"+"  请求头信息： "+headers+"\n"+"  请求参数："+params+"\n"+"  期望结果："+expectedResults;
        ExtentTest parent = extent.createTest(caseName);
        parentTest.set(parent);


        ExtentTest datail = parentTest.get().createNode(finalResult);
        test.set(datail);


    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {

        test.get().pass("用例执行通过");
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        test.get().fail(result.getThrowable());
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        test.get().skip(result.getThrowable());
    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }
}

class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null)
            createInstance("test-output/extent.html");

        return extent;
    }

    public static ExtentReports createInstance(String fileName) {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle(fileName);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName(fileName);
        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
        htmlReporter.config().setCSS(".node.level-1  ul{ display:none;} .node.level-1.active ul{display:block;}");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);




        return extent;
    }
}
