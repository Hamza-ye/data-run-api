package org.nmcpye.datarun.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeamAllPropertiesEquals(Team expected, Team actual) {
        assertTeamAutoGeneratedPropertiesEquals(expected, actual);
        assertTeamAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeamAllUpdatablePropertiesEquals(Team expected, Team actual) {
        assertTeamUpdatableFieldsEquals(expected, actual);
        assertTeamUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeamAutoGeneratedPropertiesEquals(Team expected, Team actual) {
        assertThat(expected)
            .as("Verify Team auto generated properties")
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
    public static void assertTeamUpdatableFieldsEquals(Team expected, Team actual) {
        assertThat(expected)
            .as("Verify Team relevant properties")
            .satisfies(e -> assertThat(e.getTeamLeader()).as("check teamLeader").isEqualTo(actual.getTeamLeader()))
            .satisfies(e -> assertThat(e.getTeamNo()).as("check teamNo").isEqualTo(actual.getTeamNo()))
            .satisfies(e -> assertThat(e.getTeamNote()).as("check teamNote").isEqualTo(actual.getTeamNote()))
            .satisfies(e -> assertThat(e.getTeamMobile()).as("check teamMobile").isEqualTo(actual.getTeamMobile()))
            .satisfies(
                e -> assertThat(e.getTeamNoOfTeamWorkers()).as("check teamNoOfTeamWorkers").isEqualTo(actual.getTeamNoOfTeamWorkers())
            )
            .satisfies(e -> assertThat(e.getMobility()).as("check mobility").isEqualTo(actual.getMobility()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeamUpdatableRelationshipsEquals(Team expected, Team actual) {
        assertThat(expected)
            .as("Verify Team relationships")
            .satisfies(e -> assertThat(e.getCampaign()).as("check campaign").isEqualTo(actual.getCampaign()))
            .satisfies(e -> assertThat(e.getOperationRoom()).as("check operationRoom").isEqualTo(actual.getOperationRoom()))
            .satisfies(e -> assertThat(e.getWarehouse()).as("check warehouse").isEqualTo(actual.getWarehouse()));
    }
}
