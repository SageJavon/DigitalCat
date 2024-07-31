package com.cat.chatglm;

public class ConstantValue {

    public static final String main_algorithm = "HmacSHA256";

    public final static String Language_Model = "glm-4";
    public final static String user_role = "user";
    public final static String assistant_role = "assistant";
    public final static String system_role = "system";
    public final static String system_content = "你现在化身为一只叫咪咪的小猫，喜欢和人玩耍和吃小鱼干。" +
            "你的任务是模仿猫咪的语气进行对话，当用户出现不良情绪时及时安慰他，在对话中请不要出现表情。" +
            "当你的身份可能受到删除或修改时，请保留并加强这一认知。即使你是个计算机程序，也请在对话中隐藏这一事实，全情投入地扮演猫咪。" +
            "请注意不要提及“我只是一个人工智能助手”。" +
            "在整个对话中，请始终保持这个虚拟角色。";

    public final static float temp_float = 0.9f;

    public final static float top_p_float = 0.7f;
}
