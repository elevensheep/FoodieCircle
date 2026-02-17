package com.example.map.service;

import com.example.map.dto.GroupCreateRequest;
import com.example.map.dto.GroupResponse;
import com.example.map.entity.CircleGroup;
import com.example.map.entity.GroupMember;
import com.example.map.repository.GroupMemberRepository;
import com.example.map.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public GroupResponse createGroup(Long ownerId, GroupCreateRequest request) {
        CircleGroup group = new CircleGroup();
        group.setName(request.getName());
        group.setOwnerId(ownerId);
        CircleGroup savedGroup = groupRepository.save(group);

        List<GroupMember> members = new ArrayList<>();

        GroupMember ownerMember = new GroupMember();
        ownerMember.setGroup(savedGroup);
        ownerMember.setUserId(ownerId);
        members.add(ownerMember);

        for (Long memberId : request.getMemberIds()) {
            if (!memberId.equals(ownerId)) {
                GroupMember member = new GroupMember();
                member.setGroup(savedGroup);
                member.setUserId(memberId);
                members.add(member);
            }
        }

        groupMemberRepository.saveAll(members);

        int memberCount = groupMemberRepository.countByGroup(savedGroup);
        return new GroupResponse(savedGroup.getId(), savedGroup.getName(), memberCount, savedGroup.getOwnerId());
    }

    @Transactional(readOnly = true)
    public List<GroupResponse> getMyGroups(Long userId) {
        List<GroupMember> memberships = groupMemberRepository.findByUserId(userId);

        return memberships.stream()
                .map(membership -> {
                    CircleGroup group = membership.getGroup();
                    int memberCount = groupMemberRepository.countByGroup(group);
                    return new GroupResponse(group.getId(), group.getName(), memberCount, group.getOwnerId());
                })
                .toList();
    }
}
