package fr.abes.periscope.entity.solr;

import fr.abes.periscope.repository.solr.NoticeField;
import fr.abes.periscope.repository.solr.SpecimenField;
import lombok.Getter;
import lombok.Setter;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

@Getter
@Setter
@SolrDocument
public class SpecimenSolr {

    @Id
    @Field(SpecimenField.ID)
    @Indexed(name = SpecimenField.ID, type = SpecimenField.ID_TYPE)
    private String id;

    @Field(SpecimenField.EPN)
    @Indexed(name = SpecimenField.EPN, type = SpecimenField.EPN_TYPE)
    private String epn;

    @Field(NoticeField.PPN)
    @Indexed(name = NoticeField.PPN, type = NoticeField.PPN_TYPE)
    private String ppn;

    @Field(NoticeField.TYPE)
    @Indexed(name = NoticeField.TYPE, type = NoticeField.TYPE_TYPE)
    private String type = "exemplaire";

    @Field(SpecimenField.RCR)
    @Indexed(name = SpecimenField.RCR, type = SpecimenField.RCR_TYPE)
    private String rcr;

    public SpecimenSolr(String ppn, String epn) {
        this.id = epn;
        this.epn = epn;

        this.ppn = ppn; // Lien avec la notice parent
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        return this.id != null && id.equals(((SpecimenSolr) obj).id);
    }

    @Override
    public int hashCode() {
        return 2020;
    }

    @Override
    public String toString() {
        return "SpecimenSolr {"+ "id="+ id+", rcr="+rcr+"}";
    }
}
