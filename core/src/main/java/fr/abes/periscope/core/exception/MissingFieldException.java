package fr.abes.periscope.core.exception;

import com.fasterxml.jackson.databind.node.MissingNode;

import java.util.MissingResourceException;

/**
 * Si le critère n'est pas valide
 */
public class MissingFieldException extends Exception {

    public MissingFieldException(final String msg) {
        super(msg);
    }
}
