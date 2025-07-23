package ai.medusa.anticheat.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public final class ColorUtil {

    public String translate(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
