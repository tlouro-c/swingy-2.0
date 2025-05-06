package tc.tlouro_c.entity.character.hero;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tc.tlouro_c.entity.character.CharacterType;
import tc.tlouro_c.exception.InvalidHeroCreationException;
import tc.tlouro_c.util.ValidatorUtil;

import java.util.Set;

@ValidInitialStats
public class HeroBuilder {

    @NotBlank(message = "Hero name must not be empty")
    @Size(min = 3, max = 20, message = "Hero name must have between {min} and {max} characters")
    private String name;

    private CharacterType type;

    public int getAttackDamageExtra() {
        return attackDamageExtra;
    }

    public int getArmorExtra() {
        return armorExtra;
    }

    public int getMaxHitPointsExtra() {
        return maxHitPointsExtra;
    }

    private int attackDamageExtra;
    private int armorExtra;
    private int maxHitPointsExtra;

    public HeroBuilder name(String name) {
        this.name = name;
        return this;
    }

    public HeroBuilder type(CharacterType type) {
        this.type = type;
        return this;
    }

    public HeroBuilder attackDamageExtra(int attackDamageExtra) {
        this.attackDamageExtra = attackDamageExtra;
        return this;
    }

    public HeroBuilder armorExtra(int armorExtra) {
        this.armorExtra = armorExtra;
        return this;
    }

    public HeroBuilder maxHitPointsExtra(int maxHitPointsExtra) {
        this.maxHitPointsExtra = maxHitPointsExtra;
        return this;
    }

    public Hero build() throws InvalidHeroCreationException {

        Set<ConstraintViolation<HeroBuilder>> violations = ValidatorUtil.validate(this);

        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            violations.forEach(v -> errorMessages.append("- ").append(v.getMessage()).append("\n"));
            throw new InvalidHeroCreationException(errorMessages.toString());
        }

        return new Hero(name, type, attackDamageExtra, armorExtra, maxHitPointsExtra, null);
    }
}
