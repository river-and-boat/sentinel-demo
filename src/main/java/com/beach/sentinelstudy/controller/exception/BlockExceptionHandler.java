package com.beach.sentinelstudy.controller.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.beach.sentinelstudy.controller.response.CommonResponse;

public class BlockExceptionHandler {
    public static CommonResponse errorHandlerForExceptionCount(final int id, BlockException ex) {
        return new CommonResponse(
                "error occurred and service stop, please wait 60s", 400);
    }

    public static CommonResponse errorHandlerForResponseTime(BlockException ex) {
        return new CommonResponse(
                "response time override limit and service stop, please wait 60s", 400);
    }
}
