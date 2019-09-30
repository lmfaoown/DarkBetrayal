/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package ai.fountain;

import com.ne.commons.utils.Rnd;
import com.ne.gs.ai2.AIName;
import com.ne.gs.ai2.NpcAI2;
import com.ne.gs.configs.main.AIConfig;
import com.ne.gs.configs.main.RateConfig;
import com.ne.gs.model.Race;
import com.ne.gs.model.gameobjects.Item;
import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.model.gameobjects.player.RewardType;
import com.ne.gs.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.ne.gs.services.item.ItemService;


@AIName("platinum_fountain")
public class PlatinumFountainAI2 extends NpcAI2 {

    @Override
    protected void handleDialogStart(Player player) {
        if (!AIConfig.PLATINUM_FOUNTAIN_ENABLE) {
            player.sendPck(new SM_DIALOG_WINDOW(getObjectId(), 10));
        } else {
            if (player.getCommonData().getLevel() >= 50) {
                player.sendPck(new SM_DIALOG_WINDOW(getObjectId(), 1011, questByRace(player)));
            } else {
                player.sendPck(new SM_DIALOG_WINDOW(getObjectId(), 1011));
            }
        }

    }

    @Override
    public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
        switch (dialogId) {
            case 10000:
                if (hasItem(player, 186000030)) {
                    player.sendPck(new SM_DIALOG_WINDOW(getOwner().getObjectId(), 5, questByRace(player)));
                } else {
                    player.sendPck(new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
                }
                break;
            case 18:
                Item item = player.getInventory().getFirstItemByItemId(186000030);
                player.getInventory().decreaseByObjectId(item.getObjectId(), 1);
                giveItem(player);
                int exp = AIConfig.PLATINUM_FOUNTAIN_EXP_REWARD;

                int Membership0 = (int) (exp * RateConfig.QUEST_XP_RATE);
                int Membership1 = (int) (exp * RateConfig.PREMIUM_QUEST_XP_RATE);
                int Membership2 = (int) (exp * RateConfig.VIP_QUEST_XP_RATE);

                if (player.getPlayerAccount().getMembership() == 1) {
                    player.getCommonData().addExp(Membership1, RewardType.QUEST);
                } else if (player.getPlayerAccount().getMembership() == 2) {
                    player.getCommonData().addExp(Membership2, RewardType.QUEST);
                } else {
                    player.getCommonData().addExp(Membership0, RewardType.QUEST);
                }
                player.sendPck(new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1008, questByRace(player)));
                break;
        }
        return true;
    }

    private boolean hasItem(Player player, int itemId) {
        return player.getInventory().getItemCountByItemId(itemId) > 0;
    }

    private int questByRace(Player player) {
        if (player.getRace() == Race.ASMODIANS) {
            return 21050;
        } else {
            return 11050;
        }
    }

    private void giveItem(Player player) {
        if (Rnd.chance(AIConfig.PLATINUM_FOUNTAIN_SUCCESS_CHANCE)) {
            ItemService.addItem(player, 186000096, 1);
        } else {
            ItemService.addItem(player, 182005205, 1);
        }
    }
}