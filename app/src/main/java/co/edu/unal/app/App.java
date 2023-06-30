/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package co.edu.unal.app;

import co.edu.unal.ThirdPartyLibrariesAnalyzer;
import co.edu.unal.analyzer.Analyzer;
import co.edu.unal.analyzer.impl.CommerceIncompatibilitiesAnalyzer;
import co.edu.unal.analyzer.impl.DatabaseKeywordCommerceAnalyzer;
import co.edu.unal.analyzer.impl.DeprecatedExtensionAnalyzer;
import co.edu.unal.analyzer.impl.JavaCommerceAnalyzer;
import co.edu.unal.analyzer.impl.JavascriptCommerceAnalyzer;
import co.edu.unal.data.AnalysisConfigurationData;
import co.edu.unal.utilities.ConsoleUtils;
import co.edu.unal.utilities.InitUtils;
import co.edu.unal.utilities.ReportUtils;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

  private AnalysisConfigurationData configurationData;

  public App() {
    this.configurationData = AnalysisConfigurationData.getInstance();
  }

  private static Logger log = LoggerFactory.getLogger(App.class.getName());

  public List<Analyzer> getAnalyzers() {
    List<Analyzer> analyzers = new ArrayList<>();
    if (configurationData.shouldAnalyzeDatabase()) {
      analyzers.add(new DatabaseKeywordCommerceAnalyzer());
    }
    if (configurationData.shouldAnalyzeThirdPartyExtensions()) {
      analyzers.add(new ThirdPartyLibrariesAnalyzer());
    }
    if (configurationData.shouldAnalyzeObsoleteExtensions()) {
      analyzers.add(new DeprecatedExtensionAnalyzer());
    }
    if (configurationData.shouldAnalyzeSAPCommerceIncompatibilities()) {
      analyzers.add(new CommerceIncompatibilitiesAnalyzer());
    }
    if (configurationData.shouldAnalyzeJava()) {
      analyzers.add(new JavaCommerceAnalyzer());
    }
    if (configurationData.shouldAnalyzeJavascript()) {
      analyzers.add(new JavascriptCommerceAnalyzer());
    }
    return analyzers;
  }

  public void run() {
    List<Analyzer> analyzers = this.getAnalyzers();
    for (Analyzer analyzer : analyzers) {
      analyzer.analyze();
    }
  }

  public static void main(String[] args) {
    try {
      App app = new App();
      InitUtils.init();
      app.run();
      ConsoleUtils.printImportantAnalysisMessage("Analysis ended. Results of the analysis were written in:", '-');
      log.info(ReportUtils.getReportFullPath());
    } catch (Exception e) {
      log.error("Fatal error executing analysis.", e);
    }
  }
}
