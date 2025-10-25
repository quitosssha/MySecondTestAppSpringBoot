package ru.arkhipov.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Positions;

import java.time.Year;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {

    @Override
    public double calculate(Positions position, double salary, double bonus, int workDays){
        var daysInYear = Year.now().isLeap() ? 366 : 365;
        return salary * bonus * daysInYear * position.getPositionCoefficient() / workDays;
    }

    @Override
    public double calculateQuarterBonus(Positions position, double salary, double bonus, double quarterBonusPercent) {
        if (!position.isManager())
            return 0;
        return salary * bonus * quarterBonusPercent;
    }

}
