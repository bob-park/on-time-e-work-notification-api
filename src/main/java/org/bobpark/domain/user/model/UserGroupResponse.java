package org.bobpark.domain.user.model;

public record UserGroupResponse(String id,
                                boolean isLeader,
                                String description,
                                TeamResponse group) {
}
