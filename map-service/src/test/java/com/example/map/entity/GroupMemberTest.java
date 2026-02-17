package com.example.map.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class GroupMemberTest {

    @Test
    void createGroupMember_setsFieldsCorrectly() {
        CircleGroup group = new CircleGroup();
        group.setName("Test Group");

        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUserId(42L);

        assertThat(member.getGroup()).isEqualTo(group);
        assertThat(member.getUserId()).isEqualTo(42L);
    }

    @Test
    void joinedAt_isSetAutomatically() {
        GroupMember member = new GroupMember();
        member.onCreate();

        assertThat(member.getJoinedAt()).isNotNull();
        assertThat(member.getJoinedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}
