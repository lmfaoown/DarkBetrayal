/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package quest.crafting;

import com.ne.gs.model.gameobjects.Npc;
import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.questEngine.handlers.QuestHandler;
import com.ne.gs.questEngine.model.QuestDialog;
import com.ne.gs.questEngine.model.QuestEnv;
import com.ne.gs.questEngine.model.QuestState;
import com.ne.gs.questEngine.model.QuestStatus;

/**
 * @author Gigi
 */
public class _2976ExpertHandicraftersTest extends QuestHandler {

    private final static int questId = 2976;

    public _2976ExpertHandicraftersTest() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(204108).addOnQuestStart(questId);
        qe.registerQuestNpc(204108).addOnTalkEvent(questId);
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
            if (targetId == 204108) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1011);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }

        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 204108: {
                    switch (env.getDialog()) {
                        case START_DIALOG: {
                            long itemCount1 = player.getInventory().getItemCountByItemId(182207967);
                            if (itemCount1 > 0) {
                                removeQuestItem(env, 182207967, 1);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 2375);
                            } else {
                                return sendQuestDialog(env, 2716);
                            }
                        }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204108) {
                if (env.getDialogId() == 34) {
                    return sendQuestDialog(env, 5);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }
}