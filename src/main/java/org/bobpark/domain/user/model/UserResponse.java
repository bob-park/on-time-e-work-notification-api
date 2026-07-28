package org.bobpark.domain.user.model;

import java.util.List;

public record UserResponse(String id,
                           String userId,
                           String username,
                           List<UserGroupResponse> groups,
                           PositionResponse position) {
}
