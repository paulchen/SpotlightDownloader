package at.rueckgr.spotlight.model;

import lombok.Data;

@Data
public class DownloadableImage {
    private final String id;
    private final String url;
    private final String description;
    private final String hash;
    private final DownloadUrl downloadUrl;
}
