package fr.abes.periscope.core.entity.solr;

import fr.abes.periscope.core.repository.solr.NoticeField;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;

public class SpecimenSolr {

    @Id
    @Indexed(name = NoticeField.PPN_Z, type = "string")
    private String id;
}
