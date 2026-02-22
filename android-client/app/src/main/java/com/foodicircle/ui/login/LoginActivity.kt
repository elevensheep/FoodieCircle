package com.foodicircle.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foodicircle.ui.theme.YellowMain
import com.foodicircle.ui.theme.Black
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel()) {
    val state = viewModel.loginState.collectAsState().value
    val context = LocalContext.current

    // Observe state changes
    if (state is LoginState.Success) {
        // Navigate to Main Activity (Placeholder)
        Toast.makeText(context, "로그인 성공! 메인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
    } else if (state is LoginState.Error) {
        Toast.makeText(context, "로그인 실패: ${state.message}", Toast.LENGTH_LONG).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(YellowMain),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        
        Spacer(modifier = Modifier.weight(1f))

        // Logo / Title
        Text(
            text = "FoodieCircle",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Black
        )
        Text(
            text = "믿을 수 있는 친구들의 맛집",
            fontSize = 16.sp,
            color = Black,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Kakao Login Button
        // In a real app, use the actual Resource ID for the Kakao Symbol
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 50.dp)
                .height(50.dp)
                .background(Color(0xFFFEE500), shape = RoundedCornerShape(12.dp)) // Kakao Yellow
                .clickable { viewModel.onKakaoLoginClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Image(painter = painterResource(id = R.drawable.kakao_symbol), contentDescription = null) 
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "카카오로 시작하기",
                    color = Color(0xFF000000),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        LoginScreen()
    }
}
