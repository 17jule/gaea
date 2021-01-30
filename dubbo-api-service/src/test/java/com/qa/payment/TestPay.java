package com.qa.payment;

import com.qa.BaseTest;
import com.qa.basic.annos.CaseInfo;
import com.qa.basic.driver.DefaultDataProvider;
import com.qa.service.pay.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Created by chasen on 2021/1/30.
 */
public class TestPay extends BaseTest {

    @Autowired
    PaymentService paymentService;


    @Test(dataProvider = "yaml", dataProviderClass = DefaultDataProvider.class)
    @CaseInfo(caseTile = "统一交易支付接口测试场景-正常case", author = "chasen")
    private void test_pay(HashMap<String, Object> map) {
        paymentService.pay(map);
    }

    @Test(dataProvider = "yaml", dataProviderClass = DefaultDataProvider.class)
    @CaseInfo(caseTile = "统一交易支付接口测试场景—异常case", author = "chasen")
    private void test_pay_abnormal(HashMap<String, Object> map) {
        paymentService.pay(map);
    }
}
