package ch.wisv.domain.course;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Created by Tom on 14/05/2017.
 */
@Data
@Entity
public class Instructor {

    @Id @GeneratedValue
    private long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String mail;

    @ManyToMany(mappedBy = "instructors")
    private List<Course> courses;

    public Instructor(String name, String mail) {
        this.name = name;
        this.mail = mail;
    }

    public Instructor() {
    }
}
