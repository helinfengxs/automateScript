package com.helinfengxs.utils;


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


/**
 * 测试报告模板.
 */
public class ExtentTestNGIReporterListener implements IReporter {
    //生成的路径以及文件名
    private static final String OUTPUT_FOLDER = "test-output/";
    private static final String FILE_NAME = "index.html";
    private ExtentReports extent;

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        init();
        //统计suite下的成功、失败、跳过和总的用例数
        int suiteSize=0;
        int suiteFailSize=0;
        int suitePassSize=0;
        int suiteSkipSize=0;
        boolean createSuiteNode = false;
        if(suites.size()>1){
            createSuiteNode=true;
        }
        for (ISuite suite : suites) {


            Map<String, ISuiteResult> result = suite.getResults();
            //如果suite里面没有任何用例，直接跳过，不在报告里生成
            if(result.size()==0){
                continue;
            }

            ExtentTest suiteTest=null;
            //存在多个suite的情况下，在报告中将同一个一个suite的测试结果归为一类，创建一级节点。
            if(createSuiteNode){
                suiteTest = extent.createTest(suite.getName()).assignCategory(suite.getName());
            }
            boolean createSuiteResultNode = false;
            if(result.size()>1){
                createSuiteResultNode=true;
            }
            for (ISuiteResult r : result.values()) {
                ExtentTest resultNode;
                ITestContext context = r.getTestContext();
                if(createSuiteResultNode){
                    //没有创建suite的情况下，将在SuiteResult的创建为一级节点，否则创建为suite的一个子节点。
                    if( null == suiteTest){
                        resultNode = extent.createTest(r.getTestContext().getName());
                    }else{
                        resultNode = suiteTest.createNode(r.getTestContext().getName());
                    }
                }else{
                    resultNode = suiteTest;
                }
                if(resultNode != null){
                    resultNode.getModel().setName(suite.getName()+" : "+r.getTestContext().getName());
                    if(resultNode.getModel().hasCategory()){
                        resultNode.assignCategory(r.getTestContext().getName());
                    }else{
                        resultNode.assignCategory(suite.getName(),r.getTestContext().getName());
                    }
                    resultNode.getModel().setStartTime(r.getTestContext().getStartDate());
                    resultNode.getModel().setEndTime(r.getTestContext().getEndDate());
                    //统计SuiteResult下的数据
                    int passSize = r.getTestContext().getPassedTests().size();
                    int failSize = r.getTestContext().getFailedTests().size();
                    int skipSize = r.getTestContext().getSkippedTests().size();
                    suitePassSize += passSize;
                    suiteFailSize += failSize;
                    suiteSkipSize += skipSize;
                    if(failSize>0){
                        resultNode.getModel().setStatus(Status.FAIL);
                    }
                    resultNode.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ;",passSize,failSize,skipSize));
                }
                buildTestNodes(resultNode,context.getFailedTests(), Status.FAIL);
                buildTestNodes(resultNode,context.getSkippedTests(), Status.SKIP);
                buildTestNodes(resultNode,context.getPassedTests(), Status.PASS);
            }
            if(suiteTest!= null){
                suiteTest.getModel().setDescription(String.format("Pass: %s ; Fail: %s ; Skip: %s ;",suitePassSize,suiteFailSize,suiteSkipSize));
                if(suiteFailSize>0){
                    suiteTest.getModel().setStatus(Status.FAIL);
                }
            }

        }
//        for (String s : Reporter.getOutput()) {
//            extent.setTestRunnerOutput(s);
//        }

        extent.flush();
        suiteSize = suiteFailSize + suitePassSize + suiteSkipSize;

//        //新加钉钉机器人测试报告
//        String pass= DingDingUtil.folatToPer((float)suitePassSize/suiteSize);
//        String url ="https://oapi.dingtalk.com/robot/send?access_token=457d8eb62b4f0b1e578517c0488f3862c0b9e77ab21f";
//        String context="此次运行接口用例，总用例数为："+suiteSize+"；通过："+suitePassSize+"；失败："+suiteFailSize+"；跳过："+suiteSkipSize+"；通过率为："+pass;
//        try {
//            DingDingUtil.sendVal(url,context);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void init() {
        //文件夹不存在的话进行创建
        File reportDir= new File(OUTPUT_FOLDER);
        if(!reportDir.exists()&& !reportDir .isDirectory()){
            reportDir.mkdir();
        }
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER + FILE_NAME);
        htmlReporter.config().setDocumentTitle("接口自动化测试报告");
        htmlReporter.config().setReportName("接口自动化测试报告");
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        //设置报告背景颜色
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
        htmlReporter.config().setCSS(".node.level-1  ul{ display:none;} .node.level-1.active ul{display:block;}");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setReportUsesManualConfiguration(true);
        // 设置系统信息
        Properties properties = System.getProperties();
