/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package com.ne.gs.network.aion.clientpackets;

import com.ne.gs.model.TaskId;
import com.ne.gs.model.gameobjects.Item;
import com.ne.gs.model.gameobjects.player.Equipment;
import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.model.gameobjects.state.CreatureState;
import com.ne.gs.network.aion.AionClientPacket;
import com.ne.gs.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.ne.gs.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import com.ne.gs.restrictions.RestrictionsManager;
import com.ne.gs.utils.PacketSendUtility;

/**
 * @author Avol modified by ATracer
 */
public class CM_EQUIP_ITEM extends AionClientPacket {

    public long slotRead;
    public int itemUniqueId;
    public int action;

    @Override
    protected void readImpl() {
        action = readC(); // 0/1 = equip/unequip
        slotRead = readQ();
        // 3.0
        //slotRead = readD();
        itemUniqueId = readD();
    }

    @Override
    protected void runImpl() {
        Player activePlayer = getConnection().getActivePlayer();

        activePlayer.getController().cancelUseItem();

        Equipment equipment = activePlayer.getEquipment();

        if (equipment == null || equipment.getOwner() == null) {
            activePlayer.sendMsg("equipment = " + (equipment == null));
            activePlayer.sendMsg("equipment.getOwner = " + (equipment.getOwner() == null));
            return;
        }

        Item resultItem = null;

        if (!RestrictionsManager.canChangeEquip(activePlayer)) {
            activePlayer.sendMsg("blocked.");
            return;
        }

        if (activePlayer.isInState(CreatureState.GLIDING)) {
            activePlayer.sendPck(SM_SYSTEM_MESSAGE.STR_CANT_EQUIP_ITEM_IN_ACTION);
            return;
        }

        activePlayer.sendMsg("action = " + action);
        activePlayer.sendMsg("itemUniqueId = " + 0);
        activePlayer.sendMsg("slotRead = " + slotRead);
        switch (action) {
            case 0:
                resultItem = equipment.equipItem(itemUniqueId, slotRead);
                break;
            case 1:
                resultItem = equipment.unEquipItem(itemUniqueId, slotRead);
                break;
            case 2:
                if (activePlayer.getController().hasTask(TaskId.ITEM_USE) && !activePlayer.getController().getTask(TaskId.ITEM_USE).isDone()) {
                    activePlayer.sendPck(SM_SYSTEM_MESSAGE.STR_CANT_EQUIP_ITEM_IN_ACTION);
                    return;
                }
                equipment.switchHands();
                break;
        }

        if (resultItem != null || action == 2) {
            PacketSendUtility.broadcastPacket(activePlayer, new SM_UPDATE_PLAYER_APPEARANCE(activePlayer.getObjectId(),
                            equipment.getEquippedForAppearance()), true);
        }

    }
}
