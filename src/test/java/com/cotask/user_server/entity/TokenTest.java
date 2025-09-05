package com.cotask.user_server.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * 테스트 프레임워크: JUnit 5 (Jupiter)
 * - 이 테스트는 순수 엔티티(Token)에 대한 단위 테스트로, JPA 컨텍스트 없이 Lombok 빌더/게터/세터 동작을 검증합니다.
 * - createdAt은 @CreationTimestamp에 의해 영속 시점에 생성되므로, 비영속 상태에서는 null임을 검증합니다.
 * - 모든 테스트는 Given-When-Then 형식을 따르며, 테스트 이름은 한국어로 작성됩니다.
 */
class TokenTest {

    /**
     * Token과 연관된 User는 실제 프로젝트 User 엔티티를 사용해야 하지만,
     * 현재 단위 테스트에서는 간단한 스텁 객체로 대체합니다.
     * 필요 시 실제 User 엔티티로 교체하세요.
     */
    private static User stubUser(Long id) {
        // 최소한의 스텁 구현: 아이디만 보유한 얕은 사용자 객체
        // Lombok/필드 접근 제약이 있을 수 있으므로, 가능한 기본 생성 후 리플렉션 없이 생성 가능하게 가정
        return new User() {
            private final Long stubId = id;
            @Override
            public String toString() {
                return "User(stubId=" + stubId + ")";
            }
        };
    }

    @Nested
    @DisplayName("빌더 생성 및 접근자 검증")
    class BuilderAndAccessors {

        @Test
        @DisplayName("Given 유효한 사용자와 토큰 및 만료일 When 빌더로 생성 Then 모든 필드가 기대대로 설정된다")
        void givenValidUserTokenExpiry_whenBuild_thenFieldsAreSet() {
            // Given
            User user = stubUser(1L);
            String refreshToken = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef"; // 64 hex
            Instant expiredDate = Instant.now().plusSeconds(3600);

            // When
            Token token = Token.builder()
                .user(user)
                .refreshToken(refreshToken)
                .expiredDate(expiredDate)
                .build();

            // Then
            assertNull(token.getId(), "ID는 영속 전이므로 null 이어야 합니다.");
            assertSame(user, token.getUser(), "User 연관관계가 동일 객체로 설정되어야 합니다.");
            assertEquals(refreshToken, token.getRefreshToken(), "refreshToken 값이 일치해야 합니다.");
            assertEquals(expiredDate, token.getExpiredDate(), "expiredDate 값이 일치해야 합니다.");
            assertNull(token.getCreatedAt(), "@CreationTimestamp는 영속 시점에만 설정되므로 현재는 null이어야 합니다.");
        }

        @Test
        @DisplayName("Given refreshToken과 expiredDate가 누락된 상태 When 빌더로 생성 Then 널 허용 필드만 널이어야 한다(세터로 수정 가능)")
        void givenMissingOptional_whenBuild_thenNullableFieldsAndSetters() {
            // Given
            User user = stubUser(2L);

            // When
            Token token = Token.builder()
                .user(user)
                .build();

            // Then
            assertSame(user, token.getUser(), "User는 필수이므로 설정되어야 합니다.");
            assertNull(token.getRefreshToken(), "refreshToken이 설정되지 않았으므로 null이어야 합니다.");
            assertNull(token.getExpiredDate(), "expiredDate가 설정되지 않았으므로 null이어야 합니다.");

            // And (When-Then) 세터로 값 설정 가능
            String newToken = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
            Instant newExpiry = Instant.now().plusSeconds(7200);
            token.setRefreshToken(newToken);
            token.setExpiredDate(newExpiry);

            assertEquals(newToken, token.getRefreshToken(), "세터로 설정한 refreshToken이 반영되어야 합니다.");
            assertEquals(newExpiry, token.getExpiredDate(), "세터로 설정한 expiredDate가 반영되어야 합니다.");
        }
    }

    @Nested
    @DisplayName("세터 동작 및 불변성 제약 검증")
    class SettersAndConstraints {

