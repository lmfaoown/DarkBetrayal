/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package quest.morheim;

import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.questEngine.handlers.QuestHandler;
import com.ne.gs.questEngine.model.QuestDialog;
import com.ne.gs.questEngine.model.QuestEnv;
import com.ne.gs.questEngine.model.QuestState;
import com.ne.gs.questEngine.model.QuestStatus;
import com.ne.gs.services.teleport.TeleportService;

/**
 * @author MrPoke
 * @modified Nephis, Gigi
 * @reworked vlog
 */
public class _2422LiquorThatMakesYouVanish extends QuestHandler {

    private final static int questId = 2422;

    public _2422LiquorThatMakesYouVanish() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(204326).addOnQuestStart(questId);
        qe.registerQuestNpc(204326).addOnTalkEvent(questId);
        qe.registerQuestNpc(204327).addOnTalkEvent(questId);
        qe.registerQuestNpc(204375).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 204326) { // Hapenill
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            switch (targetId) {
                case 204327: { // Sveinn
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        }
                        case STEP_TO_1: {
                            return defaultCloseDialog(env, 0, 1); // 1
                        }
                    }
                    break;
                }
                case 204375: { // Otis
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        }
                        case STEP_TO_2: {
                            qs.setQuestVar(2);
                            qs.setStatus(QuestStatus.REWARD);
                            updateQuestStatus(env);
                            TeleportService.teleportTo(player, 210020000, player.getInstanceId(), (float) 535.46, (float) 2555.62, (float) 326.63605,
                                (byte) 112);
                            return true;
                        }
                        case FINISH_DIALOG: {
                            return sendQuestSelectionDialog(env);
                        }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204326) { // Hapenill
                if (dialog == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 10002);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
}