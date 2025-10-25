package ru.arkhipov.MySecondTestAppSpringBoot.service;

import ru.arkhipov.MySecondTestAppSpringBoot.model.Positions;

public interface AnnualBonusService {
    double calculate(Positions position, double salary, double bonus, int workDays);

    double calculateQuarterBonus(Positions position, double salary, double bonus, double quarterBonusPercent);
}
