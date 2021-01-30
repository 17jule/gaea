package com.qa.basic.base;

import com.qa.basic.annos.CaseInfo;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by chasen on 2021/1/30.
 */
public class ICasesCountListener implements IInvokedMethodListener {
    private static final String ALL = "all";
    private static HashMap<String, Integer> counter = new HashMap() {
        {
            this.put("all", Integer.valueOf(0));
        }
    };

    public ICasesCountListener() {
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        Method testMethod = iInvokedMethod.getTestMethod().getConstructorOrMethod().getMethod();
        if(iInvokedMethod.isTestMethod()) {
            CaseInfo caseInfo = (CaseInfo)testMethod.getAnnotation(CaseInfo.class);
            String author = caseInfo.author();
            counter.put("all", Integer.valueOf(((Integer)counter.get("all")).intValue() + 1));
            counter.put(author, Integer.valueOf((counter.get(author) != null?((Integer)counter.get(author)).intValue():0) + 1));
        }
    }

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
    }

    public static String getCasesCountInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("All Cases: " + counter.get("all") + " | ");
        counter.forEach((k, v) -> {
            if(!"all".equals(k)) {
                sb.append(k + ": " + v + " | ");
            }
        });
        return sb.toString().substring(0, sb.toString().length() - 3);
    }
}
