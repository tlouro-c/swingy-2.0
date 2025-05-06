package tc.tlouro_c.view;

import tc.tlouro_c.controller.event.*;
import tc.tlouro_c.entity.Direction;
import tc.tlouro_c.entity.MapSquare;
import tc.tlouro_c.entity.artifact.Artifact;
import tc.tlouro_c.util.Logger;
import tc.tlouro_c.util.StyledComponents;
import tc.tlouro_c.util.Window;
import tc.tlouro_c.view.map.MapPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GuiGameplayView extends GameplayView {

    private final JFrame frame;
    private final ControlsKeyListener controls = new ControlsKeyListener();
    private boolean controlsEnabled;
    private final JPanel interactionPanel;
    private final JPanel mapPanel;

    public GuiGameplayView() {
        var window = Window.getInstance();

        if (window.getFrame() == null) {
            window.mountNewFrame();
        }

        this.frame = window.getFrame();
        this.interactionPanel = new JPanel(new BorderLayout());
        this.interactionPanel.setBackground(new Color(40, 40, 55));
        this.interactionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(70, 70, 90)),
                BorderFactory.createEmptyBorder(12, 10, 10, 10)
        ));
        this.interactionPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 120));

        this.mapPanel = new MapPanel(gameContext);
        mapPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 90), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        mapPanel.setBackground(new Color(30, 30, 40));
    }

    public GuiGameplayView(GameplayViewState currentState) {
        super(currentState);
        var window = Window.getInstance();

        if (window.getFrame() == null) {
            window.mountNewFrame();
        }

        this.frame = window.getFrame();
        this.interactionPanel = new JPanel(new BorderLayout());
        this.interactionPanel.setBackground(new Color(40, 40, 55));
        this.interactionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(70, 70, 90)),
                BorderFactory.createEmptyBorder(12, 10, 10, 10)
        ));
        this.interactionPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 120));

        this.mapPanel = new MapPanel(gameContext);
        mapPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 90), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        mapPanel.setBackground(new Color(30, 30, 40));

    }

    private class ControlsKeyListener implements KeyListener {

        public void keyPressed(KeyEvent e) {
            switch (charToLower(e.getKeyChar())) {
                case 'w' -> eventBus.publish(new PlayerMoveEvent(Direction.NORTH));
                case 's' -> eventBus.publish(new PlayerMoveEvent(Direction.SOUTH));
                case 'a' -> eventBus.publish(new PlayerMoveEvent(Direction.WEST));
                case 'd' -> eventBus.publish(new PlayerMoveEvent(Direction.EAST));
            }
        }
        private char charToLower(char c) {
            return c > 'A' && c < 'Z' ? (char)(c + 32) : c;
        }
        public void keyTyped(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}
    }

    private void enableControls() {
        if (!controlsEnabled) {
            frame.addKeyListener(controls);
            frame.requestFocusInWindow();
            controlsEnabled = true;
        }
    }

    private void disableControls() {
        if (controlsEnabled) {
            frame.removeKeyListener(controls);
            controlsEnabled = false;
        }
    }

    @Override
    protected void mapView() {
        frame.getContentPane().removeAll();

        // Main container with a dark game-themed background
        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setBackground(new Color(30, 30, 40));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Game header with styled title
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);

        // Center content panel (map + HUD)
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setOpaque(false);

        // Map panel with border
        mapPanel.repaint();
        contentPanel.add(mapPanel, BorderLayout.CENTER);

        // HUD panel on the right
        JPanel hudPanel = createHudPanel();
        hudPanel.setPreferredSize(new Dimension(frame.getWidth() / 3, Integer.MAX_VALUE));
        contentPanel.add(hudPanel, BorderLayout.EAST);

        mainContainer.add(contentPanel, BorderLayout.CENTER);

        interactionPanel.removeAll();
        interactionPanel.add(StyledComponents.textLabel("Welcome to the dungeon! Use WASD to move around.", Color.WHITE));
        interactionPanel.revalidate();
        interactionPanel.repaint();
        mainContainer.add(interactionPanel, BorderLayout.SOUTH);

        enableControls();
        frame.add(mainContainer);
        frame.revalidate();
        frame.repaint();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        // Game title
        JLabel gameTitle = StyledComponents.titleLabel("SWINGY GAMEPLAY", new Color(220, 180, 50));
        panel.add(gameTitle, BorderLayout.CENTER);

        // Controls hint
        JLabel controlsHint = new JLabel("W(↑) A(←) S(↓) D(→)");
        controlsHint.setFont(new Font("Monospaced", Font.BOLD, 14));
        controlsHint.setForeground(new Color(180, 180, 180));
        controlsHint.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(controlsHint, BorderLayout.SOUTH);

        JButton switchUiButton = new JButton("CLI");
        switchUiButton.addActionListener(e -> eventBus.publish(new SwitchUIEvent()));
        panel.add(switchUiButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createHudPanel() {
        // Hero info
        var hero = gameContext.getPlayerModel().getSelectedHero();

        // Create the main HUD panel
        JPanel hudPanel = new JPanel();
        hudPanel.setLayout(new BoxLayout(hudPanel, BoxLayout.Y_AXIS));
        hudPanel.setBackground(new Color(40, 40, 55));
        hudPanel.setPreferredSize(new Dimension(220, 400));
        hudPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 90), 2),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel portraitLabel = StyledComponents.textLabel(hero.getName() + " (" + hero.getType() + ")", Color.WHITE);
        portraitLabel.setHorizontalAlignment(SwingConstants.CENTER);

        hudPanel.add(portraitLabel);
        hudPanel.add(Box.createVerticalStrut(16));

        // Stats section title
        JLabel statsTitle = new JLabel("STATS");
        statsTitle.setFont(new Font("Dialog", Font.BOLD, 16));
        statsTitle.setForeground(new Color(220, 180, 50));
        statsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        hudPanel.add(statsTitle);
        hudPanel.add(Box.createVerticalStrut(8));

        // XP bar
        var levelState = hero.getLevelState();
        hudPanel.add(StyledComponents.textLabel("Level " + levelState.getLevel(), Color.WHITE));
        JProgressBar xpBar = createProgressBar(levelState.getExperience(), levelState.getNextLevelExperience());
        hudPanel.add(xpBar);
        hudPanel.add(Box.createVerticalStrut(4));
        JLabel levelLabel = StyledComponents.textLabel(levelState.getExperience() + " / " + levelState.getNextLevelExperience(), Color.WHITE);
        hudPanel.add(levelLabel);
        hudPanel.add(Box.createVerticalStrut(8));

        // HP bar
        hudPanel.add(StyledComponents.textLabel("Health:", Color.WHITE));
        JProgressBar hpBar = createProgressBar(hero.getHitPoints(), hero.getMaxHitPoints());
        hudPanel.add(hpBar);
        hudPanel.add(Box.createVerticalStrut(4));
        JLabel hpLabel = StyledComponents.textLabel(hero.getHitPoints() + " / " + hero.getMaxHitPoints(), Color.WHITE);
        hudPanel.add(hpLabel);
        hudPanel.add(Box.createVerticalStrut(8));

        // Attack stat
        hudPanel.add(StyledComponents.textLabel("Attack:", Color.WHITE));
        hudPanel.add(Box.createVerticalStrut(4));
        JLabel atkLabel = StyledComponents.textLabel("DMG: " + hero.getAttackDamage(), Color.WHITE);
        hudPanel.add(atkLabel);
        hudPanel.add(Box.createVerticalStrut(8));

        // Armor stat
        hudPanel.add(StyledComponents.textLabel("Armor:", Color.WHITE));
        hudPanel.add(Box.createVerticalStrut(4));
        JLabel armLabel = StyledComponents.textLabel("DEF: " + hero.getArmor(), Color.WHITE);
        hudPanel.add(armLabel);

        if (hero.getArtifact() != null) {
            var a = hero.getArtifact();
            hudPanel.add(StyledComponents.verticalSpace(8));
            JLabel title = new JLabel("Artifact: " + a.getName());
            title.setFont(new Font("Dialog", Font.BOLD, 16));
            title.setForeground(new Color(220, 180, 50));
            title.setAlignmentX(Component.LEFT_ALIGNMENT);
            hudPanel.add(title);
            hudPanel.add(StyledComponents.verticalSpace(8));
            hudPanel.add(StyledComponents.textLabel("Bonus ATK" +  " +" + a.getAttackDamageExtra(), Color.WHITE));
            hudPanel.add(StyledComponents.textLabel("Bonus ARM" + " +" + a.getArmorExtra(), Color.WHITE));
            hudPanel.add(StyledComponents.textLabel("Bonus HP" +  " +" + a.getHitPointsExtra(), Color.WHITE));
        }

        return hudPanel;
    }

    private JProgressBar createProgressBar(int value, int max) {
        JProgressBar bar = new JProgressBar(0, max);
        bar.setValue(value);
        bar.setStringPainted(false);
        bar.setBackground(new Color(60, 60, 75));
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(190, 15));
        bar.setMaximumSize(new Dimension(190, 15));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        return bar;
    }


    @Override
    protected void enemyAppearedView() {
        disableControls();
        interactionPanel.removeAll();

        var mapModel = gameContext.getMapModel();
        var playerModel = gameContext.getPlayerModel();
        var currentSquare = mapModel.getMapSquare(playerModel.getCoordinates());
        var enemy = currentSquare.getEnemy().orElseThrow(IllegalStateException::new);

        var enemyAppearedLabel = StyledComponents.textLabel("A Lv." + enemy.getLevelState().getLevel() + " " + enemy.getName() + " has appeared!", Color.WHITE);
        var fightButton = StyledComponents.button("Fight");
        var runButton = StyledComponents.button("Run");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
        buttonPanel.setOpaque(false);
        buttonPanel.add(fightButton);
        buttonPanel.add(runButton);

        fightButton.addActionListener(e -> startFightView(currentSquare));

        runButton.addActionListener(e -> eventBus.publish(new PlayerRanEvent()));

        interactionPanel.add(enemyAppearedLabel, BorderLayout.CENTER);
        interactionPanel.add(buttonPanel, BorderLayout.SOUTH);

        interactionPanel.revalidate();
        interactionPanel.repaint();

    }

    private void startFightView(MapSquare mapSquare) {
        interactionPanel.removeAll();

        var label = StyledComponents.textLabel("The fight will start...", Color.WHITE);
        var startFightButton = StyledComponents.button("Continue");

        startFightButton.addActionListener(e -> eventBus.publish(new BattleSimulationEvent(mapSquare)));

        interactionPanel.add(label, BorderLayout.CENTER);
        interactionPanel.add(startFightButton, BorderLayout.SOUTH);

        interactionPanel.revalidate();
        interactionPanel.repaint();
    }

    @Override
    protected void afterBattleView() {
        interactionPanel.removeAll();
        var hero = gameContext.getPlayerModel().getSelectedHero();

        var label = StyledComponents.textLabel("You won the fight! " + (hero.getLevelState().isLevelUp() ? "\nLevel Up!" : ""), Color.WHITE);
        var continueButton = StyledComponents.button("Continue");

        var droppedArtifact = gameContext.getDroppedArtifact();

        if (droppedArtifact != null) {
            continueButton.addActionListener(e -> artifactDroppedView(droppedArtifact));
        } else {
            continueButton.addActionListener(e -> setViewState(GameplayViewState.MAP_SCREEN));
        }

        interactionPanel.add(label, BorderLayout.CENTER);
        interactionPanel.add(continueButton, BorderLayout.SOUTH);

        interactionPanel.revalidate();
        interactionPanel.repaint();
    }

    private void artifactDroppedView(Artifact artifact) {
        interactionPanel.removeAll();

        var label = StyledComponents.textLabel("A Lv." + artifact.getLevel() + " " + artifact.getName() + " has appeared!", Color.WHITE);
        var pickUpButton = StyledComponents.button("Pick up");
        var ignoreButton = StyledComponents.button("Ignore");

        pickUpButton.addActionListener(e -> eventBus.publish(new PickUpArtifactEvent(artifact)));

        ignoreButton.addActionListener(e -> eventBus.publish(new IgnoreArtifactEvent()));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
        buttonPanel.setOpaque(false);
        buttonPanel.add(pickUpButton);
        buttonPanel.add(ignoreButton);


        interactionPanel.add(label, BorderLayout.CENTER);
        interactionPanel.add(buttonPanel, BorderLayout.SOUTH);

        interactionPanel.revalidate();
        interactionPanel.repaint();
    }

    @Override
    protected void mapFinishedView() {
        disableControls();
        frame.getContentPane().removeAll();

        frame.add(StyledComponents.titleLabel("You finished the map!", Color.BLACK), BorderLayout.CENTER);

        JButton backButton = StyledComponents.button("Back to main menu");
        backButton.addActionListener(e -> eventBus.publish(new EndGameplayEvent()));

        frame.add(backButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();

    }

    @Override
    protected void heroDiedView() {
        disableControls();
        frame.getContentPane().removeAll();
        frame.add(StyledComponents.titleLabel("Your hero died...", Color.BLACK), BorderLayout.CENTER);

        JButton backButton = StyledComponents.button("Back to main menu");
        backButton.addActionListener(e -> eventBus.publish(new EndGameplayEvent()));

        frame.add(backButton, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void unableToRunView() {
        interactionPanel.removeAll();
        var label = StyledComponents.textLabel("You were unable to run!", Color.WHITE);
        var continueButton = StyledComponents.button("Continue");

        var mapModel = gameContext.getMapModel();
        var playerModel = gameContext.getPlayerModel();
        var currentSquare = mapModel.getMapSquare(playerModel.getCoordinates());

        continueButton.addActionListener(e -> startFightView(currentSquare));
        interactionPanel.add(label, BorderLayout.CENTER);
        interactionPanel.add(continueButton, BorderLayout.SOUTH);

        interactionPanel.revalidate();
        interactionPanel.repaint();
    }

    @Override
    protected void ableToRunView() {
        interactionPanel.removeAll();

        var label = StyledComponents.textLabel("You were able to run!", Color.WHITE);
        var continueButton = StyledComponents.button("Continue");

        continueButton.addActionListener(e -> setViewState(GameplayViewState.MAP_SCREEN));

        interactionPanel.add(label, BorderLayout.CENTER);
        interactionPanel.add(continueButton, BorderLayout.SOUTH);

        interactionPanel.revalidate();
        interactionPanel.repaint();
    }


}
