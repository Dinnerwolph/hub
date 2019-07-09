package net.euphalys.hub.managers;

import net.euphalys.hub.Hub;

/**
 * @author Dinnerwolph
 */

public abstract class AbstractManager implements EntryPoints {
    protected final Hub hub;
    private String filename;

    public AbstractManager(final Hub hub, final String filename) {
        this(hub);
        this.filename = filename;
    }

    public AbstractManager(final Hub hub) {
        this.hub = hub;
    }
}
