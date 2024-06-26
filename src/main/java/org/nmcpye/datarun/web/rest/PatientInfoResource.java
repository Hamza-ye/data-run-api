package org.nmcpye.datarun.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.nmcpye.datarun.domain.PatientInfo;
import org.nmcpye.datarun.repository.PatientInfoRepository;
import org.nmcpye.datarun.service.PatientInfoService;
import org.nmcpye.datarun.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.datarun.domain.PatientInfo}.
 */
@RestController
@RequestMapping("/api/patient-infos")
public class PatientInfoResource {

    private final Logger log = LoggerFactory.getLogger(PatientInfoResource.class);

    private static final String ENTITY_NAME = "patientInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientInfoService patientInfoService;

    private final PatientInfoRepository patientInfoRepository;

    public PatientInfoResource(PatientInfoService patientInfoService, PatientInfoRepository patientInfoRepository) {
        this.patientInfoService = patientInfoService;
        this.patientInfoRepository = patientInfoRepository;
    }

    /**
     * {@code POST  /patient-infos} : Create a new patientInfo.
     *
     * @param patientInfo the patientInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patientInfo, or with status {@code 400 (Bad Request)} if the patientInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PatientInfo> createPatientInfo(@Valid @RequestBody PatientInfo patientInfo) throws URISyntaxException {
        log.debug("REST request to save PatientInfo : {}", patientInfo);
        if (patientInfo.getId() != null) {
            throw new BadRequestAlertException("A new patientInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        patientInfo = patientInfoService.save(patientInfo);
        return ResponseEntity.created(new URI("/api/patient-infos/" + patientInfo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, patientInfo.getId().toString()))
            .body(patientInfo);
    }

    /**
     * {@code PUT  /patient-infos/:id} : Updates an existing patientInfo.
     *
     * @param id the id of the patientInfo to save.
     * @param patientInfo the patientInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientInfo,
     * or with status {@code 400 (Bad Request)} if the patientInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patientInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientInfo> updatePatientInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PatientInfo patientInfo
    ) throws URISyntaxException {
        log.debug("REST request to update PatientInfo : {}, {}", id, patientInfo);
        if (patientInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        patientInfo = patientInfoService.update(patientInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patientInfo.getId().toString()))
            .body(patientInfo);
    }

    /**
     * {@code PATCH  /patient-infos/:id} : Partial updates given fields of an existing patientInfo, field will ignore if it is null
     *
     * @param id the id of the patientInfo to save.
     * @param patientInfo the patientInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientInfo,
     * or with status {@code 400 (Bad Request)} if the patientInfo is not valid,
     * or with status {@code 404 (Not Found)} if the patientInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the patientInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PatientInfo> partialUpdatePatientInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PatientInfo patientInfo
    ) throws URISyntaxException {
        log.debug("REST request to partial update PatientInfo partially : {}, {}", id, patientInfo);
        if (patientInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PatientInfo> result = patientInfoService.partialUpdate(patientInfo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patientInfo.getId().toString())
        );
    }

    /**
     * {@code GET  /patient-infos} : get all the patientInfos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patientInfos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PatientInfo>> getAllPatientInfos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PatientInfos");
        Page<PatientInfo> page;
        if (eagerload) {
            page = patientInfoService.findAllWithEagerRelationships(pageable);
        } else {
            page = patientInfoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patient-infos/:id} : get the "id" patientInfo.
     *
     * @param id the id of the patientInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientInfo> getPatientInfo(@PathVariable("id") Long id) {
        log.debug("REST request to get PatientInfo : {}", id);
        Optional<PatientInfo> patientInfo = patientInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patientInfo);
    }

    /**
     * {@code DELETE  /patient-infos/:id} : delete the "id" patientInfo.
     *
     * @param id the id of the patientInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatientInfo(@PathVariable("id") Long id) {
        log.debug("REST request to delete PatientInfo : {}", id);
        patientInfoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
