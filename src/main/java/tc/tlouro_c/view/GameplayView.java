package tc.tlouro_c.view;

import tc.tlouro_c.GameContext;
import tc.tlouro_c.controller.event.EventBus;
import tc.tlouro_c.util.Logger;

public abstract class GameplayView {

    private boolean needsRender;
    protected final EventBus eventBus;
    protected GameplayViewState viewState;
    protected final GameContext gameContext;

    protected GameplayView() {
        this.needsRender = true;
        this.eventBus = EventBus.getInstance();
        this.viewState = GameplayViewState.MAP_SCREEN;
        this.gameContext = GameContext.getInstance();
    }

    protected GameplayView(GameplayViewState currentState) {
        this.needsRender = true;
        this.eventBus = EventBus.getInstance();
        this.viewState = currentState;
        this.gameContext = GameContext.getInstance();
    }

    public void cache() {
        needsRender = false;
    }

    public void invalidate() {
        Logger.debug("Invalidating view.");
        needsRender = true;
    }

    public GameplayViewState getViewState() {
        return viewState;
    }

    public boolean needsRender() {
        return needsRender;
    }

    public void setViewState(GameplayViewState viewState) {
        this.invalidate();
        this.viewState = viewState;
    }

    public void render() {

        if (this.needsRender()) {
            this.cache();

            switch (viewState) {
                case MAP_SCREEN -> mapView();
                case ENEMY_APPEARED_SCREEN -> enemyAppearedView();
                case AFTER_BATTLE_SCREEN -> afterBattleView();
                case MAP_FINISHED_SCREEN -> mapFinishedView();
                case HERO_DIED_SCREEN -> heroDiedView();
                case UNABLE_TO_RUN_SCREEN -> unableToRunView();
                case ABLE_TO_RUN_SCREEN -> ableToRunView();
            }
        }

    }

    protected abstract void mapView() ;
    protected abstract void enemyAppearedView();
    protected abstract void afterBattleView();
    protected abstract void mapFinishedView();
    protected abstract void heroDiedView();
    protected abstract void unableToRunView();
    protected abstract void ableToRunView();
}
