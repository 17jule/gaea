package com.qa.basic.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.qa.basic.annos.CaseInfo;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chasen on 2021/1/30.
 */
public class ExtentTestNGITestListener implements ITestListener {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    public static String reportTime;
    private static String reportDir;
    private static String reportHtml;
    private static ExtentReports extent;
    private static TestNGContextHolder<ExtentTest> suiteTest;
    private static TestNGContextHolder<ExtentTest> methodTest;
    private static TestNGContextHolder<String> category;

    public ExtentTestNGITestListener() {
    }

    public synchronized void onStart(ITestContext context) {
        File repDir = new File(reportDir);
        if(!repDir.exists()) {
            repDir.mkdirs();
        }

        ExtentTest suite = extent.createTest("Default Suite".equals(context.getSuite().getName())?"All Test Cases":context.getSuite().getName());
        suiteTest.set(suite);
    }

    public synchronized void onFinish(ITestContext context) {
        int passNum = context.getPassedTests().size();
        int failNum = context.getFailedTests().size();
        int skipNum = context.getSkippedTests().size();
        if(failNum > 0) {
            ((ExtentTest)suiteTest.get()).getModel().setStatus(Status.FAIL);
        }

        ((ExtentTest)suiteTest.get()).getModel().setDescription(String.format("Pass: %s , Fail: %s , Skip: %s (%s)", new Object[]{Integer.valueOf(passNum), Integer.valueOf(failNum), Integer.valueOf(skipNum), ICasesCountListener.getCasesCountInfo()}));
        extent.flush();
    }

    public synchronized void onTestStart(ITestResult result) {
        String suiteName = "Default Suite".equals(result.getTestContext().getSuite().getName())?"All Test Cases":result.getTestContext().getSuite().getName();
        String testName = result.getTestContext().getName();
        if(category.get() == null || !((String)category.get()).equals(suiteName + testName)) {
            category.set(suiteName + testName);
            ((ExtentTest)suiteTest.get()).assignCategory(new String[]{suiteName, testName});
        }

        ((ExtentTest)suiteTest.get()).getModel().setName(((ExtentTest)suiteTest.get()).getModel().getHierarchicalName()
                + " > " + result.getTestContext().getName());

        ExtentTest method = ((ExtentTest)suiteTest.get()).createNode(result.getTestClass().getRealClass().getName()
                + "." + result.getMethod().getMethodName());

        method.getModel().setDescription("用例描述: "
                + ((CaseInfo)result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(CaseInfo.class)).caseTile()
                + "<br/>用例作者: " + ((CaseInfo)result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(CaseInfo.class)).author());

        methodTest.set(method);
        Reporter.clear();
    }

    public synchronized void onTestSuccess(ITestResult result) {
        this.outputResultLogsToExtentTest(result, (ExtentTest)methodTest.get());
        ((ExtentTest)methodTest.get()).pass("Test passed");
    }

    public synchronized void onTestFailure(ITestResult result) {
        this.outputResultLogsToExtentTest(result, (ExtentTest)methodTest.get());
        ((ExtentTest)methodTest.get()).fail(result.getThrowable());
    }

    public synchronized void onTestSkipped(ITestResult result) {
        if(methodTest.get() != null) {
            this.outputResultLogsToExtentTest(result, (ExtentTest)methodTest.get());
            ((ExtentTest)methodTest.get()).skip("Test skipped");
        }

    }

    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    public synchronized void outputResultLogsToExtentTest(ITestResult result, ExtentTest test) {
        List outputList = Reporter.getOutput(result);
        Iterator var4 = outputList.iterator();

        while(var4.hasNext()) {
            String output = (String)var4.next();
            output = output.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            test.info(output);
        }

    }

    static {
        reportTime = sdf.format(new Date());
        reportDir = System.getProperty("user.dir") + File.separator + "reports" + File.separator + reportTime;
        reportHtml = reportDir + File.separator + "AutomationReport.html";
        extent = ExtentManager.getInstance(reportHtml);
        suiteTest = new TestNGContextHolder();
        methodTest = new TestNGContextHolder();
        category = new TestNGContextHolder();
    }
}
