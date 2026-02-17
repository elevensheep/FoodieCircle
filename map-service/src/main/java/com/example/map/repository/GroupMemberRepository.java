package com.example.map.repository;

import com.example.map.entity.CircleGroup;
import com.example.map.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findByUserId(Long userId);

    int countByGroup(CircleGroup group);
}
