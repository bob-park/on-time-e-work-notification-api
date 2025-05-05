package org.bobpark.domain.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.bobpark.domain.user.model.UserResponse;

@FeignClient(name = "auth-user-api", contextId = "auth-user-api")
public interface UserFeignClient {

    @GetMapping(path = "api/v1/users/{uniqueId}")
    UserResponse getUser(@PathVariable String uniqueId);
}
