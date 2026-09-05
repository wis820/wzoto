package com.wzoto.domain.repository;

/**
 * AI生成器接口 - 领域端口
 * 由基础设施层实现（Spring AI / Mock）
 */
public interface AiGenerator {

    String generateLearningAnalysis(String childName, String weakSubjects,
                                    String recentScores, String weakPointDesc);

    String extractPreview(String fullReportContent);

    String generateResumeOptimization(String university, String major, String grade,
                                     String subjects, String bio, String experience);

    String generatePricingAnalysis(String university, String major, String grade,
                                   String subjects, String experience, Integer currentRate);

    String extractTutorPreview(String fullContent);

    /** 启发式答疑 - 分步引导解题 */
    String generateStepByStepAnswer(String subject, String questionText, String grade);

    /** 错题分析 - 定位薄弱知识点 */
    String analyzeWrongQuestions(String childName, String wrongQuestionsJson);

    /** 学习规划 - 生成每日学习计划 */
    String generateDailyPlan(String childName, String grade, String weakPoints, String studyTime);

    /** 作文批改 */
    String gradeComposition(String grade, String title, String content);

    /** 口语评测 */
    String evaluatePronunciation(String word, String audioTranscript);
}
