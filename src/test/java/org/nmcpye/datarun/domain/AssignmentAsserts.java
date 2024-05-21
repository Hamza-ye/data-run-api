package org.nmcpye.datarun.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AssignmentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentAllPropertiesEquals(Assignment expected, Assignment actual) {
        assertAssignmentAutoGeneratedPropertiesEquals(expected, actual);
        assertAssignmentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentAllUpdatablePropertiesEquals(Assignment expected, Assignment actual) {
        assertAssignmentUpdatableFieldsEquals(expected, actual);
        assertAssignmentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentAutoGeneratedPropertiesEquals(Assignment expected, Assignment actual) {
        assertThat(expected)
            .as("Verify Assignment auto generated properties")
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
    public static void assertAssignmentUpdatableFieldsEquals(Assignment expected, Assignment actual) {
        assertThat(expected)
            .as("Verify Assignment relevant properties")
            .satisfies(e -> assertThat(e.getUid()).as("check uid").isEqualTo(actual.getUid()))
            .satisfies(e -> assertThat(e.getPhaseNo()).as("check phaseNo").isEqualTo(actual.getPhaseNo()))
            .satisfies(e -> assertThat(e.getCode()).as("check code").isEqualTo(actual.getCode()))
            .satisfies(e -> assertThat(e.getDistrictCode()).as("check districtCode").isEqualTo(actual.getDistrictCode()))
            .satisfies(e -> assertThat(e.getGov()).as("check gov").isEqualTo(actual.getGov()))
            .satisfies(e -> assertThat(e.getDistrict()).as("check district").isEqualTo(actual.getDistrict()))
            .satisfies(e -> assertThat(e.getSubdistrict()).as("check subdistrict").isEqualTo(actual.getSubdistrict()))
            .satisfies(e -> assertThat(e.getVillage()).as("check village").isEqualTo(actual.getVillage()))
            .satisfies(e -> assertThat(e.getSubvillage()).as("check subvillage").isEqualTo(actual.getSubvillage()))
            .satisfies(e -> assertThat(e.getPpdName()).as("check ppdName").isEqualTo(actual.getPpdName()))
            .satisfies(e -> assertThat(e.getDayId()).as("check dayId").isEqualTo(actual.getDayId()))
            .satisfies(e -> assertThat(e.getPopulation()).as("check population").isEqualTo(actual.getPopulation()))
            .satisfies(e -> assertThat(e.getItnsPlanned()).as("check itnsPlanned").isEqualTo(actual.getItnsPlanned()))
            .satisfies(e -> assertThat(e.getTargetType()).as("check targetType").isEqualTo(actual.getTargetType()))
            .satisfies(e -> assertThat(e.getLongitude()).as("check longitude").isEqualTo(actual.getLongitude()))
            .satisfies(e -> assertThat(e.getLatitude()).as("check latitude").isEqualTo(actual.getLatitude()))
            .satisfies(e -> assertThat(e.getStartDate()).as("check startDate").isEqualTo(actual.getStartDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentUpdatableRelationshipsEquals(Assignment expected, Assignment actual) {
        assertThat(expected)
            .as("Verify Assignment relationships")
            .satisfies(e -> assertThat(e.getActivity()).as("check activity").isEqualTo(actual.getActivity()))
            .satisfies(e -> assertThat(e.getOrganisationUnit()).as("check organisationUnit").isEqualTo(actual.getOrganisationUnit()))
            .satisfies(e -> assertThat(e.getTeam()).as("check team").isEqualTo(actual.getTeam()))
            .satisfies(e -> assertThat(e.getWarehouse()).as("check warehouse").isEqualTo(actual.getWarehouse()));
    }
}
