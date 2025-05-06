package tc.tlouro_c.entity.character.hero;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InitialStatsValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInitialStats {
    String message() default "Invalid initial stats";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
