package at.rueckgr.spotlight.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MetadataFile {
    private List<MetadataInfo> downloadedFiles;
}
