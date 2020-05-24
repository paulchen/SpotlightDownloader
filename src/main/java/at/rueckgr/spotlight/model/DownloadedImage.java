package at.rueckgr.spotlight.model;

import lombok.Data;

import java.io.File;
import java.util.Locale;

@Data
public class DownloadedImage {
    private final File file;
    private final String hash;
    private final Locale locale;

    public String getName() {
        return file.getName();
    }
}
