package tc.tlouro_c.controller;

import tc.tlouro_c.config.Config;
import tc.tlouro_c.config.UIType;
import tc.tlouro_c.controller.event.EventBus;
import tc.tlouro_c.controller.event.HeroCreationEvent;
import tc.tlouro_c.dao.HeroDAO;
import tc.tlouro_c.util.Logger;
import tc.tlouro_c.util.Window;
import tc.tlouro_c.view.*;

public class MainMenuController implements Controller {

    private MainMenuView mainMenuView;

    public MainMenuController() {
        this.mainMenuView =
                Config.getInstance().getCurrentUIType() == UIType.CLI ?
                        new CliMainMenuView() :
                        new GuiMainMenuView();
    }

    public void setup() {
        var eventBus = EventBus.getInstance();

        eventBus.subscribe(HeroCreationEvent.class,
                event -> HeroDAO.getInstance().save(event.hero())
        );
    }

    @Override
    public void cleanup() {

        var eventBus = EventBus.getInstance();

        eventBus.unsubscribe(HeroCreationEvent.class);
    }

    @Override
    public void switchUIType() {
        var config = Config.getInstance();
        var currentType = config.getCurrentUIType();
        if (currentType == UIType.GUI) {
            Window.getInstance().disposeFrame();
        }
        var newType = currentType == UIType.CLI ? UIType.GUI : UIType.CLI;


        var currentViewState = mainMenuView.getViewState();
        mainMenuView = newType == UIType.CLI ? new CliMainMenuView(currentViewState) : new GuiMainMenuView(currentViewState);

        config.setCurrentUIType(newType);
    }

    @Override
    public void render() {
        mainMenuView.render();
    }
}
