package co.edu.unal.utilities;

import co.edu.unal.data.AnalysisConfigurationData;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitUtils {

  private static final Logger log = LoggerFactory.getLogger(InitUtils.class);

  public static final String HYBRIS_HOME = "HYBRIS_HOME";
  public static final String LINE_SEPARATOR = System.lineSeparator();

  private static boolean loopFlag;

  private InitUtils() {
    // This is meant to be empty to avoid instantiation of utils class.
  }

  public static boolean init() {
    printWelcomeMessage();
    initRepoPath();
    if (shouldPerformFullAnalysis()) {
      log.info("...Executing full analysis...");
    } else {
      initCustomAnalysis();
    }

    if (AnalysisConfigurationData.getInstance().shouldAnalyzeDatabase()) {
      AnalysisConfigurationUtils.setDatabaseKeywordsPathFromConsole();
    }

    if (AnalysisConfigurationData.getInstance().shouldAnalyzeSAPCommerceIncompatibilities()
        || AnalysisConfigurationData.getInstance()
        .shouldAnalyzeThirdPartyExtensions()) {
      AnalysisConfigurationUtils.setCustomFilesPath();
    }

    return Boolean.TRUE;
  }

  private static void initCustomAnalysis() {
    log.info("...Choose the features you want to execute:...");
    AnalysisConfigurationData.getInstance()
        .setAnalyzeObsoleteExtensions(shouldAnalyzeObsoleteExtensions());
    AnalysisConfigurationData.getInstance()
        .setAnalyzeSAPCommerceIncompatibilities(shouldAnalyzeSAPCommerceIncompatibilities());
    AnalysisConfigurationData.getInstance()
        .setAnalyzeThirdPartyExtensions(shouldAnalyzeThirdPartyExtensions());
    AnalysisConfigurationData.getInstance().setAnalyzeDatabase(shouldAnalyzeDatabase());
    AnalysisConfigurationData.getInstance().setAnalyzeJava(shouldAnalyzeJava());
    AnalysisConfigurationData.getInstance().setAnalyzeJavascript(shouldAnalyzeJavascript());
  }

  private static void printWelcomeMessage() {
    ConsoleUtils.printImportantAnalysisMessage("Welcome to SAP Commerce migration analyzer.", '-');
    ConsoleUtils.printImportantAnalysisMessage(
        "Please enter the following details to proceed with the analysis", '-');
  }

  private static void initRepoPath() {
    shouldUseEnvVariable();
    if (StringUtils.isBlank(AnalysisConfigurationData.getInstance().getHybrisPathToString())) {
      getPathFromConsole();
    }
  }

  private static void getPathFromConsole() {
    loopFlag = true;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    try {
      do {

        log.info("Please enter the absolute path of the hybris dir to analyze: ");
        final String inParam = reader.readLine();
        loopFlag = StringUtils.isBlank(inParam);
        if (loopFlag) {
          log.error("Path cannot be empty. Please try again.");
        } else {
          File hybrisDir = new File(inParam);
          if (Objects.nonNull(hybrisDir) && hybrisDir.isDirectory()) {
            loopFlag = false;
            AnalysisConfigurationData.getInstance().setHybrisPath(inParam);
          } else {
            loopFlag = true;
            log.error("Path provided is not a folder or does not exist. Try again.");
          }
        }
      } while (loopFlag);
    } catch (Exception e) {
      log.error("System error please try again.", e.getMessage());
    }
  }

  private static void shouldUseEnvVariable() {
    final String hybrisHome = System.getenv(HYBRIS_HOME);
    if (StringUtils.isNotBlank(hybrisHome)) {
      final String message = new StringBuilder(HYBRIS_HOME)
          .append(" environment variable found in your system:").append(LINE_SEPARATOR)
          .append(hybrisHome)
          .append("Do you want to use it as the main path to analyze?").append(LINE_SEPARATOR)
          .append("(yes). no: ").toString();
      final String response = ConsoleUtils.readYesOrNoFromConsole(message);
      if ("no".equalsIgnoreCase(response)) {
        AnalysisConfigurationData.getInstance().setHybrisPath(StringUtils.EMPTY);
      } else {
        AnalysisConfigurationData.getInstance().setHybrisPath(hybrisHome);
      }
    }
  }

  private static boolean shouldPerformFullAnalysis() {
    final String message = new StringBuilder("Do you want to do a complete analysis?")
        .append(LINE_SEPARATOR)
        .append("(yes). no.").toString();
    final String response = ConsoleUtils.readYesOrNoFromConsole(message);
    return "yes".equalsIgnoreCase(response) || StringUtils.EMPTY.equals(response);
  }

  private static boolean shouldAnalyzeObsoleteExtensions() {
    final String message = new StringBuilder(
        "Do you want to analyze usage of deprecated extensions?")
        .append(LINE_SEPARATOR)
        .append("(yes). no.").toString();
    final String response = ConsoleUtils.readYesOrNoFromConsole(message);
    return "yes".equalsIgnoreCase(response) || StringUtils.EMPTY.equals(response);
  }

  private static boolean shouldAnalyzeSAPCommerceIncompatibilities() {
    final String message = new StringBuilder(
        "Do you want to analyze SAP Commerce cloud incompatible characteristics?")
        .append(LINE_SEPARATOR)
        .append("(yes). no.").toString();
    final String response = ConsoleUtils.readYesOrNoFromConsole(message);
    return "yes".equalsIgnoreCase(response) || StringUtils.EMPTY.equals(response);
  }

  private static boolean shouldAnalyzeThirdPartyExtensions() {
    final String message = new StringBuilder(
        "Do you want to analyze the usage of third party libraries?")
        .append(LINE_SEPARATOR)
        .append("(yes). no.").toString();
    final String response = ConsoleUtils.readYesOrNoFromConsole(message);
    return "yes".equalsIgnoreCase(response) || StringUtils.EMPTY.equals(response);
  }

  private static boolean shouldAnalyzeDatabase() {
    final String message = new StringBuilder(
        "Do you want to analyze the usage of Database keywords?")
        .append(LINE_SEPARATOR)
        .append("(yes). no.").toString();
    final String response = ConsoleUtils.readYesOrNoFromConsole(message);
    return "yes".equalsIgnoreCase(response) || StringUtils.EMPTY.equals(response);
  }

  private static boolean shouldAnalyzeJava() {
    final String message = new StringBuilder("Do you want to analyze java common best practices?")
        .append(LINE_SEPARATOR)
        .append("(yes). no.").toString();
    final String response = ConsoleUtils.readYesOrNoFromConsole(message);
    return "yes".equalsIgnoreCase(response) || StringUtils.EMPTY.equals(response);
  }

  private static boolean shouldAnalyzeJavascript() {
    final String message = new StringBuilder(
        "Do you want to analyze javascript common best practices?")
        .append(LINE_SEPARATOR)
        .append("(yes). no.").toString();
    final String response = ConsoleUtils.readYesOrNoFromConsole(message);
    return "yes".equalsIgnoreCase(response) || StringUtils.EMPTY.equals(response);
  }

}
