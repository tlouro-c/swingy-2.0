package tc.tlouro_c;

import tc.tlouro_c.config.Config;
import tc.tlouro_c.config.UIType;

public class App
{
    public static void main( String[] args ) {

       if (args.length != 1) {
           showUsageAndExit();
       }

       String modeArg = args[0].trim().toLowerCase();
    
        var mode = switch (modeArg) {
            case "gui" -> UIType.GUI;
            case "console" -> UIType.CLI;
            default -> {
                showUsageAndExit();
                yield null;
            }
        };
        Config.getInstance().setCurrentUIType(mode);

        new GameEngine().start();
    }

    private static void showUsageAndExit() {
        System.err.println("❗ Please specify the user interface mode: [GUI / CLI]");
        System.exit(1);
    }
}
