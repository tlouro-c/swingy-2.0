package tc.tlouro_c.entity.character.hero;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tc.tlouro_c.entity.character.CharacterType;
import tc.tlouro_c.exception.InvalidHeroCreationException;

public class HeroBuilderTest  {

    @Test
    public void build() {

        var heroBuilder = new HeroBuilder();

        heroBuilder.armorExtra(30)
                .attackDamageExtra(30)
                .maxHitPointsExtra(30)
                .type(CharacterType.BRUISER)
                .name("Demo");

        var heroBuilder2 = new HeroBuilder();

        heroBuilder2.armorExtra(10)
                .attackDamageExtra(10)
                .maxHitPointsExtra(10)
                .type(CharacterType.BRUISER)
                .name("De");

        var heroBuilder3 = new HeroBuilder();

        heroBuilder3.armorExtra(10)
                .attackDamageExtra(10)
                .maxHitPointsExtra(10)
                .type(CharacterType.BRUISER)
                .name("Demo");

        Assertions.assertThrows(InvalidHeroCreationException.class, heroBuilder2::build);

        Assertions.assertThrows(InvalidHeroCreationException.class, heroBuilder::build);

        Assertions.assertDoesNotThrow(heroBuilder3::build);

    }
}