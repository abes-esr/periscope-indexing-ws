package fr.abes.periscope.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class PeriscopeIndexDto implements Serializable {
    private String ppn;
    private Date dateIndex;

    public PeriscopeIndexDto(String ppn, Date date) {
        this.ppn = ppn;
        this.dateIndex = date;
    }
}
