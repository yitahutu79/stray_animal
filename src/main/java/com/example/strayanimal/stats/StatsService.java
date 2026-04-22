package com.example.strayanimal.stats;

import com.example.strayanimal.adoption.AdoptionApplicationRepository;
import com.example.strayanimal.animal.Animal;
import com.example.strayanimal.animal.AnimalRepository;
import com.example.strayanimal.donation.Donation;
import com.example.strayanimal.donation.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class StatsService {

    private final AnimalRepository animalRepository;
    private final AdoptionApplicationRepository adoptionApplicationRepository;
    private final DonationRepository donationRepository;

    public StatsService(AnimalRepository animalRepository,
                        AdoptionApplicationRepository adoptionApplicationRepository,
                        DonationRepository donationRepository) {
        this.animalRepository = animalRepository;
        this.adoptionApplicationRepository = adoptionApplicationRepository;
        this.donationRepository = donationRepository;
    }

    public DashboardStats overview() {
        long totalAnimals = animalRepository.count();
        long animalsWaiting = animalRepository.countByStatus("待领养");
        long animalsAdopted = animalRepository.countByStatus("已领养");

        long totalApplications = adoptionApplicationRepository.count();
        long approvedApplications = adoptionApplicationRepository.countByStatus("APPROVED");

        long totalDonations = donationRepository.count();
        Double totalMoney = donationRepository.sumAllMoney();

        return new DashboardStats(
                totalAnimals,
                animalsWaiting,
                animalsAdopted,
                totalApplications,
                approvedApplications,
                totalDonations,
                totalMoney != null ? totalMoney : 0.0
        );
    }

    public StatsAnalyticsResponse analytics(String period) {
        String normalizedPeriod = normalizePeriod(period);
        DashboardStats overview = overview();

        List<Animal> animals = animalRepository.findAll();
        List<com.example.strayanimal.adoption.AdoptionApplication> applications = adoptionApplicationRepository.findAll();
        List<Donation> donations = donationRepository.findAll();

        List<StatsAnalyticsResponse.TimeBucketStat> trends = buildTrends(normalizedPeriod, applications, donations);
        List<StatsAnalyticsResponse.CategoryStat> animalStatusDistribution = buildAnimalStatusDistribution(animals);
        List<StatsAnalyticsResponse.CategoryStat> donationTypeDistribution = buildDonationTypeDistribution(donations);
        List<StatsAnalyticsResponse.CategoryStat> healthDemandDistribution = buildHealthDemandDistribution(animals);
        List<StatsAnalyticsResponse.ResourceMatchStat> resourceMatches = buildResourceMatches(animals, donations);
        List<StatsAnalyticsResponse.DecisionMetric> decisionMetrics = buildDecisionMetrics(overview, trends);
        List<String> managementInsights = buildManagementInsights(overview, trends, resourceMatches);

        return new StatsAnalyticsResponse(
                overview,
                normalizedPeriod,
                trends,
                animalStatusDistribution,
                donationTypeDistribution,
                healthDemandDistribution,
                resourceMatches,
                decisionMetrics,
                managementInsights
        );
    }

    public byte[] exportAnalyticsCsv(String period) {
        StatsAnalyticsResponse analytics = analytics(period);
        StringBuilder csv = new StringBuilder();
        csv.append('\uFEFF');
        csv.append("模块,指标,值,说明\n");

        DashboardStats overview = analytics.getOverview();
        appendCsvRow(csv, "基础概览", "动物总数", String.valueOf(overview.getTotalAnimals()), "当前系统内登记的动物总量");
        appendCsvRow(csv, "基础概览", "待领养数", String.valueOf(overview.getAnimalsWaiting()), "当前仍可进入领养流程的动物数量");
        appendCsvRow(csv, "基础概览", "已领养数", String.valueOf(overview.getAnimalsAdopted()), "已完成领养流程的动物数量");
        appendCsvRow(csv, "基础概览", "领养申请总数", String.valueOf(overview.getTotalApplications()), "累计申请记录");
        appendCsvRow(csv, "基础概览", "已通过申请数", String.valueOf(overview.getApprovedApplications()), "已通过审核的申请");
        appendCsvRow(csv, "基础概览", "捐赠记录总数", String.valueOf(overview.getTotalDonations()), "资金与物资捐赠合计");
        appendCsvRow(csv, "基础概览", "资金捐赠总额", String.format(Locale.US, "%.2f", overview.getTotalMoneyAmount()), "仅统计资金捐赠");

        for (StatsAnalyticsResponse.TimeBucketStat trend : analytics.getTrends()) {
            appendCsvRow(csv, "趋势分析", trend.getLabel(),
                    String.format(Locale.US, "申请=%d, 通过=%d, 资金笔数=%d, 物资笔数=%d, 金额=%.2f",
                            trend.getApplicationCount(),
                            trend.getApprovedCount(),
                            trend.getMoneyDonationCount(),
                            trend.getItemDonationCount(),
                            trend.getMoneyAmount()),
                    analytics.getPeriod());
        }

        for (StatsAnalyticsResponse.DecisionMetric metric : analytics.getDecisionMetrics()) {
            appendCsvRow(csv, "决策指标", metric.getTitle(), metric.getValue(), metric.getInsight());
        }

        for (StatsAnalyticsResponse.ResourceMatchStat match : analytics.getResourceMatches()) {
            appendCsvRow(csv, "资源匹配", match.getResourceType(),
                    "供给=" + match.getSupplyCount() + ", 需求=" + match.getDemandCount(),
                    match.getSuggestion());
        }

        for (String insight : analytics.getManagementInsights()) {
            appendCsvRow(csv, "管理建议", "建议", insight, "");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void appendCsvRow(StringBuilder csv, String module, String metric, String value, String note) {
        csv.append(csvEscape(module)).append(',')
                .append(csvEscape(metric)).append(',')
                .append(csvEscape(value)).append(',')
                .append(csvEscape(note)).append('\n');
    }

    private String csvEscape(String value) {
        String safe = value == null ? "" : value;
        return "\"" + safe.replace("\"", "\"\"") + "\"";
    }

    private String normalizePeriod(String period) {
        String value = period == null ? "MONTH" : period.trim().toUpperCase(Locale.ROOT);
        return switch (value) {
            case "YEAR" -> "YEAR";
            case "QUARTER" -> "QUARTER";
            default -> "MONTH";
        };
    }

    private List<StatsAnalyticsResponse.TimeBucketStat> buildTrends(String period,
                                                                    List<com.example.strayanimal.adoption.AdoptionApplication> applications,
                                                                    List<Donation> donations) {
        LinkedHashMap<String, BucketAccumulator> buckets = initBuckets(period);

        for (com.example.strayanimal.adoption.AdoptionApplication app : applications) {
            String key = bucketKey(app.getCreateTime(), period);
            BucketAccumulator bucket = buckets.get(key);
            if (bucket == null) {
                continue;
            }
            bucket.applicationCount++;
            if ("APPROVED".equalsIgnoreCase(app.getStatus())) {
                bucket.approvedCount++;
            }
        }

        for (Donation donation : donations) {
            String key = bucketKey(donation.getCreateTime(), period);
            BucketAccumulator bucket = buckets.get(key);
            if (bucket == null) {
                continue;
            }
            if ("MONEY".equalsIgnoreCase(donation.getType())) {
                bucket.moneyDonationCount++;
                bucket.moneyAmount += donation.getAmount() == null ? 0.0 : donation.getAmount();
            } else if ("ITEM".equalsIgnoreCase(donation.getType())) {
                bucket.itemDonationCount++;
            }
        }

        List<StatsAnalyticsResponse.TimeBucketStat> result = new ArrayList<>();
        for (Map.Entry<String, BucketAccumulator> entry : buckets.entrySet()) {
            BucketAccumulator bucket = entry.getValue();
            result.add(new StatsAnalyticsResponse.TimeBucketStat(
                    entry.getKey(),
                    bucket.applicationCount,
                    bucket.approvedCount,
                    bucket.moneyDonationCount,
                    bucket.itemDonationCount,
                    bucket.moneyAmount
            ));
        }
        return result;
    }

    private LinkedHashMap<String, BucketAccumulator> initBuckets(String period) {
        LinkedHashMap<String, BucketAccumulator> buckets = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();
        if ("YEAR".equals(period)) {
            for (int i = 2; i >= 0; i--) {
                LocalDate year = now.minusYears(i);
                buckets.put(String.valueOf(year.getYear()), new BucketAccumulator());
            }
            return buckets;
        }
        if ("QUARTER".equals(period)) {
            for (int i = 3; i >= 0; i--) {
                LocalDate date = now.minusMonths((long) i * 3);
                int quarter = date.get(IsoFields.QUARTER_OF_YEAR);
                buckets.put(date.getYear() + "-Q" + quarter, new BucketAccumulator());
            }
            return buckets;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        for (int i = 5; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            buckets.put(month.format(formatter), new BucketAccumulator());
        }
        return buckets;
    }

    private String bucketKey(LocalDateTime time, String period) {
        if (time == null) {
            return null;
        }
        if ("YEAR".equals(period)) {
            return String.valueOf(time.getYear());
        }
        if ("QUARTER".equals(period)) {
            return time.getYear() + "-Q" + time.get(IsoFields.QUARTER_OF_YEAR);
        }
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    private List<StatsAnalyticsResponse.CategoryStat> buildAnimalStatusDistribution(List<Animal> animals) {
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("待领养", 0L);
        counts.put("领养中", 0L);
        counts.put("已领养", 0L);
        counts.put("特别照顾", 0L);
        counts.put("其他", 0L);

        for (Animal animal : animals) {
            String status = animal.getStatus();
            String bucket = switch (status == null ? "" : status) {
                case "待领养" -> "待领养";
                case "领养中" -> "领养中";
                case "已领养" -> "已领养";
                case "特别照顾" -> "特别照顾";
                default -> "其他";
            };
            counts.compute(bucket, (key, value) -> value + 1L);
        }

        return toCategoryStats(counts);
    }

    private List<StatsAnalyticsResponse.CategoryStat> buildDonationTypeDistribution(List<Donation> donations) {
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("资金捐赠", 0L);
        counts.put("物资捐赠", 0L);
        for (Donation donation : donations) {
            if ("ITEM".equalsIgnoreCase(donation.getType())) {
                counts.compute("物资捐赠", (key, value) -> value + 1L);
            } else {
                counts.compute("资金捐赠", (key, value) -> value + 1L);
            }
        }
        return toCategoryStats(counts);
    }

    private List<StatsAnalyticsResponse.CategoryStat> buildHealthDemandDistribution(List<Animal> animals) {
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("治疗护理需求", 0L);
        counts.put("营养补给需求", 0L);
        counts.put("常规照护需求", 0L);

        for (Animal animal : animals) {
            String category = inferHealthDemand(animal.getHealthStatus());
            counts.compute(category, (key, value) -> value + 1L);
        }
        return toCategoryStats(counts);
    }

    private List<StatsAnalyticsResponse.ResourceMatchStat> buildResourceMatches(List<Animal> animals, List<Donation> donations) {
        long treatmentDemand = animals.stream()
                .map(Animal::getHealthStatus)
                .filter(status -> "治疗护理需求".equals(inferHealthDemand(status)))
                .count();
        long nutritionDemand = animals.stream()
                .map(Animal::getHealthStatus)
                .filter(status -> "营养补给需求".equals(inferHealthDemand(status)))
                .count();
        long routineDemand = animals.stream()
                .map(Animal::getHealthStatus)
                .filter(status -> "常规照护需求".equals(inferHealthDemand(status)))
                .count();

        long treatmentSupply = donations.stream()
                .filter(d -> "ITEM".equalsIgnoreCase(d.getType()))
                .filter(d -> "医疗用品".equals(inferDonationItemCategory(d.getRemark())))
                .count();
        long nutritionSupply = donations.stream()
                .filter(d -> "ITEM".equalsIgnoreCase(d.getType()))
                .filter(d -> "食品补给".equals(inferDonationItemCategory(d.getRemark())))
                .count();
        long routineSupply = donations.stream()
                .filter(d -> "ITEM".equalsIgnoreCase(d.getType()))
                .filter(d -> "日常用品".equals(inferDonationItemCategory(d.getRemark())))
                .count();

        List<StatsAnalyticsResponse.ResourceMatchStat> matches = new ArrayList<>();
        matches.add(new StatsAnalyticsResponse.ResourceMatchStat("医疗用品", treatmentSupply, treatmentDemand,
                buildResourceSuggestion("医疗用品", treatmentSupply, treatmentDemand)));
        matches.add(new StatsAnalyticsResponse.ResourceMatchStat("食品补给", nutritionSupply, nutritionDemand,
                buildResourceSuggestion("食品补给", nutritionSupply, nutritionDemand)));
        matches.add(new StatsAnalyticsResponse.ResourceMatchStat("日常用品", routineSupply, routineDemand,
                buildResourceSuggestion("日常用品", routineSupply, routineDemand)));
        return matches;
    }

    private List<StatsAnalyticsResponse.DecisionMetric> buildDecisionMetrics(DashboardStats overview,
                                                                             List<StatsAnalyticsResponse.TimeBucketStat> trends) {
        List<StatsAnalyticsResponse.DecisionMetric> metrics = new ArrayList<>();

        double approvalRate = overview.getTotalApplications() == 0
                ? 0.0
                : overview.getApprovedApplications() * 100.0 / overview.getTotalApplications();
        double applicationPressure = overview.getAnimalsWaiting() == 0
                ? 0.0
                : overview.getTotalApplications() * 1.0 / overview.getAnimalsWaiting();

        StatsAnalyticsResponse.TimeBucketStat latest = trends.isEmpty() ? null : trends.get(trends.size() - 1);
        double averageMoney = trends.stream().mapToDouble(StatsAnalyticsResponse.TimeBucketStat::getMoneyAmount).average().orElse(0.0);
        String donationVolatility = latest == null
                ? "暂无数据"
                : String.format(Locale.US, "%.0f%%", averageMoney == 0 ? 0.0 : (latest.getMoneyAmount() - averageMoney) * 100.0 / averageMoney);

        metrics.add(new StatsAnalyticsResponse.DecisionMetric(
                "领养申请通过率",
                String.format(Locale.US, "%.1f%%", approvalRate),
                approvalRate < 35
                        ? "通过率偏低，建议复核审核标准，或补充动物档案信息降低申请误判。"
                        : "通过率处于可接受区间，可继续结合回访质量评估筛选标准。"
        ));
        metrics.add(new StatsAnalyticsResponse.DecisionMetric(
                "单只待领养动物承接申请数",
                String.format(Locale.US, "%.2f", applicationPressure),
                applicationPressure < 1
                        ? "单只动物平均申请偏少，说明曝光或展示内容仍有提升空间。"
                        : "申请压力基本形成，后续可关注热门动物与冷门动物的展示均衡。"
        ));
        metrics.add(new StatsAnalyticsResponse.DecisionMetric(
                "本期捐赠波动",
                donationVolatility,
                "可据此判断宣传节点是否有效，并安排下一轮募捐或线下活动。"
        ));
        return metrics;
    }

    private List<String> buildManagementInsights(DashboardStats overview,
                                                 List<StatsAnalyticsResponse.TimeBucketStat> trends,
                                                 List<StatsAnalyticsResponse.ResourceMatchStat> resourceMatches) {
        List<String> insights = new ArrayList<>();

        double approvalRate = overview.getTotalApplications() == 0
                ? 0.0
                : overview.getApprovedApplications() * 100.0 / overview.getTotalApplications();
        if (overview.getAnimalsWaiting() > overview.getAnimalsAdopted() && approvalRate < 40) {
            insights.add("待领养动物数量高于已领养数量且申请通过率偏低，建议检查审核门槛是否过严，或优化动物信息展示内容。");
        }

        if (!trends.isEmpty()) {
            StatsAnalyticsResponse.TimeBucketStat latest = trends.get(trends.size() - 1);
            StatsAnalyticsResponse.TimeBucketStat previous = trends.size() > 1 ? trends.get(trends.size() - 2) : null;
            if (previous != null && latest.getMoneyAmount() < previous.getMoneyAmount()) {
                insights.add("最近一个统计周期的资金捐赠低于上一周期，适合提前安排专题宣传、病例故事或集中募捐活动。");
            }
            if (latest.getApplicationCount() > 0 && latest.getApprovedCount() == 0) {
                insights.add("最近一个统计周期已有领养申请但暂无通过记录，建议及时检查审核积压或回访流程。");
            }
        }

        for (StatsAnalyticsResponse.ResourceMatchStat match : resourceMatches) {
            if (match.getDemandCount() > match.getSupplyCount()) {
                insights.add(match.getSuggestion());
            }
        }

        if (insights.isEmpty()) {
            insights.add("当前主要指标相对平稳，建议继续按月跟踪领养通过率、捐赠金额波动和物资结构。");
        }

        return insights;
    }

    private List<StatsAnalyticsResponse.CategoryStat> toCategoryStats(Map<String, Long> counts) {
        List<StatsAnalyticsResponse.CategoryStat> stats = new ArrayList<>();
        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            stats.add(new StatsAnalyticsResponse.CategoryStat(entry.getKey(), entry.getValue()));
        }
        return stats;
    }

    private String inferHealthDemand(String healthStatus) {
        String value = healthStatus == null ? "" : healthStatus.toLowerCase(Locale.ROOT);
        if (containsAny(value, "受伤", "感染", "术后", "疾病", "骨折", "治疗", "炎症")) {
            return "治疗护理需求";
        }
        if (containsAny(value, "瘦弱", "营养", "幼龄", "虚弱", "恢复")) {
            return "营养补给需求";
        }
        return "常规照护需求";
    }

    private String inferDonationItemCategory(String remark) {
        String value = remark == null ? "" : remark.toLowerCase(Locale.ROOT);
        if (containsAny(value, "药", "医", "绷带", "消炎", "针", "驱虫", "疫苗")) {
            return "医疗用品";
        }
        if (containsAny(value, "猫粮", "狗粮", "罐头", "奶", "营养", "冻干")) {
            return "食品补给";
        }
        return "日常用品";
    }

    private String buildResourceSuggestion(String resourceType, long supplyCount, long demandCount) {
        if (demandCount > supplyCount) {
            return resourceType + "供给低于对应健康需求，建议把下一轮物资募捐重点放在这一类。";
        }
        return resourceType + "供给与当前需求大体匹配，可继续观察后续动物健康结构变化。";
    }

    private boolean containsAny(String value, String... keywords) {
        for (String keyword : keywords) {
            if (value.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private static class BucketAccumulator {
        long applicationCount;
        long approvedCount;
        long moneyDonationCount;
        long itemDonationCount;
        double moneyAmount;
    }
}
