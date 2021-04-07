package fr.abes.periscope.entity.xml;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Clob;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "NOTICESBIBIO", schema = "AUTORITES")
public class NoticesBibio implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DATA_XML")
    @ColumnTransformer(read = "XMLSERIALIZE (CONTENT data_xml as CLOB)", write = "NULLSAFE_XMLTYPE(?)")
    @Lob
    //Type Clob pour pouvoir récupérer les notices de plus de 4000 caractères
    private Clob dataXml;

}
