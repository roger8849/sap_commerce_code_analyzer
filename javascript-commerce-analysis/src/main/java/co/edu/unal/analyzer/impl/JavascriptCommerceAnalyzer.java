package co.edu.unal.analyzer.impl;

import co.edu.unal.analyzer.Analyzer;
import co.edu.unal.data.AnalysisConfigurationData;
import co.edu.unal.utilities.ConsoleUtils;
import co.edu.unal.utilities.ReportUtils;
import java.io.File;
import java.util.Collections;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.PmdAnalysis;
import net.sourceforge.pmd.RulePriority;
import net.sourceforge.pmd.cache.FileAnalysisCache;

public class JavascriptCommerceAnalyzer implements Analyzer {

  @Override
  public void analyze() {
    ConsoleUtils.printImportantAnalysisMessage("Starting javascript best practices analysis.", '*');
    final PMDConfiguration javascriptPMDConfiguration = (PMDConfiguration) this.getAnalyzerConfiguration();
    try (PmdAnalysis pmd = PmdAnalysis.create(javascriptPMDConfiguration)) {
      pmd.performAnalysis();
    }
    ConsoleUtils.printImportantAnalysisMessage("Javascript best practices analysis ended.", '*');
  }

  @Override
  public Object getAnalyzerConfiguration() {
    final AnalysisConfigurationData configurationData = AnalysisConfigurationData.getInstance();
    PMDConfiguration configuration = new PMDConfiguration();
    configuration.setMinimumPriority(RulePriority.LOW);
    configuration.addRuleSet("rulesets/ecmascript/basic.xml");
    configuration.setInputPathList(Collections.singletonList(configurationData.getHybrisPath()));
    configuration.setReportFormat("csv");
    configuration.setReportFile(
        new StringBuilder(ReportUtils.getReportFullPath()).append(
            ReportUtils.JAVASCRIPT_REPORT_FILENAME).toString());
    configuration.setAnalysisCache(new FileAnalysisCache(new File(
        new StringBuilder(ReportUtils.getCacheFolder()).append(ReportUtils.JAVASCRIPT_REPORT_CACHE)
            .toString())));
    return configuration;
  }


}
