package com.hometest.data;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pupil implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double lat;
    private double lon;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pupil_id")
    private List<Grade> grades;

    @ManyToMany
    @JoinTable(
            name = "friendship",
            joinColumns = @JoinColumn(name = "pupil_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<Pupil> friends = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school; // Reference to the school the pupil is enrolled in

    public Pupil(String name, double lat, double lon, List<Grade> grades) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "Pupil{" +
                "name='" + name + '\'' +
                ", latitude=" + lat +
                ", longitude=" + lon +
                // Omitting grades from the toString() method
                '}';
    }
}
