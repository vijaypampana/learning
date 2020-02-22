package learning.BDD.utilities.reports

import org.apache.commons.text.WordUtils

import java.util.regex.Pattern
import java.util.stream.Collectors
import java.util.stream.Stream

class TagFilterService {

    public List<String> getFilteredTags(List<String> scenarioTags, List<String> featureTags) {
        return filterFeatureTags(filterBasedOnWhiteListRules(scenarioTags), featureTags).collect(Collectors.toList())
    }

    public Stream<String> filterBasedOnWhiteListRules(List<String> scenarioTags) {
        List<String> filteredAndModifiedTags = new ArrayList<>()
        scenarioTags.each { tag ->
            String finalTag = ""
            Optional<ReportConstants.WHITELIST_RULE_FOR_TAGING> matchedTag = Stream.of(ReportConstants.WHITELIST_RULE_FOR_TAGING.values())
                .filter({ whiteListRule -> Pattern.compile(whiteListRule.getRegex()).matcher(tag).find() }).findFirst()
            if(matchedTag.isPresent()) {
                ReportConstants.TYPE_OF_OPERATION_ON_TAG valueForTags = matchedTag.get().getValueForTags()

                if(valueForTags.equals(ReportConstants.TYPE_OF_OPERATION_ON_TAG.CONSIDERED_VALUE_BEEN_SPECIFIED)) {
                    finalTag = tag
                } else if(valueForTags.equals(ReportConstants.TYPE_OF_OPERATION_ON_TAG.ALL_CAPS)) {
                    finalTag = tag.toUpperCase()
                } else {
                    finalTag = "@" + initCapBasedOnChar(tag.substring(1))
                }
                filteredAndModifiedTags.add(finalTag)
            }
        }
        return filteredAndModifiedTags.stream()
    }

    public Stream<String> filterFeatureTags(Stream<String> scenarioTags, List<String> featureTags) {
        return scenarioTags.filter({ tag -> !ReportConstants.exclusionOfTags.test(tag, featureTags) })
    }

    public String initCapBasedOnChar(String sText) {
        WordUtils.capitalizeFully(sText)
    }
}
