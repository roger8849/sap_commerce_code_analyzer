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
import net.sourceforge.pmd.lang.LanguageRegistry;

public class JavaCommerceAnalyzer implements Analyzer {

  @Override
  public void analyze() {
    ConsoleUtils.printImportantAnalysisMessage("Starting java best practices analysis.", '*');
    final PMDConfiguration javaPMDConfiguration = (PMDConfiguration) this.getAnalyzerConfiguration();
    try (PmdAnalysis pmd = PmdAnalysis.create(javaPMDConfiguration)) {
      pmd.performAnalysis();
    }
    ConsoleUtils.printImportantAnalysisMessage("Java best practices analysis ended.", '*');
  }

  @Override
  public Object getAnalyzerConfiguration() {
    final AnalysisConfigurationData configurationData = AnalysisConfigurationData.getInstance();
    PMDConfiguration configuration = new PMDConfiguration();
    configuration.setMinimumPriority(RulePriority.LOW);
    configuration.addRuleSet("rulesets/java/quickstart.xml");
    configuration.setInputPathList(Arrays.asList(configurationData.getHybrisPath()));
    configuration.setDefaultLanguageVersion(
        LanguageRegistry.findLanguageByTerseName("java").getVersion("11"));
    configuration.setReportFormat("csv");
    configuration.setReportFile(
        new StringBuilder(ReportUtils.getReportFullPath()).append(ReportUtils.JAVA_REPORT_FILENAME)
            .toString());
    configuration.setAnalysisCache(new FileAnalysisCache(new File(
        new StringBuilder(ReportUtils.getCacheFolder()).append(ReportUtils.JAVA_REPORT_CACHE)
            .toString())));
    return configuration;
  }

}
