/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package quest.beshmundir;

import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.questEngine.handlers.QuestHandler;
import com.ne.gs.questEngine.model.QuestDialog;
import com.ne.gs.questEngine.model.QuestEnv;
import com.ne.gs.questEngine.model.QuestState;
import com.ne.gs.questEngine.model.QuestStatus;

/**
 * @author vlog
 */
public class _30222AGuardsCurse extends QuestHandler {

    private static final int questId = 30222;

    public _30222AGuardsCurse() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(798979).addOnQuestStart(questId);
        qe.registerQuestNpc(798979).addOnTalkEvent(questId);
        qe.registerQuestNpc(216739).addOnKillEvent(questId);
        qe.registerQuestNpc(216239).addOnKillEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        int targetId = env.getTargetId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 798979) { // Gelon
                if (dialog == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1011);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (targetId == 798979) { // Gelon
                switch (dialog) {
                    case START_DIALOG: {
                        if (var == 1) {
                            return sendQuestDialog(env, 1352);
                        }
                    }
                    case SELECT_REWARD: {
                        changeQuestStep(env, 1, 1, true); // reward
                        return sendQuestDialog(env, 5);
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 798979) { // Gelon
                return sendQuestEndDialog(env);
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int targetId = env.getTargetId();

        if (qs != null && qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                // case 216739: { // Warrior Monument
                // Npc npc = (Npc) env.getVisibleObject();
                // QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 216239, npc.getX(), npc.getY(),
                // npc.getZ(), npc.getH());
                // return true;
                // }
                case 216239: { // Ahbana the Wicked
                    return defaultOnKillEvent(env, 216239, 0, 1); // 1
                }
            }
        }
        return false;
    }
}