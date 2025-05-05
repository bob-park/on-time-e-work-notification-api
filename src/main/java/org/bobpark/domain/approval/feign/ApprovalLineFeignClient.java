package org.bobpark.domain.approval.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.bobpark.domain.approval.model.ApprovalLineResponse;

@FeignClient(name = "on-time-e-work-api", contextId = "on-time-e-work-api-approval-line")
public interface ApprovalLineFeignClient {

    @GetMapping(path = "api/v1/approval/lines")
    List<ApprovalLineResponse> getApprovalLines();
}
