package bp.feifan.com.codescanner.encoding;

import java.util.Collection;
import java.util.HashSet;

/**
 * Implementations encode according to some scheme for encoding contact
 * information, like VCard or MECARD.
 */
abstract class ContactEncoder {

  /**
   * @return first, the best effort encoding of all data in the appropriate
   *         format; second, a display-appropriate version of the contact
   *         information
   */
  abstract String[] encode(Iterable<String> names, String organization,
      Iterable<String> addresses, Iterable<String> phones,
      Iterable<String> emails, String url, String note);

  /**
   * @return null if s is null or empty, or result of s.trim() otherwise
   */
  static String trim(String s) {
    if (s == null) {
      return null;
    }
    String result = s.trim();
    return result.length() == 0 ? null : result;
  }

  static void doAppend(StringBuilder newContents,
      StringBuilder newDisplayContents, String prefix, String value,
      Formatter fieldFormatter, char terminator) {
    String trimmed = trim(value);
    if (trimmed != null) {
      newContents.append(prefix).append(':')
          .append(fieldFormatter.format(trimmed)).append(terminator);
      newDisplayContents.append(trimmed).append('\n');
    }
  }

  static void doAppendUpToUnique(StringBuilder newContents,
      StringBuilder newDisplayContents, String prefix,
      Iterable<String> values, int max, Formatter formatter,
      Formatter fieldFormatter, char terminator) {
    if (values == null) {
      return;
    }
    int count = 0;
    Collection<String> uniques = new HashSet<String>(2);
    for (String value : values) {
      String trimmed = trim(value);
      if (trimmed != null && !uniques.contains(trimmed)) {
        newContents.append(prefix).append(':')
            .append(fieldFormatter.format(trimmed))
            .append(terminator);
        String display = formatter == null ? trimmed : formatter
            .format(trimmed);
        newDisplayContents.append(display).append('\n');
        if (++count == max) {
          break;
        }
        uniques.add(trimmed);
      }
    }
  }

}
