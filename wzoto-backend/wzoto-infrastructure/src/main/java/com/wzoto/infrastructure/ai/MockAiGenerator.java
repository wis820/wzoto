package com.wzoto.infrastructure.ai;

import com.wzoto.domain.repository.AiGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Mock AI 实现 - 开发环境无API Key时使用
 * 生成结构化的模拟学情分析报告
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "wzoto.ai.enabled", havingValue = "false", matchIfMissing = true)
public class MockAiGenerator implements AiGenerator {

    @Override
    public String generateLearningAnalysis(String childName, String weakSubjects,
                                            String recentScores, String weakPointDesc) {
        log.info("使用Mock AI生成学情报告, childName={}, subjects={}", childName, weakSubjects);

        String name = childName != null ? childName : "该生";
        String[] subjects = weakSubjects.split("[,，、]");

        StringBuilder analysisBuilder = new StringBuilder();
        analysisBuilder.append("{\"weaknessAnalysis\":[");

        for (int i = 0; i < subjects.length; i++) {
            String subject = subjects[i].trim();
            if (i > 0) analysisBuilder.append(",");
            analysisBuilder.append(String.format("""
                    {"subject":"%s","points":["%s核心概念理解","%s应用题解题思路","%s综合分析能力"],"severity":"%s"}""",
                    subject, subject, subject, subject,
                    i == 0 ? "较重" : "中等"));
        }
        analysisBuilder.append("],");

        analysisBuilder.append(String.format("""
                "knowledgeGraph":{"coreGaps":["%s基础概念体系","%s解题方法论"],"foundationLevel":"中等"},
                "suggestion":{"direction":"重点突破%s核心知识体系，建立完整知识框架","frequency":"建议每周2次辅导，每次1.5小时","duration":"预计2-3个月可见明显提升","keyActions":["系统梳理%s知识框架","针对性训练薄弱题型","建立错题本定期复盘"]},
                "recommendedTutorProfile":{"subjects":%s,"education":"本科及以上985/211院校，师范类优先","traits":["耐心细致","善于启发引导","有相关教学经验"]}
                }""",
                subjects[0].trim(), subjects[0].trim(),
                subjects[0].trim(), subjects[0].trim(),
                formatSubjectsArray(subjects)));

        return analysisBuilder.toString();
    }

    @Override
    public String extractPreview(String fullReportContent) {
        try {
            if (fullReportContent == null || fullReportContent.isEmpty()) {
                return "报告生成中，请稍后查看完整内容。";
            }
            int start = fullReportContent.indexOf("\"subject\"");
            if (start > 0) {
                int end = fullReportContent.indexOf("\"severity\"", start);
                if (end > start) {
                    String snippet = fullReportContent.substring(start, end + 30);
                    return "您的孩子存在学科薄弱环节。" +
                            snippet.replace("\"", "").replace("{", "").replace("}", "")
                                    .replace("[", "").replace("]", "").trim() +
                            "... 完整报告包含详细知识图谱和个性化辅导方案。";
                }
            }
            return "您的孩子存在学科薄弱环节，建议重点加强训练。完整报告包含详细知识图谱和个性化辅导方案。";
        } catch (Exception e) {
            return "报告预览生成中，开通会员查看完整报告。";
        }
    }

    @Override
    public String generateResumeOptimization(String university, String major, String grade,
                                             String subjects, String bio, String experience) {
        log.info("使用Mock AI生成简历优化, university={}, subjects={}", university, subjects);

        String bioText = (bio != null && !bio.isEmpty()) ? bio : "暂无个人简介";
        String expText = (experience != null && !experience.isEmpty()) ? experience : "暂无教学经验描述";

        return String.format("""
                {
                  "optimizedBio": "本人毕业于%s，%s专业，%s在读。擅长%s辅导，具备扎实的学科基础和丰富的教学经验。性格耐心细致，善于因材施教，能够根据学生特点制定个性化教学方案。",
                  "optimizedExperience": "在%s期间，积累了丰富的辅导经验。曾辅导多名学生提升成绩，平均提分15-20分。擅长知识点拆解、题型归纳和错题分析，帮助学生建立系统的学习方法。",
                  "highlights": [
                    {"title": "学历背景优势", "desc": "%s%s专业，具备学科权威性"},
                    {"title": "教学经验丰富", "desc": "有实际辅导经验，熟悉学生常见问题"},
                    {"title": "辅导科目匹配", "desc": "擅长%s，与家长需求高度匹配"}
                  ],
                  "improvementSuggestions": [
                    "建议补充具体提分案例和数据",
                    "可添加教学风格的独特亮点",
                    "建议说明可辅导的年级范围和区域"
                  ],
                  "attractivenessScore": 75,
                  "attractivenessLevel": "良好"
                }
                """, university, major, grade, subjects,
                expText, university, major, subjects);
    }

