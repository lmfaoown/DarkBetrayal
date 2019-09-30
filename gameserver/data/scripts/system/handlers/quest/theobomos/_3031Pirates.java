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
import com.ne.gs.services.QuestService;

/**
 * @author Balthazar
 */

public class _3031Pirates extends QuestHandler {

    private final static int questId = 3031;

    public _3031Pirates() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(730144).addOnQuestStart(questId);
        qe.registerQuestNpc(730144).addOnTalkEvent(questId);
        qe.registerQuestNpc(798172).addOnTalkEvent(questId);
        qe.registerQuestNpc(214219).addOnKillEvent(questId);
        qe.registerQuestNpc(214220).addOnKillEvent(questId);
        qe.registerQuestNpc(214222).addOnKillEvent(questId);
        qe.registerQuestNpc(214223).addOnKillEvent(questId);
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
            if (targetId == 730144) {
                switch (env.getDialog()) {
                    case START_DIALOG: {
                        return sendQuestDialog(env, 4762);
                    }
                    case STEP_TO_1: {
                        QuestService.startQuest(env);
                        player.sendPck(new SM_DIALOG_WINDOW(0, 0));
                        return true;
                    }
                    default:
                        return sendQuestStartDialog(env);
                }
            }
        }

        if (qs == null) {
            return false;
        }

        if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 798172) {
                if (env.getDialogId() == 1009) {
                    return sendQuestDialog(env, 5);
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

        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        }

        int targetId = 0;

        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }

        if (targetId == 214219 || targetId == 214220) {
            switch (qs.getQuestVarById(1)) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14: {
                    qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
                    updateQuestStatus(env);

                    if (qs.getQuestVarById(1) == 15 && qs.getQuestVarById(2) == 12) {
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return true;
                    }
                    return true;
                }
            }
        } else if (targetId == 214222 || targetId == 214223) {
            switch (qs.getQuestVarById(2)) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11: {
                    qs.setQuestVarById(2, qs.getQuestVarById(2) + 1);
                    updateQuestStatus(env);

                    if (qs.getQuestVarById(1) == 15 && qs.getQuestVarById(2) == 12) {
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return true;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}