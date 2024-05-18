package org.nmcpye.datarun.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.nmcpye.datarun.domain.TeamAsserts.*;
import static org.nmcpye.datarun.web.rest.TestUtil.createUpdateProxyForBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nmcpye.datarun.IntegrationTest;
import org.nmcpye.datarun.domain.Team;
import org.nmcpye.datarun.repository.TeamRepository;
import org.nmcpye.datarun.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TeamResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TeamResourceIT {

    private static final String DEFAULT_TEAM_LEADER = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_LEADER = "BBBBBBBBBB";

    private static final Long DEFAULT_TEAM_NO = 1L;
    private static final Long UPDATED_TEAM_NO = 2L;

    private static final String DEFAULT_TEAM_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_TEAM_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_MOBILE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TEAM_NO_OF_TEAM_WORKERS = 1;
    private static final Integer UPDATED_TEAM_NO_OF_TEAM_WORKERS = 2;

    private static final String DEFAULT_MOBILITY = "AAAAAAAAAA";
    private static final String UPDATED_MOBILITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/teams";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TeamRepository teamRepository;

    @Mock
    private TeamRepository teamRepositoryMock;

    @Mock
    private TeamService teamServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeamMockMvc;

    private Team team;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Team createEntity(EntityManager em) {
        Team team = new Team()
            .teamLeader(DEFAULT_TEAM_LEADER)
            .teamNo(DEFAULT_TEAM_NO)
            .teamNote(DEFAULT_TEAM_NOTE)
            .teamMobile(DEFAULT_TEAM_MOBILE)
            .teamNoOfTeamWorkers(DEFAULT_TEAM_NO_OF_TEAM_WORKERS)
            .mobility(DEFAULT_MOBILITY);
        return team;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Team createUpdatedEntity(EntityManager em) {
        Team team = new Team()
            .teamLeader(UPDATED_TEAM_LEADER)
            .teamNo(UPDATED_TEAM_NO)
            .teamNote(UPDATED_TEAM_NOTE)
            .teamMobile(UPDATED_TEAM_MOBILE)
            .teamNoOfTeamWorkers(UPDATED_TEAM_NO_OF_TEAM_WORKERS)
            .mobility(UPDATED_MOBILITY);
        return team;
    }

    @BeforeEach
    public void initTest() {
        team = createEntity(em);
    }

    @Test
    @Transactional
    void createTeam() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Team
        var returnedTeam = om.readValue(
            restTeamMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(team)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Team.class
        );

        // Validate the Team in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTeamUpdatableFieldsEquals(returnedTeam, getPersistedTeam(returnedTeam));
    }

    @Test
    @Transactional
    void createTeamWithExistingId() throws Exception {
        // Create the Team with an existing ID
        team.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(team)))
            .andExpect(status().isBadRequest());

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTeamNoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        team.setTeamNo(null);

        // Create the Team, which fails.

        restTeamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(team)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTeams() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList
        restTeamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamLeader").value(hasItem(DEFAULT_TEAM_LEADER)))
            .andExpect(jsonPath("$.[*].teamNo").value(hasItem(DEFAULT_TEAM_NO.intValue())))
            .andExpect(jsonPath("$.[*].teamNote").value(hasItem(DEFAULT_TEAM_NOTE)))
            .andExpect(jsonPath("$.[*].teamMobile").value(hasItem(DEFAULT_TEAM_MOBILE)))
            .andExpect(jsonPath("$.[*].teamNoOfTeamWorkers").value(hasItem(DEFAULT_TEAM_NO_OF_TEAM_WORKERS)))
            .andExpect(jsonPath("$.[*].mobility").value(hasItem(DEFAULT_MOBILITY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTeamsWithEagerRelationshipsIsEnabled() throws Exception {
        when(teamServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeamMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(teamServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTeamsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(teamServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeamMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(teamRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get the team
        restTeamMockMvc
            .perform(get(ENTITY_API_URL_ID, team.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(team.getId().intValue()))
            .andExpect(jsonPath("$.teamLeader").value(DEFAULT_TEAM_LEADER))
            .andExpect(jsonPath("$.teamNo").value(DEFAULT_TEAM_NO.intValue()))
            .andExpect(jsonPath("$.teamNote").value(DEFAULT_TEAM_NOTE))
            .andExpect(jsonPath("$.teamMobile").value(DEFAULT_TEAM_MOBILE))
            .andExpect(jsonPath("$.teamNoOfTeamWorkers").value(DEFAULT_TEAM_NO_OF_TEAM_WORKERS))
            .andExpect(jsonPath("$.mobility").value(DEFAULT_MOBILITY));
    }

    @Test
    @Transactional
    void getNonExistingTeam() throws Exception {
        // Get the team
        restTeamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the team
        Team updatedTeam = teamRepository.findById(team.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTeam are not directly saved in db
        em.detach(updatedTeam);
        updatedTeam
            .teamLeader(UPDATED_TEAM_LEADER)
            .teamNo(UPDATED_TEAM_NO)
            .teamNote(UPDATED_TEAM_NOTE)
            .teamMobile(UPDATED_TEAM_MOBILE)
            .teamNoOfTeamWorkers(UPDATED_TEAM_NO_OF_TEAM_WORKERS)
            .mobility(UPDATED_MOBILITY);

        restTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTeam.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTeam))
            )
            .andExpect(status().isOk());

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTeamToMatchAllProperties(updatedTeam);
    }

    @Test
    @Transactional
    void putNonExistingTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        team.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamMockMvc
            .perform(put(ENTITY_API_URL_ID, team.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(team)))
            .andExpect(status().isBadRequest());

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        team.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(team))
            )
            .andExpect(status().isBadRequest());

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        team.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(team)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeamWithPatch() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the team using partial update
        Team partialUpdatedTeam = new Team();
        partialUpdatedTeam.setId(team.getId());

        partialUpdatedTeam.teamNo(UPDATED_TEAM_NO).teamNoOfTeamWorkers(UPDATED_TEAM_NO_OF_TEAM_WORKERS);

        restTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeam.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeam))
            )
            .andExpect(status().isOk());

        // Validate the Team in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeamUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTeam, team), getPersistedTeam(team));
    }

    @Test
    @Transactional
    void fullUpdateTeamWithPatch() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the team using partial update
        Team partialUpdatedTeam = new Team();
        partialUpdatedTeam.setId(team.getId());

        partialUpdatedTeam
            .teamLeader(UPDATED_TEAM_LEADER)
            .teamNo(UPDATED_TEAM_NO)
            .teamNote(UPDATED_TEAM_NOTE)
            .teamMobile(UPDATED_TEAM_MOBILE)
            .teamNoOfTeamWorkers(UPDATED_TEAM_NO_OF_TEAM_WORKERS)
            .mobility(UPDATED_MOBILITY);

        restTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeam.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeam))
            )
            .andExpect(status().isOk());

        // Validate the Team in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeamUpdatableFieldsEquals(partialUpdatedTeam, getPersistedTeam(partialUpdatedTeam));
    }

    @Test
    @Transactional
    void patchNonExistingTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        team.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamMockMvc
            .perform(patch(ENTITY_API_URL_ID, team.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(team)))
            .andExpect(status().isBadRequest());

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        team.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(team))
            )
            .andExpect(status().isBadRequest());

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeam() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        team.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeamMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(team)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Team in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the team
        restTeamMockMvc
            .perform(delete(ENTITY_API_URL_ID, team.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return teamRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Team getPersistedTeam(Team team) {
        return teamRepository.findById(team.getId()).orElseThrow();
    }

    protected void assertPersistedTeamToMatchAllProperties(Team expectedTeam) {
        assertTeamAllPropertiesEquals(expectedTeam, getPersistedTeam(expectedTeam));
    }

    protected void assertPersistedTeamToMatchUpdatableProperties(Team expectedTeam) {
        assertTeamAllUpdatablePropertiesEquals(expectedTeam, getPersistedTeam(expectedTeam));
    }
}
