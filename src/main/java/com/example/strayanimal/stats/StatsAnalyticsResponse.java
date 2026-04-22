package com.example.strayanimal.stats;

import java.util.List;

public class StatsAnalyticsResponse {

    private DashboardStats overview;
    private String period;
    private List<TimeBucketStat> trends;
    private List<CategoryStat> animalStatusDistribution;
    private List<CategoryStat> donationTypeDistribution;
    private List<CategoryStat> healthDemandDistribution;
    private List<ResourceMatchStat> resourceMatches;
    private List<DecisionMetric> decisionMetrics;
    private List<String> managementInsights;

    public StatsAnalyticsResponse(DashboardStats overview,
                                  String period,
                                  List<TimeBucketStat> trends,
                                  List<CategoryStat> animalStatusDistribution,
                                  List<CategoryStat> donationTypeDistribution,
                                  List<CategoryStat> healthDemandDistribution,
                                  List<ResourceMatchStat> resourceMatches,
                                  List<DecisionMetric> decisionMetrics,
                                  List<String> managementInsights) {
        this.overview = overview;
        this.period = period;
        this.trends = trends;
        this.animalStatusDistribution = animalStatusDistribution;
        this.donationTypeDistribution = donationTypeDistribution;
        this.healthDemandDistribution = healthDemandDistribution;
        this.resourceMatches = resourceMatches;
        this.decisionMetrics = decisionMetrics;
        this.managementInsights = managementInsights;
    }

    public DashboardStats getOverview() {
        return overview;
    }

    public void setOverview(DashboardStats overview) {
        this.overview = overview;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<TimeBucketStat> getTrends() {
        return trends;
    }

    public void setTrends(List<TimeBucketStat> trends) {
        this.trends = trends;
    }

    public List<CategoryStat> getAnimalStatusDistribution() {
        return animalStatusDistribution;
    }

    public void setAnimalStatusDistribution(List<CategoryStat> animalStatusDistribution) {
        this.animalStatusDistribution = animalStatusDistribution;
    }

    public List<CategoryStat> getDonationTypeDistribution() {
        return donationTypeDistribution;
    }

    public void setDonationTypeDistribution(List<CategoryStat> donationTypeDistribution) {
        this.donationTypeDistribution = donationTypeDistribution;
    }

    public List<CategoryStat> getHealthDemandDistribution() {
        return healthDemandDistribution;
    }

    public void setHealthDemandDistribution(List<CategoryStat> healthDemandDistribution) {
        this.healthDemandDistribution = healthDemandDistribution;
    }

    public List<ResourceMatchStat> getResourceMatches() {
        return resourceMatches;
    }

    public void setResourceMatches(List<ResourceMatchStat> resourceMatches) {
        this.resourceMatches = resourceMatches;
    }

    public List<DecisionMetric> getDecisionMetrics() {
        return decisionMetrics;
    }

    public void setDecisionMetrics(List<DecisionMetric> decisionMetrics) {
        this.decisionMetrics = decisionMetrics;
    }

    public List<String> getManagementInsights() {
        return managementInsights;
    }

    public void setManagementInsights(List<String> managementInsights) {
        this.managementInsights = managementInsights;
    }

    public static class TimeBucketStat {
        private String label;
        private long applicationCount;
        private long approvedCount;
        private long moneyDonationCount;
        private long itemDonationCount;
        private double moneyAmount;

        public TimeBucketStat(String label, long applicationCount, long approvedCount, long moneyDonationCount, long itemDonationCount, double moneyAmount) {
            this.label = label;
            this.applicationCount = applicationCount;
            this.approvedCount = approvedCount;
            this.moneyDonationCount = moneyDonationCount;
            this.itemDonationCount = itemDonationCount;
            this.moneyAmount = moneyAmount;
        }

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public long getApplicationCount() { return applicationCount; }
        public void setApplicationCount(long applicationCount) { this.applicationCount = applicationCount; }
        public long getApprovedCount() { return approvedCount; }
        public void setApprovedCount(long approvedCount) { this.approvedCount = approvedCount; }
        public long getMoneyDonationCount() { return moneyDonationCount; }
        public void setMoneyDonationCount(long moneyDonationCount) { this.moneyDonationCount = moneyDonationCount; }
        public long getItemDonationCount() { return itemDonationCount; }
        public void setItemDonationCount(long itemDonationCount) { this.itemDonationCount = itemDonationCount; }
        public double getMoneyAmount() { return moneyAmount; }
        public void setMoneyAmount(double moneyAmount) { this.moneyAmount = moneyAmount; }
    }

    public static class CategoryStat {
        private String label;
        private long count;

        public CategoryStat(String label, long count) {
            this.label = label;
            this.count = count;
        }

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
    }

    public static class ResourceMatchStat {
        private String resourceType;
        private long supplyCount;
        private long demandCount;
        private String suggestion;

        public ResourceMatchStat(String resourceType, long supplyCount, long demandCount, String suggestion) {
            this.resourceType = resourceType;
            this.supplyCount = supplyCount;
            this.demandCount = demandCount;
            this.suggestion = suggestion;
        }

        public String getResourceType() { return resourceType; }
        public void setResourceType(String resourceType) { this.resourceType = resourceType; }
        public long getSupplyCount() { return supplyCount; }
        public void setSupplyCount(long supplyCount) { this.supplyCount = supplyCount; }
        public long getDemandCount() { return demandCount; }
        public void setDemandCount(long demandCount) { this.demandCount = demandCount; }
        public String getSuggestion() { return suggestion; }
        public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
    }

    public static class DecisionMetric {
        private String title;
        private String value;
        private String insight;

        public DecisionMetric(String title, String value, String insight) {
            this.title = title;
            this.value = value;
            this.insight = insight;
        }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String getInsight() { return insight; }
        public void setInsight(String insight) { this.insight = insight; }
    }
}
