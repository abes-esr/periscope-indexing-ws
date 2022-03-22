package fr.abes.periscope.entity;

import fr.abes.periscope.exception.IllegalPublicationYearException;
import lombok.Getter;
import lombok.Setter;

/**
 * Représente une année de publication avec un indice de confiance
 */
@Getter
@Setter
public class PublicationYear {

    /** Année de publication */
    private String year;

    /**
     * Indice de confiance entre 0 et 100.
     * La valeur 100 indique une confiance absolue sur l'année de publication
     * La valeur 0 indique une confiance très médiocre sur l'année de publication
     */
    private Integer confidenceIndex;

    /**
     * Constructeur d'une année de publication sans indice de confiance.
     * L'indice vaut par défaut 0
     */
    public PublicationYear() {
        this.year=null;
        this.confidenceIndex = 0;
    }

    /**
     * Constructeur d'une année de publication avec un indice confiance
     * @param candidateYear Année de publication
     * @param candidateConfidenceIndex Indice de confiance
     */
    public PublicationYear(String candidateYear, int candidateConfidenceIndex) {
        this.year = candidateYear;
        if (candidateConfidenceIndex < 0 || candidateConfidenceIndex > 100) {
            throw new IllegalPublicationYearException("Confidence index is out of range [0:100]");
        }
        this.confidenceIndex = candidateConfidenceIndex;
    }

    @Override
    public String toString() {
        return "PublicationYear {year="+ year +"("+ confidenceIndex +")}";
    }
}
