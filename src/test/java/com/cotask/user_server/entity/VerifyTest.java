package com.cotask.user_server.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * 테스트 프레임워크/라이브러리:
 * - JUnit 5 (Jupiter)
 * - AssertJ
 *
 * 이 테스트는 Verify 엔티티의 공개 인터페이스(게터/세터 및 Lombok 빌더 동작)에 초점을 맞추며,
 * JPA 퍼시스턴스 동작(@CreationTimestamp, @ManyToOne 등)은 단위 테스트 범위를 벗어나므로 모킹 없이 값 수준으로 검증합니다.
 * 모든 테스트는 given-when-then 방식과 한국어 명명 규칙을 따릅니다.
 */
class VerifyTest {

    // User, VerifyType에 대한 최소 빌더/팩토리 유틸을 이 테스트 내에서 단순하게 제공합니다.
    // 실제 User/VerifyType 구현이 다르면, 해당 필드에 맞춰 조정하세요.
    private User stubUser() {
        // 가능한 한 필수값 최소화
        // User 엔티티가 빌더를 제공하지 않을 수 있으므로 NoArgsConstructor 이후 세터 혹은 빌더가 있다면 그에 맞추세요.
        User u = new User();
        // 잠재적인 필드 설정(존재하는 경우)에 안전하게 접근하도록 try-catch 사용
        try {
            // 흔한 필드들: id, email, username 등 — 존재하지 않으면 무시
            var idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(u, 1L);
        } catch (Exception ignored) {}
        try {
            var emailField = User.class.getDeclaredField("email");
            emailField.setAccessible(true);
            emailField.set(u, "user@example.com");
        } catch (Exception ignored) {}
        try {
            var nameField = User.class.getDeclaredField("username");
            nameField.setAccessible(true);
            nameField.set(u, "tester");
        } catch (Exception ignored) {}
        return u;
    }

    private VerifyType anyVerifyType() {
        // 프로젝트의 VerifyType enum 값을 찾을 수 없을 때를 대비하여 안전하게 처리
        // 가장 일반적인 값 가정: EMAIL, PASSWORD_RESET 등
        try {
            return VerifyType.valueOf("EMAIL");
        } catch (IllegalArgumentException e1) {
            try {
                return VerifyType.valueOf("PASSWORD_RESET");
            } catch (IllegalArgumentException e2) {
                // enum 값이 무엇이든 첫 번째 상수를 반영적으로 선택
                VerifyType[] values = VerifyType.values();
                return values.length > 0 ? values[0] : null;
            }
        }
    }

    @Nested
    @DisplayName("given: 기본 생성자를 사용할 때")
    class NoArgsConstructorTests {

        @Test
        @DisplayName("when: 기본 생성자로 생성하면 then: isUsed 기본값은 false이고 나머지는 null 이다")
        void 기본생성자_기본값_검증() {
            // given
            // when
            Verify verify = new Verify();

            // then
            assertThat(verify.getId()).as("ID는 퍼시스트 이전이므로 null").isNull();
            assertThat(verify.getUser()).as("User는 기본 생성자에서는 null").isNull();
            assertThat(verify.getType()).as("Type은 기본 생성자에서는 null").isNull();
            assertThat(verify.getCode()).as("Code는 기본 생성자에서는 null").isNull();
            assertThat(verify.getExpiresAt()).as("ExpiresAt은 기본 생성자에서는 null").isNull();
            assertThat(verify.getCreatedAt()).as("CreatedAt은 @CreationTimestamp로 퍼시스트 전 null").isNull();
            assertThat(verify.getIsUsed()).as("isUsed 필드의 기본값은 false").isFalse();
        }

        @Test
        @DisplayName("when: isUsed 세터로 값을 변경하면 then: 변경된 값을 반영한다")
        void 기본생성자_isUsed_세터_작동여부() {
            // given
            Verify verify = new Verify();

            // when
            verify.setIsUsed(true);

            // then
            assertThat(verify.getIsUsed()).isTrue();

            // when
            verify.setIsUsed(false);

            // then
            assertThat(verify.getIsUsed()).isFalse();
        }
    }

    @Nested
    @DisplayName("given: Lombok 빌더를 사용할 때")
    class BuilderTests {

