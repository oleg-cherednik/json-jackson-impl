package foo;

import javax.persistence.Column;
import javax.persistence.Id;

public class Person extends Entity {
    @Id
    public int id;
    @Column
    public String name;

}
