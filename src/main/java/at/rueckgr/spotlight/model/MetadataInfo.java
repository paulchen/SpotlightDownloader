package at.rueckgr.spotlight.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetadataInfo {
    private String hash;
    private String filename;
    private LocaleInfo locale;
}
