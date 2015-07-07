package bp.feifan.com.codescanner.encoding;

/**
 * Encapsulates some simple formatting logic, to aid refactoring in {@link ContactEncoder}.
 * 
 */
interface Formatter {

  String format(String source);

}
