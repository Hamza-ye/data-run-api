package org.nmcpye.datarun.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.nmcpye.datarun.domain.PatientInfoAsserts.*;
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
import org.nmcpye.datarun.domain.PatientInfo;
import org.nmcpye.datarun.domain.enumeration.Gender;
import org.nmcpye.datarun.repository.PatientInfoRepository;
import org.nmcpye.datarun.service.PatientInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PatientInfoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PatientInfoResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String ENTITY_API_URL = "/api/patient-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PatientInfoRepository patientInfoRepository;

    @Mock
    private PatientInfoRepository patientInfoRepositoryMock;

    @Mock
    private PatientInfoService patientInfoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientInfoMockMvc;

    private PatientInfo patientInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientInfo createEntity(EntityManager em) {
        PatientInfo patientInfo = new PatientInfo()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .age(DEFAULT_AGE)
            .gender(DEFAULT_GENDER);
        return patientInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientInfo createUpdatedEntity(EntityManager em) {
        PatientInfo patientInfo = new PatientInfo()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER);
        return patientInfo;
    }

    @BeforeEach
    public void initTest() {
        patientInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createPatientInfo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PatientInfo
        var returnedPatientInfo = om.readValue(
            restPatientInfoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientInfo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PatientInfo.class
        );

        // Validate the PatientInfo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPatientInfoUpdatableFieldsEquals(returnedPatientInfo, getPersistedPatientInfo(returnedPatientInfo));
    }

    @Test
    @Transactional
    void createPatientInfoWithExistingId() throws Exception {
        // Create the PatientInfo with an existing ID
        patientInfo.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientInfo)))
            .andExpect(status().isBadRequest());

        // Validate the PatientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPatientInfos() throws Exception {
        // Initialize the database
        patientInfoRepository.saveAndFlush(patientInfo);

        // Get all the patientInfoList
        restPatientInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPatientInfosWithEagerRelationshipsIsEnabled() throws Exception {
        when(patientInfoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPatientInfoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(patientInfoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPatientInfosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(patientInfoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPatientInfoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(patientInfoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPatientInfo() throws Exception {
        // Initialize the database
        patientInfoRepository.saveAndFlush(patientInfo);

        // Get the patientInfo
        restPatientInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, patientInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patientInfo.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPatientInfo() throws Exception {
        // Get the patientInfo
        restPatientInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPatientInfo() throws Exception {
        // Initialize the database
        patientInfoRepository.saveAndFlush(patientInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patientInfo
        PatientInfo updatedPatientInfo = patientInfoRepository.findById(patientInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPatientInfo are not directly saved in db
        em.detach(updatedPatientInfo);
        updatedPatientInfo.uid(UPDATED_UID).code(UPDATED_CODE).name(UPDATED_NAME).age(UPDATED_AGE).gender(UPDATED_GENDER);

        restPatientInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPatientInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPatientInfo))
            )
            .andExpect(status().isOk());

        // Validate the PatientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPatientInfoToMatchAllProperties(updatedPatientInfo);
    }

    @Test
    @Transactional
    void putNonExistingPatientInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patientInfo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(patientInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatientInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patientInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(patientInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatientInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patientInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patientInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientInfoWithPatch() throws Exception {
        // Initialize the database
        patientInfoRepository.saveAndFlush(patientInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patientInfo using partial update
        PatientInfo partialUpdatedPatientInfo = new PatientInfo();
        partialUpdatedPatientInfo.setId(patientInfo.getId());

        partialUpdatedPatientInfo.code(UPDATED_CODE).name(UPDATED_NAME).gender(UPDATED_GENDER);

        restPatientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPatientInfo))
            )
            .andExpect(status().isOk());

        // Validate the PatientInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPatientInfoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPatientInfo, patientInfo),
            getPersistedPatientInfo(patientInfo)
        );
    }

    @Test
    @Transactional
    void fullUpdatePatientInfoWithPatch() throws Exception {
        // Initialize the database
        patientInfoRepository.saveAndFlush(patientInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patientInfo using partial update
        PatientInfo partialUpdatedPatientInfo = new PatientInfo();
        partialUpdatedPatientInfo.setId(patientInfo.getId());

        partialUpdatedPatientInfo.uid(UPDATED_UID).code(UPDATED_CODE).name(UPDATED_NAME).age(UPDATED_AGE).gender(UPDATED_GENDER);

        restPatientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPatientInfo))
            )
            .andExpect(status().isOk());

        // Validate the PatientInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPatientInfoUpdatableFieldsEquals(partialUpdatedPatientInfo, getPersistedPatientInfo(partialUpdatedPatientInfo));
    }

    @Test
    @Transactional
    void patchNonExistingPatientInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patientInfo.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patientInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(patientInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatientInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patientInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(patientInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatientInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patientInfo.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientInfoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(patientInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatientInfo() throws Exception {
        // Initialize the database
        patientInfoRepository.saveAndFlush(patientInfo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the patientInfo
        restPatientInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, patientInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return patientInfoRepository.count();
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

    protected PatientInfo getPersistedPatientInfo(PatientInfo patientInfo) {
        return patientInfoRepository.findById(patientInfo.getId()).orElseThrow();
    }

    protected void assertPersistedPatientInfoToMatchAllProperties(PatientInfo expectedPatientInfo) {
        assertPatientInfoAllPropertiesEquals(expectedPatientInfo, getPersistedPatientInfo(expectedPatientInfo));
    }

    protected void assertPersistedPatientInfoToMatchUpdatableProperties(PatientInfo expectedPatientInfo) {
        assertPatientInfoAllUpdatablePropertiesEquals(expectedPatientInfo, getPersistedPatientInfo(expectedPatientInfo));
    }
}
