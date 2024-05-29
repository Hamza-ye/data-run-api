package org.nmcpye.datarun.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProjectAllPropertiesEquals(Project expected, Project actual) {
        assertProjectAutoGeneratedPropertiesEquals(expected, actual);
        assertProjectAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProjectAllUpdatablePropertiesEquals(Project expected, Project actual) {
        assertProjectUpdatableFieldsEquals(expected, actual);
        assertProjectUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProjectAutoGeneratedPropertiesEquals(Project expected, Project actual) {
        assertThat(expected)
            .as("Verify Project auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()))
            .satisfies(e -> assertThat(e.getCreatedBy()).as("check createdBy").isEqualTo(actual.getCreatedBy()))
            .satisfies(e -> assertThat(e.getCreatedDate()).as("check createdDate").isEqualTo(actual.getCreatedDate()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProjectUpdatableFieldsEquals(Project expected, Project actual) {
        assertThat(expected)
            .as("Verify Project relevant properties")
            .satisfies(e -> assertThat(e.getUid()).as("check uid").isEqualTo(actual.getUid()))
            .satisfies(e -> assertThat(e.getCode()).as("check code").isEqualTo(actual.getCode()))
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDisplayed()).as("check displayed").isEqualTo(actual.getDisplayed()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProjectUpdatableRelationshipsEquals(Project expected, Project actual) {}
}
