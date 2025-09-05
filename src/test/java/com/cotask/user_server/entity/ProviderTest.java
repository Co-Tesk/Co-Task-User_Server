package com.cotask.user_server.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 테스트 프레임워크: JUnit 5 (Jupiter)
 * - 각 테스트는 한글 테스트명과 given-when-then 방식을 따릅니다.
 */
class ProviderTest {

    @Test
    @DisplayName("given: Provider 열거형이 선언되어 있을 때, when: values()를 호출하면, then: 모든 상수가 선언 순서대로 반환된다")
    void given_Provider_열거형_when_values_호출_then_모든_상수가_선언_순서대로_반환된다() {
        // given
        // Provider enum은 고정적으로 선언되어 있음

        // when
        Provider[] values = Provider.values();

        // then
        assertNotNull(values, "values()는 null을 반환하지 않아야 한다.");
        assertEquals(3, values.length, "상수 개수는 3이어야 한다.");
        assertArrayEquals(new Provider[]{Provider.GOOGLE, Provider.KAKAO, Provider.NAVER}, values, "선언 순서와 동일해야 한다.");
    }

    @Test
    @DisplayName("given: 유효한 이름이 주어지면, when: valueOf를 호출하면, then: 해당 상수를 반환한다")
    void given_유효한_이름_when_valueOf_호출_then_해당_상수를_반환한다() {
        // given
        String name = "KAKAO";

        // when
        Provider provider = Provider.valueOf(name);

        // then
        assertEquals(Provider.KAKAO, provider);
    }

    @Test
    @DisplayName("given: 잘못된(대소문자 불일치) 이름이 주어지면, when: valueOf를 호출하면, then: IllegalArgumentException이 발생한다")
    void given_잘못된_이름_when_valueOf_호출_then_IllegalArgumentException_발생한다() {
        // given
        String invalid = "kakao"; // 소문자

        // when & then
        assertThrows(IllegalArgumentException.class, () -> Provider.valueOf(invalid));
    }

    @Test
    @DisplayName("given: null이 주어지면, when: valueOf를 호출하면, then: NullPointerException이 발생한다")
    void given_null_when_valueOf_호출_then_NullPointerException_발생한다() {
        // given
        String nullName = null;

        // when & then
        assertThrows(NullPointerException.class, () -> Provider.valueOf(nullName));
    }

    @Test
    @DisplayName("given: 각 상수에 대해, when: ordinal을 확인하면, then: 선언 순서를 따른다")
    void given_각_상수_when_ordinal_확인_then_선언_순서를_따른다() {
        // given - no preconditions

        // then
        assertAll(
            () -> assertEquals(0, Provider.GOOGLE.ordinal()),
            () -> assertEquals(1, Provider.KAKAO.ordinal()),
            () -> assertEquals(2, Provider.NAVER.ordinal())
        );
    }

    @Test
    @DisplayName("given: 각 상수에 대해, when: name()과 toString()을 비교하면, then: 동일한 문자열을 반환한다")
    void given_각_상수_when_name과_toString_확인_then_동일한_문자열을_반환한다() {
        // given - no preconditions

        // when
        Provider[] values = Provider.values();

        // then
        for (Provider p : values) {
            assertEquals(p.name(), p.toString(), "name()과 toString()은 동일해야 한다.");
        }
        assertEquals("GOOGLE", Provider.GOOGLE.name());
        assertEquals("KAKAO", Provider.KAKAO.name());
        assertEquals("NAVER", Provider.NAVER.name());
    }
}