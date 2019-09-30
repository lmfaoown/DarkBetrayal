/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package quest.sarpan;

import com.ne.commons.network.util.ThreadPoolManager;
import com.ne.gs.dataholders.DataManager;
import com.ne.gs.model.TeleportAnimation;
import com.ne.gs.model.gameobjects.Npc;
import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.network.aion.SystemMessageId;
import com.ne.gs.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.ne.gs.questEngine.QuestEngine;
import com.ne.gs.questEngine.handlers.QuestHandler;
import com.ne.gs.questEngine.model.QuestDialog;
import com.ne.gs.questEngine.model.QuestEnv;
import com.ne.gs.questEngine.model.QuestState;
import com.ne.gs.questEngine.model.QuestStatus;
import com.ne.gs.services.instance.InstanceService;
import com.ne.gs.services.teleport.TeleportService;
import com.ne.gs.skillengine.SkillEngine;
import com.ne.gs.world.World;
import com.ne.gs.world.WorldMapInstance;
import com.ne.gs.world.knownlist.Visitor;
import com.ne.gs.world.zone.ZoneName;

/**
 * @author zhkchi
 */
public class _20040TheGrandAssault extends QuestHandler {

    private final static int questId = 20040;

    public _20040TheGrandAssault() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerOnEnterWorld(questId);
        qe.registerOnLevelUp(questId);
        qe.registerQuestNpc(799225).addOnQuestStart(questId);
        qe.registerQuestNpc(800085).addOnTalkEvent(questId);
        qe.registerQuestNpc(799722).addOnTalkEvent(questId);
        qe.registerQuestNpc(800280).addOnTalkEvent(questId);
        qe.registerQuestNpc(205617).addOnTalkEvent(questId);
        qe.registerQuestNpc(730528).addOnTalkEvent(questId);
        qe.registerQuestNpc(205812).addOnAttackEvent(questId);
        qe.registerOnLeaveZone(ZoneName.get("SADONICS_CAPTAINS_CABIN_300410000"), questId);
        qe.registerOnLeaveZone(ZoneName.get("SADONICS_DECK_300410000"), questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        if (defaultOnLvlUpEvent(env)) {
            int[] ids = {20050, 20051, 20052, 20053};
            for (int id : ids) {
                QuestEngine.getInstance().onEnterZoneMissionEnd(new QuestEnv(env.getVisibleObject(), env.getPlayer(), id, env.getDialogId()));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        if (player.getWorldId() != 300410000) {
            if (qs != null && qs.getStatus() == QuestStatus.START) {
                int var = qs.getQuestVarById(0);
                if (var > 1) {
                    qs.setQuestVar(1);
                    updateQuestStatus(env);
                    player.sendPck(new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId)
                        .getName()));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        int targetId = env.getTargetId();

        if (qs == null) {
            if (targetId == 799225) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1011);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }

        if (qs == null) {
            return false;
        }

        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 799225 && var == 0) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1011);
                } else if (env.getDialog() == QuestDialog.STEP_TO_1) {
                    changeQuestStep(env, 0, 1, false);
                    return closeDialogWindow(env);
                } else {
                    return sendQuestStartDialog(env);
                }
            } else if (targetId == 800085 && var == 1) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1352);
                } else if (env.getDialog() == QuestDialog.STEP_TO_2) {
                    WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300410000);
                    InstanceService.registerPlayerWithInstance(newInstance, player);
                    TeleportService.teleportTo(player, 300410000, newInstance.getInstanceId(), 791, 745, 997, (byte) 2, TeleportAnimation.BEAM_ANIMATION);
                    changeQuestStep(env, 1, 2, false);
                    // playQuestMovie(env, 18);
                    return closeDialogWindow(env);
                } else {
                    return sendQuestStartDialog(env);
                }
            } else if (targetId == 799722 && var == 2) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1693);
                } else if (env.getDialog() == QuestDialog.SELECT_ACTION_1694) {
                    return sendQuestDialog(env, 1694);
                } else if (env.getDialog() == QuestDialog.STEP_TO_3) {
                    changeQuestStep(env, 2, 3, false);
                    return closeDialogWindow(env);
                } else {
                    return sendQuestStartDialog(env);
                }
            } else if (targetId == 730528 && var == 3) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    TeleportService.teleportTo(player, 300410000, player.getInstanceId(), 774f, 743f, 997f);
                    return true;
                } else {
                    return sendQuestStartDialog(env);
                }
            } else if (targetId == 800280) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 2716);
                } else if (env.getDialog() == QuestDialog.SELECT_ACTION_2717) {
                    return sendQuestDialog(env, 2717);
                } else if (env.getDialog() == QuestDialog.SET_REWARD) {
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    TeleportService.teleportTo(player, 600020000, 1, 1511, 1559, 1359, (byte) 70, TeleportAnimation.BEAM_ANIMATION);
                    return closeDialogWindow(env);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
            return false;
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 205617) {
                if (dialog == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }

    @Override
    public boolean onAttackEvent(final QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVars().getQuestVars();
            if (var == 3) {
                int targetId = env.getTargetId();

                if (targetId == 205812) {
                    qs.setQuestVar(4);

                    SkillEngine.getInstance().applyEffectDirectly(20412, player, player, 18000);

                    WorldMapInstance instance = InstanceService.getRegisteredInstance(300410000, player.getObjectId());
                    for (Npc npc : instance.getNpcs()) {
                        switch (npc.getNpcId()) {
                            case 800280:
                            case 800295:
                            case 800296:
                            case 800294:
                            case 800297:
                            case 800298:
                                break;
                            default:
                                SkillEngine.getInstance().applyEffectDirectly(20412, npc, npc, 18000);
                        }
                    }

                    messadges(env);
                    ThreadPoolManager.getInstance().schedule(new Runnable() {

                        @Override
                        public void run() {

                            WorldMapInstance instance = InstanceService.getRegisteredInstance(300410000, player.getObjectId());

                            for (Npc npc : instance.getNpcs()) {
                                switch (npc.getNpcId()) {
                                    case 800280:
                                    case 800295:
                                    case 800296:
                                    case 800294:
                                    case 800297:
                                    case 800298:
                                        npc.getEffectController().removeAllEffects();

                                        break;
                                    default:
                                        npc.getController().delete();
                                }
                            }
                            qs.setQuestVar(11);
                            updateQuestStatus(env);
                        }
                    }, 18000);
                }
            }
        }
        return false;
    }

    @Override
    public boolean onDieEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var > 1) {
                qs.setQuestVar(1);
                updateQuestStatus(env);
                player.sendPck(new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId)
                    .getName()));
                return true;
            }
        }
        return false;
    }

    private void messadges(QuestEnv env) {
        for (Npc npc : World.getInstance().getNpcs()) {
            int npcId = npc.getNpcId();
            int objectId = npc.getObjectId();
            switch (npcId) {
                case 800280:
                    sendMsg(1111387, objectId, true, 1000);
                    sendMsg(1111390, objectId, true, 12000);
                    sendMsg(1111391, objectId, true, 18000);
                    break;
                case 800294:
                    sendMsg(1111388, objectId, true, 4000);
                    break;
                case 800298:
                    sendMsg(1111389, objectId, true, 8000);
                    break;
                case 800297:
                    sendMsg(1111392, objectId, true, 15000);
                    break;
            }
        }
    }

    private void sendMsg(final int msg, final int Obj, final boolean isShout, int time) {
        ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {

                    @Override
                    public void visit(Player player) {
                        if (player.isOnline()) {
                            player.sendPck(new SM_SYSTEM_MESSAGE(isShout, msg, Obj, 0));
                        }
                    }
                });
            }

        }, time);
    }

    @Override
    public boolean onLeaveZoneEvent(QuestEnv env, ZoneName zoneName) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        }
        if (zoneName.equals(ZoneName.get("SADONICS_CAPTAINS_CABIN_300410000"))) {
            if (qs.getQuestVarById(0) < 3) {
                qs.setQuestVarById(0, 1);
                return true;
            }
        } else if (zoneName.equals(ZoneName.get("SADONICS_DECK_300410000"))) {
            qs.setQuestVarById(0, 1);
            return true;
        }
        return false;
    }
}