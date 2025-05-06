package tc.tlouro_c.entity.character.hero;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tc.tlouro_c.util.Logger;

public class InitialStatsValidator implements ConstraintValidator<ValidInitialStats, HeroBuilder> {

    @Override
    public void initialize(ValidInitialStats constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(HeroBuilder heroBuilder, ConstraintValidatorContext constraintValidatorContext) {
        Logger.debug("Running validation");
        return heroBuilder.getArmorExtra() + heroBuilder.getMaxHitPointsExtra() + heroBuilder.getAttackDamageExtra() <= 30;
    }
}
