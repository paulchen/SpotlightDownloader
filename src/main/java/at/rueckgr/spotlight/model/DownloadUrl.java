package at.rueckgr.spotlight.model;

import lombok.Data;

import java.util.Locale;

@Data
public class DownloadUrl {
    private final String url;
    private final Locale locale;
}
