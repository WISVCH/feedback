package ch.wisv.domain.course;

import ch.wisv.converters.StringCryptoConverter;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Data;

<<<<<<< HEAD
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import java.util.List;

=======
>>>>>>> 1e0dce1 (Add instructor encryption for email and name)
/**
 * Created by Tom on 14/05/2017.
 */
@Data
@Entity
public class Instructor {

    @Id @GeneratedValue
    private long id;

    @NotEmpty
    @Convert(converter = StringCryptoConverter.class)
    private String name;

    @NotEmpty
    @Convert(converter = StringCryptoConverter.class)
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
