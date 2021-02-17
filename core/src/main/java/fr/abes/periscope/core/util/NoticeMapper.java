package fr.abes.periscope.core.util;

import fr.abes.periscope.core.entity.solr.NoticeSolr;
import fr.abes.periscope.core.entity.xml.DataField;
import fr.abes.periscope.core.entity.xml.NoticeXml;
import fr.abes.periscope.core.entity.xml.SubField;
import fr.abes.periscope.core.exception.IllegalOperatorException;
import fr.abes.periscope.core.exception.IllegalPublicationYearException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NoticeMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Fonction de mapping générique pour des listes
     *
     * @param source      Liste source
     * @param targetClass Classe des objets cibles
     * @return Liste des objets cibles
     */
    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    /**
     * Fonction de mapping générique pour un objet
     *
     * @param source      Objet source
     * @param targetClass Classe de l'objet cible
     * @return Objet cible
     */
    public <S, T> T map(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }

    /**
     * Convertisseur pour les notices XML vers les notices SolR
     */
    @Bean
    public void converterNoticeXML() {

        Converter<NoticeXml, NoticeSolr> myConverter = new Converter<NoticeXml, NoticeSolr>() {

            public NoticeSolr convert(MappingContext<NoticeXml, NoticeSolr> context) {
                NoticeXml source = context.getSource();
                NoticeSolr target = new NoticeSolr();
                try {

                    // Champs PPN
                    target.setPpn(source.getControlFields().stream().filter(elm -> elm.getTag().equalsIgnoreCase("001")).findFirst().orElseThrow().getValue());

                    // Champs IISN
                    Iterator<DataField> iterator = source.getDataFields().iterator();
                    while (iterator.hasNext()) {
                        DataField dataField = iterator.next();
                        if (dataField.getTag().equalsIgnoreCase("035")) {

                            Iterator<SubField> subFieldIterator = dataField.getSubFields().iterator();
                            while (subFieldIterator.hasNext()) {
                                SubField subField = subFieldIterator.next();

                                if (subField.getCode().equalsIgnoreCase("a") && subField.getValue().contains("issn")) {
                                    target.setIssn(subField.getValue().substring(4));
                                }
                            }
                        }
                    }

                    // Champs

                    return target;
                } catch (Exception ex) {
                    throw new IllegalOperatorException(ex.getLocalizedMessage());
                }

            }
        };
        modelMapper.addConverter(myConverter);
    }

}
