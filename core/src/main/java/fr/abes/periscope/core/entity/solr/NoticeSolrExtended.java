package fr.abes.periscope.core.entity.solr;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.solr.core.mapping.ChildDocument;

import java.util.List;

/**
 * Représente une notice au format d'indexation SolR V2
 */
@NoArgsConstructor
@Getter @Setter
public class NoticeSolrExtended extends NoticeSolr {

    @ChildDocument
    private List<SpecimenSolr> specimens;

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

        return this.ppn != null && ppn.equals(((NoticeSolrExtended) obj).ppn);
    }

    @Override
    public int hashCode() {
        return 2020;
    }

    @Override
    public String toString() {
        return "NoticeSolr {"+ "ppn="+ ppn+", issn="+issn+"}";
    }
}
