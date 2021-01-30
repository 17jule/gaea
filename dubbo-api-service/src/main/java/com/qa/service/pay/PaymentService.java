package com.qa.service.pay;

import com.qa.basic.annos.DubboAPI;
import com.qa.basic.base.APIInvokeBuilder;
import com.qa.basic.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by chasen on 2021/1/30.
 */
@Component
public class PaymentService {

    @Autowired
    APIInvokeBuilder apiInvokeBuilder;

    @DubboAPI(api = "pay", service = "com.qa.trade.PaymentFacade",
            method = "pay", paramTypes = {"java.lang.long", "com.qa.trade.paymentRequest"}, schema = "payment.pay")
    public Object pay(HashMap<String, Object> map) {
        Method currentMethod = ReflectionUtils.getCurrentMethod(HashMap.class);
        Object result = null;
        try {
            result = apiInvokeBuilder.buildDubboAPI(currentMethod).invoke(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
