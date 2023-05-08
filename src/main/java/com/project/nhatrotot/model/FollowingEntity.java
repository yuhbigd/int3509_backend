package com.project.nhatrotot.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;



@Entity
@Table(name = "followings")
public class FollowingEntity {
    @EmbeddedId
    private FollowingKey id;
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;
    @ManyToOne
    @MapsId("following_id")
    @JoinColumn(name = "following_id", referencedColumnName = "user_id")
    private UserEntity following;
    public FollowingKey getId() {
        return id;
    }
    public UserEntity getUser() {
        return user;
    }
    public UserEntity getFollowing() {
        return following;
    }
    public void setId(FollowingKey id) {
        this.id = id;
    }
    public void setUser(UserEntity user) {
        this.user = user;
    }
    public void setFollowing(UserEntity following) {
        this.following = following;
    }
}
