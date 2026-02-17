package com.example.user.repository;

import com.example.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByKakaoId_존재하는_회원_조회() {
        User user = User.builder()
                .kakaoId(12345L)
                .nickname("테스터")
                .email("test@kakao.com")
                .profileImageUrl("http://img.kakao.com/profile.jpg")
                .build();
        userRepository.save(user);

        Optional<User> found = userRepository.findByKakaoId(12345L);

        assertThat(found).isPresent();
        assertThat(found.get().getNickname()).isEqualTo("테스터");
        assertThat(found.get().getUuid()).isNotNull();
    }

    @Test
    void findByKakaoId_존재하지_않는_회원_조회() {
        Optional<User> found = userRepository.findByKakaoId(99999L);

        assertThat(found).isEmpty();
    }

    @Test
    void save_신규_회원_저장() {
        User user = User.builder()
                .kakaoId(67890L)
                .nickname("신규유저")
                .email("new@kakao.com")
                .build();

        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUuid()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void save_기존_회원_정보_업데이트() {
        User user = User.builder()
                .kakaoId(11111L)
                .nickname("원래닉네임")
                .email("old@kakao.com")
                .build();
        User saved = userRepository.save(user);

        saved.updateProfile("새닉네임", "new@kakao.com", "http://new-img.jpg");
        userRepository.flush();

        User updated = userRepository.findByKakaoId(11111L).orElseThrow();
        assertThat(updated.getNickname()).isEqualTo("새닉네임");
        assertThat(updated.getEmail()).isEqualTo("new@kakao.com");
        assertThat(updated.getProfileImageUrl()).isEqualTo("http://new-img.jpg");
    }
}
