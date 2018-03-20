package ClaimCalculator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrossoverCalculatorTest {

    private Integer coinsuranceDays;
    private Integer coveredDays;
    private Double medicaidRate;
    private Double medicarePaidAmount;
    private Double medicareRate;
    private Double medicareSequestration;

    @BeforeEach
    void setUp() {
        coinsuranceDays = 1;
        coveredDays = 1;
        medicaidRate = 205.00;
        medicarePaidAmount = 100.00;
        medicareRate = 20.00;
        medicareSequestration = 5.00;
    }

    @AfterEach
    void tearDown() {
        coinsuranceDays = null;
        coveredDays = null;
        medicaidRate = null;
        medicarePaidAmount = null;
        medicareRate = null;
        medicareSequestration = null;
    }

    @Test
    void getMedicarePaidPerDiem() {
        Assertions.assertEquals(125, CrossoverCalculator.getMedicarePaidPerDiem(
                coinsuranceDays, coveredDays, medicarePaidAmount, medicareRate, medicareSequestration
        ));
    }

    @Test
    void getNetOwed() {
        Assertions.assertEquals(100, CrossoverCalculator.getNetOwed(
                coinsuranceDays, coveredDays, medicaidRate, medicarePaidAmount, medicareRate,
                medicareSequestration
        ));
    }
}