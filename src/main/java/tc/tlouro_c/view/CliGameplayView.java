package tc.tlouro_c.view;

import tc.tlouro_c.controller.event.*;
import tc.tlouro_c.entity.Direction;
import tc.tlouro_c.entity.MapSquare;
import tc.tlouro_c.entity.character.hero.Hero;
import tc.tlouro_c.util.InputReader;

import java.util.Set;

public class CliGameplayView extends GameplayView {

    private final InputReader inputReader;

    public CliGameplayView() {
        this.inputReader = InputReader.getInstance();
    }

    public CliGameplayView(GameplayViewState currentState) {
        super(currentState);
        this.inputReader = InputReader.getInstance();
    }


    @Override
    protected void enemyAppearedView() {
        System.out.println("An enemy appeared...");
        inputReader.pressEnterToContinue();

        var mapModel = gameContext.getMapModel();
        var playerModel = gameContext.getPlayerModel();
        var currentSquare = mapModel.getMapSquare(playerModel.getCoordinates());
        var enemy = currentSquare.getEnemy().orElseThrow(IllegalStateException::new);

        System.out.printf("====== %s Lv.%d found! ======%n", enemy.getName(), enemy.getLevelState().getLevel());
        System.out.println("""
                [ 1 ]  Fight
                [ 2 ]  Run
                =======================
                """);

        var choice = inputReader.nextIntInRange(1, 2);

        switch (choice) {
            case 1 -> startFight(currentSquare);
            case 2 -> eventBus.publish(new PlayerRanEvent());
        }
    }

    private void startFight(MapSquare mapSquare) {
        System.out.println("The fight will start...");
        inputReader.pressEnterToContinue();
        eventBus.publish(new BattleSimulationEvent(mapSquare));
    }

    @Override
    protected void afterBattleView() {
        var hero = gameContext.getPlayerModel().getSelectedHero();

        System.out.println("You won the fight!");
        if (hero.getLevelState().isLevelUp()) {
            System.out.println("Level up!");
        }

        var droppedArtifact = gameContext.getDroppedArtifact();
        if (droppedArtifact != null) {
            System.out.printf("====== %s Lv.%d was dropped! ======%n", droppedArtifact.getName(), droppedArtifact.getLevel());
            System.out.println("""
                [ 1 ]  Pick Up
                [ 2 ]  Ignore
                =======================
                """);
            int choice = inputReader.nextIntInRange(1, 2);
            if (choice == 1) {
                eventBus.publish(new PickUpArtifactEvent(droppedArtifact));
                System.out.println("Picked up the artifact.");
            } else {
                eventBus.publish(new IgnoreArtifactEvent());
                System.out.println("Artifact Ignored.");
            }
        }

        inputReader.pressEnterToContinue();
        setViewState(GameplayViewState.MAP_SCREEN);
    }

    @Override
    protected void mapFinishedView() {
        System.out.println("You completed this map!");
        inputReader.pressEnterToContinue();
        eventBus.publish(new EndGameplayEvent());
    }

    @Override
    protected void heroDiedView() {
        System.out.println("Your hero died...");
        inputReader.pressEnterToContinue();
        eventBus.publish(new EndGameplayEvent());
    }

    @Override
    protected void unableToRunView() {
        System.out.println("You couldn't run!");
        inputReader.pressEnterToContinue();
        var mapModel = gameContext.getMapModel();
        var playerModel = gameContext.getPlayerModel();
        var currentSquare = mapModel.getMapSquare(playerModel.getCoordinates());
        startFight(currentSquare);
    }

    @Override
    protected void ableToRunView() {
        System.out.println("You were able to run!");
        inputReader.pressEnterToContinue();
        setViewState(GameplayViewState.MAP_SCREEN);
    }

    @Override
    protected void mapView() {

        var mapModel = gameContext.getMapModel();
        var playerModel = gameContext.getPlayerModel();

        var mapGrid = mapModel.getMapGrid();

        for (MapSquare[] row : mapGrid) {
            for (MapSquare square : row) {

                if (playerModel.getCoordinates().equals(square.getCoordinates())) {
                    System.out.print("\033[33mP \033[0m");
                } else if (square.hasEnemy()) {
                    System.out.print("\033[31mE \033[0m");
                } else {
                    System.out.print("0 ");
                }

            }
            System.out.println();
        }
        hud(playerModel.getSelectedHero());
        nextMovePrompt();
    }

    private void hud(Hero hero) {
        System.out.println("\n======= HERO STATUS =======");
        System.out.println("Name: " + hero.getName());
        System.out.println("Type: " + hero.getType());
        System.out.println("-----------------------------");
        System.out.println("⚔️ Attack Damage : " + hero.getAttackDamage());
        System.out.println("🛡️ Armor         : " + hero.getArmor());
        System.out.println("❤️ Hit Points    : " + hero.getHitPoints() + "/" + hero.getMaxHitPoints());
        System.out.println("🎖️ Level         : " + hero.getLevelState().getLevel());
        System.out.println("⭐ Experience    : " + hero.getLevelState().getExperience() + "/" + hero.getLevelState().getNextLevelExperience());

        if (hero.getArtifact() != null) {
            var artifact = hero.getArtifact();
            System.out.println("\n--- Equipped Artifact ---");
            System.out.println("🔮 Name: " + artifact.getName());
            System.out.println("🧪 Level: " + artifact.getLevel());
            System.out.println("⚡ Bonuses:");
            System.out.println("  + Attack Damage: " + artifact.getAttackDamageExtra());
            System.out.println("  + Armor        : " + artifact.getArmorExtra());
            System.out.println("  + Hit Points   : " + artifact.getHitPointsExtra());
        } else {
            System.out.println("\nNo Artifact equipped.");
        }

        System.out.println("=============================\n");
    }

    private void nextMovePrompt() {
        System.out.println("""
                Choose your next move:
                [W] Up  [A] Left  [S] Down  [D] Right
                
                [G] Switch to GUI
                """);

        var choice = inputReader.nextFromOptionsIgnoreCase(Set.of("w", "a", "s", "d", "g"));

        if (choice.equalsIgnoreCase("g")) {
            eventBus.publish(new SwitchUIEvent());
            return;
        }

        var direction = Direction.fromKey(choice.charAt(0))
                .orElseThrow(() -> new IllegalStateException("Unexpected input: " + choice));

        eventBus.publish(new PlayerMoveEvent(direction));
    }

}
