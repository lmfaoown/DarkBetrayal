/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package quest.theobomos;

import com.ne.gs.model.gameobjects.Npc;
import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.ne.gs.questEngine.handlers.QuestHandler;
import com.ne.gs.questEngine.model.QuestEnv;
import com.ne.gs.questEngine.model.QuestState;
import com.ne.gs.questEngine.model.QuestStatus;

/**
 * @author Balthazar
 */

public class _3013AClueLeftByTheDead extends QuestHandler {

    private final static int questId = 3013;

    public _3013AClueLeftByTheDead() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(798132).addOnQuestStart(questId);
        qe.registerQuestNpc(798132).addOnTalkEvent(questId);
        qe.registerQuestNpc(798146).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 798132) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        return sendQuestDialog(env, 4762);
                    }
                    default:
                        return sendQuestStartDialog(env);
                }
            }
        }

        if (qs == null) {
            return false;
        }

        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 798132: {
                    switch (env.getDialog()) {
                        case START_DIALOG: {
                            if (qs.getQuestVarById(0) == 0) {
                                long itemCount = player.getInventory().getItemCountByItemId(182208008);
                                if (itemCount >= 1) {
                                    return sendQuestDialog(env, 1011);
                                } else {
                                    return sendQuestDialog(env, 10001);
                                }
                            }
                        }
                        case CHECK_COLLECTED_ITEMS: {
                            removeQuestItem(env, 182208008, 1);
                            qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                            updateQuestStatus(env);
                            player.sendPck(new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                    }
                }
                case 798146: {
                    switch (env.getDialog()) {
                        case START_DIALOG: {
                            if (qs.getQuestVarById(0) == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        }
                        case SET_REWARD: {
                            updateQuestStatus(env);
                            qs.setStatus(QuestStatus.REWARD);
                            player.sendPck(new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 798132) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        return sendQuestDialog(env, 10002);
                    }
                    case SELECT_REWARD: {
                        return sendQuestDialog(env, 5);
                    }
                    default:
                        return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
}