package at.rueckgr.spotlight.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocaleInfo {
    private String country;
    private String language;
}