    @Override
    public String generatePricingAnalysis(String university, String major, String grade,
                                          String subjects, String experience, Integer currentRate) {
        log.info("使用Mock AI生成定价分析, university={}, currentRate={}", university, currentRate);

        int rate = currentRate != null ? currentRate : 100;
        int suggestedLow = Math.max(50, rate - 20);
        int suggestedHigh = rate + 30;
        int suggestedOptimal = rate + 10;

        return String.format("""
                {
                  "currentRateAnalysis": {
                    "rate": %d,
                    "level": "市场中等偏下",
                    "comment": "当前定价在市场中处于中等水平，有一定调整空间"
                  },
                  "marketReference": {
                    "range": "%d-%d元/小时",
                    "average": "%d元/小时",
                    "percentile": 50
                  },
                  "suggestedPricing": {
                    "conservative": %d,
                    "optimal": %d,
                    "aggressive": %d,
                    "recommendation": "建议定价%d元/小时，平衡竞争力与收益"
                  },
                  "factors": [
                    {"factor": "学校层次", "impact": "positive", "desc": "%s为优质院校，可适当溢价"},
                    {"factor": "辅导科目", "impact": "positive", "desc": "%s为热门科目，市场需求大"},
                    {"factor": "教学经验", "impact": "neutral", "desc": "经验尚可，随着好评积累可逐步提价"}
                  ],
                  "strategy": {
                    "shortTerm": "首月建议略低于市场价吸引首批客户",
                    "midTerm": "积累3-5个好评后可提价至建议最优价",
                    "longTerm": "口碑稳定后可考虑高端定位"
                  }
                }
                """, rate, suggestedLow, suggestedHigh, rate,
                suggestedLow, suggestedOptimal, suggestedHigh, suggestedOptimal,
                university, subjects);
    }

    @Override
    public String extractTutorPreview(String fullContent) {
        try {
            if (fullContent == null || fullContent.isEmpty()) {
                return "优化结果生成中，请稍后查看完整内容。";
            }
            if (fullContent.contains("optimizedBio")) {
                int start = fullContent.indexOf("\"optimizedBio\"");
                int end = fullContent.indexOf("\"optimizedExperience\"", start);
                if (end > start) {
                    String snippet = fullContent.substring(start, Math.min(end, start + 100));
                    return "AI已为您生成优化的个人简介，突出学历优势和教学特色..." +
                            snippet.replace("\"", "").replace("{", "").replace("}", "").trim() +
                            "... 完整结果包含优化建议、亮点提炼和改进方向。";
                }
            }
            if (fullContent.contains("suggestedPricing")) {
                return "AI已为您生成定价分析报告，包含市场参考价、建议定价策略和影响因素分析...完整结果包含详细定价建议。";
            }
            return "AI优化结果已生成，查看完整内容获取详细建议。";
        } catch (Exception e) {
            return "优化结果预览生成中，解锁查看完整内容。";
        }
    }

