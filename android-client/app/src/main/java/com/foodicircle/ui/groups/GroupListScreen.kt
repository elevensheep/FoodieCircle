package com.foodicircle.ui.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodicircle.data.model.CircleResponse
import com.foodicircle.ui.theme.YellowMain

// Ideally, this should be driven by a ViewModel. For simplicity in this step, I'll assume data is passed or placeholder.
// In the full integration, we'd inject the ViewModel.

@Composable
fun GroupListScreen(
    groups: List<CircleResponse> = emptyList() // To be populated by ViewModel
) {
    // Placeholder data if empty for visualization
    val displayGroups = if (groups.isEmpty()) listOf(
        CircleResponse(1, "대학 동기들", "졸업생 모임", 8, 24, "김철수"),
        CircleResponse(2, "회사 팀원들", "점심 맛집 공유", 12, 45, "박팀장"),
        CircleResponse(3, "가족", "우리 가족 맛집", 5, 18, "엄마"),
        CircleResponse(4, "맛집 탐방러들", "서울 맛집 정복", 15, 67, "이맛집")
    ) else groups

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        // App Bar Area (Reusing similar style or use Scaffold)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(YellowMain)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "FoodieCircle",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "믿을 수 있는 친구들의 맛집",
                    fontSize = 14.sp
                )
            }
        }

        // Header
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "그룹 (Circle)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${displayGroups.size}개의 Circle이 활동 중",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            FloatingActionButton(
                onClick = { /* Create Group */ },
                containerColor = YellowMain,
                contentColor = Color.Black,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }

        // List
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(displayGroups) { group ->
                GroupItem(group)
            }
        }
    }
}

@Composable
fun GroupItem(group: CircleResponse) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Placeholder
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFF4285F4), shape = RoundedCornerShape(8.dp)), // Blue-ish
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "👥 ${group.memberCount}명",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "📍 ${group.restaurantCount}곳",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}
