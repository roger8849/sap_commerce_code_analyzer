package co.edu.unal.analyzer.impl;

import static co.edu.unal.utilities.ReportUtils.REPORT_FILE_KEY;

import co.edu.unal.analyzer.Analyzer;
import co.edu.unal.data.AnalysisConfigurationData;
import co.edu.unal.data.RuleViolationData;
import co.edu.unal.utilities.CSVUtils;
import co.edu.unal.utilities.ConsoleUtils;
import co.edu.unal.utilities.ReportUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import net.sourceforge.pmd.RulePriority;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommerceIncompatibilitiesAnalyzer implements Analyzer {

  private static final Logger log = LoggerFactory.getLogger(
      CommerceIncompatibilitiesAnalyzer.class);

  private static final String MANAGED_PROPERTIES_CONFIG_FILE_PATH = new StringBuilder(
      StringUtils.EMPTY)
      .append(Paths.get(StringUtils.EMPTY).toAbsolutePath())
      .append(File.separatorChar)
      .append("..")
      .append(File.separatorChar)
      .append("commerce-incompatibilities-analysis")
      .append(File.separatorChar)
      .append("src")
      .append(File.separatorChar)
      .append("main")
      .append(File.separatorChar)
      .append("resources")
      .append(File.separatorChar)
      .append("commerce.managed.properties").toString();
  private Set<String> managedProperties;

  private void initManagedProperties() {
    try (InputStream input = new FileInputStream(MANAGED_PROPERTIES_CONFIG_FILE_PATH)) {
      Properties managedProperties = new Properties();
      managedProperties.load(input);
      this.managedProperties = managedProperties.keySet().stream().map(Object::toString).collect(
          Collectors.toSet());
    } catch (IOException exception) {
      log.error("Error reading commerce managed properties", exception);
    }
  }

  private static Properties getPropertyFromLine(String line) {
    Properties prop = new Properties();
    try {
      prop.load(new StringReader(line));
    } catch (IOException e) {
      log.error("error loading property line {}", line, e);
    }
    return prop;
  }

  private void validateDuplicates(final Set<String> propertiesInFile, final String propKey,
      final File file,
      final int line, List<RuleViolationData> violations) {
    if (propertiesInFile.contains(propKey)) {
      violations.add(new RuleViolationData()
          .setFile(file.getAbsolutePath())
          .setPriority(String.valueOf(RulePriority.MEDIUM_HIGH.getPriority()))
          .setLine(String.valueOf(line))
          .setDescription(String.format(
              "Properties file contains a duplicated property %s",
              propKey))
          .setRuleSet("Commerce Incompatibilities").setRule("ValidateDuplicates in file"));
    }
  }

  private void validateSensitiveKey(final String propKey, final File file,
      final int line, List<RuleViolationData> violations) {
    List<String> sensitiveKeys = Arrays.asList(".userid", ".username", ".password", ".user", ".id",
        ".key", ".pwd");
    for (String sensitive : sensitiveKeys) {
      if (StringUtils.containsIgnoreCase(propKey, sensitive)) {
        violations.add(new RuleViolationData()
            .setFile(file.getAbsolutePath())
            .setPriority(String.valueOf(RulePriority.MEDIUM_HIGH.getPriority()))
            .setLine(String.valueOf(line))
            .setDescription(String.format(
                "Property '%s' contains the key '%s' which seems to contains sensitive information",
                propKey, sensitive))
            .setRuleSet("Commerce Incompatibilities").setRule("ValidateSensitiveKey"));
      }
    }
  }

  private void validateManagedProperty(final String propKey, final File file,
      final int line, List<RuleViolationData> violations) {
    if (this.managedProperties.contains(propKey)) {
      violations.add(new RuleViolationData()
          .setFile(file.getAbsolutePath())
          .setPriority(String.valueOf(RulePriority.HIGH.getPriority()))
          .setLine(String.valueOf(line))
          .setDescription(String.format(
              "Properties file contains a managed property '%s'.",
              propKey))
          .setRuleSet("Commerce Incompatibilities").setRule("ManagedPropertiesValidation"));
    }
  }

  public void analyzePropertiesFile(File propFile, List<RuleViolationData> violations) {
    try (BufferedReader reader = new BufferedReader(new FileReader(propFile))) {
      int lineCount = 1;
      String line = reader.readLine();
      Set<String> propertiesInFile = new HashSet<>();
      while (Objects.nonNull(line)) {
        Properties lineProp = getPropertyFromLine(line);
        if (Objects.nonNull(lineProp) && lineProp.size() > 0) {
          // Line prop only contain a single property.
          final String propKey = (String) lineProp.keySet().stream().findFirst().get();
          this.validateDuplicates(propertiesInFile, propKey, propFile, lineCount, violations);
          propertiesInFile.add(propKey);
          this.validateManagedProperty(propKey, propFile, lineCount, violations);
          this.validateSensitiveKey(propKey, propFile, lineCount, violations);
        }
        line = reader.readLine();
        lineCount++;
      }
    } catch (IOException exception) {
      log.error("Error reading the properties file {}", propFile.getName(), exception);
    }
  }

  @Override
  public void analyze() {
    ConsoleUtils.printImportantAnalysisMessage("Starting commerce incompatibilities analysis.",
        '*');

    final Map<String, String> configuration = (Map<String, String>) getAnalyzerConfiguration();
    final Set<File> propertiesFiles = AnalysisConfigurationData.getInstance()
        .getPropertiesPaths();
    log.info("Analyzing {} found properties files.", CollectionUtils.size(propertiesFiles));
    final List<RuleViolationData> violations = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(propertiesFiles)) {
      propertiesFiles.stream().forEach(prop -> this.analyzePropertiesFile(prop, violations));
    }
    CSVUtils.writeRuleViolationToCSV(configuration.get(ReportUtils.REPORT_FILE_KEY), violations);
    ConsoleUtils.printImportantAnalysisMessage("Commerce incompatibilities analysis ended.",
        '*');
  }

  @Override
  public Object getAnalyzerConfiguration() {
    this.initManagedProperties();
    Map<String, String> configurationMap = new HashMap<>();
    final String reportFile = new StringBuilder(ReportUtils.getReportFullPath()).append(
        ReportUtils.INCOMPATIBILITIES_REPORT_FILENAME).toString();
    configurationMap.put(REPORT_FILE_KEY, reportFile);
    return configurationMap;
  }
}