    private String formatSubjectsArray(String[] subjects) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < subjects.length; i++) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(subjects[i].trim()).append("\"");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String generateStepByStepAnswer(String subject, String questionText, String grade) {
        log.info("使用Mock AI生成启发式答疑, subject={}, grade={}", subject, grade);
        return String.format("""
                {
                  "steps": [
                    {"step": 1, "hint": "首先，让我们审题。这道%s题考查的是什么知识点？", "detail": "仔细阅读题目，找出已知条件和求解目标"},
                    {"step": 2, "hint": "接下来，回忆相关公式或方法", "detail": "根据%s年级的%s知识，我们可以用什么方法来解决？"},
                    {"step": 3, "hint": "然后，列出解题步骤", "detail": "按照逻辑顺序，一步步推导计算"},
                    {"step": 4, "hint": "最后，验证答案", "detail": "将答案代入原题，检查是否正确"}
                  ],
                  "answer": "根据以上步骤，最终答案为示例结果",
                  "knowledgeTags": ["%s基础", "逻辑推理"],
                  "difficulty": "MEDIUM",
                  "followUpQuestion": "如果题目条件变化，你还能解出来吗？试试看！"
                }
                """, subject, grade, subject, subject);
    }

    @Override
    public String analyzeWrongQuestions(String childName, String wrongQuestionsJson) {
        log.info("使用Mock AI分析错题, childName={}", childName);
        String name = childName != null ? childName : "该生";
        return String.format("""
                {
                  "childName": "%s",
                  "totalAnalyzed": 10,
                  "weakPoints": [
                    {"point": "加减法进位退位", "wrongCount": 4, "severity": "较重", "suggestion": "多做进位退位专项练习"},
                    {"point": "应用题理解", "wrongCount": 3, "severity": "中等", "suggestion": "加强题目关键词提取训练"},
                    {"point": "图形认知", "wrongCount": 2, "severity": "轻微", "suggestion": "多看图形示例，建立空间感"}
                  ],
                  "overallLevel": "基础薄弱，需重点加强计算能力",
                  "recommendedActions": [
                    "每天完成10道基础计算练习",
                    "每周做2套应用题专项训练",
                    "使用微课视频复习薄弱知识点"
                  ]
                }
                """, name);
    }

    @Override
    public String generateDailyPlan(String childName, String grade, String weakPoints, String studyTime) {
        log.info("使用Mock AI生成学习计划, childName={}, grade={}", childName, grade);
        String name = childName != null ? childName : "该生";
        String time = studyTime != null ? studyTime : "1小时";
        return String.format("""
                {
                  "childName": "%s",
                  "planDate": "%s",
                  "totalTime": "%s",
                  "tasks": [
                    {"time": "09:00-09:30", "type": "微课学习", "content": "观看%s数学基础微课", "resourceId": 1},
                    {"time": "09:30-10:00", "type": "专项练习", "content": "完成10道%s相关练习题", "resourceId": 2},
                    {"time": "10:00-10:15", "type": "休息", "content": "休息活动", "resourceId": null},
                    {"time": "10:15-10:45", "type": "错题复习", "content": "复习昨日错题，AI分析薄弱点", "resourceId": null},
                    {"time": "10:45-11:00", "type": "口语练习", "content": "英语单词跟读练习", "resourceId": 3}
                  ],
                  "weakPointsFocus": "%s",
                  "aiSuggestion": "建议%s同学今天重点突破薄弱知识点，循序渐进，保持学习节奏。"
                }
                """, name, java.time.LocalDate.now().toString(), time,
                grade, weakPoints != null ? weakPoints : "基础知识",
                weakPoints != null ? weakPoints : "基础", name);
    }

    @Override
    public String gradeComposition(String grade, String title, String content) {
        log.info("使用Mock AI批改作文, grade={}, title={}", grade, title);
        int wordCount = content != null ? content.length() : 0;
        int score = Math.min(100, 60 + wordCount / 10);
        return String.format("""
                {
                  "score": %d,
                  "level": "%s",
                  "wordCount": %d,
                  "highlights": [
                    {"text": "开头部分", "comment": "开门见山，引入自然"},
                    {"text": "中间段落", "comment": "内容较充实，条理清晰"}
                  ],
                  "errors": [
                    {"original": "示例错别字", "corrected": "正确写法", "type": "错别字"},
                    {"original": "这个句子很好", "corrected": "这个句子非常好", "type": "病句-缺少修饰语"}
                  ],
                  "suggestions": [
                    "建议增加具体细节描写，让文章更生动",
                    "结尾可以加入自己的感悟和思考",
                    "注意段落之间的过渡衔接"
                  ],
                  "overallComment": "%s年级作文《%s》整体结构完整，内容较充实，建议加强细节描写和语言锤炼。"
                }
                """, score, score >= 80 ? "优秀" : score >= 60 ? "良好" : "待提高",
                wordCount, grade, title != null ? title : "未命名");
    }

    @Override
    public String evaluatePronunciation(String word, String audioTranscript) {
        log.info("使用Mock AI评测口语, word={}", word);
        int score = 85;
        return String.format("""
                {
                  "score": %d,
                  "level": "%s",
                  "targetWord": "%s",
                  "recognizedText": "%s",
                  "pronunciationDetails": [
                    {"phoneme": "/示例音标/", "accuracy": 90, "comment": "发音准确"},
                    {"phoneme": "/示例音标2/", "accuracy": 75, "comment": "注意舌位"}
                  ],
                  "suggestions": [
                    "注意单词的重音位置",
                    "尝试放慢语速，每个音节都发音清晰",
                    "多听原声跟读，模仿语调"
                  ],
                  "encouragement": "发音不错！继续保持练习，你会越来越棒！"
                }
                """, score, score >= 80 ? "优秀" : "良好", word,
                audioTranscript != null ? audioTranscript : "未识别到语音");
    }
}
