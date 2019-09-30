/*
 * This file is part of Neon-Eleanor project
 *
 * This is proprietary software. See the EULA file distributed with
 * this project for additional information regarding copyright ownership.
 *
 * Copyright (c) 2011-2013, Neon-Eleanor Team. All rights reserved.
 */
package quest.beshmundir;

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
public class _30203GroupHalttheCeremony extends QuestHandler {

    private final static int questId = 30203;
    private final static int[] npc_ids = {798926};

    public _30203GroupHalttheCeremony() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(798926).addOnQuestStart(questId);
        qe.registerQuestNpc(216175).addOnKillEvent(questId);
        qe.registerQuestNpc(216177).addOnKillEvent(questId);
        qe.registerQuestNpc(216179).addOnKillEvent(questId);
        qe.registerQuestNpc(216181).addOnKillEvent(questId);
        qe.registerQuestNpc(216263).addOnKillEvent(questId);
        for (int npc_id : npc_ids) {
            qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
        }
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        if (qs == null || qs.getStatus() == QuestStatus.NONE) {
            if (targetId == 798926) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 4762);
                } else {
                    return sendQuestStartDialog(env);
                }
            }
        }

        if (qs == null) {
            return false;
        }

        if (qs.getStatus() == QuestStatus.START) {
            if (targetId == 798926) {
                switch (env.getDialog()) {
                    case START_DIALOG:
                        return sendQuestDialog(env, 10002);
                }
            }
            return false;
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 798926) {
                return sendQuestEndDialog(env);
            }
            return false;
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

        int var = qs.getQuestVarById(0);
        int var1 = qs.getQuestVarById(1);
        int var2 = qs.getQuestVarById(2);
        int var3 = qs.getQuestVarById(3);

        switch (targetId) {
            case 216175:
                if (var == 0 || var1 == 0 || var2 == 1 || var3 == 1 || var1 == 0 || var2 == 0 || var3 == 0) {
                    qs.setQuestVarById(0, 1);
                    updateQuestStatus(env);
                }
                break;
            case 216177:
                if (var == 1 || var1 == 0 || var2 == 1 || var3 == 1 || var == 0 || var2 == 0 || var3 == 0) {
                    qs.setQuestVarById(1, 1);
                    updateQuestStatus(env);
                }
                break;
            case 216179:
                if (var == 1 || var1 == 0 || var2 == 0 || var3 == 1 || var == 0 || var1 == 0 || var3 == 0) {
                    qs.setQuestVarById(2, 1);
                    updateQuestStatus(env);
                }
                break;
            case 216181:
                if (var == 1 || var1 == 0 || var2 == 1 || var3 == 0 || var == 0 || var2 == 0 || var1 == 0) {
                    qs.setQuestVarById(3, 1);
                    updateQuestStatus(env);
                }
                break;
            case 216263:
                if (var == 1 && var1 == 1 && var2 == 1 && var3 == 1) {
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    playQuestMovie(env, 443);
                }
                break;
        }
        return false;
    }
}