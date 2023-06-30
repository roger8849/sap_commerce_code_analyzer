package co.edu.unal.data;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class AnalysisConfigurationData {

  private static AnalysisConfigurationData instance;
  private String hybrisPath;
  private String resultPath;
  private String databaseKeywordsPath;
  private String databaseKeywordsEngineName;
  private String analysisName;
  private boolean analyzeObsoleteExtensions;
  private boolean analyzeSAPCommerceIncompatibilities;
  private boolean analyzeThirdPartyExtensions;
  private boolean analyzeDatabase;
  private boolean analyzeJava;
  private boolean analyzeJavascript;

  private Set<File> propertiesPaths;

  private Set<File> jarPaths;

  private AnalysisConfigurationData() {
    this.hybrisPath = StringUtils.EMPTY;
    this.resultPath = StringUtils.EMPTY;
    this.databaseKeywordsPath = StringUtils.EMPTY;
    this.databaseKeywordsEngineName = StringUtils.EMPTY;
    this.analysisName = StringUtils.EMPTY;
    this.analyzeObsoleteExtensions = Boolean.TRUE;
    this.analyzeSAPCommerceIncompatibilities = Boolean.TRUE;
    this.analyzeThirdPartyExtensions = Boolean.TRUE;
    this.analyzeDatabase = Boolean.TRUE;
    this.analyzeJava = Boolean.TRUE;
    this.analyzeJavascript = Boolean.TRUE;
    this.propertiesPaths = new HashSet<>();
    this.jarPaths = new HashSet<>();
  }

  public static synchronized AnalysisConfigurationData getInstance() {
    if (Objects.isNull(instance)) {
      instance = new AnalysisConfigurationData();
    }
    return instance;
  }

  public Path getHybrisPath() {
    return Paths.get(this.getHybrisPathToString());
  }

  public String getHybrisPathToString() {
    return this.hybrisPath;
  }

  public void setHybrisPath(String hybrisPath) {
    this.hybrisPath = hybrisPath;
  }

  public String getResultPath() {
    return this.resultPath;
  }

  public void setResultPath(String resultPath) {
    this.resultPath = resultPath;
  }

  public String getDatabaseKeywordsPath() {
    return this.databaseKeywordsPath;
  }

  public void setDatabaseKeywordsPath(String databaseKeywordsPath) {
    this.databaseKeywordsPath = databaseKeywordsPath;
  }

  public String getDatabaseKeywordsEngineName() {
    return this.databaseKeywordsEngineName;
  }

  public void setDatabaseKeywordsEngineName(String databaseKeywordsEngineName) {
    this.databaseKeywordsEngineName = databaseKeywordsEngineName;
  }

  public String getAnalysisName() {
    return this.analysisName;
  }

  public void setAnalysisName(String analysisName) {
    this.analysisName = analysisName;
  }

  public boolean shouldAnalyzeObsoleteExtensions() {
    return analyzeObsoleteExtensions;
  }

  public void setAnalyzeObsoleteExtensions(boolean analyzeObsoleteExtensions) {
    this.analyzeObsoleteExtensions = analyzeObsoleteExtensions;
  }

  public boolean shouldAnalyzeSAPCommerceIncompatibilities() {
    return analyzeSAPCommerceIncompatibilities;
  }

  public void setAnalyzeSAPCommerceIncompatibilities(boolean analyzeSAPCommerceIncompatibilities) {
    this.analyzeSAPCommerceIncompatibilities = analyzeSAPCommerceIncompatibilities;
  }

  public boolean shouldAnalyzeThirdPartyExtensions() {
    return analyzeThirdPartyExtensions;
  }

  public void setAnalyzeThirdPartyExtensions(boolean analyzeThirdPartyExtensions) {
    this.analyzeThirdPartyExtensions = analyzeThirdPartyExtensions;
  }

  public boolean shouldAnalyzeDatabase() {
    return analyzeDatabase;
  }

  public void setAnalyzeDatabase(boolean analyzeDatabase) {
    this.analyzeDatabase = analyzeDatabase;
  }

  public boolean shouldAnalyzeJava() {
    return analyzeJava;
  }

  public void setAnalyzeJava(boolean analyzeJava) {
    this.analyzeJava = analyzeJava;
  }

  public boolean shouldAnalyzeJavascript() {
    return analyzeJavascript;
  }

  public void setAnalyzeJavascript(boolean analyzeJavascript) {
    this.analyzeJavascript = analyzeJavascript;
  }

  public Set<File> getPropertiesPaths() {
    return propertiesPaths;
  }

  public void setPropertiesPaths(Set<File> propertiesPaths) {
    this.propertiesPaths = propertiesPaths;
  }

  public Set<File> getJarPaths() {
    return jarPaths;
  }

  public void setJarPaths(Set<File> jarPaths) {
    this.jarPaths = jarPaths;
  }
}
