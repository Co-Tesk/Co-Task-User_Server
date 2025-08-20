package com.cotask.user_server.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
// If AssertJ is available in the project, you can switch to:
// import static org.assertj.core.api.Assertions.assertThat;

class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    @Test
    @DisplayName("given_비밀번호가_null일_때_when_검증하면_then_false를_반환한다")
    void given_비밀번호가_null일_때_when_검증하면_then_false를_반환한다() {
        // Given
        String password = null;

        // When
        boolean result = validator.isValid(password, null);

        // Then
        assertFalse(result);
        // assertThat(result).isFalse();
    }

    @Test
    @DisplayName("given_비밀번호가_빈문자열일_때_when_검증하면_then_false를_반환한다")
    void given_비밀번호가_빈문자열일_때_when_검증하면_then_false를_반환한다() {
        // Given
        String password = "";

        // When
        boolean result = validator.isValid(password, null);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("given_길이가_8미만일_때_when_검증하면_then_false를_반환한다")
    void given_길이가_8미만일_때_when_검증하면_then_false를_반환한다() {
        // Given
        String password = "Aa1!aa"; // length 6

        // When
        boolean result = validator.isValid(password, null);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("given_대문자가_없을_때_when_검증하면_then_false를_반환한다")
    void given_대문자가_없을_때_when_검증하면_then_false를_반환한다() {
        // Given
        String password = "aa1!aaaa";

        // When
        boolean result = validator.isValid(password, null);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("given_소문자가_없을_때_when_검증하면_then_false를_반환한다")
    void given_소문자가_없을_때_when_검증하면_then_false를_반환한다() {
        // Given
        String password = "AA1!AAAA";

        // When
        boolean result = validator.isValid(password, null);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("given_숫자가_없을_때_when_검증하면_then_false를_반환한다")
    void given_숫자가_없을_때_when_검증하면_then_false를_반환한다() {
        // Given
        String password = "Aa!aaaaa";

        // When
        boolean result = validator.isValid(password, null);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("given_특수문자가_없을_때_when_검증하면_then_false를_반환한다")
    void given_특수문자가_없을_때_when_검증하면_then_false를_반환한다() {
        // Given
        String password = "Aa1aaaaa";

        // When
        boolean result = validator.isValid(password, null);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("given_모든_요건_충족_정확히8자_when_검증하면_then_true를_반환한다")
    void given_모든_요건_충족_정확히8자_when_검증하면_then_true를_반환한다() {
        // Given
        String password = "Aa1!aaab"; // length 8, includes upper, lower, digit, special

        // When
        boolean result = validator.isValid(password, null);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("given_모든_요건_충족_긴문자열_when_검증하면_then_true를_반환한다")
    void given_모든_요건_충족_긴문자열_when_검증하면_then_true를_반환한다() {
        // Given
        String password = "ThisIsA-Valid_Password#2025!";

        // When
        boolean result = validator.isValid(password, null);

        // Then
        assertTrue(result);
    }

    @Nested
    @DisplayName("특수문자_해석_행동_확인")
    class SpecialCharacterBehavior {

        @Test
        @DisplayName("given_공백문자를_포함할_때_when_검증하면_then_true를_반환한다")
        void given_공백문자를_포함할_때_when_검증하면_then_true를_반환한다() {
            // Given: 공백(space)은 정규식에서 [^a-zA-Z0-9]에 해당하여 특수문자로 처리됨
            String password = "Aa1 aaaa";

            // When
            boolean result = validator.isValid(password, null);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("given_탭문자를_포함할_때_when_검증하면_then_true를_반환한다")
        void given_탭문자를_포함할_때_when_검증하면_then_true를_반환한다() {
            // Given: 탭 역시 [^a-zA-Z0-9]로 간주됨
            String password = "Aa1\taaaa";

            // When
            boolean result = validator.isValid(password, null);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("given_언더스코어를_포함할_때_when_검증하면_then_true를_반환한다")
        void given_언더스코어를_포함할_때_when_검증하면_then_true를_반환한다() {
            // Given: '_'는 영숫자가 아니므로 특수문자로 간주됨
            String password = "Aa1_aaaa";

            // When
            boolean result = validator.isValid(password, null);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("given_비ASCII문자를_포함할_때_when_검증하면_then_true를_반환한다")
        void given_비ASCII문자를_포함할_때_when_검증하면_then_true를_반환한다() {
            // Given: '가'는 [^a-zA-Z0-9]에 매칭되어 특수문자 요건을 충족
            String password = "Aa1가aaaa";

            // When
            boolean result = validator.isValid(password, null);

            // Then
            assertTrue(result);
        }
    }

    @Nested
    @DisplayName("경계조건_및_유사케이스")
    class BoundaryAndSimilarCases {

        @Test
        @DisplayName("given_대문자와_소문자만_포함_when_검증하면_then_false를_반환한다")
        void given_대문자와_소문자만_포함_when_검증하면_then_false를_반환한다() {
            // Given
            String password = "Abcdefgh";

            // When
            boolean result = validator.isValid(password, null);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("given_소문자와_숫자만_포함_when_검증하면_then_false를_반환한다")
        void given_소문자와_숫자만_포함_when_검증하면_then_false를_반환한다() {
            // Given
            String password = "abcde123";

            // When
            boolean result = validator.isValid(password, null);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("given_대문자와_숫자만_포함_when_검증하면_then_false를_반환한다")
        void given_대문자와_숫자만_포함_when_검증하면_then_false를_반환한다() {
            // Given
            String password = "ABCDE123";

            // When
            boolean result = validator.isValid(password, null);

            // Then
            assertFalse(result);
        }
    }
}