package org.nmcpye.datarun.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.nmcpye.datarun.domain.Activity;
import org.nmcpye.datarun.repository.ActivityRepository;
import org.nmcpye.datarun.service.ActivityService;
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
 * REST controller for managing {@link org.nmcpye.datarun.domain.Activity}.
 */
@RestController
@RequestMapping("/api/activities")
public class ActivityResource {

    private final Logger log = LoggerFactory.getLogger(ActivityResource.class);

    private static final String ENTITY_NAME = "activity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivityService activityService;

    private final ActivityRepository activityRepository;

    public ActivityResource(ActivityService activityService, ActivityRepository activityRepository) {
        this.activityService = activityService;
        this.activityRepository = activityRepository;
    }

    /**
     * {@code POST  /activities} : Create a new activity.
     *
     * @param activity the activity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activity, or with status {@code 400 (Bad Request)} if the activity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Activity> createActivity(@Valid @RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to save Activity : {}", activity);
        if (activity.getId() != null) {
            throw new BadRequestAlertException("A new activity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        activity = activityService.save(activity);
        return ResponseEntity.created(new URI("/api/activities/" + activity.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, activity.getId().toString()))
            .body(activity);
    }

    /**
     * {@code PUT  /activities/:id} : Updates an existing activity.
     *
     * @param id the id of the activity to save.
     * @param activity the activity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activity,
     * or with status {@code 400 (Bad Request)} if the activity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Activity activity
    ) throws URISyntaxException {
        log.debug("REST request to update Activity : {}, {}", id, activity);
        if (activity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        activity = activityService.update(activity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activity.getId().toString()))
            .body(activity);
    }

    /**
     * {@code PATCH  /activities/:id} : Partial updates given fields of an existing activity, field will ignore if it is null
     *
     * @param id the id of the activity to save.
     * @param activity the activity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activity,
     * or with status {@code 400 (Bad Request)} if the activity is not valid,
     * or with status {@code 404 (Not Found)} if the activity is not found,
     * or with status {@code 500 (Internal Server Error)} if the activity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Activity> partialUpdateActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Activity activity
    ) throws URISyntaxException {
        log.debug("REST request to partial update Activity partially : {}, {}", id, activity);
        if (activity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Activity> result = activityService.partialUpdate(activity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activity.getId().toString())
        );
    }

    /**
     * {@code GET  /activities} : get all the activities.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Activity>> getAllActivities(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Activities");
        Page<Activity> page;
        if (eagerload) {
            page = activityService.findAllWithEagerRelationships(pageable);
        } else {
            page = activityService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /activities/:id} : get the "id" activity.
     *
     * @param id the id of the activity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivity(@PathVariable("id") Long id) {
        log.debug("REST request to get Activity : {}", id);
        Optional<Activity> activity = activityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activity);
    }

    /**
     * {@code DELETE  /activities/:id} : delete the "id" activity.
     *
     * @param id the id of the activity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable("id") Long id) {
        log.debug("REST request to delete Activity : {}", id);
        activityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
