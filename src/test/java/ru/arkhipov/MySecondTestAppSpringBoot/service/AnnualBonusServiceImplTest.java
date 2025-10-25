package ru.arkhipov.MySecondTestAppSpringBoot.service;

import org.junit.jupiter.api.Test;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Positions;

import java.time.Year;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AnnualBonusServiceImplTest {

    @Test
    void calculate() {
        var position = Positions.HR;
        var salary = 100000.0;
        var bonus = 2.0;
        var workDays = 243;

        var result = new AnnualBonusServiceImpl().calculate(position, salary, bonus, workDays);

        var expected = Year.now().isLeap() ? 361481.48148148146 : 360493.8271604938;
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void calculateQuarterBonus(){
        var positionNotManager = Positions.DEV;
        var positionManager = Positions.TPM;
        var salary = 100000.0;
        var bonus = 2.0;
        var quarterBonusPercent = 0.3;

        var service = new AnnualBonusServiceImpl();

        var resultNotManager = service.calculateQuarterBonus(positionNotManager, salary, bonus, quarterBonusPercent);
        assertThat(resultNotManager).isZero();

        var resultManager = service.calculateQuarterBonus(positionManager, salary, bonus, quarterBonusPercent);
        var expected = 60000.0;
        assertThat(resultManager).isEqualTo(expected);
    }
}