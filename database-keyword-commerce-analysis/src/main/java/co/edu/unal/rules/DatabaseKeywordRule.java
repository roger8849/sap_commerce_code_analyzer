package co.edu.unal.rules;

import co.edu.unal.data.AnalysisConfigurationData;
import co.edu.unal.data.DatabaseKeywordData;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseKeywordRule extends AbstractJavaRule {

  private static final Logger log = LoggerFactory.getLogger(DatabaseKeywordRule.class);
  private List<DatabaseKeywordData> databaseKeywords;

  private void init() {
    final AnalysisConfigurationData configurationData = AnalysisConfigurationData.getInstance();
    final File keywordsBaseDir = new File(configurationData.getDatabaseKeywordsPath());
    databaseKeywords = new ArrayList<>();
    final Set<String> configFilesPaths =
        Stream.of(keywordsBaseDir.listFiles())
            .filter(File::isFile)
            .map(File::getAbsolutePath)
            .collect(Collectors.toSet());

    for (String configFilePath : configFilesPaths) {
      final File configFile = new File(configFilePath);
      final Set<String> keywordsFromConfigFile = getKeywordsFromFile(configFile);
      final String baseName = StringUtils.upperCase(
          FilenameUtils.getBaseName(configFile.getName()));
      databaseKeywords.add(
          new DatabaseKeywordData(
              baseName,
              keywordsFromConfigFile)
      );
    }
  }

  private Set<String> getKeywordsFromFile(File file) {
    Set<String> keywords = new HashSet<>();
    try (LineIterator it = FileUtils.lineIterator(file, StandardCharsets.UTF_8.toString())) {
      while (it.hasNext()) {
        keywords.add(StringUtils.upperCase(it.nextLine()));
      }
    } catch (IOException io) {

    }
    return keywords;
  }

  @Override
  public Object visit(ASTLiteral node, Object data) {
    this.init();
    if (Objects.nonNull(node) && node.isStringLiteral()) {
      final String value = StringUtils.upperCase(node.getImage());
      checkIfLiteralHasKeywords(node, data, value);
    }
    return data;
  }

  private void checkIfLiteralHasKeywords(ASTLiteral node, Object data, String value) {
    for (DatabaseKeywordData databaseKeyword : databaseKeywords) {
      for (String keyword : databaseKeyword.getKeywords()) {
        if (StringUtils.isNotBlank(value)) {
          searchKeyWordInValueString(node, data, value, databaseKeyword, keyword);
        }
      }
    }
  }

  private void searchKeyWordInValueString(ASTLiteral node, Object data, String value,
      DatabaseKeywordData databaseKeyword, String keyword) {
    final String words[] = value.split(" ");
    for (String word : words) {
      final int indexOfOpeningParenthesis = StringUtils.indexOf(word, "(");
      if (indexOfOpeningParenthesis == 0 && StringUtils.length(word) > 1) {
        word = StringUtils.substring(word, 1);
      } else if (indexOfOpeningParenthesis > 0) {
        word = StringUtils.substring(word, 0, indexOfOpeningParenthesis);
      }
      if (StringUtils.equalsIgnoreCase(word, keyword)) {
        asCtx(data).addViolation(node,
            node.getImage(),
            databaseKeyword.getKeywordGroup(),
            keyword,
            AnalysisConfigurationData.getInstance().getDatabaseKeywordsEngineName());
      }
    }
  }
}