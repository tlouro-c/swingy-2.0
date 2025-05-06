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
import tc.tlouro_c.util.Logger;
import tc.tlouro_c.util.StyledComponents;
import tc.tlouro_c.util.Window;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GuiMainMenuView extends MainMenuView {

    private final JFrame frame;

    public GuiMainMenuView() {
        var window = Window.getInstance();

        if (window.getFrame() == null) {
            window.mountNewFrame();
        }

        this.frame = window.getFrame();
    }

    public GuiMainMenuView(MainMenuViewState currentState) {
        super(currentState);
        var window = Window.getInstance();

        if (window.getFrame() == null) {
            window.mountNewFrame();
        }

        this.frame = window.getFrame();

    }


    @Override
    protected void heroCreationView() {
        frame.getContentPane().removeAll();
        frame.add(StyledComponents.titleLabel("Create Your Hero", Color.BLACK), BorderLayout.NORTH);

        // Main panel with consistent border padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        // Name field with consistent sizing
        JLabel nameLabel = StyledComponents.textLabel("Hero Name", Color.BLACK);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameField);
        mainPanel.add(Box.createVerticalStrut(16));

        // Character Class Dropdown with consistent sizing
        JLabel classLabel = StyledComponents.textLabel("Hero Class", Color.BLACK);
        classLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(classLabel);

        JComboBox<CharacterType> classSelector = new JComboBox<>(CharacterType.values());
        classSelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        classSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(classSelector);
        mainPanel.add(Box.createVerticalStrut(16));

        // Stat allocation section
        var remainingPoints = new AtomicInteger(30);
        JLabel remainingLabel = StyledComponents.textLabel("Remaining Points: 30", Color.BLACK);
        remainingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(remainingLabel);
        mainPanel.add(Box.createVerticalStrut(16));

        // Create sliders with consistent dimensions
        JSlider atkSlider = statSlider();
        JSlider defSlider = statSlider();
        JSlider hpSlider = statSlider();

        // Add stat rows with consistent spacing
        mainPanel.add(statRow("Attack", atkSlider));
        mainPanel.add(Box.createVerticalStrut(16));
        mainPanel.add(statRow("Armor", defSlider));
        mainPanel.add(Box.createVerticalStrut(16));
        mainPanel.add(statRow("HP", hpSlider));
        mainPanel.add(Box.createVerticalStrut(16));


        // --- Control Buttons ---
        JButton createButton = StyledComponents.button("Create Hero");
        createButton.addActionListener(e -> {
            try {
                Hero hero = new HeroBuilder()
                        .name(nameField.getText().trim())
                        .type((CharacterType) classSelector.getSelectedItem())
                        .attackDamageExtra(atkSlider.getValue())
                        .armorExtra(defSlider.getValue())
                        .maxHitPointsExtra(hpSlider.getValue())
                        .build();

                eventBus.publish(new HeroCreationEvent(hero));
                setViewState(MainMenuViewState.INITIAL_SCREEN);

                JOptionPane.showMessageDialog(frame, "Hero created successfully!");
            } catch (InvalidHeroCreationException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Invalid Hero", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = StyledComponents.button("Back");
        backButton.addActionListener(e -> setViewState(MainMenuViewState.INITIAL_SCREEN));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
        buttonPanel.add(backButton);
        buttonPanel.add(createButton);

        // Update label on changes
        ChangeListener updateRemaining = e -> {
            int total = atkSlider.getValue() + defSlider.getValue() + hpSlider.getValue();
            remainingPoints.set(30 - total);
            remainingLabel.setText("Remaining Points: " + remainingPoints.get());

            createButton.setEnabled(remainingPoints.get() >= 0);
        };
        atkSlider.addChangeListener(updateRemaining);
        defSlider.addChangeListener(updateRemaining);
        hpSlider.addChangeListener(updateRemaining);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }

    private JPanel statRow(String label, JSlider slider) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));  // Add spacing between label and slider
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel statLabel = StyledComponents.textLabel(label, Color.BLACK);
        statLabel.setPreferredSize(new Dimension(80, 20));  // Consistent label width
        panel.add(statLabel, BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);

        return panel;
    }

    private JSlider statSlider() {
        JSlider slider = new JSlider(0, 30, 0);
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setOpaque(false);

        return slider;
    }

    @Override
    protected void heroSelectionView() {
        frame.getContentPane().removeAll();
        frame.add(StyledComponents.titleLabel("Choose Your Hero", Color.BLACK), BorderLayout.NORTH);

        var selectedHero = new AtomicReference<Hero>();
        var existingHeroes = HeroDAO.getInstance().findAll();

        // --- Hero List Panel ---
        JPanel heroListPanel = new JPanel();
        heroListPanel.setLayout(new BoxLayout(heroListPanel, BoxLayout.Y_AXIS));
        heroListPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // --- Preview Hero Panel ---
        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
        previewPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // --- Control Buttons ---
        JButton startGameButton = StyledComponents.button("Start Game");
        startGameButton.setEnabled(false);
        startGameButton.addActionListener(e -> {
            eventBus.publish(new StartGameplayEvent(selectedHero.get()));
        });

        JButton backButton = StyledComponents.button("Back");
        backButton.addActionListener(e -> setViewState(MainMenuViewState.INITIAL_SCREEN));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
        buttonPanel.add(backButton);
        buttonPanel.add(startGameButton);

        for (var hero : existingHeroes) {
            JButton heroButton = new JButton(hero.getName());
            heroButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            heroButton.addActionListener(e -> {
                selectedHero.set(hero);
                updateHeroPreview(previewPanel, hero);
                startGameButton.setEnabled(true);
            });
            heroListPanel.add(heroButton);
            heroListPanel.add(StyledComponents.verticalSpace(8));
        }

        // --- Split Pane to prevent layout jumps ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, heroListPanel, previewPanel);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setResizeWeight(0.5); // Balance both panels
        splitPane.setDividerSize(16);
        splitPane.setEnabled(false); // optional: prevent dragging

        frame.add(splitPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }


    private void updateHeroPreview(JPanel previewPanel, Hero hero) {
        previewPanel.removeAll();
        previewPanel.setLayout(new GridBagLayout()); // Centering

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(300, 400));
        card.setBackground(new Color(45, 45, 60));
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // --- Header ---
        JLabel nameLabel = new JLabel(hero.getName().toUpperCase());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLabel);

        JLabel classLabel = new JLabel(hero.getType().name());
        classLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        classLabel.setForeground(Color.GRAY);
        classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(classLabel);

        // --- Level & XP ---
        card.add(StyledComponents.verticalSpace(16));
        card.add(labelSection("Hero Information"));
        card.add(StyledComponents.verticalSpace(16));
        card.add(labelPair("Level", String.valueOf(hero.getLevelState().getLevel())));
        card.add(labelPair("XP", hero.getLevelState().getExperience() + " / " +
                                 hero.getLevelState().getNextLevelExperience()));

        // --- Stats ---
        card.add(labelPair("Attack", String.valueOf(hero.getAttackDamage())));
        card.add(labelPair("Armor", String.valueOf(hero.getArmor())));
        card.add(labelPair("Health", hero.getHitPoints() + " / " + hero.getMaxHitPoints()));

        // --- Artifact ---
        if (hero.getArtifact() != null) {
            var a = hero.getArtifact();
            card.add(StyledComponents.verticalSpace(16));
            card.add(labelSection("Artifact: " + a.getName()));
            card.add(StyledComponents.verticalSpace(16));
            card.add(labelPair("Bonus ATK", "+" + a.getAttackDamageExtra()));
            card.add(labelPair("Bonus ARM", "+" + a.getArmorExtra()));
            card.add(labelPair("Bonus HP", "+" + a.getHitPointsExtra()));
        }

        // --- Add to preview ---
        previewPanel.add(card);
        previewPanel.revalidate();
        previewPanel.repaint();
    }


    private Component labelPair(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel key = new JLabel(label + ":");
        key.setForeground(new Color(180, 180, 180));
        key.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel val = new JLabel(value);
        val.setForeground(Color.WHITE);
        val.setFont(new Font("SansSerif", Font.BOLD, 13));
        val.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(key, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        return row;
    }

    private Component labelSection(String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(new Color(140, 170, 255));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    @Override
    protected void initialScreenView() {
        Logger.debug("GUI Initial Screen");

        // Clear existing components
        frame.getContentPane().removeAll();

        // Title Label
        JLabel title = new JLabel("Welcome to Swingy", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 100, 50, 100));
        buttonPanel.setOpaque(false);

        // Create Buttons
        JButton createHeroBtn = StyledComponents.button("Create a New Hero");
        JButton selectHeroBtn = StyledComponents.button("Select an Existing Hero");
        JButton closeGameBtn = StyledComponents.button("Close the Game");
        JButton switchUIButton = StyledComponents.button("Switch to CLI");

        // Add actions
        createHeroBtn.addActionListener(e -> setViewState(MainMenuViewState.HERO_CREATION));
        selectHeroBtn.addActionListener(e -> setViewState(MainMenuViewState.HERO_SELECTION));
        closeGameBtn.addActionListener(e -> eventBus.publish(new CloseGameEvent()));
        switchUIButton.addActionListener(e -> eventBus.publish(new SwitchUIEvent()));

        // Add buttons with spacing
        buttonPanel.add(createHeroBtn);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(selectHeroBtn);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(closeGameBtn);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(switchUIButton);

        // Add components to frame
        frame.add(title, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Add components to frame
        frame.add(title, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Refresh frame
        frame.revalidate();
        frame.repaint();
    }


}
