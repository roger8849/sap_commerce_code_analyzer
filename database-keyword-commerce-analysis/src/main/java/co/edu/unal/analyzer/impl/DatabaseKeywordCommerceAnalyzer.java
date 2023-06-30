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
import net.sourceforge.pmd.lang.LanguageRegistry;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseKeywordCommerceAnalyzer implements Analyzer {

  private static final Logger log = LoggerFactory.getLogger(DatabaseKeywordCommerceAnalyzer.class);

  @Override
  public void analyze() {
    ConsoleUtils.printImportantAnalysisMessage("Starting database keywords analysis.", '*');
    final File keywordsBaseDir = new File(
        AnalysisConfigurationData.getInstance().getDatabaseKeywordsPath());
    log.info("Using selected keywords set {}",
        StringUtils.upperCase(FilenameUtils.getBaseName(keywordsBaseDir.getName())));
    final PMDConfiguration databaseKeywordJavaPMDConfiguration = (PMDConfiguration) this.getAnalyzerConfiguration();
    try (PmdAnalysis pmd = PmdAnalysis.create(databaseKeywordJavaPMDConfiguration)) {
      pmd.performAnalysis();
    }
    ConsoleUtils.printImportantAnalysisMessage("Database keywords analysis ended.", '*');
  }

  @Override
  public Object getAnalyzerConfiguration() {
    final AnalysisConfigurationData configurationData = AnalysisConfigurationData.getInstance();
    PMDConfiguration configuration = new PMDConfiguration();
    configuration.setMinimumPriority(RulePriority.MEDIUM_HIGH);
    configuration.addRuleSet("rulesets/java/database-keyword-java-ruleset.xml");
    configuration.setInputPathList(Collections.singletonList(configurationData.getHybrisPath()));
    configuration.setDefaultLanguageVersion(
        LanguageRegistry.findLanguageByTerseName("java").getVersion("11"));
    configuration.setReportFormat("csv");
    configuration.setReportFile(
        new StringBuilder(ReportUtils.getReportFullPath()).append(
            ReportUtils.DATABASE_REPORT_FILENAME).toString());
    configuration.setAnalysisCache(new FileAnalysisCache(new File(
        new StringBuilder(ReportUtils.getCacheFolder()).append(ReportUtils.DATABASE_REPORT_CACHE)
            .toString())));
    return configuration;
  }
}
