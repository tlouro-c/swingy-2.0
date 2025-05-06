package tc.tlouro_c.view;

import tc.tlouro_c.GameContext;
import tc.tlouro_c.controller.event.EventBus;

public abstract class MainMenuView {

    private boolean needsRender;
    protected final EventBus eventBus;
    protected MainMenuViewState viewState;
    protected final GameContext gameContext;

    protected MainMenuView() {
        this.needsRender = true;
        this.eventBus = EventBus.getInstance();
        this.viewState = MainMenuViewState.INITIAL_SCREEN;
        this.gameContext = GameContext.getInstance();
    }

    protected MainMenuView(MainMenuViewState currentState) {
        this.needsRender = true;
        this.eventBus = EventBus.getInstance();
        this.viewState = currentState;
        this.gameContext = GameContext.getInstance();
    }


    public void cache() {
        needsRender = false;
    }

    public void invalidate() {
        needsRender = true;
    }

    public boolean needsRender() {
        return needsRender;
    }

    public MainMenuViewState getViewState() {
        return viewState;
    }

    public void render() {

        if (this.needsRender()) {
            this.cache();

            switch (viewState) {
                case INITIAL_SCREEN -> initialScreenView();
                case HERO_SELECTION -> heroSelectionView();
                case HERO_CREATION -> heroCreationView();
            }

        }
    }

    protected abstract void initialScreenView();

    protected abstract void heroSelectionView();

    protected abstract void heroCreationView();

    public void setViewState(MainMenuViewState viewState) {
        this.invalidate();
        this.viewState = viewState;
    }
}