//        extent.setSystemInfo("os.name",properties.getProperty("os.name","未知"));
//        extent.setSystemInfo("os.arch",properties.getProperty("os.arch","未知"));
//        extent.setSystemInfo("os.version",properties.getProperty("os.version","未知"));
//        extent.setSystemInfo("java.version",properties.getProperty("java.version","未知"));
//        extent.setSystemInfo("java.home",properties.getProperty("java.home","未知"));
//        extent.setSystemInfo("user.name",properties.getProperty("user.name","未知"));
//        extent.setSystemInfo("user.dir",properties.getProperty("user.dir","未知"));

        // 设置自定义配置信息
        Map<String,String> systemmap = new  HashMap<>();
        MySystemInfo mySystemInfo = new MySystemInfo();
        systemmap = mySystemInfo.getSystemInfo();
        for (Map.Entry<String,String> entry:
                systemmap.entrySet()) {
            extent.setSystemInfo(entry.getKey(),entry.getValue());
        }

    }

    private void buildTestNodes(ExtentTest extenttest, IResultMap tests, Status status) {
        //存在父节点时，获取父节点的标签
        String[] categories=new String[0];
        if(extenttest != null ){
            List<TestAttribute> categoryList = extenttest.getModel().getCategoryContext().getAll();
            categories = new String[categoryList.size()];
            for(int index=0;index<categoryList.size();index++){
                categories[index] = categoryList.get(index).getName();
            }
        }

        ExtentTest test;

        if (tests.size() > 0) {
            //调整用例排序，按时间排序
            Set<ITestResult> treeSet = new TreeSet<ITestResult>(new Comparator<ITestResult>() {
                @Override
                public int compare(ITestResult o1, ITestResult o2) {
                    return o1.getStartMillis()<o2.getStartMillis()?-1:1;
                }
            });
            treeSet.addAll(tests.getAllResults());
            for (ITestResult result : treeSet) {
                Object[] parameters = result.getParameters();
//                String name="";
                //如果有参数，则使用参数的toString组合代替报告中的name
//                for(Object param:parameters){
//                    name+=param.toString();
//                }
//                if(name.length()>0){
//                    if(name.length()>50){
//                        name= name.substring(0,49)+"...";
//                    }
//                }else{
//                    name = result.getMethod().getMethodName();
//                }
                JSONObject jsonObject = JSONObject.parseObject(parameters[0].toString());
                String caseName = jsonObject.getString("caseName");
                Integer caseNum = jsonObject.getInteger("caseNum");
                String requestAddr = jsonObject.getString("requestAddr");
                String requestType = jsonObject.getString("requestType");
                String headers = jsonObject.getString("headers");
                String params = jsonObject.getString("params");
                String expectedResults = jsonObject.getString("expectedResults");
                String statusCode = jsonObject.getString("statusCode");
                String finalResult = "请求地址： "+requestAddr+"||"+"  请求方式： "+requestType+"||"+"  请求头信息： "+headers+"||"+"  请求参数："+params+"||"+" 请求返回状态码： "+statusCode+"||"+"  期望结果："+expectedResults;

                if(extenttest==null){
                    test = extent.createTest(finalResult);
                }else{
                    //作为子节点进行创建时，设置同父节点的标签一致，便于报告检索。
                    test = extenttest.createNode(finalResult).assignCategory(categories);
                }
                //test.getModel().setDescription(description.toString());
                //test = extent.createTest(result.getMethod().getMethodName());
                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);

                List<String> outputList = Reporter.getOutput(result);
                for(String output:outputList){
                    //将用例的log输出报告中
//                    test.debug(output);
                    test.debug(output.replaceAll("<","&lt;").replaceAll(">","&gt;"));
                }
                if (result.getThrowable() != null) {
                    test.log(status, result.getThrowable());
                }
                else {
                    test.log(status, "Test " + status.toString().toLowerCase() + "ed");
                }

                test.getModel().setStartTime(getTime(result.getStartMillis()));
                test.getModel().setEndTime(getTime(result.getEndMillis()));
            }
        }
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}


