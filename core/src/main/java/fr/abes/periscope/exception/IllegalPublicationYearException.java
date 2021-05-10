package fr.abes.periscope.exception;

/**
 * Si les années de publication sont abérantes
 */
public class IllegalPublicationYearException extends IllegalArgumentException {

    public IllegalPublicationYearException(final String msg) {
        super(msg);
    }
}