        @Test
        @DisplayName("Given 기존 값이 있는 토큰 When refreshToken을 변경 Then 새 값으로 반영된다")
        void givenExistingToken_whenChangeRefreshToken_thenUpdated() {
            // Given
            Token token = Token.builder()
                .user(stubUser(3L))
                .refreshToken("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb")
                .expiredDate(Instant.now().plusSeconds(1000))
                .build();

            String updated = "cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc";

            // When
            token.setRefreshToken(updated);

            // Then
            assertEquals(updated, token.getRefreshToken(), "refreshToken 세터가 새 값을 반영해야 합니다.");
        }

        @Test
        @DisplayName("Given 기존 값이 있는 토큰 When expiredDate를 변경 Then 새 만료일로 반영된다")
        void givenExistingToken_whenChangeExpiredDate_thenUpdated() {
            // Given
            Instant initial = Instant.now().plusSeconds(1000);
            Token token = Token.builder()
                .user(stubUser(4L))
                .refreshToken("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd")
                .expiredDate(initial)
                .build();
            Instant updated = initial.plusSeconds(500);

            // When
            token.setExpiredDate(updated);

            // Then
            assertEquals(updated, token.getExpiredDate(), "expiredDate 세터가 새 값을 반영해야 합니다.");
        }

        @Test
        @DisplayName("Given 비영속 토큰 When createdAt 접근 Then null(영속 시에만 생성)이다")
        void givenNonPersistedToken_whenGetCreatedAt_thenNull() {
            // Given
            Token token = Token.builder()
                .user(stubUser(5L))
                .refreshToken("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")
                .expiredDate(Instant.now().plusSeconds(2000))
                .build();

            // When
            LocalDateTime createdAt = token.getCreatedAt();

            // Then
            assertNull(createdAt, "@CreationTimestamp는 영속화 시점 기준으로 설정되므로 비영속에선 null이어야 합니다.");
        }
    }

    @Nested
    @DisplayName("엣지 케이스 검증")
    class EdgeCases {

        @Test
        @DisplayName("Given refreshToken이 null When 빌드 Then Null 허용 필드로서 null 저장을 허용한다(컬럼 제약은 영속 단계에서 유효)")
        void givenNullRefreshToken_whenBuild_thenAllowedAtObjectLevel() {
            // Given
            User user = stubUser(6L);
            Instant expiry = Instant.now().plusSeconds(300);

            // When
            Token token = Token.builder()
                .user(user)
                .refreshToken(null)
                .expiredDate(expiry)
                .build();

            // Then
            assertNull(token.getRefreshToken(), "객체 수준에서는 null 허용(영속 시 DB 제약으로 실패 가능).");
            assertEquals(expiry, token.getExpiredDate());
        }

        @Test
        @DisplayName("Given expiredDate가 과거 When 설정 Then 값은 그대로 보관되며 비즈니스 검증은 상위 계층에서 처리되어야 한다")
        void givenPastExpiry_whenSet_thenStoredAsIs() {
            // Given
            Token token = Token.builder()
                .user(stubUser(7L))
                .build();
            Instant past = Instant.now().minusSeconds(10);

            // When
            token.setExpiredDate(past);

            // Then
            assertEquals(past, token.getExpiredDate(), "엔티티는 값을 그대로 보관하고 검증은 서비스/도메인 규칙에서 처리.");
        }

        @Test
        @DisplayName("Given 64자 미만의 refreshToken When 설정 Then 객체는 보관하지만 DB 저장 시 제약 위반 가능(여기서는 단위레벨 보관만 검증)")
        void givenShortRefreshToken_whenSet_thenObjectStoresButDbMayReject() {
            // Given
            Token token = Token.builder()
                .user(stubUser(8L))
                .build();
            String shortToken = "short";

            // When
            token.setRefreshToken(shortToken);

            // Then
            assertEquals(shortToken, token.getRefreshToken(),
                "엔티티 객체 수준에서는 길이 제약을 강제하지 않음(스키마/검증 계층에서 처리).");
        }
    }
}