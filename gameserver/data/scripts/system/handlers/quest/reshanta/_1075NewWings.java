/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package quest.reshanta;

import com.ne.gs.model.EmotionType;
import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.model.gameobjects.state.CreatureState;
import com.ne.gs.network.aion.serverpackets.SM_EMOTION;
import com.ne.gs.questEngine.handlers.QuestHandler;
import com.ne.gs.questEngine.model.QuestDialog;
import com.ne.gs.questEngine.model.QuestEnv;
import com.ne.gs.questEngine.model.QuestState;
import com.ne.gs.questEngine.model.QuestStatus;
import com.ne.gs.services.QuestService;
import com.ne.gs.utils.PacketSendUtility;

/**
 * @author Rhys2002
 * @reworked vlog
 */
public class _1075NewWings extends QuestHandler {

    private final static int questId = 1075;
    private boolean killed = false;

    public _1075NewWings() {
        super(questId);
    }

    @Override
    public void register() {
        int[] npcs = {278506, 279023, 278643};
        qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerOnLevelUp(questId);
        qe.registerQuestNpc(214102).addOnKillEvent(questId);
        for (int npc : npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);
        int targetId = env.getTargetId();
        QuestDialog dialog = env.getDialog();

        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 278506: { // Tellus
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        }
                        case SELECT_ACTION_1013: {
                            playQuestMovie(env, 272);
                            break;
                        }
                        case STEP_TO_1: {
                            return defaultCloseDialog(env, 0, 1); // 1
                        }
                    }
                    break;
                }
                case 279023: { // Agemonerk
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        }
                        case STEP_TO_2: {
                            player.setState(CreatureState.FLIGHT_TELEPORT);
                            player.unsetState(CreatureState.ACTIVE);
                            player.setFlightTeleportId(57001);
                            player.sendPck(new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 57001, 0));
                            return defaultCloseDialog(env, 1, 2); // 2
                        }
                    }
                    break;
                }
                case 278643: { // Raithor
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 2) {
                                return sendQuestDialog(env, 1693);
                            } else if (var == 3) {
                                if (killed) {
                                    return sendQuestDialog(env, 2034);
                                }
                            }
                        }
                        case STEP_TO_3: {
                            if (var == 2) {
                                QuestService.addNewSpawn(400010000, player.getInstanceId(), 214102, 2344.32f, 1789.96f, 2258.88f, (byte) 86);
                                QuestService.addNewSpawn(400010000, player.getInstanceId(), 214102, 2344.51f, 1786.01f, 2258.88f, (byte) 52);
                                return defaultCloseDialog(env, 2, 3); // 3
                            }
                        }
                        case STEP_TO_4: {
                            if (var == 3) {
                                player.setState(CreatureState.FLIGHT_TELEPORT);
                                player.unsetState(CreatureState.ACTIVE);
                                player.setFlightTeleportId(58001);
                                player.sendPck(new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 58001, 0));
                                qs.setQuestVarById(0, 12);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestSelectionDialog(env);
                            }
                        }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 279023) { // Agemonerk
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
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (env.getTargetId() == 214102) {
                if (var == 3) {
                    killed = true;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env, 1072);
    }

    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        int[] quests = {1701, 1072};
        return defaultOnLvlUpEvent(env, quests, true);
    }
}