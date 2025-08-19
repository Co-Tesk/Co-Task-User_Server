package com.cotask.user_server.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Testing stack:
 * - Framework: JUnit 5 (Jupiter)
 * - Assertions: AssertJ + JUnit
 *
 * Tests follow Given-When-Then structure and Korean naming conventions.
 */
class SocialAccountTest {

    // Helper types to allow building an instance without depending on full User/Provider implementations
    // If real classes exist in the project, these will refer to the actual ones from the entity package.
    // Here, we only interact via types and do not persist to any DB.

    @Test
    @DisplayName("given_필수값으로_빌더생성_when_게터호출_then_설정값과_일치한다")
    void given_필수값으로_빌더생성_when_게터호출_then_설정값과_일치한다() {
        // Given
        User dummyUser = new User(); // Relying on entity type presence; fields not used directly
        Provider provider = Provider.GOOGLE; // Assuming enum contains GOOGLE; adjust if necessary
        String providerUid = "uid-12345";

        // When
        SocialAccount socialAccount = SocialAccount.builder()
            .user(dummyUser)
            .provider(provider)
            .providerUid(providerUid)
            .build();

        // Then
        assertAll(
            () -> assertThat(socialAccount.getId()).as("ID는 빌더에서 설정하지 않으므로 null 이어야 함").isNull(),
            () -> assertThat(socialAccount.getUser()).isSameAs(dummyUser),
            () -> assertThat(socialAccount.getProvider()).isEqualTo(provider),
            () -> assertThat(socialAccount.getProviderUid()).isEqualTo(providerUid),
            () -> assertThat(socialAccount.getCreatedAt()).as("영속화 전이므로 생성시각은 null").isNull(),
            () -> assertThat(socialAccount.getUpdatedAt()).as("영속화 전이므로 갱신시각은 null").isNull()
        );
    }

    @Test
    @DisplayName("given_기본생성자_when_필드미설정_then_기본값_null이다")
    void given_기본생성자_when_필드미설정_then_기본값_null이다() {
        // Given
        // 기본 생성자 이용
        // When
        SocialAccount socialAccount = new SocialAccount();

        // Then
        assertAll(
            () -> assertThat(socialAccount.getId()).isNull(),
            () -> assertThat(socialAccount.getUser()).isNull(),
            () -> assertThat(socialAccount.getProvider()).isNull(),
            () -> assertThat(socialAccount.getProviderUid()).isNull(),
            () -> assertThat(socialAccount.getCreatedAt()).isNull(),
            () -> assertThat(socialAccount.getUpdatedAt()).isNull()
        );
    }

    @Test
    @DisplayName("given_리플렉션_when_id필드_어노테이션점검_then_Id와_GeneratedValue_IDENTITY설정이_존재한다")
    void given_리플렉션_when_id필드_어노테이션점검_then_Id와_GeneratedValue_IDENTITY설정이_존재한다() throws Exception {
        // Given
        Field idField = SocialAccount.class.getDeclaredField("id");

        // When
        Id idAnn = idField.getAnnotation(Id.class);
        GeneratedValue genAnn = idField.getAnnotation(GeneratedValue.class);

        // Then
        assertAll(
            () -> assertThat(idAnn).as("@Id 존재 여부").isNotNull(),
            () -> assertThat(genAnn).as("@GeneratedValue 존재 여부").isNotNull(),
            () -> assertThat(genAnn.strategy()).as("GenerationType.IDENTITY 사용 여부")
                    .isEqualTo(GenerationType.IDENTITY)
        );
    }

    @Test
    @DisplayName("given_리플렉션_when_user필드_어노테이션점검_then_ManyToOne과_JoinColumn_user_id_notNull이_존재한다")
    void given_리플렉션_when_user필드_어노테이션점검_then_ManyToOne과_JoinColumn_user_id_notNull이_존재한다() throws Exception {
        // Given
        Field userField = SocialAccount.class.getDeclaredField("user");

        // When
        ManyToOne manyToOne = userField.getAnnotation(ManyToOne.class);
        JoinColumn joinColumn = userField.getAnnotation(JoinColumn.class);

        // Then
        assertAll(
            () -> assertThat(manyToOne).as("@ManyToOne 존재 여부").isNotNull(),
            () -> assertThat(manyToOne.optional()).as("optional=false 확인").isFalse(),
            () -> assertThat(joinColumn).as("@JoinColumn 존재 여부").isNotNull(),
            () -> assertThat(joinColumn.name()).as("조인 컬럼명").isEqualTo("user_id"),
            () -> assertThat(joinColumn.nullable()).as("nullable=false 확인").isFalse()
        );
    }

    @Test
    @DisplayName("given_리플렉션_when_provider필드_어노테이션점검_then_EnumType_STRING과_Column_notNull이_존재한다")
    void given_리플렉션_when_provider필드_어노테이션점검_then_EnumType_STRING과_Column_notNull이_존재한다() throws Exception {
        // Given
        Field providerField = SocialAccount.class.getDeclaredField("provider");

        // When
        Enumerated enumerated = providerField.getAnnotation(Enumerated.class);
        Column column = providerField.getAnnotation(Column.class);

        // Then
        assertAll(
            () -> assertThat(enumerated).isNotNull(),
            () -> assertThat(enumerated.value()).isEqualTo(EnumType.STRING),
            () -> assertThat(column).isNotNull(),
            () -> assertThat(column.name()).isEqualTo("provider"),
            () -> assertThat(column.nullable()).isFalse()
        );
    }

