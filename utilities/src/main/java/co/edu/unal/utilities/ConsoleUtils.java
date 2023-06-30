package co.edu.unal.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleUtils {

  private static Logger log = LoggerFactory.getLogger(ConsoleUtils.class);

  public static String readYesOrNoFromConsole(final String message) {
    log.info(message);
    String response = StringUtils.EMPTY;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    try {
      boolean shouldContinue;
      do {
        final String tempInParam = StringUtils.trimToEmpty(reader.readLine());
        shouldContinue = Arrays.asList("yes", "no", StringUtils.EMPTY).stream()
            .noneMatch(r -> r.equalsIgnoreCase(tempInParam));
        if (shouldContinue) {
          log.error("Please enter yes or no. Or leave it empty to use (yes) as default.");
        }
        response = tempInParam;
      } while (shouldContinue);
    } catch (Exception exception) {
      log.error("Error reading yes or no parameter", exception);
    }
    return response;
  }

  public static int readNumberOptionFromConsole(final String message, final int min,
      final int max) {
    log.info(message);
    int response = 0;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    try {
      boolean shouldContinue = Boolean.TRUE;
      do {
        final String tempInParam = StringUtils.trimToEmpty(reader.readLine());
        if (NumberUtils.isCreatable(tempInParam)) {
          final int chosenOption = NumberUtils.toInt(tempInParam);
          if (chosenOption >= min && chosenOption <= max) {
            return chosenOption;
          }
        }
        log.info(new StringBuilder("Invalid option please enter a number between: ")
            .append(min).append(" and ").append(max).toString());
      } while (shouldContinue);
    } catch (Exception exception) {
      log.error("Error reading yes or no parameter", exception);
    }
    return response;
  }


  public static void printImportantAnalysisMessage(final String message, final char separatorChar) {
    final StringBuilder middleMessage = new StringBuilder().append(separatorChar).append(" ")
        .append(message)
        .append(" ").append(separatorChar);

    final StringBuilder separator = new StringBuilder(StringUtils.EMPTY);
    for (int i = 0; i < middleMessage.length(); i++) {
      separator.append(separatorChar);
    }

    log.info(separator.toString());
    log.info(middleMessage.toString());
    log.info(separator.toString());
  }
}
