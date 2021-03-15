package fr.abes.periscope.util;

import fr.abes.periscope.entity.solr.NoticeSolrExtended;
import fr.abes.periscope.entity.solr.SpecimenSolr;
import fr.abes.periscope.entity.xml.DataField;
import fr.abes.periscope.entity.xml.NoticeXml;
import fr.abes.periscope.entity.xml.SubField;
import fr.abes.periscope.exception.MissingFieldException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.ErrorMessage;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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
     * Convertisseur pour les notices XML vers les notices SolR avec des exemplaires
     */
    @Bean
    public void converterNoticeXML() {

        Converter<NoticeXml, NoticeSolrExtended> myConverter = new Converter<NoticeXml, NoticeSolrExtended>() {

            public NoticeSolrExtended convert(MappingContext<NoticeXml, NoticeSolrExtended> context) {
                NoticeXml source = context.getSource();
                NoticeSolrExtended target = new NoticeSolrExtended();
                try {

                    // ID
                    target.setId(source.getControlFields().stream().filter(elm -> elm.getTag().equalsIgnoreCase("001")).findFirst().orElseThrow().getValue());

                    // Champs PPN
                    target.setPpn(source.getControlFields().stream().filter(elm -> elm.getTag().equalsIgnoreCase("001")).findFirst().orElseThrow().getValue());

                    // Champs data fields
                    Iterator<DataField> iterator = source.getDataFields().iterator();
                    while (iterator.hasNext()) {
                        DataField dataField = iterator.next();

                        // Zone 035
                        if (dataField.getTag().equalsIgnoreCase("035")) {

                            Iterator<SubField> subFieldIterator = dataField.getSubFields().iterator();
                            while (subFieldIterator.hasNext()) {
                                SubField subField = subFieldIterator.next();

                                // zone 035-a
                                if (subField.getCode().equalsIgnoreCase("a") && subField.getValue().contains("issn")) {
                                    target.setIssn(subField.getValue().substring(4));
                                }
                            }
                        }

                        // Zone 200
                        if (dataField.getTag().equalsIgnoreCase("200")) {

                            Iterator<SubField> subFieldIterator = dataField.getSubFields().iterator();
                            while (subFieldIterator.hasNext()) {
                                SubField subField = subFieldIterator.next();

                                // zone 200-a
                                if (subField.getCode().equalsIgnoreCase("a")) {
                                    target.setProperTitle(subField.getValue());
                                }

                                // zone 200-c
                                if (subField.getCode().equalsIgnoreCase("c")) {
                                    target.setTitleFromDifferentAuthor(subField.getValue());
                                }

                                // zone 200-d
                                if (subField.getCode().equalsIgnoreCase("d")) {
                                    target.setParallelTitle(subField.getValue());
                                }

                                // zone 200-e
                                if (subField.getCode().equalsIgnoreCase("e")) {
                                    target.setTitleComplement(subField.getValue());
                                }

                                // zone 200-i
                                if (subField.getCode().equalsIgnoreCase("i")) {
                                    target.setSectionTitle(subField.getValue());
                                }
                            }

                        }

                        // Zone 9XX
                        if (dataField.getTag().startsWith("9")) {

                            // On cherche la sous-zone 5 qui contient le EPN
                            SubField specimenIdField = dataField.getSubFields().stream().filter(elm -> elm.getCode().equalsIgnoreCase("5"))
                                    .findAny().orElse(null);

                            if (specimenIdField == null) {
                                throw new MissingFieldException("Zone "+dataField.getTag()+" doesn't have a subfield code=\"5\"");
                            }

                            String epn = specimenIdField.getValue().split(":")[1];

                            // On récupère l'exemplaire ou on le crée s'il n'existe pas
                            SpecimenSolr specimen = target.getSpecimens().stream().filter(elm -> elm.getId().equalsIgnoreCase(epn))
                                        .findAny().orElse(null);

                            if (specimen == null) {
                                specimen = new SpecimenSolr(target.getPpn(),epn);
                                target.addSpecimen(specimen);
                            }

                            // On itère sur les autres sous-zone
                            Iterator<SubField> subFieldIterator = dataField.getSubFields().iterator();
                            while (subFieldIterator.hasNext()) {
                                SubField subField = subFieldIterator.next();

                                if (dataField.getTag().equalsIgnoreCase("930") && subField.getCode().equalsIgnoreCase("b")) {
                                        specimen.setRcr(subField.getValue());
                                }
                            }
                        }
                    }

                    return target;

                } catch (Exception ex) {
                    throw new MappingException(Arrays.asList(new ErrorMessage(ex.getMessage())));
                }

            }
        };
        modelMapper.addConverter(myConverter);
    }

}
