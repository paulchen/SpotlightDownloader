package at.rueckgr.spotlight.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SpotlightException extends RuntimeException {
    public SpotlightException(final String message) {
        super(message);
    }

    public SpotlightException(final Exception e) {
        super(e);
    }
}
