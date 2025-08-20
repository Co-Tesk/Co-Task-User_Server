package com.cotask.user_server.annotation;

import com.cotask.user_server.dto.request.PasswordMatchable;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 테스트 프레임워크: JUnit 5 (Jupiter)
 * 목킹 라이브러리: Mockito
 * 스타일: Given-When-Then (GWT), 한글 테스트 이름
 */
public class PasswordMatchesValidatorTest {

    // 간단한 테스트 더블 구현체
    private static class TestPasswordDTO implements PasswordMatchable {
        private final String password;
        private final String passwordConfirm;

        TestPasswordDTO(String password, String passwordConfirm) {
            this.password = password;
            this.passwordConfirm = passwordConfirm;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getPasswordConfirm() {
            return passwordConfirm;
        }
    }

    @Test
    @DisplayName("given_서로_같은_비밀번호와_확인값_when_검증하면_then_true를_반환한다")
    void givenSamePasswords_whenValidate_thenReturnsTrue() {
        // Given
        PasswordMatchesValidator validator = new PasswordMatchesValidator();
        PasswordMatchable dto = new TestPasswordDTO("Abc123!@", "Abc123!@");
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertTrue(result, "비밀번호와 확인값이 같다면 true 이어야 한다.");
        verify(context, never()).disableDefaultConstraintViolation();
        verifyNoMoreInteractions(context);
    }

    @Test
    @DisplayName("given_서로_다른_비밀번호와_확인값_when_검증하면_then_false를_반환하고_커스텀_제약_메시지를_설정한다")
    void givenDifferentPasswords_whenValidate_thenReturnsFalseAndAddsViolation() {
        // Given
        PasswordMatchesValidator validator = new PasswordMatchesValidator();
        PasswordMatchable dto = new TestPasswordDTO("Abc123!@", "Different!");
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        // ConstraintValidatorContext의 빌더 체인을 목킹
        ConstraintValidatorContext.ConstraintViolationBuilder builder =
                Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder =
                Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);

        when(context.buildConstraintViolationWithTemplate("비밀번호가 일치하지 않습니다.")).thenReturn(builder);
        when(builder.addPropertyNode("passwordConfirm")).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result, "비밀번호와 확인값이 다르면 false 이어야 한다.");
        verify(context, times(1)).disableDefaultConstraintViolation();
        verify(context, times(1)).buildConstraintViolationWithTemplate("비밀번호가 일치하지 않습니다.");
        verify(builder, times(1)).addPropertyNode("passwordConfirm");
        verify(nodeBuilder, times(1)).addConstraintViolation();
        verifyNoMoreInteractions(builder, nodeBuilder);
    }

    @Test
    @DisplayName("given_null_비밀번호_when_검증하면_then_false를_반환하고_추가_제약_설정이_없다")
    void givenNullPassword_whenValidate_thenReturnsFalseAndNoCustomViolation() {
        // Given
        PasswordMatchesValidator validator = new PasswordMatchesValidator();
        PasswordMatchable dto = new TestPasswordDTO(null, "Abc123!@");
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result, "비밀번호가 null 이면 false 이어야 한다.");
        verify(context, never()).disableDefaultConstraintViolation();
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
        verifyNoMoreInteractions(context);
    }

    @Test
    @DisplayName("given_null_확인값_when_검증하면_then_false를_반환하고_추가_제약_설정이_없다")
    void givenNullPasswordConfirm_whenValidate_thenReturnsFalseAndNoCustomViolation() {
        // Given
        PasswordMatchesValidator validator = new PasswordMatchesValidator();
        PasswordMatchable dto = new TestPasswordDTO("Abc123!@", null);
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result, "비밀번호 확인값이 null 이면 false 이어야 한다.");
        verify(context, never()).disableDefaultConstraintViolation();
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
        verifyNoMoreInteractions(context);
    }

    @Test
    @DisplayName("given_둘_다_null값_when_검증하면_then_false를_반환하고_추가_제약_설정이_없다")
    void givenBothNull_whenValidate_thenReturnsFalseAndNoCustomViolation() {
        // Given
        PasswordMatchesValidator validator = new PasswordMatchesValidator();
        PasswordMatchable dto = new TestPasswordDTO(null, null);
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result, "비밀번호와 확인값이 모두 null 이면 false 이어야 한다.");
        verify(context, never()).disableDefaultConstraintViolation();
        verify(context, never()).buildConstraintViolationWithTemplate(anyString());
        verifyNoMoreInteractions(context);
    }

    @Test
    @DisplayName("given_대소문자만_다른_문자열_when_검증하면_then_false를_반환한다")
    void givenCaseOnlyDifference_whenValidate_thenReturnsFalse() {
        // Given
        PasswordMatchesValidator validator = new PasswordMatchesValidator();
        PasswordMatchable dto = new TestPasswordDTO("Password", "password");
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        ConstraintValidatorContext.ConstraintViolationBuilder builder =
                Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder =
                Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);

        when(context.buildConstraintViolationWithTemplate("비밀번호가 일치하지 않습니다.")).thenReturn(builder);
        when(builder.addPropertyNode("passwordConfirm")).thenReturn(nodeBuilder);
        when(nodeBuilder.addConstraintViolation()).thenReturn(context);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertFalse(result, "대소문자가 다르면 equals 가 false 이어야 한다.");
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("비밀번호가 일치하지 않습니다.");
        verify(builder).addPropertyNode("passwordConfirm");
        verify(nodeBuilder).addConstraintViolation();
    }

    @Test
    @DisplayName("given_빈문자열과_빈문자열_when_검증하면_then_true를_반환한다")
    void givenEmptyStrings_whenValidate_thenReturnsTrue() {
        // Given
        PasswordMatchesValidator validator = new PasswordMatchesValidator();
        PasswordMatchable dto = new TestPasswordDTO("", "");
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertTrue(result, "두 값이 빈 문자열로 동일하면 true 이어야 한다.");
        verify(context, never()).disableDefaultConstraintViolation();
        verifyNoMoreInteractions(context);
    }
}