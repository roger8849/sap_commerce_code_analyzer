package co.edu.unal.utilities;

import co.edu.unal.data.RuleViolationData;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVUtils {

  private static final Logger LOG = LoggerFactory.getLogger(CSVUtils.class);

  private CSVUtils() {
    // mean to be empty due avoid utils instantiation.
  }

  public static void writeRuleViolationToCSV(final String csvPath,
      final List<RuleViolationData> violations) {
    try (
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvPath));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
            .withHeader("Problem", "Package", "File", "Priority", "Line", "Description", "Rule set",
                "Rule"));
    ) {
      if (CollectionUtils.isNotEmpty(violations)) {
        violations.stream().forEach(violation -> {
          try {
            csvPrinter.printRecord(violation.getProblem(), violation.getPackageValue(),
                violation.getFile(), violation.getPriority(), violation.getLine(),
                violation.getDescription(), violation.getRuleSet(), violation.getRule());
          } catch (IOException e) {
            LOG.error("Error writing record {}", violation, e);
          }
        });
      }
      csvPrinter.flush();
    } catch (IOException exception) {
      LOG.error("Error writing file {}", csvPath, exception);
    }
  }
}
