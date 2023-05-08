package com.project.nhatrotot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class FollowingKey implements Serializable {
    @Column(name = "user_id")
    private String userId;

    @Column(name = "following_id")
    private String followingId;

    public FollowingKey() {
    }

    public FollowingKey(String userId, String followingId) {
        this.userId = userId;
        this.followingId = followingId;
    }
}
