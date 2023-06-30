package co.edu.unal.utilities;

import co.edu.unal.data.AnalysisConfigurationData;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;

public class ReportUtils {

  private static String FOLDER_TIMESTAMP = StringUtils.EMPTY;
  private static final String REPORT = "report";
  private static final String ANALYSIS = "analysis";
  public static final String REPORT_FILE_KEY = "reportFile";


  public static final String DATABASE_REPORT_FILENAME = "database_report.csv";
  public static final String DATABASE_REPORT_CACHE = "database_cache";
  public static final String INCOMPATIBILITIES_REPORT_FILENAME = "incompatibilities_report.csv";
  public static final String INCOMPATIBILITIES_REPORT_CACHE = "incompatibilities_cache";
  public static final String DEPRECATED_EXTENSIONS_REPORT_FILENAME = "deprecated_extensions_report.csv";
  public static final String DEPRECATED_EXTENSIONS_REPORT_CACHE = "deprecated_extensions_cache";
  public static final String THIRD_PARTY_REPORT_FILENAME = "third_party_libraries_report.csv";
  public static final String THIRD_PARTY_REPORT_CACHE = "third_party_libraries_cache";
  public static final String JAVA_REPORT_FILENAME = "java_report.csv";
  public static final String JAVA_REPORT_CACHE = "java_cache";
  public static final String JAVASCRIPT_REPORT_FILENAME = "javascript_report.csv";
  public static final String JAVASCRIPT_REPORT_CACHE = "javascript_cache";

  private static final String REPORT_DIR =
      new StringBuilder(StringUtils.EMPTY)
          .append(File.separatorChar)
          .append(REPORT)
          .append('s')
          .append(File.separatorChar).toString();

  public static String getReportFullPath() {
    final StringBuilder fullPath = new StringBuilder(getReportHome()).append(getReportFolderName())
        .append(File.separatorChar);
    File reportDir = new File(fullPath.toString());
    if (!reportDir.exists()) {
      reportDir.mkdir();
    }
    return fullPath.toString();
  }

  public static String getCacheFolder() {
    final StringBuilder cacheFolderPath = new StringBuilder(getReportHome()).append("cache")
        .append(File.separatorChar);
    final File cacheDir = new File(cacheFolderPath.toString());
    if (!cacheDir.exists()) {
      cacheDir.mkdir();
    }
    return cacheFolderPath.toString();
  }

  public static String getReportFolderName() {
    final StringBuilder folderName;
    if (StringUtils.isNotBlank(AnalysisConfigurationData.getInstance().getAnalysisName())) {
      folderName = new StringBuilder(
          AnalysisConfigurationData.getInstance().getAnalysisName()).append('-');
    } else {
      folderName = new StringBuilder(ANALYSIS).append('-');

    }
    return folderName.append(REPORT).append('-').append(getCurrentDateFormat()).toString();
  }

  private static String getReportHome() {
    final StringBuilder reportHome;
    if (StringUtils.isNotBlank(AnalysisConfigurationData.getInstance().getResultPath())) {
      reportHome = new StringBuilder(AnalysisConfigurationData.getInstance().getResultPath());
    } else {
      reportHome = new StringBuilder(System.getProperty("user.home"));
    }
    reportHome.append(REPORT_DIR).append(File.separatorChar);
    File reportDir = new File(reportHome.toString());
    if (!reportDir.exists()) {
      reportDir.mkdir();
    }
    return reportHome.toString();
  }

  private static String getCurrentDateFormat() {
    if (StringUtils.isBlank(FOLDER_TIMESTAMP)) {
      LocalDateTime now = LocalDateTime.now();
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyymmddHHmmss");
      FOLDER_TIMESTAMP = dtf.format(now);
    }
    return FOLDER_TIMESTAMP;
  }
}
