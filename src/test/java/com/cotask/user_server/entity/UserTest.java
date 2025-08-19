package com.cotask.user_server.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import net.minidev.json.annotate.JsonIgnore;

class UserTest {

    // Helper: reflectively get declared field
    private Field getField(String name) {
        try {
            Field f = User.class.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            fail("필드가 존재해야 합니다: " + name);
            return null; // unreachable
        }
    }

    // Helper: check if method exists
    private boolean hasMethod(String name, Class<?>... parameterTypes) {
        try {
            User.class.getDeclaredMethod(name, parameterTypes);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    @Test
    @DisplayName("given_필수필드가_유효할때_when_빌더로_User_생성하면_then_필드값이_정상설정되고_기본값이_적용된다")
    void given_필수필드가_유효할때_when_빌더로_User_생성하면_then_필드값이_정상설정되고_기본값이_적용된다() {
        // given
        String email = "user@example.com";
        String password = "encoded-password";
        String nickname = "홍길동";

        // when
        User user = User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        // then
        assertNull(user.getId(), "ID는 초기 생성 시 null 이어야 합니다.");
        assertEquals(email, user.getEmail(), "이메일이 빌더 값과 일치해야 합니다.");
        assertEquals(password, user.getPassword(), "비밀번호가 빌더 값과 일치해야 합니다.");
        assertEquals(nickname, user.getNickname(), "닉네임이 빌더 값과 일치해야 합니다.");

        // 기본값 검증
        assertNotNull(user.getIsVerify(), "isVerify는 null 이 아니어야 합니다.");
        assertFalse(user.getIsVerify(), "isVerify의 기본값은 false 이어야 합니다.");

        assertNotNull(user.getIsDeleted(), "isDeleted는 null 이 아니어야 합니다.");
        assertFalse(user.getIsDeleted(), "isDeleted의 기본값은 false 이어야 합니다.");

        assertNull(user.getDeletedAt(), "deletedAt의 기본값은 null 이어야 합니다.");

        // JPA가 관리하는 타임스탬프는 영속화 전에는 null일 수 있음
        assertNull(user.getCreatedAt(), "createdAt은 영속화 전 null일 수 있습니다.");
        assertNull(user.getUpdatedAt(), "updatedAt은 영속화 전 null일 수 있습니다.");
    }

    @Test
    @DisplayName("given_생성된_User_when_세터로_변경하면_then_변경사항이_정상반영된다")
    void given_생성된_User_when_세터로_변경하면_then_변경사항이_정상반영된다() {
        // given
        User user = User.builder()
                .email("a@b.com")
                .password("p1")
                .nickname("닉1")
                .build();

        // when
        user.setPassword("p2");
        user.setNickname("닉2");
        user.setIsVerify(true);
        user.setIsDeleted(true);
        LocalDateTime now = LocalDateTime.now();
        user.setDeletedAt(now);

        // then
        assertEquals("p2", user.getPassword(), "비밀번호 세터 반영 실패");
        assertEquals("닉2", user.getNickname(), "닉네임 세터 반영 실패");
        assertTrue(user.getIsVerify(), "isVerify 세터 반영 실패");
        assertTrue(user.getIsDeleted(), "isDeleted 세터 반영 실패");
        assertEquals(now, user.getDeletedAt(), "deletedAt 세터 반영 실패");
    }

    @Test
    @DisplayName("given_User_엔티티_when_허용되지_않은_필드에_세터_존재여부를_확인하면_then_세터가_존재하지_않아야_한다")
    void given_User_엔티티_when_허용되지_않은_필드에_세터_존재여부를_확인하면_then_세터가_존재하지_않아야_한다() {
        // given + when
        boolean hasSetId = hasMethod("setId", Long.class);
        boolean hasSetEmail = hasMethod("setEmail", String.class);
        boolean hasSetCreatedAt = hasMethod("setCreatedAt", LocalDateTime.class);
        boolean hasSetUpdatedAt = hasMethod("setUpdatedAt", LocalDateTime.class);

        // then
        assertFalse(hasSetId, "id 필드에 대한 세터가 존재하면 안됩니다.");
        assertFalse(hasSetEmail, "email 필드에 대한 세터가 존재하면 안됩니다.");
        assertFalse(hasSetCreatedAt, "createdAt 필드에 대한 세터가 존재하면 안됩니다.");
        assertFalse(hasSetUpdatedAt, "updatedAt 필드에 대한 세터가 존재하면 안됩니다.");
    }

    @Test
    @DisplayName("given_User_엔티티_when_JPA_엔티티_및_컬럼_어노테이션을_검증하면_then_정의가_정확해야_한다")
    void given_User_엔티티_when_JPA_엔티티_및_컬럼_어노테이션을_검증하면_then_정의가_정확해야_한다() {
        // given + when
        Annotation entity = User.class.getAnnotation(Entity.class);
        Table table = User.class.getAnnotation(Table.class);

        // then
        assertNotNull(entity, "@Entity 어노테이션이 있어야 합니다.");
        assertNotNull(table, "@Table 어노테이션이 있어야 합니다.");
        assertEquals("users", table.name(), "@Table(name) 값이 'users' 이어야 합니다.");

        // 필드별 Column 설정 검증
        Column emailCol = getField("email").getAnnotation(Column.class);
        assertNotNull(emailCol, "email에는 @Column이 있어야 합니다.");
        assertFalse(emailCol.nullable(), "email은 nullable=false 이어야 합니다.");
        assertTrue(emailCol.unique(), "email은 unique=true 이어야 합니다.");
        assertEquals(100, emailCol.length(), "email length=100 이어야 합니다.");

        Column passwordCol = getField("password").getAnnotation(Column.class);
        assertNotNull(passwordCol, "password에는 @Column이 있어야 합니다.");
        assertFalse(passwordCol.nullable(), "password는 nullable=false 이어야 합니다.");

        Column nicknameCol = getField("nickname").getAnnotation(Column.class);
        assertNotNull(nicknameCol, "nickname에는 @Column이 있어야 합니다.");
        assertFalse(nicknameCol.nullable(), "nickname은 nullable=false 이어야 합니다.");
        assertEquals(30, nicknameCol.length(), "nickname length=30 이어야 합니다.");

        Column isVerifyCol = getField("isVerify").getAnnotation(Column.class);
        assertNotNull(isVerifyCol, "isVerify에는 @Column이 있어야 합니다.");
        assertFalse(isVerifyCol.nullable(), "isVerify는 nullable=false 이어야 합니다.");

        Column createdAtCol = getField("createdAt").getAnnotation(Column.class);
        assertNotNull(createdAtCol, "createdAt에는 @Column이 있어야 합니다.");
        assertFalse(createdAtCol.nullable(), "createdAt은 nullable=false 이어야 합니다.");
        assertFalse(createdAtCol.updatable(), "createdAt은 updatable=false 이어야 합니다.");

        Column updatedAtCol = getField("updatedAt").getAnnotation(Column.class);
        assertNotNull(updatedAtCol, "updatedAt에는 @Column이 있어야 합니다.");
        assertFalse(updatedAtCol.nullable(), "updatedAt은 nullable=false 이어야 합니다.");

        Column isDeletedCol = getField("isDeleted").getAnnotation(Column.class);
        assertNotNull(isDeletedCol, "isDeleted에는 @Column이 있어야 합니다.");
        assertFalse(isDeletedCol.nullable(), "isDeleted는 nullable=false 이어야 합니다.");

        // ID, GeneratedValue 전략 확인
        Id idAnn = getField("id").getAnnotation(Id.class);
        GeneratedValue gen = getField("id").getAnnotation(GeneratedValue.class);
        assertNotNull(idAnn, "id에는 @Id가 있어야 합니다.");
        assertNotNull(gen, "id에는 @GeneratedValue가 있어야 합니다.");
        assertEquals(GenerationType.IDENTITY, gen.strategy(), "ID 생성 전략은 IDENTITY 이어야 합니다.");
    }

    @Test
    @DisplayName("given_User_엔티티_when_password_필드의_JSON_직렬화_정책을_확인하면_then_JsonIgnore_어노테이션이_존재해야_한다")
    void given_User_엔티티_when_password_필드의_JSON_직렬화_정책을_확인하면_then_JsonIgnore_어노테이션이_존재해야_한다() {
        // given + when
        JsonIgnore ignore = getField("password").getAnnotation(JsonIgnore.class);

        // then
        assertNotNull(ignore, "password 필드에는 net.minidev.json.annotate.JsonIgnore 가 있어야 합니다.");
    }

    @Test
    @DisplayName("given_JsonSmart_가_사용가능할때_when_User를_JSON으로_직렬화하면_then_password_필드는_제외된다")
    void given_JsonSmart_가_사용가능할때_when_User를_JSON으로_직렬화하면_then_password_필드는_제외된다() {
        // given
        User user = User.builder()
                .email("user@example.com")
                .password("secret")
                .nickname("별명")
                .build();

        // when
        // json-smart 의존성이 있을 경우만 수행: 리플렉션으로 존재 확인 후 동적 호출
        try {
            Class<?> jsonValueClass = Class.forName("net.minidev.json.JSONValue");
            Method toJsonString = jsonValueClass.getMethod("toJSONString", Object.class);
            String json = (String) toJsonString.invoke(null, user);

            // then
            assertNotNull(json, "JSON 문자열이 생성되어야 합니다.");
            assertTrue(json.contains("\"email\""), "JSON에 email 필드는 있어야 합니다.");
            assertTrue(json.contains("\"nickname\""), "JSON에 nickname 필드는 있어야 합니다.");
            assertFalse(json.contains("password"), "JSON에 password 필드는 포함되면 안됩니다.");
        } catch (ClassNotFoundException e) {
            // json-smart 미존재 시: 테스트를 관대한 방식으로 패스 처리
            // then
            assertTrue(true, "json-smart 미존재: 직렬화 제외 검증은 건너뜁니다.");
        } catch (ReflectiveOperationException e) {
            fail("json-smart 직렬화 중 오류: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("given_올아규먼츠_생성자를_사용할때_when_모든필드를_설정하면_then_게터로_동일값을_조회할수있다")
    void given_올아규먼츠_생성자를_사용할때_when_모든필드를_설정하면_then_게터로_동일값을_조회할수있다() {
        // given
        Long id = 1L;
        String email = "user@ex.com";
        String password = "pwd";
        String nickname = "닉";
        Boolean isVerify = true;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();
        Boolean isDeleted = true;
        LocalDateTime deletedAt = LocalDateTime.now().minusHours(2);

        // when
        User user = new User(id, email, password, nickname, isVerify, createdAt, updatedAt, isDeleted, deletedAt);

        // then
        assertEquals(id, user.getId());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(nickname, user.getNickname());
        assertEquals(isVerify, user.getIsVerify());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
        assertEquals(isDeleted, user.getIsDeleted());
        assertEquals(deletedAt, user.getDeletedAt());
    }

    @Test
    @DisplayName("given_노아규먼츠_생성자를_사용할때_when_기본생성하면_then_기본값과_null값이_의도대로_설정된다")
    void given_노아규먼츠_생성자를_사용할때_when_기본생성하면_then_기본값과_null값이_의도대로_설정된다() {
        // given + when
        User user = new User();

        // then
        assertNull(user.getId(), "id 기본값은 null 이어야 합니다.");
        assertNull(user.getEmail(), "email 기본값은 null 이어야 합니다.");
        assertNull(user.getPassword(), "password 기본값은 null 이어야 합니다.");
        assertNull(user.getNickname(), "nickname 기본값은 null 이어야 합니다.");

        assertNotNull(user.getIsVerify(), "isVerify는 null 이 아니어야 합니다.");
        assertFalse(user.getIsVerify(), "isVerify 기본값 false");

        assertNull(user.getCreatedAt(), "createdAt 기본값 null");
        assertNull(user.getUpdatedAt(), "updatedAt 기본값 null");

        assertNotNull(user.getIsDeleted(), "isDeleted는 null 이 아니어야 합니다.");
        assertFalse(user.getIsDeleted(), "isDeleted 기본값 false");

        assertNull(user.getDeletedAt(), "deletedAt 기본값 null");
    }
}