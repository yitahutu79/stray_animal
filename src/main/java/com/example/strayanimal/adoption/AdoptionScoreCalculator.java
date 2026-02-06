package com.example.strayanimal.adoption;

import com.example.strayanimal.animal.Animal;

public class AdoptionScoreCalculator {

    /**
     * 评分规则（原始分，之后换算成 0-100）：
     * - 是否有其他宠物：无 10 分，有 5 分
     * - 收入水平：HIGH 20，MIDDLE 10，LOW/其他 5 分
     * - 是否有养宠经验：有 20 分，无 0 分
     * - 动物种类：狗 +5 分，其它 0 分
     * 原始满分为 55 分，最后线性折算到 100 分制。
     */
    public int calculate(AdoptionApplication app, Animal animal) {
        int raw = 0;

        // 是否有其他宠物
        if (Boolean.TRUE.equals(app.getHasOtherPet())) {
            raw += 5;
        } else {
            raw += 10;
        }

        // 收入水平
        if (app.getIncomeLevel() != null) {
            switch (app.getIncomeLevel()) {
                case "HIGH" -> raw += 20;
                case "MIDDLE" -> raw += 10;
                default -> raw += 5;
            }
        }

        // 养宠经验
        if (app.getExperience() != null && !app.getExperience().isBlank()) {
            raw += 20;
        }

        // 动物种类简单加分
        if (animal != null && "狗".equals(animal.getSpecies())) {
            raw += 5;
        }

        // 将原始 0-55 分折算到 0-100 分
        double scaled = raw * 100.0 / 55.0;
        return (int) Math.round(Math.min(100, Math.max(0, scaled)));
    }
}

