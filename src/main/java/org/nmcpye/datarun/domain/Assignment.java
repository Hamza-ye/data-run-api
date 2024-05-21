package org.nmcpye.datarun.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Assignment.
 */
@Entity
@Table(name = "assignment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Assignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 11)
    @Column(name = "uid", length = 11, unique = true)
    private String uid;

    @Column(name = "phase_no")
    private Integer phaseNo;

    @Column(name = "code")
    private String code;

    @Column(name = "district_code")
    private Integer districtCode;

    @Column(name = "gov")
    private String gov;

    @Column(name = "district")
    private String district;

    @Column(name = "subdistrict")
    private String subdistrict;

    @Column(name = "village")
    private String village;

    @Column(name = "subvillage")
    private String subvillage;

    @Column(name = "ppd_name")
    private String ppdName;

    @Column(name = "day_id")
    private Integer dayId;

    @Column(name = "population")
    private Double population;

    @Column(name = "itns_planned")
    private Integer itnsPlanned;

    @Column(name = "target_type")
    private Integer targetType;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "start_day_date")
    private Instant startDayDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "project" }, allowSetters = true)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    private MVillagesLocations location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "activity", "operationRoom", "warehouse", "userInfo" }, allowSetters = true)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "activity" }, allowSetters = true)
    private Warehouse warehouse;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Assignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return this.uid;
    }

    public Assignment uid(String uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getPhaseNo() {
        return this.phaseNo;
    }

    public Assignment phaseNo(Integer phaseNo) {
        this.setPhaseNo(phaseNo);
        return this;
    }

    public void setPhaseNo(Integer phaseNo) {
        this.phaseNo = phaseNo;
    }

    public String getCode() {
        return this.code;
    }

    public Assignment code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDistrictCode() {
        return this.districtCode;
    }

    public Assignment districtCode(Integer districtCode) {
        this.setDistrictCode(districtCode);
        return this;
    }

    public void setDistrictCode(Integer districtCode) {
        this.districtCode = districtCode;
    }

    public String getGov() {
        return this.gov;
    }

    public Assignment gov(String gov) {
        this.setGov(gov);
        return this;
    }

    public void setGov(String gov) {
        this.gov = gov;
    }

    public String getDistrict() {
        return this.district;
    }

    public Assignment district(String district) {
        this.setDistrict(district);
        return this;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSubdistrict() {
        return this.subdistrict;
    }

    public Assignment subdistrict(String subdistrict) {
        this.setSubdistrict(subdistrict);
        return this;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public String getVillage() {
        return this.village;
    }

    public Assignment village(String village) {
        this.setVillage(village);
        return this;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getSubvillage() {
        return this.subvillage;
    }

    public Assignment subvillage(String subvillage) {
        this.setSubvillage(subvillage);
        return this;
    }

    public void setSubvillage(String subvillage) {
        this.subvillage = subvillage;
    }

    public String getPpdName() {
        return this.ppdName;
    }

    public Assignment ppdName(String ppdName) {
        this.setPpdName(ppdName);
        return this;
    }

    public void setPpdName(String ppdName) {
        this.ppdName = ppdName;
    }

    public Integer getDayId() {
        return this.dayId;
    }

    public Assignment dayId(Integer dayId) {
        this.setDayId(dayId);
        return this;
    }

    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    public Double getPopulation() {
        return this.population;
    }

    public Assignment population(Double population) {
        this.setPopulation(population);
        return this;
    }

    public void setPopulation(Double population) {
        this.population = population;
    }

    public Integer getItnsPlanned() {
        return this.itnsPlanned;
    }

    public Assignment itnsPlanned(Integer itnsPlanned) {
        this.setItnsPlanned(itnsPlanned);
        return this;
    }

    public void setItnsPlanned(Integer itnsPlanned) {
        this.itnsPlanned = itnsPlanned;
    }

    public Integer getTargetType() {
        return this.targetType;
    }

    public Assignment targetType(Integer targetType) {
        this.setTargetType(targetType);
        return this;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Assignment longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Assignment latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Instant getStartDayDate() {
        return this.startDayDate;
    }

    public Assignment startDayDate(Instant startDayDate) {
        this.setStartDayDate(startDayDate);
        return this;
    }

    public void setStartDayDate(Instant startDayDate) {
        this.startDayDate = startDayDate;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Assignment activity(Activity activity) {
        this.setActivity(activity);
        return this;
    }

    public MVillagesLocations getLocation() {
        return this.location;
    }

    public void setLocation(MVillagesLocations mVillagesLocations) {
        this.location = mVillagesLocations;
    }

    public Assignment location(MVillagesLocations mVillagesLocations) {
        this.setLocation(mVillagesLocations);
        return this;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Assignment team(Team team) {
        this.setTeam(team);
        return this;
    }

    public Warehouse getWarehouse() {
        return this.warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Assignment warehouse(Warehouse warehouse) {
        this.setWarehouse(warehouse);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assignment)) {
            return false;
        }
        return getId() != null && getId().equals(((Assignment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assignment{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", phaseNo=" + getPhaseNo() +
            ", code='" + getCode() + "'" +
            ", districtCode=" + getDistrictCode() +
            ", gov='" + getGov() + "'" +
            ", district='" + getDistrict() + "'" +
            ", subdistrict='" + getSubdistrict() + "'" +
            ", village='" + getVillage() + "'" +
            ", subvillage='" + getSubvillage() + "'" +
            ", ppdName='" + getPpdName() + "'" +
            ", dayId=" + getDayId() +
            ", population=" + getPopulation() +
            ", itnsPlanned=" + getItnsPlanned() +
            ", targetType=" + getTargetType() +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            ", startDayDate='" + getStartDayDate() + "'" +
            "}";
    }
}
