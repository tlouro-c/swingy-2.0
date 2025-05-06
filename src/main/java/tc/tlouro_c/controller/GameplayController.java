package tc.tlouro_c.controller;

import tc.tlouro_c.GameContext;
import tc.tlouro_c.config.Config;
import tc.tlouro_c.config.UIType;
import tc.tlouro_c.controller.event.*;
import tc.tlouro_c.exception.EnemyAppearedException;
import tc.tlouro_c.exception.MapFinishedException;
import tc.tlouro_c.model.BattleSimulator;
import tc.tlouro_c.model.MapModel;
import tc.tlouro_c.util.Logger;
import tc.tlouro_c.util.Window;
import tc.tlouro_c.view.*;

public class GameplayController implements Controller {

    private final GameContext gameContext;
    private GameplayView gameplayView;

    public GameplayController() {
        this.gameContext = GameContext.getInstance();
        this.gameplayView =
                Config.getInstance().getCurrentUIType() == UIType.CLI ?
                        new CliGameplayView() :
                        new GuiGameplayView();
    }

    public void setup() {
        if (gameContext.getPlayerModel() == null) {
            throw new IllegalStateException();
        }
        gameContext.setMapModel(new MapModel(gameContext.getPlayerModel()));

        var eventBus = EventBus.getInstance();

        eventBus.subscribe(PlayerMoveEvent.class,
                event -> {
                    try {
                        Logger.debug("Moving event triggered...");
                        var player = gameContext.getPlayerModel();
                        var map = gameContext.getMapModel();
                        player.attemptMove(event.direction(), map);
                        gameplayView.invalidate();
                    } catch (EnemyAppearedException e) {
                        gameplayView.setViewState(GameplayViewState.ENEMY_APPEARED_SCREEN);
                    } catch (MapFinishedException e) {
                        gameplayView.setViewState(GameplayViewState.MAP_FINISHED_SCREEN);
                    }
                });

        eventBus.subscribe(BattleSimulationEvent.class,
                event -> {
                    var player = gameContext.getPlayerModel();
                    var enemy = event.mapSquare().getEnemy().orElseThrow(IllegalStateException::new);
                    var result = BattleSimulator.simulate(player.getSelectedHero(), enemy);

                    switch (result) {
                        case PLAYER_WINS -> {
                            if (enemy.hasArtifact()) {
                                gameContext.setDroppedArtifact(enemy.getArtifact());
                            }
                            event.mapSquare().setEnemy(null);
                            gameplayView.setViewState(GameplayViewState.AFTER_BATTLE_SCREEN);
                        }
                        case PLAYER_DIES -> {
                            var hero = player.getSelectedHero();
                            hero.heal(hero.getMaxHitPoints());
                            gameplayView.setViewState(GameplayViewState.HERO_DIED_SCREEN);
                        }
                    }
                });

        eventBus.subscribe(PlayerRanEvent.class,
                event -> {
                    if (Math.random() >= 0.5) {
                        gameplayView.setViewState(GameplayViewState.UNABLE_TO_RUN_SCREEN);
                    } else {
                        gameplayView.setViewState(GameplayViewState.ABLE_TO_RUN_SCREEN);
                        var player = gameContext.getPlayerModel();
                        player.setCoordinates(player.getPreviousCoordinates());
                    }
                });

        eventBus.subscribe(PickUpArtifactEvent.class,
                event -> {
                    var hero = gameContext.getPlayerModel().getSelectedHero();
                    var artifact = event.artifact();
                    hero.setArtifact(artifact);
                    gameplayView.setViewState(GameplayViewState.MAP_SCREEN);
                    gameContext.setDroppedArtifact(null);
                });

        eventBus.subscribe(IgnoreArtifactEvent.class,
                event -> {
                    gameplayView.setViewState(GameplayViewState.MAP_SCREEN);
                    gameContext.setDroppedArtifact(null);
                });
    }

    public void cleanup() {
        // Get the EventBus instance
        var eventBus = EventBus.getInstance();

        // Remove all listeners registered by this controller
        eventBus.unsubscribe(PlayerMoveEvent.class);
        eventBus.unsubscribe(BattleSimulationEvent.class);
        eventBus.unsubscribe(PlayerRanEvent.class);
        eventBus.unsubscribe(PickUpArtifactEvent.class);
        eventBus.unsubscribe(IgnoreArtifactEvent.class);
    }

    @Override
    public void switchUIType() {
        var config = Config.getInstance();
        var currentType = config.getCurrentUIType();
        if (currentType == UIType.GUI) {
            Window.getInstance().disposeFrame();
        }
        var newType = currentType == UIType.CLI ? UIType.GUI : UIType.CLI;

        var currentViewState = gameplayView.getViewState();
        gameplayView = newType == UIType.CLI ? new CliGameplayView(currentViewState) : new GuiGameplayView(currentViewState);

        config.setCurrentUIType(newType);
    }


    @Override
    public void render() {
        gameplayView.render();
    }
}
