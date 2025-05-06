package tc.tlouro_c.view;

import tc.tlouro_c.controller.event.CloseGameEvent;
import tc.tlouro_c.controller.event.HeroCreationEvent;
import tc.tlouro_c.controller.event.StartGameplayEvent;
import tc.tlouro_c.controller.event.SwitchUIEvent;
import tc.tlouro_c.dao.HeroDAO;
import tc.tlouro_c.entity.character.CharacterType;
import tc.tlouro_c.entity.character.hero.Hero;
import tc.tlouro_c.entity.character.hero.HeroBuilder;
import tc.tlouro_c.exception.InvalidHeroCreationException;
import tc.tlouro_c.util.InputReader;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CliMainMenuView extends MainMenuView {

    private final InputReader inputReader;

    public CliMainMenuView() {
        inputReader = InputReader.getInstance();
    }

    public CliMainMenuView(MainMenuViewState currentState) {
        super(currentState);
        this.inputReader = InputReader.getInstance();
    }

    @Override
    protected void initialScreenView() {
        System.out.println("""
                ====== Welcome to Swingy ======
                [ 1 ]  Create a new hero
                [ 2 ]  Select an existing hero
                [ 3 ]  Close the game
                [ 4 ]  Switch to GUI
                ===============================
                """);

        int input = inputReader.nextIntInRange(1, 4);

        switch (input) {
            case 1 -> setViewState(MainMenuViewState.HERO_CREATION);
            case 2 -> setViewState(MainMenuViewState.HERO_SELECTION);
            case 3 -> eventBus.publish(new CloseGameEvent());
            case 4 -> eventBus.publish(new SwitchUIEvent());
        }
    }

    @Override
    protected void heroSelectionView() {
        System.out.println("====== Existing heroes ======");

        var existingHeroes = HeroDAO.getInstance().findAll();

        int index = 1;
        for (var hero : existingHeroes) {
            System.out.printf("[ %d ]  %s\n", index, hero);
            index++;
        }
        System.out.printf("[ %d ]  Go back\n", index);
        System.out.println("=======================");

        int input = inputReader.nextIntInRange(1, index);

        if (input == index) {
            setViewState(MainMenuViewState.INITIAL_SCREEN);
        } else {
            eventBus.publish(new StartGameplayEvent(existingHeroes.get(input - 1)));
        }
    }

    @Override
    protected void heroCreationView() {
        System.out.println("""
                ====== Create a hero ======
                Each hero begins with the following base stats:
                  - 50 Attack
                  - 50 Defense
                  - 200 Hit Points
                
                You have an additional 30 points to allocate as you choose.
                Your class selection will also influence the overall stats.
                
                Press Enter to continue...
                """);

        var remainingPoints = new AtomicInteger(30);
        var builder = new HeroBuilder();
        Optional<Hero> hero = Optional.empty();

        while (hero.isEmpty()) {
            try {
                hero = Optional.of(builder
                        .name(collectName())
                        .type(collectType())
                        .attackDamageExtra(collectAttackDamageExtra(remainingPoints))
                        .armorExtra(collectArmorExtra(remainingPoints))
                        .maxHitPointsExtra(collectMaxHitPointsExtra(remainingPoints))
                        .build());
            } catch (InvalidHeroCreationException e) {
                System.out.println(e.getMessage());
            }
        }

        eventBus.publish(new HeroCreationEvent(hero.get()));
        System.out.println("Hero created successfully: " + hero.get());

        setViewState(MainMenuViewState.INITIAL_SCREEN);
    }

    private String collectName() {
        System.out.println("Hero Name (min 3, max 20 characters)");
        return inputReader.nextStringInRange(3, 20);
    }


    private CharacterType collectType() {
        System.out.println("Hero Type");
        for (CharacterType type : CharacterType.values()) {
            System.out.printf("[ %d ] %s\n", type.ordinal() + 1, type.name());
        }

        int choice = inputReader.nextIntInRange(1, CharacterType.values().length);
        var type = CharacterType.values()[choice - 1];
        System.out.println("Selected: " + type.name());
        return type;
    }

    private int collectAttackDamageExtra(AtomicInteger remainingPoints) {
        System.out.println("Attack Damage (remaining " + remainingPoints + " points)");
        int points = inputReader.nextIntInRange(0, remainingPoints.get());

        remainingPoints.addAndGet(-points);  // Deduct allocated points from remaining points
        return points;
    }


    private int collectArmorExtra(AtomicInteger remainingPoints) {
        System.out.println("Armor (remaining " + remainingPoints + " points)");
        int points = inputReader.nextIntInRange(0, remainingPoints.get());

        remainingPoints.addAndGet(-points);  // Deduct allocated points from remaining points
        return points;
    }


    private int collectMaxHitPointsExtra(AtomicInteger remainingPoints) {
        System.out.println("Max Hit Points (remaining " + remainingPoints + " points)");
        int points = inputReader.nextIntInRange(0, remainingPoints.get());

        remainingPoints.addAndGet(-points);  // Deduct allocated points from remaining points
        return points;
    }

}
