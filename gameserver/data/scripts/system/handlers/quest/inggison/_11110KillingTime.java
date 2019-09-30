/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package quest.inggison;

import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.questEngine.handlers.QuestHandler;
import com.ne.gs.questEngine.model.QuestDialog;
import com.ne.gs.questEngine.model.QuestEnv;
import com.ne.gs.questEngine.model.QuestState;
import com.ne.gs.questEngine.model.QuestStatus;

/**
 * Kill Enemies of the Abandoned Jotun Studio (217039, 217040) (10). Talk with Suleion (799075).
 *
 * @author vlog
 */
public class _11110KillingTime extends QuestHandler {

    private final static int questId = 11110;

    public _11110KillingTime() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(217039).addOnKillEvent(questId);
        qe.registerQuestNpc(217040).addOnKillEvent(questId);
        qe.registerQuestNpc(799075).addOnQuestStart(questId);
        qe.registerQuestNpc(799075).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (env.getTargetId() == 799075) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1011);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (env.getTargetId() == 799075) {
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 1352);
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
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            int[] mobs = {217039, 217040};
            if (var >= 0 && var < 9) {
                return defaultOnKillEvent(env, mobs, 0, 9);
            } else if (var == 9) {
                return defaultOnKillEvent(env, mobs, 9, true);
            }
        }
        return false;
    }
}