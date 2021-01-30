package com.qa.basic.base;

import com.aventstack.extentreports.ExtentReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Created by chasen on 2021/1/30.
 */
public class ExtentManager {
    private static ExtentReports extent;

    public ExtentManager() {
    }

    public static ExtentReports getInstance(String filePath) {
        if(extent == null) {
            createInstance(filePath);
        }

        return extent;
    }

    public static void createInstance(String filePath) {
        extent = new ExtentReports();
        extent.setSystemInfo("os", "Windows");
        extent.attachReporter(new ExtentReporter[]{createHtmlReporter(filePath)});
    }

    public static ExtentHtmlReporter createHtmlReporter(String filePath) {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filePath);
        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle(filePath);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("Dubbo接口自动化测试报告");
        htmlReporter.config().setDocumentTitle("Dubbo接口自动化测试报告");
        return htmlReporter;
    }

    public static ExtentXReporter createExtentXReporter() {
        ExtentXReporter extentx = new ExtentXReporter("127.0.0.1", 27017);
        extentx.config().setProjectName("dubbo-api-automation");
        extentx.config().setReportName("dubbo-api-test-report");
        extentx.config().setServerUrl("http://localhost:1337");
        return extentx;
    }
}
