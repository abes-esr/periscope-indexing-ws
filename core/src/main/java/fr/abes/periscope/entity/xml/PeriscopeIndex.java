package fr.abes.periscope.entity.xml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PERISCOPE_INDEX", schema = "PERISCOPE")
public class PeriscopeIndex implements Serializable {
    @Id
    @Column(name = "PPN")
    private String ppn;

    @Column(name = "DATE_INDEX")
    private Date dateIndex;

    @Override
    public String toString() {
        return "PPN : " + this.ppn + " Date index : " + this.dateIndex;
    }
}
