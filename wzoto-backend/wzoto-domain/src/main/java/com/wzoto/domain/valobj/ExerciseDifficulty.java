package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 习题难度 - 值对象
 */
@Getter
public enum ExerciseDifficulty {

    EASY("EASY", "基础"),
    MEDIUM("MEDIUM", "提高"),
    HARD("HARD", "挑战");

    private final String code;
    private final String desc;

    ExerciseDifficulty(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ExerciseDifficulty fromCode(String code) {
        for (ExerciseDifficulty type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的难度: " + code);
    }
}