    @Test
    @DisplayName("given_리플렉션_when_providerUid필드_어노테이션점검_then_Column_notNull이_존재한다")
    void given_리플렉션_when_providerUid필드_어노테이션점검_then_Column_notNull이_존재한다() throws Exception {
        // Given
        Field providerUidField = SocialAccount.class.getDeclaredField("providerUid");

        // When
        Column column = providerUidField.getAnnotation(Column.class);

        // Then
        assertAll(
            () -> assertThat(column).isNotNull(),
            () -> assertThat(column.name()).isEqualTo("provider_uid"),
            () -> assertThat(column.nullable()).isFalse()
        );
    }

    @Test
    @DisplayName("given_리플렉션_when_createdAt_updatedAt_어노테이션점검_then_CreationTimestamp_UpdateTimestamp_및_컬럼옵션이_존재한다")
    void given_리플렉션_when_createdAt_updatedAt_어노테이션점검_then_CreationTimestamp_UpdateTimestamp_및_컬럼옵션이_존재한다() throws Exception {
        // Given
        Field createdAtField = SocialAccount.class.getDeclaredField("createdAt");
        Field updatedAtField = SocialAccount.class.getDeclaredField("updatedAt");

        // When
        CreationTimestamp creationTimestamp = createdAtField.getAnnotation(CreationTimestamp.class);
        Column createdAtCol = createdAtField.getAnnotation(Column.class);

        UpdateTimestamp updateTimestamp = updatedAtField.getAnnotation(UpdateTimestamp.class);
        Column updatedAtCol = updatedAtField.getAnnotation(Column.class);

        // Then
        assertAll(
            () -> assertThat(creationTimestamp).isNotNull(),
            () -> assertThat(createdAtCol).isNotNull(),
            () -> assertThat(createdAtCol.updatable()).as("생성시각은 updatable=false").isFalse(),
            () -> assertThat(createdAtCol.nullable()).isFalse(),
            () -> assertThat(updateTimestamp).isNotNull(),
            () -> assertThat(updatedAtCol).isNotNull(),
            () -> assertThat(updatedAtCol.nullable()).isFalse()
        );
    }

    @Test
    @DisplayName("given_리플렉션_when_엔티티_테이블_제약점검_then_uk_provider_uid_유니크제약이_정의된다")
    void given_리플렉션_when_엔티티_테이블_제약점검_then_uk_provider_uid_유니크제약이_정의된다() {
        // Given
        Table table = SocialAccount.class.getAnnotation(Table.class);

        // When
        UniqueConstraint[] uniqueConstraints = table != null ? table.uniqueConstraints() : new UniqueConstraint[0];

        // Then
        assertAll(
            () -> assertThat(table).as("@Table 존재 여부").isNotNull(),
            () -> assertThat(table.name()).as("테이블명").isEqualTo("social_accounts"),
            () -> assertThat(uniqueConstraints).as("유니크 제약 존재 여부").isNotEmpty(),
            () -> assertThat(uniqueConstraints[0].name()).as("유니크 제약명").isEqualTo("uk_provider_uid"),
            () -> assertThat(uniqueConstraints[0].columnNames()).as("유니크 컬럼 구성")
                    .containsExactly("provider", "provider_uid")
        );
    }

    @Nested
    @DisplayName("경계값 및 예외적 상황")
    class EdgeCases {

        @Test
        @DisplayName("given_null값_with_빌더_when_생성_then_널허용필드는_null_유지된다")
        void given_null값_with_빌더_when_생성_then_널허용필드는_null_유지된다() {
            // Given
            // user/provider/providerUid 중 일부를 null 로 설정
            // When
            SocialAccount socialAccount = SocialAccount.builder()
                .user(null)
                .provider(null)
                .providerUid(null)
                .build();

            // Then
            // JPA 제약은 영속 시점에 검증되므로, 단위테스트에서는 null 유지만 확인
            assertAll(
                () -> assertThat(socialAccount.getUser()).isNull(),
                () -> assertThat(socialAccount.getProvider()).isNull(),
                () -> assertThat(socialAccount.getProviderUid()).isNull()
            );
        }

        @Test
        @DisplayName("given_미래시각수동설정_when_리플렉션으로_세팅_then_값이_유지된다")
        void given_미래시각수동설정_when_리플렉션으로_세팅_then_값이_유지된다() throws Exception {
            // Given
            SocialAccount sa = SocialAccount.builder().build();
            LocalDateTime future = LocalDateTime.now().plusYears(10);

            // When
            Field createdAtField = SocialAccount.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(sa, future);

            Field updatedAtField = SocialAccount.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(sa, future);

            // Then
            assertAll(
                () -> assertThat(sa.getCreatedAt()).isEqualTo(future),
                () -> assertThat(sa.getUpdatedAt()).isEqualTo(future)
            );
        }
    }
}