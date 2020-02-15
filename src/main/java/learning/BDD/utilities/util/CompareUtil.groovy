package learning.BDD.utilities.util

import learning.BDD.utilities.Context
import org.hamcrest.Matchers
import org.junit.Assert

class CompareUtil {

    static boolean equalTo(String actual, String expected, Boolean bAssert) {
        return compare(CompareType.EQUAL_TO, actual, expected, bAssert)
    }

    static boolean equalToIgnoreCase(String actual, String expected, Boolean bAssert) {
        return compare(CompareType.EQUAL_TO_IGNORE_CASE, actual, expected, bAssert)
    }

    static boolean notEqualTo(String actual, String expected, Boolean bAssert) {
        return compare(CompareType.NOT_EQUAL_TO, actual, expected, bAssert)
    }

    static boolean contains(String actual, String expected, Boolean bAssert) {
        return compare(CompareType.CONTAINS, actual, expected, bAssert)
    }

    static boolean notContains(String actual, String expected, Boolean bAssert) {
        return compare(CompareType.NOT_CONTAINS, actual, expected, bAssert)
    }

    static boolean startsWith(String actual, String expected, Boolean bAssert) {
        return compare(CompareType.STARTS_WITH, actual, expected, bAssert)
    }

    static boolean endsWith(String actual, String expected, Boolean bAssert) {
        return compare(CompareType.ENDS_WITH, actual, expected, bAssert)
    }

    private static Boolean compare(CompareType compareType, String actual, String expected, Boolean bAssert) {
        expected    = CoreUtil.process(expected)
        actual      = CoreUtil.process(actual)
        Boolean bFlag = false
        try {
            switch (compareType) {
                case "EQUAL_TO":
                    Assert.assertThat(actual, Matchers.equalTo(expected))
                    break
                case "EQUAL_TO_IGNORE_CASE":
                    Assert.assertThat(actual, Matchers.equalToIgnoringCase(expected))
                    break
                case "NOT_EQUAL_TO":
                    Assert.assertThat(actual, Matchers.not(Matchers.equalTo(expected)))
                    break
                case "CONTAINS":
                    Assert.assertThat(actual, Matchers.containsString(expected))
                    break
                case "NOT_CONTAINS":
                    Assert.assertThat(actual, Matchers.not(Matchers.containsString(expected)))
                    break
                case "STARTS_WITH":
                    Assert.assertThat(actual, Matchers.startsWith(expected))
                    break
                case "ENDS_WITH":
                    Assert.assertThat(actual, Matchers.endsWith(expected))
                    break
            }
            bFlag = true
        } catch (AssertionError e) {
            if(bAssert) {
                Context.getInstance().getReports().stepFail("")
            } else {
                Context.getInstance().getReports().stepError("")
            }
        }
        return bFlag
    }
}
