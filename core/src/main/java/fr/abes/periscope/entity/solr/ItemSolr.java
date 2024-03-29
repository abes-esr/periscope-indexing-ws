package fr.abes.periscope.entity.solr;

import lombok.Getter;
import lombok.Setter;
import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.annotations.Index;
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

    @Field(ItemSolrField.PARENT)
    @Indexed(name = ItemSolrField.PARENT)
    private String id_parent;

    @Field(NoticeSolrField.TITLE_TYPE)
    @Indexed(name = NoticeSolrField.TITLE_TYPE)
    private String type = "exemplaire";

    @Field(ItemSolrField.RCR)
    @Indexed(name = ItemSolrField.RCR)
    private String rcr;

    @Field(ItemSolrField.PCP)
    @Indexed(name = ItemSolrField.PCP)
    private List<String> pcp = new ArrayList<>();

    @Field(ItemSolrField.STATUT)
    @Indexed(ItemSolrField.STATUT)
    private String statutBibliotheque;

    public ItemSolr(String id_parent, String epn) {
        this.id = epn;
        this.epn = epn;
        this.id_parent = id_parent; // Lien avec la notice parent
    }

    public void addPcp(String pcp) {
        this.pcp.add(pcp);
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
