package at.rueckgr.spotlight.service;

import at.rueckgr.spotlight.model.DownloadedImage;
import at.rueckgr.spotlight.model.LocaleInfo;
import at.rueckgr.spotlight.model.MetadataFile;
import at.rueckgr.spotlight.model.MetadataInfo;
import at.rueckgr.spotlight.util.SpotlightException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Setter
public class MetadataService {
    public static final String METADATA_FILENAME = ".meta";
    public Map<String, Locale> fileLocales = new HashMap<>();

    private final LocalesService localesService;
    private DownloadDirectoryService downloadDirectoryService;

    public void loadMetadata() {
        final File metadataFile = new File(downloadDirectoryService.getFolderName() + File.separator + METADATA_FILENAME);
        if (metadataFile.exists()) {
            final Gson gson = new Gson();
            try {
                final MetadataFile metadata = gson.fromJson(new FileReader(metadataFile), MetadataFile.class);
                for (final MetadataInfo downloadedFile : metadata.getDownloadedFiles()) {
                    final String hash = downloadedFile.getHash();

                    final String country = downloadedFile.getLocale().getCountry();
                    final String language = downloadedFile.getLocale().getLanguage();

                    final Locale locale = localesService.getLocale(country, language);

                    if (locale != null) {
                        fileLocales.put(hash, locale);
                    }
                }
            }
            catch (FileNotFoundException e) {
                throw new SpotlightException(e);
            }
        }
    }

    public void saveMetadata() {
        final List<MetadataInfo> downloadedFiles = new ArrayList<>();
        for (final DownloadedImage downloadedImage : downloadDirectoryService.getDownloadedFiles().values()) {
            final String hash = downloadedImage.getHash();
            final Locale locale = downloadedImage.getLocale();
            if (locale == null) {
                continue;
            }
            final LocaleInfo localeInfo = new LocaleInfo(locale.getCountry(), locale.getLanguage());
            final MetadataInfo metadataInfo = new MetadataInfo(hash, downloadedImage.getName(), localeInfo);

            downloadedFiles.add(metadataInfo);
        }
        final MetadataFile metadata = new MetadataFile(downloadedFiles);

        final File metadataFile = new File(downloadDirectoryService.getFolderName() + File.separator + METADATA_FILENAME);
        final Gson gson = new Gson();
        try (final FileWriter writer = new FileWriter(metadataFile)) {
            gson.toJson(metadata, writer);
        }
        catch (IOException e) {
            throw new SpotlightException(e);
        }
    }

    public Locale getLocale(final String hash) {
        return fileLocales.get(hash);
    }
}
