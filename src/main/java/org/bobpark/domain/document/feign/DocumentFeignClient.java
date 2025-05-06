package org.bobpark.domain.document.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.bobpark.domain.document.model.VacationDocumentResponse;

@FeignClient(name = "on-time-e-work-api", contextId = "on-time-e-work-api-document")
public interface DocumentFeignClient {

    /*
     * vacation
     */
    @GetMapping(path = "api/v1/documents/vacations/{id:\\d+}")
    VacationDocumentResponse getVacationDocument(@PathVariable Long id);
}
