package fr.abes.periscope.core.exception;

/**
 * Si le critère n'est pas valide
 */
public class MissingFieldException extends Exception {

    public MissingFieldException(final String msg) {
        super(msg);
    }
}
