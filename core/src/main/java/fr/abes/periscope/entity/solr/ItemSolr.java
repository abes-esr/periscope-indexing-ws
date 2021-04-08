package fr.abes.periscope.entity.solr;

import lombok.Getter;
import lombok.Setter;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@SolrDocument
public class ItemSolr {

    @Id
    @Field(ItemSolrField.ID)
    @Indexed(name = ItemSolrField.ID)
    private String id;

    @Field(ItemSolrField.EPN)
    @Indexed(name = ItemSolrField.EPN)
    private String epn;

    @Field(NoticeSolrField.PPN)
    @Indexed(name = NoticeSolrField.PPN)
    private String ppn;

    @Field(NoticeSolrField.TITLE_TYPE)
    @Indexed(name = NoticeSolrField.TITLE_TYPE)
    private String type = "exemplaire";

    @Field(ItemSolrField.RCR)
    @Indexed(name = ItemSolrField.RCR)
    private String rcr;

    @Field(ItemSolrField.PCP)
    @Indexed(name = ItemSolrField.PCP)
    private List<String> pcp = new ArrayList<>();

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
        return "ItemSolr {"+ "id="+ id+", rcr="+rcr+"}";
    }
}
