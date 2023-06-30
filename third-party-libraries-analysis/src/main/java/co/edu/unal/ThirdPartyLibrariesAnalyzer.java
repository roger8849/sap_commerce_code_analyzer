package co.edu.unal;

import co.edu.unal.analyzer.Analyzer;
import co.edu.unal.data.AnalysisConfigurationData;
import co.edu.unal.data.RuleViolationData;
import co.edu.unal.utilities.CSVUtils;
import co.edu.unal.utilities.ConsoleUtils;
import co.edu.unal.utilities.ReportUtils;
import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarFile;
import net.sourceforge.pmd.RulePriority;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThirdPartyLibrariesAnalyzer implements Analyzer {

  private static final Logger log = LoggerFactory.getLogger(ThirdPartyLibrariesAnalyzer.class);
  public static final String THIRD_PARTY_LIBRARIES = "Third party libraries";

  public static String getVersionFromMavenCentral(final String symbolicName) {
    final String TARGET_URL = new StringBuilder()
        .append("https://search.maven.org/solrsearch/select?q=text:")
        .append(symbolicName).toString();
    String mavenCentralVersion = StringUtils.EMPTY;
    try {
      URI targetURI = new URI(TARGET_URL);
      HttpRequest httpRequest = HttpRequest.newBuilder()
          .uri(targetURI)
          .GET()
          .build();
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpResponse<String> response = httpClient.send(httpRequest,
          HttpResponse.BodyHandlers.ofString());
      final String responseBody = response.body();
      if (StringUtils.isNotEmpty(responseBody)) {
        mavenCentralVersion = getMavenCentralVersionFromJSONString(responseBody);
      }
    } catch (Exception e) {
      log.error("Error getting information from maven central for jar {}", symbolicName);
    }
    return mavenCentralVersion;
  }

  private static String getMavenCentralVersionFromJSONString(String responseBody) {
    String mavenCentralVersion = StringUtils.EMPTY;
    JSONObject rootObject = new JSONObject(responseBody);
    if (Objects.nonNull(rootObject)) {
      JSONObject response = rootObject.getJSONObject("response");
      if (Objects.nonNull(response)) {
        JSONArray docs = response.getJSONArray("docs");
        if (Objects.nonNull(docs) && !docs.isEmpty()) {
          mavenCentralVersion = docs.getJSONObject(0).getString("latestVersion");
        }
      }
    }
    return mavenCentralVersion;
  }

  public static void analyzeJar(File jarFile, List<RuleViolationData> violations) {
    String currentVersion = StringUtils.EMPTY;
    String mavenCentralVersion = StringUtils.EMPTY;
    try {
      // Create a jar file
      JarFile jar = new JarFile(jarFile);

      // Get the manifest
      // String manifest = jar.getManifest().getMainAttributes().getValue("Manifest-Version");
      // final String symbolicName = jar.getManifest().getMainAttributes()
      //     .getValue("Bundle-SymbolicName");
      final String symbolicName = FilenameUtils.getBaseName(jar.getName());
      if (StringUtils.isNotBlank(
          jar.getManifest().getMainAttributes().getValue("Implementation-Version"))) {
        currentVersion = jar.getManifest().getMainAttributes().getValue("Implementation-Version");
      } else if (StringUtils.isNotBlank(
          jar.getManifest().getMainAttributes().getValue("Bundle-Version"))) {
        currentVersion = jar.getManifest().getMainAttributes().getValue("Bundle-Version");
      }
      if (StringUtils.isNotBlank(currentVersion)) {
        mavenCentralVersion = getVersionFromMavenCentral(symbolicName);
        // Semver jarVersion = new Semver(currentVersion);
        // Semver mavenVersion = new Semver(mavenCentralVersion);
        ComparableVersion jarVersion = new ComparableVersion(currentVersion);

        if (StringUtils.isNotBlank(mavenCentralVersion)) {
          ComparableVersion mavenVersion = new ComparableVersion(mavenCentralVersion);
          if (Objects.nonNull(mavenVersion) && mavenVersion.compareTo(jarVersion) > 0) {
            violations.add(new RuleViolationData()
                .setPriority(String.valueOf(RulePriority.MEDIUM_HIGH.getPriority()))
                .setFile(jarFile.getAbsolutePath())
                .setDescription(String.format(
                    "Newer jar version %s found in maven central for jar %s. Current version: %s",
                    mavenCentralVersion, symbolicName, currentVersion))
                .setRuleSet(THIRD_PARTY_LIBRARIES)
                .setRule("NewerJarVersionInMavenCentral"));
          }
        } else {
          violations.add(new RuleViolationData()
              .setPriority(String.valueOf(RulePriority.LOW.getPriority()))
              .setFile(jarFile.getAbsolutePath())
              .setDescription(String.format(
                  "Jar with filename %s not found in maven central.",
                  symbolicName))
              .setRuleSet(THIRD_PARTY_LIBRARIES)
              .setRule("JarVersionNotFoundInMavenCentral"));
        }
      } else {
        violations.add(new RuleViolationData()
            .setPriority(String.valueOf(RulePriority.LOW.getPriority()))
            .setFile(jarFile.getAbsolutePath())
            .setDescription(
                String.format("Jar with name %s couldn't find the version in its manifest.",
                    symbolicName))
            .setRuleSet(THIRD_PARTY_LIBRARIES)
            .setRule("JarVersionNotInJarManifest"));
      }
    } catch (Exception e) {
      log.error("Error getting manifest file {}", jarFile.getName(), e);
    }

  }

  @Override
  public void analyze() {
    ConsoleUtils.printImportantAnalysisMessage("Starting third party libraries analysis.", '*');
    final Set<File> jarFiles = AnalysisConfigurationData.getInstance().getJarPaths();
    log.info("Analyzing {} found jar files.", CollectionUtils.size(jarFiles));
    List<RuleViolationData> violations = new ArrayList<>();
    jarFiles.stream().forEach(jar -> ThirdPartyLibrariesAnalyzer.analyzeJar(jar, violations));
    final Map<String, String> configuration = (Map<String, String>) getAnalyzerConfiguration();
    CSVUtils.writeRuleViolationToCSV(configuration.get(ReportUtils.REPORT_FILE_KEY), violations);
    ConsoleUtils.printImportantAnalysisMessage("Third party libraries analysis ended.", '*');

  }

  @Override
  public Object getAnalyzerConfiguration() {
    Map<String, String> configurationMap = new HashMap<>();
    final String reportFile = new StringBuilder(ReportUtils.getReportFullPath()).append(
        ReportUtils.THIRD_PARTY_REPORT_FILENAME).toString();
    configurationMap.put(ReportUtils.REPORT_FILE_KEY, reportFile);
    return configurationMap;
  }
}