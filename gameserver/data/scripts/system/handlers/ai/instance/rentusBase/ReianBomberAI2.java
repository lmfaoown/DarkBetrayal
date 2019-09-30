/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package ai.instance.rentusBase;

import java.util.concurrent.atomic.AtomicBoolean;
import ai.GeneralNpcAI2;

import com.ne.commons.network.util.ThreadPoolManager;
import com.ne.gs.ai2.AIName;
import com.ne.gs.ai2.AIState;
import com.ne.gs.ai2.AISubState;
import com.ne.gs.ai2.manager.WalkManager;
import com.ne.gs.model.EmotionType;
import com.ne.gs.model.gameobjects.Npc;
import com.ne.gs.network.aion.serverpackets.SM_EMOTION;
import com.ne.gs.skillengine.SkillEngine;
import com.ne.gs.utils.PacketSendUtility;
import com.ne.gs.world.WorldMapInstance;
import com.ne.gs.world.WorldPosition;

/**
 * @author xTz
 */
@AIName("reian_bomber")
public class ReianBomberAI2 extends GeneralNpcAI2 {

    private final AtomicBoolean hasArrivedBoss = new AtomicBoolean(false);
    private int position = 1;

    @Override
    protected void handleSpawned() {
        super.handleSpawned();
        getSpawnTemplate().setWalkerId("30028000024");
        WalkManager.startWalking(this);
        getOwner().setState(1);
        PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
    }

    @Override
    protected void handleMoveArrived() {
        int point = getOwner().getMoveController().getCurrentPoint();
        super.handleMoveArrived();
        if (hasArrivedBoss.get()) {
            startHelpEvent();
        } else if (point == 7 && hasArrivedBoss.compareAndSet(false, true)) {
            getSpawnTemplate().setWalkerId(null);
            WalkManager.stopWalking(this);
            startHelpEvent();
        }
    }

    private void startHelpEvent() {
        getMoveController().abortMove();
        setStateIfNot(AIState.IDLE);
        setSubStateIfNot(AISubState.NONE);
        SkillEngine.getInstance().getSkill(getOwner(), 19374, 60, getOwner()).useNoAnimationSkill();
        ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                if (!isAlreadyDead()) {
                    setSubStateIfNot(AISubState.WALK_RANDOM);
                    setStateIfNot(AIState.WALKING);
                    switch (position) {
                        case 1:
                            help(359.763f, 585.597f, 145.525f);
                            getMoveController().moveToPoint(346.47787f, 604.0337f, 145.8766f);
                            position++;
                            break;
                        case 2:
                            help(346.086f, 597.062f, 146.119f);
                            getMoveController().moveToPoint(370.93597f, 607.6427f, 145.41916f);
                            position++;
                            break;
                        case 3:
                            help(362.143f, 604.723f, 146.125f);
                            getMoveController().moveToPoint(361.7722f, 584.4937f, 145.63573f);
                            position = 1;
                            break;
                    }
                }
            }
        }, 8000);

    }

    private void deleteNpc(Npc npc) {
        if (npc != null) {
            npc.getController().onDelete();
        }
    }

    private void help(float x, float y, float z) {
        WorldMapInstance instance = getPosition().getWorldMapInstance();
        if (instance != null) {
            for (Npc npc : instance.getNpcs(282530)) {
                WorldPosition p = npc.getPosition();
                if (p.getX() == x && p.getY() == y) {
                    deleteNpc(npc);
                }
            }
            for (Npc npc : instance.getNpcs(282387)) {
                WorldPosition p = npc.getPosition();
                if (p.getX() == x && p.getY() == y) {
                    return;
                }
            }
            Npc npc = (Npc) spawn(282387, x, y, z, (byte) 0);
            SkillEngine.getInstance().getSkill(npc, 19731, 1, npc).useNoAnimationSkill();
        }
    }

}