package com.example.map.controller;

import com.example.map.dto.GroupCreateRequest;
import com.example.map.dto.GroupResponse;
import com.example.map.service.GroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GroupControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createGroup_returnsCreatedGroup() throws Exception {
        GroupResponse response = new GroupResponse(10L, "Work Lunch", 3, 1L);
        when(groupService.createGroup(eq(1L), any(GroupCreateRequest.class))).thenReturn(response);

        GroupCreateRequest request = new GroupCreateRequest("Work Lunch", List.of(2L, 3L));

        mockMvc.perform(post("/api/map/groups")
                        .header("X-USER-ID", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("그룹이 생성되었습니다"))
                .andExpect(jsonPath("$.data.groupId").value(10))
                .andExpect(jsonPath("$.data.name").value("Work Lunch"))
                .andExpect(jsonPath("$.data.memberCount").value(3))
                .andExpect(jsonPath("$.data.ownerId").value(1));
    }

    @Test
    void getMyGroups_returnsGroupList() throws Exception {
        List<GroupResponse> responses = List.of(
                new GroupResponse(10L, "Work Lunch", 3, 1L),
                new GroupResponse(20L, "Reunion", 5, 1L)
        );
        when(groupService.getMyGroups(1L)).thenReturn(responses);

        mockMvc.perform(get("/api/map/groups")
                        .header("X-USER-ID", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("내 그룹 목록 조회 성공"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].groupId").value(10))
                .andExpect(jsonPath("$.data[0].name").value("Work Lunch"))
                .andExpect(jsonPath("$.data[1].groupId").value(20))
                .andExpect(jsonPath("$.data[1].name").value("Reunion"));
    }

    @Test
    void getMyGroups_returnsEmptyList() throws Exception {
        when(groupService.getMyGroups(99L)).thenReturn(List.of());

        mockMvc.perform(get("/api/map/groups")
                        .header("X-USER-ID", 99L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }
}
