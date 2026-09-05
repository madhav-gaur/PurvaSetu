package com.nerlogistics.backend.entity;

import com.nerlogistics.backend.enums.RoadStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "roads")
public class Road {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 50)
    private String roadNumber;

    @Column(nullable = false, length = 50)
    private String state;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RoadStatus status = RoadStatus.OPEN;

    public Road() {}

    public Road(Long id, String name, String roadNumber, String state, RoadStatus status) {
        this.id = id;
        this.name = name;
        this.roadNumber = roadNumber;
        this.state = state;
        this.status = status != null ? status : RoadStatus.OPEN;
    }

    public static RoadBuilder builder() {
        return new RoadBuilder();
    }

    public static class RoadBuilder {
        private Long id;
        private String name;
        private String roadNumber;
        private String state;
        private RoadStatus status = RoadStatus.OPEN;

        public RoadBuilder id(Long id) { this.id = id; return this; }
        public RoadBuilder name(String name) { this.name = name; return this; }
        public RoadBuilder roadNumber(String roadNumber) { this.roadNumber = roadNumber; return this; }
        public RoadBuilder state(String state) { this.state = state; return this; }
        public RoadBuilder status(RoadStatus status) { this.status = status; return this; }

        public Road build() {
            return new Road(id, name, roadNumber, state, status);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRoadNumber() { return roadNumber; }
    public void setRoadNumber(String roadNumber) { this.roadNumber = roadNumber; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public RoadStatus getStatus() { return status; }
    public void setStatus(RoadStatus status) { this.status = status; }
}
