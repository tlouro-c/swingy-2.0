package tc.tlouro_c.dao;

import tc.tlouro_c.entity.artifact.Artifact;
import tc.tlouro_c.entity.artifact.ArtifactFactory;
import tc.tlouro_c.entity.artifact.ArtifactType;
import tc.tlouro_c.entity.character.CharacterType;
import tc.tlouro_c.entity.character.hero.Hero;
import tc.tlouro_c.entity.character.LevelState;
import tc.tlouro_c.util.DatabaseConnectionFactory;
import tc.tlouro_c.util.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HeroDAO {


    public void save(Hero hero) {
        Logger.debug("Saving hero on database...");
        String query = "INSERT OR REPLACE INTO heroes (id, name, type, attack_damage, armor, hit_points, max_hit_points) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (var conn = DatabaseConnectionFactory.getConnection();
             var pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Set basic hero attributes
            var id = hero.getId();
            if (id != -1) {
                pstmt.setInt(1, id);
            }
            pstmt.setString(2, hero.getName());
            pstmt.setString(3, hero.getType().toString());
            pstmt.setInt(4, hero.getAttackDamage());
            pstmt.setInt(5, hero.getArmor());
            pstmt.setInt(6, hero.getHitPoints());
            pstmt.setInt(7, hero.getMaxHitPoints());

            pstmt.executeUpdate();

            try (var generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    var heroId = generatedKeys.getInt(1);
                    hero.setId(heroId);
                    Logger.debug("Generated hero ID: " + heroId);
                } else {
                    throw new RuntimeException("Failed to retrieve hero ID after insert.");
                }
            }

            saveHeroLevel(conn, hero);
            saveHeroArtifact(conn, hero);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save hero: " + e.getMessage(), e);
        }
    }

    private void saveHeroLevel(Connection conn, Hero hero) throws SQLException {
        if (hero.getLevelState() == null) {
            throw new IllegalStateException();
        }

        String query = "INSERT OR REPLACE INTO hero_levels (hero_id, level, experience, next_level_experience) VALUES (?, ?, ?, ?)";

        try (var pstmt = conn.prepareStatement(query)) {

            var levelState = hero.getLevelState();

            pstmt.setInt(1, hero.getId());
            pstmt.setInt(2, levelState.getLevel());
            pstmt.setInt(3, levelState.getExperience());
            pstmt.setInt(4, levelState.getNextLevelExperience());

            pstmt.executeUpdate();
        }
    }

    private void saveHeroArtifact(Connection conn, Hero hero) throws SQLException {
        if (hero.getArtifact() == null) {
            return;
        }

        String query = "INSERT OR REPLACE INTO hero_artifacts (hero_id, name, level) VALUES (?, ?, ?)";

        try (var pstmt = conn.prepareStatement(query)) {

            var artifact = hero.getArtifact();

            pstmt.setInt(1, hero.getId());
            pstmt.setString(2, artifact.getName());
            pstmt.setInt(3, artifact.getLevel());

            pstmt.executeUpdate();
        }
    }

    public List<Hero> findAll() {
        Logger.debug("Fetching all heroes from the database...");

        List<Hero> heroes = new ArrayList<>();

        String query = "SELECT * FROM heroes";

        try (var conn = DatabaseConnectionFactory.getConnection();
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int heroId = rs.getInt("id");
                String name = rs.getString("name");
                String typeStr = rs.getString("type");
                CharacterType type = CharacterType.fromString(typeStr);
                int attackDamage = rs.getInt("attack_damage");
                int armor = rs.getInt("armor");
                int hitPoints = rs.getInt("hit_points");
                int maxHitPoints = rs.getInt("max_hit_points");

                var hero = new Hero(heroId, name, type, attackDamage, armor, hitPoints, maxHitPoints, null);

                Logger.debug(hero.getName());

                var heroLevelState = getHeroLevel(conn, hero);
                hero.setLevelState(heroLevelState);

                var heroArtifact = getHeroArtifact(conn, heroId);
                hero.setArtifact(heroArtifact);

                heroes.add(hero);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return heroes;
    }

    private LevelState getHeroLevel(Connection conn, Hero hero) {

        String levelQuery = "SELECT * FROM hero_levels WHERE hero_id = ? LIMIT 1";

        try (var levelStmt = conn.prepareStatement(levelQuery)) {
            levelStmt.setInt(1, hero.getId());
            try (var rs = levelStmt.executeQuery()) {
                if (rs.next()) {
                    int heroLevel = rs.getInt("level");
                    int experience = rs.getInt("experience");
                    int nextLevelExperience = rs.getInt("next_level_experience");
                    return new LevelState(hero, heroLevel, experience, nextLevelExperience);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error fetching hero level: " + e.getMessage());
        }

        return null;
    }

    private Artifact getHeroArtifact(Connection conn, int heroId) {

        String artifactQuery = "SELECT * FROM hero_artifacts WHERE hero_id = ? LIMIT 1";

        try (var artifactStmt = conn.prepareStatement(artifactQuery)) {
            artifactStmt.setInt(1, heroId);
            try (var rs = artifactStmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    ArtifactType type = ArtifactType.fromString(name.replace(' ', '_').toUpperCase());
                    int level = rs.getInt("level");
                    return ArtifactFactory.getInstance().createArtifact(type, level);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error fetching hero artifact: " + e.getMessage());
        }

        return null;
    }

    @SuppressWarnings("SqlNoDataSourceInspection")
    private void initialize() {
        String createHeroesTableSQL = """
            CREATE TABLE IF NOT EXISTS heroes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                attack_damage INTEGER,
                armor INTEGER,
                hit_points INTEGER,
                max_hit_points INTEGER,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
        """;

        String createHeroLevelsTableSQL = """
            CREATE TABLE IF NOT EXISTS hero_levels (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                hero_id INTEGER UNIQUE,
                level INTEGER,
                experience INTEGER,
                next_level_experience INTEGER,
                FOREIGN KEY (hero_id) REFERENCES heroes(id) ON DELETE CASCADE
            );
        """;

        String createHeroArtifactsTableSQL = """
            CREATE TABLE IF NOT EXISTS hero_artifacts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                hero_id INTEGER UNIQUE,
                name TEXT NOT NULL,
                level INTEGER,
                attack_damage_extra INTEGER,
                armor_extra INTEGER,
                hit_points_extra INTEGER,
                FOREIGN KEY (hero_id) REFERENCES heroes(id) ON DELETE CASCADE
            );
        """;

        try (var conn = DatabaseConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            // Execute each table creation individually
            stmt.execute(createHeroesTableSQL);
            stmt.execute(createHeroLevelsTableSQL);
            stmt.execute(createHeroArtifactsTableSQL);

        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }


    // Singleton
    private HeroDAO() { this.initialize(); }

    private static class Holder {
        private static final HeroDAO INSTANCE = new HeroDAO();
    }

    public static HeroDAO getInstance() {
        return Holder.INSTANCE;
    }
}
