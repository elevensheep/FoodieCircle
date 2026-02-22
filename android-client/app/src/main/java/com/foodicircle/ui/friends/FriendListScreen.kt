package com.foodicircle.ui.friends

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodicircle.data.model.Friend
import com.foodicircle.ui.theme.YellowMain

@Composable
fun FriendListScreen() {
    val friends = listOf(
        Friend(1, "김민지", "", 12, "청담 스시 이로"),
        Friend(2, "박준호", "", 8, "이태원 멕시칸 레스토랑"),
        Friend(3, "이서연", "", 15, "성수동 브런치 카페"),
        Friend(4, "최도현", "", 6, "강남 파스타 맛집"),
        Friend(5, "정수빈", "", 20, "홍대 떡볶이집"),
        Friend(6, "윤재훈", "", 11, "역삼 일식 오마카세")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        // App Bar
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
        PaddingValues(16.dp).let { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Text(
                    text = "친구 목록",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${friends.size}명의 친구가 72개 맛집을 공유하고 있어요",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        // List
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(friends) { friend ->
                FriendItem(friend)
            }
        }
    }
}

@Composable
fun FriendItem(friend: Friend) {
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
            // Profile Image Placeholder
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                // Image()
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = friend.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = YellowMain,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = " ${friend.starCount}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Text(
                    text = "최근: ${friend.recentActivity}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}
