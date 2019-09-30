/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package com.ne.gs.network.aion.clientpackets;

import com.ne.gs.model.gameobjects.Pet;
import com.ne.gs.model.gameobjects.PetEmote;
import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.network.aion.AionClientPacket;
import com.ne.gs.network.aion.serverpackets.SM_PET_EMOTE;
import com.ne.gs.utils.PacketSendUtility;
import com.ne.gs.world.World;

/**
 * @author ATracer
 */
public class CM_PET_EMOTE extends AionClientPacket {

    private PetEmote emote;
    private int emoteId;

    private float x1;
    private float y1;
    private float z1;

    private byte h;

    private float x2;
    private float y2;
    private float z2;

    private int emotionId;
    private int unk2;

    @Override
    protected void readImpl() {
        emoteId = readC();
        emote = PetEmote.getEmoteById(emoteId);

        switch (emote) {
            case MOVE_STOP:
                x1 = readF();
                y1 = readF();
                z1 = readF();
                h = readSC();
                break;
            case MOVETO:
                x1 = readF();
                y1 = readF();
                z1 = readF();
                h = readSC();
                x2 = readF();
                y2 = readF();
                z2 = readF();
                break;
            default:
                emotionId = readC();
                unk2 = readC();
        }
    }

    @Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        Pet pet = player.getPet();

        if (pet == null) {
            return;
        }

        // sometimes client is crazy enough to send -2.4457384E7 as z coordinate
        // TODO (check retail) either its client bug or packet problem somewhere
        // reproducible by flying randomly and falling from long height with fly resume
        if (x1 < 0 || y1 < 0 || z1 < 0) {
            return;
        }
        // log.info("CM_PET_EMOTE emote {}, unk1 {}, unk2 {}", new Object[] { emoteId, unk1, unk2 });
        switch (emote) {
            case UNKNOWN:
                break;
            case ALARM:
                PacketSendUtility.broadcastPacket(player, new SM_PET_EMOTE(pet, emote), true);
                break;
            case MOVE_STOP:
                World.getInstance().updatePosition(pet, x1, y1, z1, h);
                PacketSendUtility.broadcastPacket(player, new SM_PET_EMOTE(pet, emote, x1, y1, z1, h), true);
                break;
            case MOVETO:
                World.getInstance().updatePosition(pet, x1, y1, z1, h);
                pet.getMoveController().setNewDirection(x2, y2, z2, h);
                PacketSendUtility.broadcastPacket(player, new SM_PET_EMOTE(pet, emote, x1, y1, z2, x2, y2, z2, h), true);
                break;
            case FLY:
                PacketSendUtility.broadcastPacket(player, new SM_PET_EMOTE(pet, emote, emotionId, unk2), true);
                break;
            default:
                if (emotionId > 0) {
                    player.sendPck(new SM_PET_EMOTE(pet, emote, emotionId, unk2));
                } else {
                    PacketSendUtility.broadcastPacket(player, new SM_PET_EMOTE(pet, emote, 0, unk2), true);
                }
        }
    }
}