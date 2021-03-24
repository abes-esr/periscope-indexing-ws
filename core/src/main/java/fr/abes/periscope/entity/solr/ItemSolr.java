package fr.abes.periscope.entity.solr;

import fr.abes.periscope.repository.solr.NoticeField;
import fr.abes.periscope.repository.solr.ItemField;
import lombok.Getter;
import lombok.Setter;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

@Getter
@Setter
@SolrDocument
public class ItemSolr {

    @Id
    @Field(ItemField.ID)
    @Indexed(name = ItemField.ID, type = ItemField.ID_TYPE)
    private String id;

    @Field(ItemField.EPN)
    @Indexed(name = ItemField.EPN, type = ItemField.EPN_TYPE)
    private String epn;

    @Field(NoticeField.PPN)
    @Indexed(name = NoticeField.PPN, type = NoticeField.PPN_TYPE)
    private String ppn;

    @Field(NoticeField.TITLE_TYPE)
    @Indexed(name = NoticeField.TITLE_TYPE, type = NoticeField.TITLE_TYPE_TYPE)
    private String type = "exemplaire";

    @Field(ItemField.RCR)
    @Indexed(name = ItemField.RCR, type = ItemField.RCR_TYPE)
    private String rcr;

    public ItemSolr(String ppn, String epn) {
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

        return this.id != null && id.equals(((ItemSolr) obj).id);
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
