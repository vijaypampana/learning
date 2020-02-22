package learning.BDD.utilities.reports;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class ReportConstants {

    public enum WHITELIST_RULE_FOR_TAGING {

        API("(?i)^@API$", TYPE_OF_OPERATION_ON_TAG.ALL_CAPS),
        UI("(?i)^@UI$", TYPE_OF_OPERATION_ON_TAG.ALL_CAPS),
        PRIORITY("(?i)^PRIORITY_(High|Medium|Low)\\b", TYPE_OF_OPERATION_ON_TAG.INIT_CAPS),
        MODULE("(?i)^@Module_$", TYPE_OF_OPERATION_ON_TAG.CONSIDERED_VALUE_BEEN_SPECIFIED),
        TEST_FOLDER("(?i)^@TF\\d+$", TYPE_OF_OPERATION_ON_TAG.ALL_CAPS),
        FEATURE("(?i)^@F\\d+$", TYPE_OF_OPERATION_ON_TAG.ALL_CAPS);

        private String regex;
        private TYPE_OF_OPERATION_ON_TAG valueForTags;

        WHITELIST_RULE_FOR_TAGING(String regex, TYPE_OF_OPERATION_ON_TAG valueForTags) {
            this.regex = regex;
            this.valueForTags = valueForTags;
        }

        String getRegex() {
            return regex;
        }

        TYPE_OF_OPERATION_ON_TAG getValueForTags() {
            return valueForTags;
        }
        }

    public enum TYPE_OF_OPERATION_ON_TAG {
        CONSIDERED_VALUE_BEEN_SPECIFIED, ALL_CAPS, INIT_CAPS
    }

    public static final BiPredicate<String, List<String>> exclusionOfTags = (scenarioTag, featureTags) -> (featureTags.contains(scenarioTag));

    public static final Predicate<String> predicateForWhiteListing = tagsToBeFiltered -> (
      Stream.of(WHITELIST_RULE_FOR_TAGING.values()).map(whiteListRules -> Pattern.compile(whiteListRules.getRegex())).anyMatch(regExMatcher -> regExMatcher.matcher(tagsToBeFiltered).find()));

}


//      ^       :   Start of Line
//      $       :   End of Line
//      (?i)    :   Case insenstive
//      \d+     :   one or more digits
//      \D+     :   A non digit characters
//      \b      :   Matches whole word
//      \s      :   one or more spaces
//      \S      :   A non white Space
//      (\d+)?  :   ? tells it is optional field
//      \.      :   matches .
//      .       :   Matches any character
//      [abc]   :   can match a or b or c
//      [^abc]  :   Any character except a or b or c
//      X|Z     :   Finds X or Z
//      \w      :   a word character [a-zA-Z0-9_]
//      \W      :   not a word character
//      *       :   Occurs 0 or more times {0,} //first digit is min and second digit is max
//      +       :   {1,}
//      ?       :   {0,1}
//      {X}     :   Occurs X number of times like \d{3} means 3 digits
//      {X, Y}  :   Occurs min x times to max Y times like \d{1,4}, matches min one digit to max 4 digits
//      *?      :   Stop after first match
// rexegg.com/regex-quickstart.html