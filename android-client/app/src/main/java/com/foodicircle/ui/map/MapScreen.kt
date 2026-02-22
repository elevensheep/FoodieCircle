package com.foodicircle.ui.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodicircle.ui.theme.YellowMain

@Composable
fun MapScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar
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
        
        // Search & Filter Bar (Overlay or Top)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Search */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Place, contentDescription = null, tint = YellowMain)
                Spacer(modifier = Modifier.width(4.dp))
                Text("서울 맛집 지도", color = Color.Black)
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(
                onClick = { /* Filter */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                Text("필터", color = Color.Black)
            }
        }

        // Map Area (Canvas to simulate Grid Map from design)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFF0F8FF)) // Light Blue-ish background
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val step = 50.dp.toPx()
                val width = size.width
                val height = size.height

                // Draw Grid
                for (x in 0..width.toInt() step step.toInt()) {
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        start = Offset(x.toFloat(), 0f),
                        end = Offset(x.toFloat(), height),
                        strokeWidth = 1f
                    )
                }
                for (y in 0..height.toInt() step step.toInt()) {
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        start = Offset(0f, y.toFloat()),
                        end = Offset(width, y.toFloat()),
                        strokeWidth = 1f
                    )
                }

                // Draw Stars (Mock Restaurants)
                // In real app, these coordinates come from API
                drawCircle(Color(0xFFFFD400), radius = 20.dp.toPx(), center = Offset(width * 0.3f, height * 0.4f))
                drawCircle(Color(0xFFFFD400), radius = 20.dp.toPx(), center = Offset(width * 0.7f, height * 0.3f))
                drawCircle(Color(0xFFFFD400), radius = 20.dp.toPx(), center = Offset(width * 0.5f, height * 0.6f))
                
                // Add star icon logic here if using specialized painters, 
                // but circles represent the design's star backdrops nicely for now.
            }
        }
        
        // Bottom Sheet / List Preview (Simplified)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "맛집 목록 (6)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = YellowMain
                )
                // Divider
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                // Single Item Preview
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(60.dp).background(Color.Gray, RoundedCornerShape(8.dp)))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("청담 스시 이로", fontWeight = FontWeight.Bold)
                        Text("일식", fontSize = 12.sp, color = Color.Gray)
                        Text("2명이 추천", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("★ 4.8", color = YellowMain)
                }
            }
        }
    }
}