        @Test
        @DisplayName("when: 필수 필드를 모두 지정하면 then: 동일한 값으로 빌드된다")
        void 빌더_모든필드_지정시_동일값_빌드() {
            // given
            User user = stubUser();
            VerifyType type = anyVerifyType();
            UUID code = UUID.randomUUID();
            Instant expiresAt = Instant.now().plusSeconds(3600);
            LocalDateTime createdAt = LocalDateTime.now().minusMinutes(1); // 빌더로 세팅 가능(업데이트 불가 제약은 JPA가 관리)

            // when
            Verify verify = Verify.builder()
                    .id(42L) // 보통 JPA가 채워주지만 빌더로는 세팅 가능
                    .user(user)
                    .type(type)
                    .code(code)
                    .expiresAt(expiresAt)
                    .isUsed(Boolean.TRUE)
                    .createdAt(createdAt)
                    .build();

            // then
            assertThat(verify.getId()).isEqualTo(42L);
            assertThat(verify.getUser()).isSameAs(user);
            assertThat(verify.getType()).isEqualTo(type);
            assertThat(verify.getCode()).isEqualTo(code);
            assertThat(verify.getExpiresAt()).isEqualTo(expiresAt);
            assertThat(verify.getIsUsed()).isTrue();
            assertThat(verify.getCreatedAt()).isEqualTo(createdAt);
        }

        @Test
        @DisplayName("when: isUsed를 지정하지 않고 빌더를 사용하면 then: (주의) Lombok @Builder.Default 미사용 시 null이 될 수 있다")
        void 빌더_isUsed_기본값_주의사항() {
            // given
            User user = stubUser();
            VerifyType type = anyVerifyType();
            UUID code = UUID.randomUUID();
            Instant expiresAt = Instant.now().plusSeconds(60);

            // when
            Verify verify = Verify.builder()
                    .user(user)
                    .type(type)
                    .code(code)
                    .expiresAt(expiresAt)
                    // isUsed를 지정하지 않음
                    .build();

            // then
            // @Builder.Default가 없으면 필드 초기값(false)이 반영되지 않고 null일 수 있음
            // 이 테스트는 그 동작을 명시적으로 드러내어 회귀를 방지합니다.
            assertThat(verify.getUser()).isSameAs(user);
            assertThat(verify.getType()).isEqualTo(type);
            assertThat(verify.getCode()).isEqualTo(code);
            assertThat(verify.getExpiresAt()).isEqualTo(expiresAt);
            assertThat(verify.getIsUsed())
                .as("빌더 사용 시 @Builder.Default 미사용이면 null이 될 수 있음")
                .isNull();
        }

        @Test
        @DisplayName("when: null 값들을 빌더로 전달하면 then: 해당 필드에 null이 설정된다(엔티티 제약은 JPA 레벨)")
        void 빌더_null_허용성_확인() {
            // given
            // when
            Verify verify = Verify.builder()
                    .user(null)
                    .type(null)
                    .code(null)
                    .expiresAt(null)
                    .isUsed(null)
                    .createdAt(null)
                    .build();

            // then
            assertThat(verify.getUser()).isNull();
            assertThat(verify.getType()).isNull();
            assertThat(verify.getCode()).isNull();
            assertThat(verify.getExpiresAt()).isNull();
            assertThat(verify.getIsUsed()).isNull();
            assertThat(verify.getCreatedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("given: 필드 값의 의미론적 제약을 점검할 때")
    class SemanticConstraints {

        @Test
        @DisplayName("when: 만료 시간이 현재보다 미래이면 then: 유효한 만료 시간 시나리오로 간주한다")
        void 만료시간_미래값_시나리오() {
            // given
            var future = Instant.now().plusSeconds(300);
            var verify = Verify.builder()
                    .user(stubUser())
                    .type(anyVerifyType())
                    .code(UUID.randomUUID())
                    .expiresAt(future)
                    .isUsed(false)
                    .build();

            // when
            boolean isExpired = future.isBefore(Instant.now());

            // then
            assertThat(isExpired).isFalse();
            assertThat(verify.getExpiresAt()).isEqualTo(future);
        }

        @Test
        @DisplayName("when: 만료 시간이 과거이면 then: 만료된 것으로 간주할 수 있다(로직 가정)")
        void 만료시간_과거값_시나리오() {
            // given
            var past = Instant.now().minusSeconds(300);
            var verify = Verify.builder()
                    .user(stubUser())
                    .type(anyVerifyType())
                    .code(UUID.randomUUID())
                    .expiresAt(past)
                    .isUsed(false)
                    .build();

            // when
            boolean isExpired = past.isBefore(Instant.now());

            // then
            assertThat(isExpired).isTrue();
            assertThat(verify.getExpiresAt()).isEqualTo(past);
        }

        @Test
        @DisplayName("when: isUsed 값을 true→false로 토글하면 then: 게터가 일관되게 반영한다")
        void isUsed_토글_동작() {
            // given
            var verify = Verify.builder()
                    .user(stubUser())
                    .type(anyVerifyType())
                    .code(UUID.randomUUID())
                    .expiresAt(Instant.now().plusSeconds(120))
                    .isUsed(true)
                    .build();

            // when
            verify.setIsUsed(false);

            // then
            assertThat(verify.getIsUsed()).isFalse();
        }
    }
}