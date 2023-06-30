package co.edu.unal.analyzer.impl;

import co.edu.unal.analyzer.Analyzer;
import co.edu.unal.data.AnalysisConfigurationData;
import co.edu.unal.utilities.ConsoleUtils;
import co.edu.unal.utilities.ReportUtils;
import java.io.File;
import java.util.Arrays;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.PmdAnalysis;
import net.sourceforge.pmd.RulePriority;
import net.sourceforge.pmd.cache.FileAnalysisCache;

public class DeprecatedExtensionAnalyzer implements Analyzer {

  @Override
  public void analyze() {
    ConsoleUtils.printImportantAnalysisMessage("Starting deprecated extensions analysis.", '*');
    final PMDConfiguration deprecatedExtensionsConfiguration = (PMDConfiguration) this.getAnalyzerConfiguration();
    try (PmdAnalysis pmd = PmdAnalysis.create(deprecatedExtensionsConfiguration)) {
      pmd.performAnalysis();
    }
    ConsoleUtils.printImportantAnalysisMessage("Deprecated extensions analysis ended.", '*');
  }

  @Override
  public Object getAnalyzerConfiguration() {
    final AnalysisConfigurationData configurationData = AnalysisConfigurationData.getInstance();
    PMDConfiguration configuration = new PMDConfiguration();
    configuration.setMinimumPriority(RulePriority.HIGH);
    configuration.addRuleSet("rulesets/xml/basic.xml");
    configuration.setInputPathList(Arrays.asList(configurationData.getHybrisPath()));
    configuration.setReportFormat("csv");
    configuration.setReportFile(
        new StringBuilder(ReportUtils.getReportFullPath()).append(
            ReportUtils.DEPRECATED_EXTENSIONS_REPORT_FILENAME).toString());
    configuration.setAnalysisCache(new FileAnalysisCache(new File(
        new StringBuilder(ReportUtils.getCacheFolder()).append(
                ReportUtils.DEPRECATED_EXTENSIONS_REPORT_CACHE)
            .toString())));
    return configuration;
  }
}
