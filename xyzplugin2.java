package asahi2986github.com.xyzplugin2;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class XYZPlugin extends JavaPlugin implements CommandExecutor {

    private Map<UUID, Long> commandCooldowns = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("XYZPlugin has been enabled.");
        getCommand("xyz").setExecutor(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("XYZPlugin has been disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("xyz") && sender instanceof Player) {
            Player player = (Player) sender;

            long currentTime = System.currentTimeMillis();
            if (!commandCooldowns.containsKey(player.getUniqueId()) || currentTime - commandCooldowns.get(player.getUniqueId()) >= 30000) {
                double x = player.getLocation().getX();
                double y = player.getLocation().getY();
                double z = player.getLocation().getZ();

                String message = String.format("%sさんの座標は X=%.2f, Y=%.2f, Z=%.2f です！", player.getName(), x, y, z);
                getServer().broadcastMessage(message);

                commandCooldowns.put(player.getUniqueId(), currentTime);
            } else {
                long remainingCooldown = (commandCooldowns.get(player.getUniqueId()) + 30000 - currentTime) / 1000;
                player.sendMessage("コマンドのクールダウン中です。" + remainingCooldown + "秒後に再試行してください。");
            }

            return true;
        }
        return false;
    }
}