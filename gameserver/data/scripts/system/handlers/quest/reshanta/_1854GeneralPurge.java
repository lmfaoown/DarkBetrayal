package quest.reshanta;

import com.ne.gs.model.gameobjects.player.Player;
import com.ne.gs.questEngine.handlers.QuestHandler;
import com.ne.gs.questEngine.model.QuestDialog;
import com.ne.gs.questEngine.model.QuestEnv;
import com.ne.gs.questEngine.model.QuestState;
import com.ne.gs.questEngine.model.QuestStatus;
import com.ne.gs.utils.stats.AbyssRankEnum;

public class _1854GeneralPurge extends QuestHandler {

    private final static int questId = 1854;

    public _1854GeneralPurge() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(278501).addOnQuestStart(questId);
        qe.registerQuestNpc(278501).addOnTalkEvent(questId);
        qe.registerOnKillRanked(AbyssRankEnum.GENERAL, questId);
    }

    @Override
    public boolean onKillRankedEvent(QuestEnv env) {
        return defaultOnKillRankedEvent(env, 0, 3, true); // reward
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (env.getTargetId() == 278501) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 1011);
                }
                else {
                    return sendQuestStartDialog(env);
                }
            }
            else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                if (env.getTargetId() == 278501) {
                    if (env.getDialog() == QuestDialog.USE_OBJECT) {
                        return sendQuestDialog(env, 1352);
                    }
                    else {
                        return sendQuestEndDialog(env);
                    }
                }
            }
        }
        return false;
    }
}