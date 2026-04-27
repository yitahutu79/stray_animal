package com.example.strayanimal.adoption;

import com.example.strayanimal.animal.Animal;

/**
 * 根据申请人填写的信息给出一段文字评价，用于辅助解释评分.
 */
public class AdoptionEvaluationUtil {

    public static String buildEvaluation(AdoptionApplication app, Animal animal, int score) {
        StringBuilder sb = new StringBuilder();

        // 基本匹配度
        sb.append("综合评分：").append(score).append(" 分，匹配度");
        if (score >= 80) {
            sb.append("较高。");
        } else if (score >= 60) {
            sb.append("一般。");
        } else {
            sb.append("偏低。");
        }

        // 养宠经验
        String experience = (app.getExperience() != null) ? app.getExperience().trim() : "";
        if (!experience.isEmpty() && !experience.equals("无") && !experience.equals("无经验")) {
            sb.append("申请人有养宠经验，");
        } else {
            sb.append("申请人暂无明确养宠经验，");
        }

        // 居住环境
        String homeEnv = (app.getHomeEnv() != null) ? app.getHomeEnv().trim() : "";
        if (!homeEnv.isEmpty()) {
            if (homeEnv.contains("房") || homeEnv.contains("院") || homeEnv.contains("大") || homeEnv.contains("宽敞")) {
                sb.append("居住环境描述较好，");
            } else if (homeEnv.contains("合租") || homeEnv.contains("小") || homeEnv.contains("公寓")) {
                sb.append("居住环境为公寓或合租，需关注空间是否充足，");
            } else {
                sb.append("已提供居住环境描述，");
            }
        } else {
            sb.append("未提供居住环境描述，");
        }

        // 其他宠物
        if (Boolean.TRUE.equals(app.getHasOtherPet())) {
            sb.append("家中已有其他宠物，需注意与现有宠物的相处情况，");
        } else {
            sb.append("家中暂无其他宠物，环境相对更容易适应，");
        }

        // 收入水平
        String income = app.getIncomeLevel();
        if (income != null) {
            switch (income) {
                case "HIGH" -> sb.append("收入水平较高，有较好的经济保障，");
                case "MIDDLE" -> sb.append("收入水平中等，具备基本经济保障，");
                case "LOW" -> sb.append("收入水平偏低，需重点评估长期稳定性，");
                default -> sb.append("收入水平未明确，");
            }
        }

        // 动物简单特征
        if (animal != null && animal.getSpecies() != null) {
            sb.append("申请的动物为：").append(animal.getSpecies());
            if (animal.getBreed() != null) {
                sb.append("（").append(animal.getBreed()).append("）");
            }
            sb.append("。");
        }

        return sb.toString();
    }
}

