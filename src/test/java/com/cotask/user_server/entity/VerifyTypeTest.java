package com.cotask.user_server.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

import static org.assertj.core.api.Assertions.*;

/**
 * 테스트 프레임워크: JUnit 5 (Jupiter)
 * 단언 라이브러리: AssertJ
 *
 * 본 테스트는 VerifyType enum의 공개 인터페이스를 검증한다.
 * - values(), valueOf(String), name(), ordinal(), toString()
 * - 예외 및 경계 케이스 포함
 *
 * 명명 규칙: 한국어 Given-When-Then 형식, 메서드에는 언더스코어를 사용.
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class VerifyTypeTest {

    // Helper: 안전한 조회 래퍼 (실제 사용 시 valueOf를 감싸는 유틸과 동일한 동작을 가정)
    private VerifyType safeLookup(String name) {
        try {
            return VerifyType.valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }

    @Test
    @DisplayName("given_정의된_상수들_when_values_호출_then_모든_상수와_순서를_반환한다")
    void given_정의된_상수들_when_values_호출_then_모든_상수와_순서를_반환한다() {
        // given
        // enum 정의: EMAIL, PASSWORD

        // when
        VerifyType[] values = VerifyType.values();

        // then
        assertThat(values)
                .isNotNull()
                .hasSize(2)
                .containsExactly(VerifyType.EMAIL, VerifyType.PASSWORD);
    }

    @Test
    @DisplayName("given_유효한_이름_EMAIL_when_valueOf_호출_then_EMAIL_을_반환한다")
    void given_유효한_이름_EMAIL_when_valueOf_호출_then_EMAIL_을_반환한다() {
        // given
        String name = "EMAIL";

        // when
        VerifyType result = VerifyType.valueOf(name);

        // then
        assertThat(result).isSameAs(VerifyType.EMAIL);
    }

    @Test
    @DisplayName("given_유효한_이름_PASSWORD_when_valueOf_호출_then_PASSWORD_을_반환한다")
    void given_유효한_이름_PASSWORD_when_valueOf_호출_then_PASSWORD_을_반환한다() {
        // given
        String name = "PASSWORD";

        // when
        VerifyType result = VerifyType.valueOf(name);

        // then
        assertThat(result).isSameAs(VerifyType.PASSWORD);
    }

    @Test
    @DisplayName("given_정의되지_않은_이름_when_valueOf_호출_then_IllegalArgumentException_을_던진다")
    void given_정의되지_않은_이름_when_valueOf_호출_then_IllegalArgumentException_을_던진다() {
        // given
        String invalid = "USERNAME";

        // when / then
        assertThatThrownBy(() -> VerifyType.valueOf(invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(invalid);
    }

    @Test
    @DisplayName("given_null_이름_when_valueOf_호출_then_NullPointerException_을_던진다")
    void given_null_이름_when_valueOf_호출_then_NullPointerException_을_던진다() {
        // given
        String nullName = null;

        // when / then
        assertThatThrownBy(() -> VerifyType.valueOf(nullName))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("given_소문자_이름_when_valueOf_호출_then_일치하지_않아_IllegalArgumentException_을_던진다")
    void given_소문자_이름_when_valueOf_호출_then_일치하지_않아_IllegalArgumentException_을_던진다() {
        // given
        String lower = "email";

        // when / then
        assertThatThrownBy(() -> VerifyType.valueOf(lower))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("given_ENUM_상수_when_toString_호출_then_name_과_동일한_문자열을_반환한다")
    void given_ENUM_상수_when_toString_호출_then_name_과_동일한_문자열을_반환한다() {
        // given
        VerifyType email = VerifyType.EMAIL;
        VerifyType password = VerifyType.PASSWORD;

        // when
        String emailStr = email.toString();
        String passwordStr = password.toString();

        // then
        assertThat(emailStr).isEqualTo(email.name());
        assertThat(passwordStr).isEqualTo(password.name());
    }

    @Test
    @DisplayName("given_ENUM_정의_순서_when_ordinal_확인_then_EMAIL_이_0이고_PASSWORD_가_1이다")
    void given_ENUM_정의_순서_when_ordinal_확인_then_EMAIL_이_0이고_PASSWORD_가_1이다() {
        // given
        // enum 선언 순서: EMAIL(0), PASSWORD(1)

        // when
        int emailOrdinal = VerifyType.EMAIL.ordinal();
        int passwordOrdinal = VerifyType.PASSWORD.ordinal();

        // then
        assertThat(emailOrdinal).isZero();
        assertThat(passwordOrdinal).isEqualTo(1);
    }

    @Test
    @DisplayName("given_안전한_조회_when_존재하지_않는_이름을_전달_then_null을_반환한다")
    void given_안전한_조회_when_존재하지_않는_이름을_전달_then_null을_반환한다() {
        // given
        String invalid = "NOT_A_TYPE";

        // when
        VerifyType lookedUp = safeLookup(invalid);

        // then
        assertThat(lookedUp).isNull();
    }

    @Test
    @DisplayName("given_values_결과_when_불변성_확인_then_복사본을_변경해도_ENUM_정의에는_영향이_없다")
    void given_values_결과_when_불변성_확인_then_복사본을_변경해도_ENUM_정의에는_영향이_없다() {
        // given
        VerifyType[] copy = VerifyType.values().clone();

        // when
        // 배열 요소 교환 시도 (배열 자체는 수정 가능하지만 enum 정의는 불변)
        VerifyType temp = copy[0];
        copy[0] = copy[1];
        copy[1] = temp;

        // then
        // 원본의 순서와 내용은 여전히 동일해야 함
        assertThat(VerifyType.values()).containsExactly(VerifyType.EMAIL, VerifyType.PASSWORD);
    }
}