package com.hometest.data;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class School implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double lat;
    private double lon;
    private int minimumGpa;
    private int maxNumberOfPupils;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    private List<Pupil> enrolledPupils = new ArrayList<>();

    public School(String name, double lat, double lon, int minimumGpa, int maxNumberOfPupils) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.minimumGpa = minimumGpa;
        this.maxNumberOfPupils = maxNumberOfPupils;
    }
}
