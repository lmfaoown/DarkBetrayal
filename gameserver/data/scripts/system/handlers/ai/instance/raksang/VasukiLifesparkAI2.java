/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package ai.instance.raksang;

import java.util.concurrent.atomic.AtomicBoolean;
import ai.AggressiveNpcAI2;

import com.ne.commons.network.util.ThreadPoolManager;
import com.ne.gs.ai2.AI2Actions;
import com.ne.gs.ai2.AIName;
import com.ne.gs.ai2.poll.AIAnswer;
import com.ne.gs.ai2.poll.AIAnswers;
import com.ne.gs.ai2.poll.AIQuestion;
import com.ne.gs.model.gameobjects.Creature;
import com.ne.gs.model.gameobjects.Npc;
import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.services.NpcShoutsService;
import com.ne.gs.skillengine.SkillEngine;
import com.ne.gs.utils.MathUtil;

/**
 * @author xTz
 */
@AIName("vasuki_lifespark")
public class VasukiLifesparkAI2 extends AggressiveNpcAI2 {

    private final AtomicBoolean startedEvent = new AtomicBoolean(false);
    private boolean think = false;

    @Override
    public boolean canThink() {
        return think;
    }

    @Override
    protected void handleSpawned() {
        if (getNpcId() == 217764) {
            think = true;
        } else {
            ThreadPoolManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    if (!isAlreadyDead()) {
                        SkillEngine.getInstance().getSkill(getOwner(), 19126, 46, getOwner()).useNoAnimationSkill();
                    }
                }

            }, 3000);
        }
        super.handleSpawned();
    }

    @Override
    protected void handleCreatureMoved(Creature creature) {
        if (creature instanceof Player) {
            Player player = (Player) creature;
            if (MathUtil.getDistance(getOwner(), player) <= 30) {
                if (startedEvent.compareAndSet(false, true)) {
                    final int level;
                    int shoutId;
                    int skill;
                    switch (getNpcId()) {
                        case 217760:
                            skill = 19972;
                            level = 45;
                            shoutId = 1401107;
                            break;
                        case 217761:
                            skill = 19972;
                            level = 46;
                            shoutId = 1401171;
                            break;
                        case 217763:
                            skill = 20087;
                            level = 46;
                            shoutId = 0;
                            break;
                        default:
                            skill = 20039;
                            level = 46;
                            shoutId = 1401110;
                            break;
                    }
                    if (shoutId != 0) {
                        sendMsg(shoutId);
                    }
                    SkillEngine.getInstance().getSkill(getOwner(), skill, level, getOwner()).useNoAnimationSkill();
                    if (getNpcId() != 217764) {
                        ThreadPoolManager.getInstance().schedule(new Runnable() {

                            @Override
                            public void run() {
                                if (!isAlreadyDead()) {
                                    if (getNpcId() == 217763) {
                                        getPosition().getWorldMapInstance().getDoors().get(219).setOpen(true);
                                    }
                                    SkillEngine.getInstance().getSkill(getOwner(), 19967, level, getOwner()).useNoAnimationSkill();
                                    ThreadPoolManager.getInstance().schedule(new Runnable() {

                                        @Override
                                        public void run() {
                                            if (!isAlreadyDead()) {
                                                AI2Actions.deleteOwner(VasukiLifesparkAI2.this);
                                            }
                                        }

                                    }, 3500);

                                }
                            }

                        }, 3500);
                    } else {
                        SkillEngine.getInstance().getSkill(getOwner(), 19974, 46, getOwner()).useNoAnimationSkill();
                    }
                }
            }
        }
    }

    private void sendMsg(int msg) {
        NpcShoutsService.getInstance().sendMsg(getOwner(), msg, getObjectId(), false, 0, 0);
    }

    @Override
    public AIAnswer ask(AIQuestion question) {
        switch (question) {
            case CAN_RESIST_ABNORMAL:
                return AIAnswers.POSITIVE;
            default:
                return AIAnswers.NEGATIVE;
        }
    }

    @Override
    protected void handleDied() {
        if (getNpcId() == 217764) {
            sendMsg(1401111);
            Npc soul = getPosition().getWorldMapInstance().getNpc(217471);
            Npc sapping = getPosition().getWorldMapInstance().getNpc(217472);
            if (soul != null) {
                soul.getEffectController().removeEffect(19126);
            }
            if (sapping != null) {
                sapping.getEffectController().removeEffect(19126);
            }
            NpcShoutsService.getInstance().sendMsg(getOwner(), 1401140);
        }
        super.handleDied();
    }

}