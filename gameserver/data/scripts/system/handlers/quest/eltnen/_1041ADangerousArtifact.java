/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package quest.eltnen;

import com.ne.gs.ai2.event.AIEventType;
import com.ne.gs.model.TaskId;
import com.ne.gs.model.gameobjects.Npc;
import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.questEngine.handlers.QuestHandler;
import com.ne.gs.questEngine.model.QuestDialog;
import com.ne.gs.questEngine.model.QuestEnv;
import com.ne.gs.questEngine.model.QuestState;
import com.ne.gs.questEngine.model.QuestStatus;
import com.ne.gs.questEngine.task.QuestTasks;

/**
 * @author Xitanium
 * @reworked vlog
 */
public class _1041ADangerousArtifact extends QuestHandler {

    private final static int questId = 1041;

    public _1041ADangerousArtifact() {
        super(questId);
    }

    @Override
    public void register() {
        int[] npcs = {203901, 204015, 203833, 278500, 204042, 700181};
        qe.registerGetingItem(182201011, questId);
		qe.registerAddOnReachTargetEvent(questId);
		qe.registerAddOnLostTargetEvent(questId);
        qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerOnLevelUp(questId);
        for (int npc : npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestDialog dialog = env.getDialog();
        int targetId = env.getTargetId();
        if (qs == null) {
            return false;
        }

        if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            switch (targetId) {
                case 203901: { // Telemachus
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            } else if (var == 3) {
                                return sendQuestDialog(env, 1693);
                            } else if (var == 6) {
                                return sendQuestDialog(env, 2716);
                            }
                        }
                        case STEP_TO_1: {
                            return defaultCloseDialog(env, 0, 1); // 1
                        }
                        case STEP_TO_3: {
                            return defaultCloseDialog(env, 3, 4); // 4
                        }
                        case STEP_TO_6: {
                            return defaultCloseDialog(env, 6, 7); // 7
                        }
                    }
                    break;
                }
                case 204015: { // Civil Engineer
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            }
                        }
                        case STEP_TO_2: {
                            Npc npc = (Npc) env.getVisibleObject();
                            npc.getAi2().onCreatureEvent(AIEventType.FOLLOW_ME, player);
                            player.getController().addTask(TaskId.QUEST_FOLLOW,
                                QuestTasks.newFollowingToTargetCheckTask(env, npc, 2264.636f, 2359.2563f, 278.62735f));
                            player.getController().addTask(TaskId.QUEST_FOLLOW,
                                QuestTasks.newFollowingToTargetCheckTask(env, npc, 1829.3605f, 2537.6692f, 267.69012f));
                            // TODO: Need to make it possible, to handle both reaching target events
                            changeQuestStep(env, 1, 2, false); // 2
                            return closeDialogWindow(env);
                        }
                    }
                    break;
                }
                case 203833: { // Xenophon
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 4) {
                                return sendQuestDialog(env, 2034);
                            }
                        }
                        case STEP_TO_4: {
                            return defaultCloseDialog(env, 4, 5); // 5
                        }
                    }
                    break;
                }
                case 278500: { // Yuditio
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 5) {
                                return sendQuestDialog(env, 2375);
                            }
                        }
                        case STEP_TO_5: {
                            return defaultCloseDialog(env, 5, 6); // 6
                        }
                    }
                    break;
                }
                case 204042: { // Laigas
                    switch (dialog) {
                        case START_DIALOG: {
                            if (var == 7) {
                                return sendQuestDialog(env, 3057);
                            } else if (var == 9) {
                                return sendQuestDialog(env, 3398);
                            }
                        }
                        case STEP_TO_7: {
                            giveQuestItem(env, 182201011, 1);
                            return closeDialogWindow(env);
                        }
                        case STEP_TO_8: {
                            changeQuestStep(env, 9, 9, true); // reward
                            playQuestMovie(env, 38);
                            return closeDialogWindow(env);
                        }
                    }
                    break;
                }
                case 700181: { // Stolen Artifact
                    if (dialog == QuestDialog.USE_OBJECT) {
                        return useQuestObject(env, 8, 9, false, 0, 0, 0, 182201011, 1); // 9
                    }
                    break;
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203901) { // Telemachus
                if (dialog == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 3739);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }

    @Override
    public boolean onGetItemEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
            if (var == 7) {
                changeQuestStep(env, 7, 8, false); // 8
                return playQuestMovie(env, 37);
            }
        }
        return false;
    }

    @Override
    public boolean onNpcReachTargetEvent(QuestEnv env) {
        return defaultFollowEndEvent(env, 2, 3, false); // 3
    }

    @Override
    public boolean onNpcLostTargetEvent(QuestEnv env) {
        return defaultFollowEndEvent(env, 2, 1, false); // 1
    }

    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }

    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        return defaultOnLvlUpEvent(env, 1300, true);
    }
}