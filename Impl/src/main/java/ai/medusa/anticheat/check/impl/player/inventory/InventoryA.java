package ai.medusa.anticheat.check.impl.player.inventory;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;

@CheckInfo(name = "Inventory (A)", description = "Checks for sprinting in inventory.", experimental = true)
public final class InventoryA extends Check {

    public InventoryA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            if (data.getActionProcessor().isInventory()) {
                if (data.getActionProcessor().isSprinting()) {
                    if (++buffer > 5) {
                        fail();
                        if (buffer >= 20) {
                            data.getPlayer().closeInventory();
                            buffer = 0;
                            data.getActionProcessor().setInventory(false);
                        }
                    }
                } else {
                    buffer -= buffer > 0 ? 1 : 0;
                }
            }
        }
    }
}
