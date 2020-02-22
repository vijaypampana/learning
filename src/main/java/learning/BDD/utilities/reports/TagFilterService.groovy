package learning.BDD.utilities.reports

import java.util.stream.Collectors
import java.util.stream.Stream

class TagFilterService {

    public List<String> getFilteredTags(List<String> scenarioTags, List<String> featureTags) {
        return filterFeatureTags(filterBasedOnWhiteListRules(scenarioTags), featureTags).collect(Collectors.toList())
    }

    public Stream<String> filterBasedOnWhiteListRules(List<String> scenarioTags) {
        List<String> filteredAndModifiedTags = new ArrayList<>()
        scenarioTags.each { tag ->

        }
    }

}
