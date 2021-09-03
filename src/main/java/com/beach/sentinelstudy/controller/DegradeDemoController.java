package com.beach.sentinelstudy.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.beach.sentinelstudy.controller.exception.BlockExceptionHandler;
import com.beach.sentinelstudy.controller.response.CommonResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("degrade")
public class DegradeDemoController {

    @PostConstruct
    public void initSentinelRule() {
        List<DegradeRule> degradeRules = new ArrayList<>();

        // 给定时间段内异常数
        DegradeRule exceptionCountRule = new DegradeRule();
        exceptionCountRule.setResource("testForExceptionCountDegrades");
        exceptionCountRule.setCount(5);
        exceptionCountRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
        exceptionCountRule.setTimeWindow(60);
        exceptionCountRule.setStatIntervalMs(5000);

        // 超时熔断
        DegradeRule timeoutRule = new DegradeRule();
        timeoutRule.setResource("testForResponseTimeOutDegrades");
        timeoutRule.setStatIntervalMs(60000);
        timeoutRule.setMinRequestAmount(2);
        timeoutRule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        timeoutRule.setCount(3000);
        timeoutRule.setTimeWindow(60);

        degradeRules.add(exceptionCountRule);
        degradeRules.add(timeoutRule);
        DegradeRuleManager.loadRules(degradeRules);
    }

    @GetMapping("{id}")
    @SentinelResource(
            value = "testForExceptionCountDegrades",
            blockHandlerClass = BlockExceptionHandler.class,
            blockHandler = "errorHandlerForExceptionCount"
    )
    public CommonResponse testForExceptionCountDegrades(@PathVariable final int id) throws Exception {
        if (id == 1) {
            throw new Exception("test for sentinel");
        }
        List<String> users = List.of("JYZ", "ZYM");
        return new CommonResponse(users, 200);
    }

    @GetMapping
    @SentinelResource(
            value = "testForResponseTimeOutDegrades",
            blockHandlerClass = BlockExceptionHandler.class,
            blockHandler = "errorHandlerForResponseTime"
    )
    public CommonResponse testForResponseTimeOutDegrades() throws InterruptedException {
        Thread.sleep(5000);
        List<String> users = List.of("JYZ", "ZYM");
        return new CommonResponse(users, 200);
    }
}
