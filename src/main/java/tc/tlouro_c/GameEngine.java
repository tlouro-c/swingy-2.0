package tc.tlouro_c;

import tc.tlouro_c.controller.Controller;
import tc.tlouro_c.controller.GameplayController;
import tc.tlouro_c.controller.MainMenuController;
import tc.tlouro_c.controller.event.*;
import tc.tlouro_c.dao.HeroDAO;
import tc.tlouro_c.util.Logger;
import tc.tlouro_c.util.Window;

import static java.lang.Thread.sleep;

public class GameEngine {
    private Controller activeController;
    private final GameContext gameContext;

    public GameEngine() {
        activeController = new MainMenuController();
        gameContext = GameContext.getInstance();
    }

    public void start() {
        setupGlobalEvents();

        var lastController = activeController;
        lastController.setup();

        while (activeController != null) {
            if (lastController != activeController) {
                lastController.cleanup();
                lastController = activeController;
                activeController.setup();
            }
            activeController.render();
            try {
                sleep(16);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setupGlobalEvents() {

        EventBus eventBus = EventBus.getInstance();

        eventBus.subscribe(StartGameplayEvent.class,
                event -> {
                    Logger.debug("Gameplay Starting...");
                    gameContext.getPlayerModel().setSelectedHero(event.selectedHero());
                    activeController = new GameplayController();
                }
        );

        eventBus.subscribe(EndGameplayEvent.class,
                event -> {
                    HeroDAO.getInstance().save(gameContext.getPlayerModel().getSelectedHero());
                    activeController = new MainMenuController();
                }
        );

        eventBus.subscribe(CloseGameEvent.class,
                event -> {
                    activeController = null;
                    Window.getInstance().disposeFrame();
                    System.out.print(
                            """
                            Bye!
                            Closing the game...
                            """
                    );
                }
        );

        eventBus.subscribe(SwitchUIEvent.class,
                event -> {
                    activeController.switchUIType();
                }
        );
    }
}
