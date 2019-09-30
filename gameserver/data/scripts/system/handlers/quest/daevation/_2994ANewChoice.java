/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package quest.daevation;

import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.questEngine.handlers.QuestHandler;
import com.ne.gs.questEngine.model.QuestEnv;
import com.ne.gs.questEngine.model.QuestState;
import com.ne.gs.questEngine.model.QuestStatus;
import com.ne.gs.services.QuestService;

/**
 * @author Tiger
 *         edited by Wakizashi
 */
public class _2994ANewChoice extends QuestHandler {

    private final static int questId = 2994;
    private final static int dialogs[] = {1013, 1034, 1055, 1076, 5103, 1098, 1119, 1140, 1161, 1183};
    private final static int items[] = {100000723, 100900554, 101300538, 100200673, 101700594, 100100568, 101500566, 100600608, 100500572, 115000826};
    private int choice = 0;
    private int item;

    public _2994ANewChoice() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(204077).addOnQuestStart(questId); // Bor
        qe.registerQuestNpc(204077).addOnTalkEvent(questId); // Bor
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int dialogId = env.getDialogId();

        if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
            if (targetId == 204077) { // Bor
                if (dialogId == 54) {
                    QuestService.startQuest(env);
                    return sendQuestDialog(env, 1011);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        } else if (qs != null && qs.getStatus() == QuestStatus.START) {
            if (targetId == 204077) { // Bor
                if (dialogId == -1) {
                    return sendQuestDialog(env, 1011);
                }
                switch (env.getDialogId()) {
                    case 1012:
                    case 1097:
                    case 1182:
                    case 1267:
                        return sendQuestDialog(env, dialogId);
                    case 1013:
                    case 1034:
                    case 1055:
                    case 1076:
                    case 5103:
                    case 1098:
                    case 1119:
                    case 1140:
                    case 1161:
                    case 1183: {
                        item = getItem(dialogId);
                        if (player.getInventory().getItemCountByItemId(item) > 0) {
                            return sendQuestDialog(env, 1013);
                        } else {
                            return sendQuestDialog(env, 1352);
                        }
                    }
                    case 10000:
                    case 10001:
                    case 10002:
                    case 10003: {
                        if (player.getInventory().getItemCountByItemId(186000041) == 0) {
                            return sendQuestDialog(env, 1009);
                        }
                        changeQuestStep(env, 0, 0, true);
                        choice = dialogId - 10000;
                        return sendQuestDialog(env, choice + 5);
                    }
                }
            }
        } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 204077) { // Bor
                removeQuestItem(env, item, 1);
                removeQuestItem(env, 186000041, 1);
                return sendQuestEndDialog(env, choice);
            }
        }
        return false;
    }

    private int getItem(int dialogId) {
        int x = 0;
        for (int id : dialogs) {
            if (id == dialogId) {
                break;
            }
            x++;
        }
        return (items[x]);
    }
}