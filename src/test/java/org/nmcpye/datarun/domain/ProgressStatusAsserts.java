package org.nmcpye.datarun.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ProgressStatusAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProgressStatusAllPropertiesEquals(ProgressStatus expected, ProgressStatus actual) {
        assertProgressStatusAutoGeneratedPropertiesEquals(expected, actual);
        assertProgressStatusAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProgressStatusAllUpdatablePropertiesEquals(ProgressStatus expected, ProgressStatus actual) {
        assertProgressStatusUpdatableFieldsEquals(expected, actual);
        assertProgressStatusUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProgressStatusAutoGeneratedPropertiesEquals(ProgressStatus expected, ProgressStatus actual) {
        assertThat(expected)
            .as("Verify ProgressStatus auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProgressStatusUpdatableFieldsEquals(ProgressStatus expected, ProgressStatus actual) {
        assertThat(expected)
            .as("Verify ProgressStatus relevant properties")
            .satisfies(
                e -> assertThat(e.getProgressStatusLabel()).as("check progressStatusLabel").isEqualTo(actual.getProgressStatusLabel())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProgressStatusUpdatableRelationshipsEquals(ProgressStatus expected, ProgressStatus actual) {}
}
