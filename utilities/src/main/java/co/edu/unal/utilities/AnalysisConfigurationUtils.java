package co.edu.unal.utilities;

import co.edu.unal.data.AnalysisConfigurationData;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class AnalysisConfigurationUtils {

  private static final String BASE_DATABASE_KEYWORDS_PATH =
      new StringBuilder(StringUtils.EMPTY)
          .append(Paths.get(StringUtils.EMPTY).toAbsolutePath())
          .append(File.separatorChar)
          .append("..")
          .append(File.separatorChar)
          .append("database-keyword-commerce-analysis")
          .append(File.separatorChar)
          .append("src")
          .append(File.separatorChar)
          .append("main")
          .append(File.separatorChar)
          .append("resources")
          .append(File.separatorChar)
          .append("keywords").toString();

  protected static void setDatabaseKeywordsPathFromConsole() {
    ConsoleUtils.printImportantAnalysisMessage("Which database keywords do you want to detect.",
        '-');
    File[] directories = new File(BASE_DATABASE_KEYWORDS_PATH).listFiles(File::isDirectory);
    int count = 1;
    StringBuilder message = new StringBuilder(System.lineSeparator());
    for (File dir : directories) {
      message.append(count).append(". ").append(dir.getName()).append(".")
          .append(System.lineSeparator());
      count++;
    }
    final int databaseChosen =
        ConsoleUtils.readNumberOptionFromConsole(message.toString(), 1, directories.length) - 1;
    AnalysisConfigurationData configurationData = AnalysisConfigurationData.getInstance();
    configurationData.setDatabaseKeywordsPath(directories[databaseChosen].getAbsolutePath());
    configurationData.setDatabaseKeywordsEngineName(
        StringUtils.upperCase(directories[databaseChosen].getName()));
  }

  protected static void setCustomFilesPath() {
    List<String> extensions = new ArrayList<>();

    if (AnalysisConfigurationData.getInstance().shouldAnalyzeThirdPartyExtensions()) {
      extensions.add("properties");
    }
    if (AnalysisConfigurationData.getInstance().shouldAnalyzeSAPCommerceIncompatibilities()) {
      extensions.add("jar");
    }

    final File rootDirectory = new File(
        AnalysisConfigurationData.getInstance().getHybrisPathToString());
    Collection<File> files = FileUtils.listFiles(rootDirectory, extensions.toArray(new String[0]),
        Boolean.TRUE);
    if (CollectionUtils.isNotEmpty(files)) {
      AnalysisConfigurationData.getInstance().
          setPropertiesPaths(files.stream().filter(
              file -> StringUtils.equalsIgnoreCase("properties",
                  FilenameUtils.getExtension(file.getName()))).collect(
              Collectors.toSet()));
      AnalysisConfigurationData.getInstance().
          setJarPaths(files.stream().filter(file -> StringUtils.equalsIgnoreCase("jar",
              FilenameUtils.getExtension(file.getName()))).collect(
              Collectors.toSet()));
    }
  }


}
