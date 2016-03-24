package com.emb.tictactoe.util;

import java.io.Serializable;
import java.util.logging.Level;

/**
 *
 * @author bu
 */
public class LOG implements Serializable {

    private static final java.util.logging.Logger LOG
            = java.util.logging.Logger.getLogger(
                    LOG.class.getName());

    public static void exception(Exception e) {
        if (e != null) {
            LOG.log(Level.WARNING, "{0}", e.getLocalizedMessage());
        } else {
            LOG.log(Level.WARNING, "{0}", e);
        }
    }

    public static void info(String message) {
        LOG.log(Level.INFO, "{0}", message);
    }

    public static void info(String message, Object value) {
        LOG.log(Level.INFO, "{0}: {1}", new Object[]{message, value});
    }

    public static void error(String message) {
        LOG.log(Level.SEVERE, "{0}", message);
    }

    public static void severe(String message) {
        LOG.log(Level.SEVERE, "{0}", message);
    }

    public static void severe(String message, Object value) {
        LOG.log(Level.SEVERE, "{0}: {1}", new Object[]{message, value});
    }

    public static void warning(String message) {
        LOG.log(Level.WARNING, "{0}", message);
    }

    public static void log(String message) {
        LOG.log(Level.INFO, "{0}", message);
    }

    public static void log(String message, Object value) {
        LOG.log(Level.INFO, "{0}: {1}", new Object[]{message, value});
    }

    public static void warning(String message, Object value) {
        LOG.log(Level.WARNING, "{0}: {1}", new Object[]{message, value});
    }

}
