package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 知识点掌握度 - 值对象
 * 红黄绿三色区分未掌握/一般/熟练
 */
@Getter
public enum MasteryLevel {

    NOT_MASTERED("NOT_MASTERED", "未掌握", "#FF4D4F"),
    GENERAL("GENERAL", "一般", "#FAAD14"),
    PROFICIENT("PROFICIENT", "熟练", "#52C41A");

    private final String code;
    private final String desc;
    private final String color;

    MasteryLevel(String code, String desc, String color) {
        this.code = code;
        this.desc = desc;
        this.color = color;
    }

    public static MasteryLevel fromCode(String code) {
        for (MasteryLevel level : values()) {
            if (level.code.equals(code)) {
                return level;
            }
        }
        throw new IllegalArgumentException("无效的掌握度: " + code);
    }

    /**
     * 根据正确率计算掌握度
     */
    public static MasteryLevel fromAccuracy(double accuracy) {
        if (accuracy >= 0.85) {
            return PROFICIENT;
        } else if (accuracy >= 0.60) {
            return GENERAL;
        } else {
            return NOT_MASTERED;
        }
    }
}
