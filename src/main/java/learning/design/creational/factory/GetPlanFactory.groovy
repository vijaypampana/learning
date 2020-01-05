package learning.design.creational.factory

import org.apache.commons.lang3.StringUtils

class GetPlanFactory {

    public Plan getPlan(String planType) {

        if(StringUtils.isEmpty(planType)) {
            return null;
        } else if("DOMESTICPLAN".equalsIgnoreCase(planType)) {
            return new DomesticPlan()
        } else if("COMMERCIALPLAN".equalsIgnoreCase(planType)) {
            return new CommercialPlan()
        } else if("INSTITUTIONALPLAN".equalsIgnoreCase(planType)) {
            return new InstitutionalPlan()
        }

        return null
    }
}
