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

public class _3006TheShugoFugitive extends QuestHandler {

    private final static int questId = 3006;

    public _3006TheShugoFugitive() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(798132).addOnQuestStart(questId);
        qe.registerQuestNpc(798132).addOnTalkEvent(questId);
        qe.registerQuestNpc(798146).addOnTalkEvent(questId);
        qe.registerQuestNpc(700339).addOnTalkEvent(questId);
        qe.registerOnMovieEndQuest(361, questId);
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
                        return sendQuestDialog(env, 1011);
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
                case 798146: {
                    switch (env.getDialog()) {
                        case START_DIALOG: {
                            if (qs.getQuestVarById(0) == 0) {
                                return sendQuestDialog(env, 1352);
                            }
                        }
                        case STEP_TO_1: {
                            qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                            updateQuestStatus(env);
                            player.sendPck(new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                    }
                }
                case 700339: {
                    switch (env.getDialog()) {
                        case USE_OBJECT: {
                            if (qs.getQuestVarById(0) == 1) {
                                return sendQuestDialog(env, 1693);
                            }
                        }
                        case SELECT_ACTION_1694: {
                            if (qs.getQuestVarById(0) == 1) {
                                playQuestMovie(env, 361);
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 798132) {
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
    public boolean onMovieEndEvent(QuestEnv env, int movieId) {
        if (movieId != 361) {
            return false;
        }
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        }

        qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
        qs.setStatus(QuestStatus.REWARD);
        updateQuestStatus(env);
        return sendQuestDialog(env, 1353);
    }
}