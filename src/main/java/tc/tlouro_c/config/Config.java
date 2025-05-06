package tc.tlouro_c.config;

public class Config {

    private final String databaseUrl;
    private final boolean loggingOn;
    private final double enemySpawnProbability;
    private final int maxLevel;
    private UIType currentUIType;

    public UIType getCurrentUIType() { return currentUIType; }

    public void setCurrentUIType(UIType currentUIType) {
        this.currentUIType = currentUIType;
    }

    public boolean isLoggingOn() { return loggingOn; }

    public String getDatabaseUrl() { return databaseUrl; }

    public double getEnemySpawnProbability() {
        return enemySpawnProbability;
    }

    public int getMaxLevel() { return maxLevel; }

    // Singleton
    private Config() {
        databaseUrl = "jdbc:sqlite:database.db";
        loggingOn = false;
        enemySpawnProbability = 0.1;
        maxLevel = 8;
        currentUIType = UIType.CLI;
    }

    private static class Holder {
        private static final Config INSTANCE = new Config();
    }

    public static Config getInstance() {
        return Holder.INSTANCE;
    }
}
