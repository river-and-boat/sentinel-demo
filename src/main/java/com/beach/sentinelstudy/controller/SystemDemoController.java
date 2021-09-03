package com.beach.sentinelstudy.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.beach.sentinelstudy.controller.exception.BlockExceptionHandler;
import com.beach.sentinelstudy.controller.response.CommonResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system")
public class SystemDemoController {
    @PostConstruct
    public void initSystemRules() {
        List<SystemRule> rules = new ArrayList<>();
        SystemRule rule = new SystemRule();
        rule.setHighestCpuUsage(1);
        rule.setResource("testForSystemResource");
        rules.add(rule);
        SystemRuleManager.loadRules(rules);
    }

    @SentinelResource(
            value = "testForSystemResource",
            blockHandlerClass = BlockExceptionHandler.class,
            blockHandler = "errorHandlerForFlowRefuse"
    )
    @GetMapping
    public CommonResponse testForSystemRule() {
        List<String> users = List.of("JYZ", "ZYM");
        return new CommonResponse(users, 200);
    }
}
