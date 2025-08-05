package org.bobpark.domain.user.model;

public record UserResponse(String id,
                           String userId,
                           String username,
                           TeamResponse group,
                           PositionResponse position) {
}
