/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package quest.altgard;

import com.ne.gs.model.gameobjects.Npc;
import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.ne.gs.questEngine.handlers.QuestHandler;
import com.ne.gs.questEngine.model.QuestDialog;
import com.ne.gs.questEngine.model.QuestEnv;
import com.ne.gs.questEngine.model.QuestState;
import com.ne.gs.questEngine.model.QuestStatus;

/**
 * @author Ritsu
 */
public class _2266ATrustworthyMessenger extends QuestHandler {

    private final static int questId = 2266;

    public _2266ATrustworthyMessenger() {
        super(questId);
    }

    @Override
    public void register() {

        qe.registerQuestNpc(203558).addOnQuestStart(questId);
        qe.registerQuestNpc(203558).addOnTalkEvent(questId);
        qe.registerQuestNpc(203655).addOnTalkEvent(questId);
        qe.registerQuestNpc(203654).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 203558) {
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1011);
                } else if (dialog == QuestDialog.ACCEPT_QUEST) {
                    if (!giveQuestItem(env, 182203244, 1)) {
                        return true;
                    }
                    return sendQuestStartDialog(env);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (targetId == 203655) {
                switch (dialog) {
                    case START_DIALOG:
                        if (var == 0) {
                            return sendQuestDialog(env, 1352);
                        }
                    case STEP_TO_1:
                        if (var == 0) {
                            qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                            updateQuestStatus(env);
                            player.sendPck(new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                }
            }
            if (targetId == 203654) {
                switch (dialog) {
                    case START_DIALOG:
                        if (var == 1) {
                            qs.setQuestVar(3);
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                            return sendQuestDialog(env, 2375);
                        }
                    case SELECT_REWARD:
                        if (var == 3) {
                            removeQuestItem(env, 182203244, 1);
                            return sendQuestEndDialog(env);
                        }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            return sendQuestEndDialog(env);
        }
        return false;
    }
}