package fr.abes.periscope.entity.solr;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.solr.core.mapping.ChildDocument;

import java.util.HashSet;
import java.util.Set;

/**
 * Représente une notice au format d'indexation SolR V2
 */
@NoArgsConstructor
@Getter @Setter
public class NoticeSolrExtended extends NoticeSolr {

    @ChildDocument
    protected Set<SpecimenSolr> specimens = new HashSet<>();

    public void addSpecimen(SpecimenSolr specimen) {
        this.specimens.add(specimen);
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

        return this.ppn != null && ppn.equals(((NoticeSolrExtended) obj).ppn);
    }

    @Override
    public int hashCode() {
        return 2020;
    }

    @Override
    public String toString() {
        return "NoticeSolrExtended {"+ "ppn="+ ppn+", issn="+issn+" , specimens="+specimens+"}";
    }
}
