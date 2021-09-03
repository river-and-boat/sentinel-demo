package com.beach.sentinelstudy.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.beach.sentinelstudy.controller.exception.BlockExceptionHandler;
import com.beach.sentinelstudy.controller.response.CommonResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("flow")
public class FlowDemoController {

    @PostConstruct
    public void initFlowRules() {
        List<FlowRule> flowRules = new ArrayList<>();
        // define refuse direct rule
        FlowRule refuseDirectRule = new FlowRule();
        refuseDirectRule.setResource("testForRefuseDirect");
        refuseDirectRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        refuseDirectRule.setCount(2);

        // define refuse warm up
        FlowRule refuseWarmupRule = new FlowRule();
        refuseWarmupRule.setResource("testForRefuseWarmup");
        refuseWarmupRule.setCount(20);
        refuseWarmupRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        refuseWarmupRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP);
        refuseWarmupRule.setWarmUpPeriodSec(10);

        // define refuse queue
        FlowRule refuseQueueRule = new FlowRule();
        refuseQueueRule.setResource("testForRefuseQueue");
        refuseQueueRule.setCount(10);
        refuseQueueRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        refuseQueueRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
        // wait time
        refuseQueueRule.setMaxQueueingTimeMs(20 * 1000);

        // define refuse reference
        FlowRule refuseReferenceRule = new FlowRule("testForReferenceSecondaryResource")
                .setCount(1)
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                .setStrategy(RuleConstant.STRATEGY_RELATE)
                .setRefResource("testForReferencePrimaryResource");

        flowRules.add(refuseDirectRule);
        flowRules.add(refuseWarmupRule);
        flowRules.add(refuseQueueRule);
        flowRules.add(refuseReferenceRule);

        FlowRuleManager.loadRules(flowRules);
    }

    @SentinelResource(
            value = "testForRefuseDirect",
            blockHandlerClass = BlockExceptionHandler.class,
            blockHandler = "errorHandlerForFlowRefuse"
    )
    @GetMapping("refuse")
    public CommonResponse testForRefuseDirect() {
        List<String> users = List.of("JYZ", "ZYM");
        return new CommonResponse(users, 200);
    }

    @SentinelResource(
            value = "testForRefuseWarmup",
            blockHandlerClass = BlockExceptionHandler.class,
            blockHandler = "errorHandlerForFlowRefuse"
    )
    @GetMapping("warm-up")
    public CommonResponse testForRefuseWarmup() {
        List<String> users = List.of("JYZ", "ZYM");
        return new CommonResponse(users, 200);
    }

    @SentinelResource(
            value = "testForRefuseQueue",
            blockHandlerClass = BlockExceptionHandler.class,
            blockHandler = "errorHandlerForFlowRefuse"
    )
    @GetMapping("queue")
    public CommonResponse testForQueueMode() {
        List<String> users = List.of("JYZ", "ZYM");
        return new CommonResponse(users, 200);
    }

    @SentinelResource(
            value = "testForReferencePrimaryResource",
            blockHandlerClass = BlockExceptionHandler.class,
            blockHandler = "errorHandlerForFlowRefuse"
    )
    @GetMapping("reference/primary")
    public CommonResponse testForReferenceModePrimaryResource() {
        String result = "I am the primary resource";
        return new CommonResponse(result, 200);
    }

    @SentinelResource(
            value = "testForReferenceSecondaryResource",
            blockHandlerClass = BlockExceptionHandler.class,
            blockHandler = "errorHandlerForFlowRefuse"
    )
    @GetMapping("reference/secondary")
    public CommonResponse testForReferenceModeSecondaryResource() {
        String result = "I am the secondary resource";
        return new CommonResponse(result, 200);
    }
}
