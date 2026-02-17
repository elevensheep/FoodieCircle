package com.example.map.service;

import com.example.map.dto.GroupCreateRequest;
import com.example.map.dto.GroupResponse;
import com.example.map.entity.CircleGroup;
import com.example.map.entity.GroupMember;
import com.example.map.repository.GroupMemberRepository;
import com.example.map.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @InjectMocks
    private GroupService groupService;

    private Long ownerId;
    private GroupCreateRequest request;

    @BeforeEach
    void setUp() {
        ownerId = 1L;
        request = new GroupCreateRequest("Work Lunch", List.of(2L, 3L));
    }

    @Test
    void createGroup_savesGroupWithCorrectFields() {
        CircleGroup savedGroup = new CircleGroup();
        savedGroup.setId(10L);
        savedGroup.setName("Work Lunch");
        savedGroup.setOwnerId(1L);
        when(groupRepository.save(any(CircleGroup.class))).thenReturn(savedGroup);
        when(groupMemberRepository.countByGroup(savedGroup)).thenReturn(3);

        GroupResponse response = groupService.createGroup(ownerId, request);

        ArgumentCaptor<CircleGroup> captor = ArgumentCaptor.forClass(CircleGroup.class);
        verify(groupRepository).save(captor.capture());
        CircleGroup captured = captor.getValue();
        assertThat(captured.getName()).isEqualTo("Work Lunch");
        assertThat(captured.getOwnerId()).isEqualTo(1L);
    }

    @Test
    void createGroup_addsOwnerAsMember() {
        CircleGroup savedGroup = new CircleGroup();
        savedGroup.setId(10L);
        savedGroup.setName("Work Lunch");
        savedGroup.setOwnerId(1L);
        when(groupRepository.save(any(CircleGroup.class))).thenReturn(savedGroup);
        when(groupMemberRepository.countByGroup(savedGroup)).thenReturn(3);

        groupService.createGroup(ownerId, request);

        ArgumentCaptor<List<GroupMember>> captor = ArgumentCaptor.forClass(List.class);
        verify(groupMemberRepository).saveAll(captor.capture());
        List<GroupMember> members = captor.getValue();

        List<Long> userIds = members.stream().map(GroupMember::getUserId).toList();
        assertThat(userIds).contains(1L);
    }

    @Test
    void createGroup_addsAllRequestedMembers() {
        CircleGroup savedGroup = new CircleGroup();
        savedGroup.setId(10L);
        savedGroup.setName("Work Lunch");
        savedGroup.setOwnerId(1L);
        when(groupRepository.save(any(CircleGroup.class))).thenReturn(savedGroup);
        when(groupMemberRepository.countByGroup(savedGroup)).thenReturn(3);

        groupService.createGroup(ownerId, request);

        ArgumentCaptor<List<GroupMember>> captor = ArgumentCaptor.forClass(List.class);
        verify(groupMemberRepository).saveAll(captor.capture());
        List<GroupMember> members = captor.getValue();

        List<Long> userIds = members.stream().map(GroupMember::getUserId).toList();
        assertThat(userIds).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    void createGroup_returnsCorrectResponse() {
        CircleGroup savedGroup = new CircleGroup();
        savedGroup.setId(10L);
        savedGroup.setName("Work Lunch");
        savedGroup.setOwnerId(1L);
        when(groupRepository.save(any(CircleGroup.class))).thenReturn(savedGroup);
        when(groupMemberRepository.countByGroup(savedGroup)).thenReturn(3);

        GroupResponse response = groupService.createGroup(ownerId, request);

        assertThat(response.getGroupId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("Work Lunch");
        assertThat(response.getMemberCount()).isEqualTo(3);
        assertThat(response.getOwnerId()).isEqualTo(1L);
    }

    @Test
    void getMyGroups_returnsGroupsWhereUserIsMember() {
        CircleGroup group1 = new CircleGroup();
        group1.setId(10L);
        group1.setName("Work Lunch");
        group1.setOwnerId(1L);

        CircleGroup group2 = new CircleGroup();
        group2.setId(20L);
        group2.setName("Reunion");
        group2.setOwnerId(5L);

        GroupMember member1 = new GroupMember();
        member1.setGroup(group1);
        member1.setUserId(1L);

        GroupMember member2 = new GroupMember();
        member2.setGroup(group2);
        member2.setUserId(1L);

        when(groupMemberRepository.findByUserId(1L)).thenReturn(List.of(member1, member2));
        when(groupMemberRepository.countByGroup(group1)).thenReturn(3);
        when(groupMemberRepository.countByGroup(group2)).thenReturn(5);

        List<GroupResponse> responses = groupService.getMyGroups(1L);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getGroupId()).isEqualTo(10L);
        assertThat(responses.get(0).getName()).isEqualTo("Work Lunch");
        assertThat(responses.get(0).getMemberCount()).isEqualTo(3);
        assertThat(responses.get(1).getGroupId()).isEqualTo(20L);
        assertThat(responses.get(1).getName()).isEqualTo("Reunion");
        assertThat(responses.get(1).getMemberCount()).isEqualTo(5);
    }

    @Test
    void getMyGroups_returnsEmptyListWhenNoGroups() {
        when(groupMemberRepository.findByUserId(99L)).thenReturn(List.of());

        List<GroupResponse> responses = groupService.getMyGroups(99L);

        assertThat(responses).isEmpty();
    }
}
