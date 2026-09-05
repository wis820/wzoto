package com.wzoto.infrastructure.ai;

import com.wzoto.domain.repository.AiGenerator;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * OpenAI兼容API调用实现 - 直接HTTP请求
 * 适配SpringAI设计规范，使用领域端口AiGenerator
 * 支持任何OpenAI兼容接口（OpenAI / DeepSeek / 本地Ollama等）
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "wzoto.ai.enabled", havingValue = "true", matchIfMissing = false)
public class OpenAiHttpGenerator implements AiGenerator {

    @Value("${wzoto.ai.base-url:https://api.openai.com}")
    private String baseUrl;

    @Value("${wzoto.ai.api-key:}")
    private String apiKey;

    @Value("${wzoto.ai.model:gpt-3.5-turbo}")
    private String model;

    @Value("${wzoto.ai.temperature:0.7}")
    private double temperature;

    private static final String SYSTEM_PROMPT = """
            你是一位资深的中小学教育分析师，擅长根据学生的学科薄弱点生成专业的学情分析报告。
            
            请严格按照以下JSON格式输出报告内容，不要输出任何其他文字：
            {
              "weaknessAnalysis": [
                {
                  "subject": "学科名称",
                  "points": ["薄弱知识点1", "薄弱知识点2", "薄弱知识点3"],
                  "severity": "轻度/中等/较重/严重"
                }
              ],
              "knowledgeGraph": {
                "coreGaps": ["核心知识缺口1", "核心知识缺口2"],
                "foundationLevel": "基础/中等/良好"
              },
              "suggestion": {
                "direction": "辅导方向建议",
                "frequency": "建议每周上课频次和时长",
                "duration": "预计提升周期",
                "keyActions": ["行动建议1", "行动建议2", "行动建议3"]
              },
              "recommendedTutorProfile": {
                "subjects": ["建议寻找的教员擅长学科"],
                "education": "建议教员学历背景",
                "traits": ["建议教员特质1", "建议教员特质2"]
              }
            }
            """;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    public String generateLearningAnalysis(String childName, String weakSubjects,
                                            String recentScores, String weakPointDesc) {
        try {
            String userPrompt = String.format(
                    "请为以下学生生成学情分析报告：\n学生姓名：%s\n薄弱学科：%s\n近期分数：%s\n薄弱知识点描述：%s\n请严格按照指定JSON格式输出完整报告。",
                    childName != null ? childName : "该生",
                    weakSubjects,
                    recentScores != null ? recentScores : "未提供",
                    weakPointDesc != null ? weakPointDesc : "未提供"
            );

            JSONObject requestBody = new JSONObject()
                    .put("model", model)
                    .put("temperature", temperature)
                    .put("messages", new JSONArray()
                            .put(new JSONObject().put("role", "system").put("content", SYSTEM_PROMPT))
                            .put(new JSONObject().put("role", "user").put("content", userPrompt)));

            Request request = new Request.Builder()
                    .url(baseUrl + "/v1/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body() != null ? response.body().string() : "";

            if (response.isSuccessful()) {
                JSONObject jsonResponse = new JSONObject(responseBody);
                String content = jsonResponse.getJSONArray("choices")
                        .getJSONObject(0).getJSONObject("message").getString("content");
                log.info("OpenAI API生成学情报告成功, childName={}", childName);
                return content;
            } else {
                log.error("OpenAI API调用失败, status={}, body={}", response.code(), responseBody);
                return generateFallbackReport(childName, weakSubjects, recentScores, weakPointDesc);
            }
        } catch (Exception e) {
            log.error("OpenAI API调用异常, 使用降级方案", e);
            return generateFallbackReport(childName, weakSubjects, recentScores, weakPointDesc);
        }
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
                    return "您的孩子存在学科薄弱环节。" + snippet.replace("\"", "").replace("{", "").replace("}", "").replace("[", "").replace("]", "").trim() + "... 完整报告包含详细知识图谱和个性化辅导方案。";
                }
            }
            return "您的孩子存在学科薄弱环节，建议重点加强训练。完整报告包含详细知识图谱和个性化辅导方案。";
        } catch (Exception e) {
            return "报告预览生成中，开通会员查看完整报告。";
        }
    }

    private String generateFallbackReport(String childName, String weakSubjects,
                                           String recentScores, String weakPointDesc) {
        return String.format("""
                {
                  "weaknessAnalysis": [
                    {
                      "subject": "%s",
                      "points": ["需根据具体测试进一步分析薄弱知识点"],
                      "severity": "中等"
                    }
                  ],
                  "knowledgeGraph": {
                    "coreGaps": ["需要更详细的测试数据来精准定位"],
                    "foundationLevel": "中等"
                  },
                  "suggestion": {
                    "direction": "建议针对%s进行系统性查漏补缺",
                    "frequency": "建议每周2-3次辅导，每次1.5小时",
                    "duration": "预计2-3个月可见明显提升",
                    "keyActions": ["进行学科诊断测试精确定位薄弱点", "制定分阶段提升计划", "定期检测学习效果"]
                  },
                  "recommendedTutorProfile": {
                    "subjects": ["%s"],
                    "education": "本科及以上学历，相关专业背景",
                    "traits": ["耐心细致", "善于引导", "有教学经验"]
                  }
                }
                """, weakSubjects, weakSubjects, weakSubjects);
    }

    private static final String RESUME_SYSTEM_PROMPT = """
            你是一位资深的大学生简历优化专家，擅长为家教教员优化个人简介和教学经验描述。
            
            请严格按照以下JSON格式输出优化结果，不要输出任何其他文字：
            {
              "optimizedBio": "优化后的个人简介",
              "optimizedExperience": "优化后的教学经验描述",
              "highlights": [
                {"title": "亮点标题", "desc": "亮点描述"}
              ],
              "improvementSuggestions": ["改进建议1", "改进建议2"],
              "attractivenessScore": 85,
              "attractivenessLevel": "优秀"
            }
            """;

    @Override
    public String generateResumeOptimization(String university, String major, String grade,
                                             String subjects, String bio, String experience) {
        try {
            String userPrompt = String.format(
                    "请为以下教员优化简历：\n学校：%s\n专业：%s\n年级：%s\n辅导科目：%s\n当前简介：%s\n当前经验描述：%s\n请严格按照指定JSON格式输出优化结果。",
                    university, major, grade, subjects,
                    bio != null ? bio : "无", experience != null ? experience : "无"
            );

            String content = callOpenAi(RESUME_SYSTEM_PROMPT, userPrompt);
            log.info("OpenAI API生成简历优化成功, university={}", university);
            return content;
        } catch (Exception e) {
            log.error("OpenAI API生成简历优化异常, 使用降级方案", e);
            return generateFallbackResume(university, major, grade, subjects);
        }
    }

    private static final String PRICING_SYSTEM_PROMPT = """
            你是一位资深的家教市场定价分析师，擅长根据教员的背景给出合理定价建议。
            
            请严格按照以下JSON格式输出定价分析，不要输出任何其他文字：
            {
              "currentRateAnalysis": {
                "rate": 当前时薪,
                "level": "市场水平",
                "comment": "评价"
              },
              "marketReference": {
                "range": "市场参考范围",
                "average": "市场均价",
                "percentile": 50
              },
              "suggestedPricing": {
                "conservative": 保守价,
                "optimal": 最优价,
                "aggressive": 激进价,
                "recommendation": "推荐定价"
              },
              "factors": [
                {"factor": "因素", "impact": "positive/negative/neutral", "desc": "描述"}
              ],
              "strategy": {
                "shortTerm": "短期策略",
                "midTerm": "中期策略",
                "longTerm": "长期策略"
              }
            }
            """;

    @Override
    public String generatePricingAnalysis(String university, String major, String grade,
                                          String subjects, String experience, Integer currentRate) {
        try {
            String userPrompt = String.format(
                    "请为以下教员分析定价：\n学校：%s\n专业：%s\n年级：%s\n辅导科目：%s\n教学经验：%s\n当前时薪：%d元/小时\n请严格按照指定JSON格式输出定价分析。",
                    university, major, grade, subjects,
                    experience != null ? experience : "无",
                    currentRate != null ? currentRate : 100
            );

            String content = callOpenAi(PRICING_SYSTEM_PROMPT, userPrompt);
            log.info("OpenAI API生成定价分析成功, university={}", university);
            return content;
        } catch (Exception e) {
            log.error("OpenAI API生成定价分析异常, 使用降级方案", e);
            return generateFallbackPricing(currentRate);
        }
    }

    @Override
    public String extractTutorPreview(String fullContent) {
        try {
            if (fullContent == null || fullContent.isEmpty()) {
                return "优化结果生成中，请稍后查看完整内容。";
            }
            if (fullContent.contains("optimizedBio")) {
                int start = fullContent.indexOf("\"optimizedBio\"");
                if (start > 0) {
                    int end = fullContent.indexOf("\"optimizedExperience\"", start);
                    if (end > start) {
                        String snippet = fullContent.substring(start, Math.min(end, start + 100));
                        return "AI已为您优化个人简介..." +
                                snippet.replace("\"", "").replace("{", "").replace("}", "").trim() +
                                "... 完整结果包含优化建议和亮点提炼。";
                    }
                }
            }
            if (fullContent.contains("suggestedPricing")) {
                return "AI定价分析已生成，包含市场参考价、建议定价策略和影响因素分析。";
            }
            return "AI优化结果已生成，查看完整内容获取详细建议。";
        } catch (Exception e) {
            return "优化结果预览生成中，解锁查看完整内容。";
        }
    }

    private String callOpenAi(String systemPrompt, String userPrompt) throws Exception {
        JSONObject requestBody = new JSONObject()
                .put("model", model)
                .put("temperature", temperature)
                .put("messages", new JSONArray()
                        .put(new JSONObject().put("role", "system").put("content", systemPrompt))
                        .put(new JSONObject().put("role", "user").put("content", userPrompt)));

        Request request = new Request.Builder()
                .url(baseUrl + "/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body() != null ? response.body().string() : "";

        if (response.isSuccessful()) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            return jsonResponse.getJSONArray("choices")
                    .getJSONObject(0).getJSONObject("message").getString("content");
        } else {
            log.error("OpenAI API调用失败, status={}, body={}", response.code(), responseBody);
            throw new RuntimeException("OpenAI API调用失败: " + response.code());
        }
    }

    private String generateFallbackResume(String university, String major, String grade, String subjects) {
        return String.format("""
                {
                  "optimizedBio": "本人毕业于%s，%s专业，%s在读。擅长%s辅导，具备扎实学科基础。",
                  "optimizedExperience": "有丰富辅导经验，帮助学生有效提升成绩。",
                  "highlights": [
                    {"title": "学历优势", "desc": "%s%s专业"}
                  ],
                  "improvementSuggestions": ["建议补充具体案例"],
                  "attractivenessScore": 70,
                  "attractivenessLevel": "良好"
                }
                """, university, major, grade, subjects, university, major);
    }

    private String generateFallbackPricing(Integer currentRate) {
        int rate = currentRate != null ? currentRate : 100;
        return String.format("""
                {
                  "currentRateAnalysis": {"rate": %d, "level": "中等", "comment": "定价合理"},
                  "marketReference": {"range": "%d-%d元/小时", "average": "%d元/小时", "percentile": 50},
                  "suggestedPricing": {"conservative": %d, "optimal": %d, "aggressive": %d, "recommendation": "建议%d元/小时"},
                  "factors": [{"factor": "综合评估", "impact": "neutral", "desc": "需要更多信息分析"}],
                  "strategy": {"shortTerm": "低价获客", "midTerm": "逐步提价", "longTerm": "高端定位"}
                }
                """, rate, Math.max(50, rate-20), rate+30, rate, Math.max(50, rate-20), rate+10, rate+30, rate+10);
    }

    @Override
    public String generateStepByStepAnswer(String subject, String questionText, String grade) {
        try {
            String prompt = String.format("请为%s年级%s学科题目提供分步解题引导（JSON格式）：\n题目：%s\n要求：输出steps数组（含hint和detail）、answer、knowledgeTags、followUpQuestion", grade, subject, questionText);
            return callOpenAi("你是一位启发式教学专家，擅长分步引导学生解题。请输出JSON格式。", prompt);
        } catch (Exception e) {
            log.error("OpenAI启发式答疑异常", e);
            return "{\"steps\":[{\"step\":1,\"hint\":\"请仔细审题\",\"detail\":\"AI暂时无法回答\"}],\"answer\":\"请稍后重试\",\"knowledgeTags\":[],\"followUpQuestion\":\"\"}";
        }
    }

    @Override
    public String analyzeWrongQuestions(String childName, String wrongQuestionsJson) {
        try {
            String prompt = String.format("请分析学生%s的错题数据（JSON格式输出weakPoints、overallLevel、recommendedActions）：\n%s", childName, wrongQuestionsJson);
            return callOpenAi("你是一位学情分析专家，擅长从错题中定位薄弱知识点。请输出JSON格式。", prompt);
        } catch (Exception e) {
            log.error("OpenAI错题分析异常", e);
            return "{\"childName\":\"" + childName + "\",\"weakPoints\":[],\"overallLevel\":\"数据不足\",\"recommendedActions\":[]}";
        }
    }

    @Override
    public String generateDailyPlan(String childName, String grade, String weakPoints, String studyTime) {
        try {
            String prompt = String.format("请为学生%s（%s年级）生成每日学习计划（JSON格式输出tasks、weakPointsFocus、aiSuggestion）：\n薄弱点：%s\n可用时间：%s", childName, grade, weakPoints, studyTime);
            return callOpenAi("你是一位学习规划师，擅长为学生制定个性化每日学习计划。请输出JSON格式。", prompt);
        } catch (Exception e) {
            log.error("OpenAI学习计划生成异常", e);
            return "{\"childName\":\"" + childName + "\",\"tasks\":[],\"weakPointsFocus\":\"\",\"aiSuggestion\":\"AI暂时无法生成计划\"}";
        }
    }

    @Override
    public String gradeComposition(String grade, String title, String content) {
        try {
            String prompt = String.format("请批改%s年级作文《%s》（JSON格式输出score、highlights、errors、suggestions、overallComment）：\n%s", grade, title, content);
            return callOpenAi("你是一位资深语文教师，擅长批改小学生作文。请输出JSON格式。", prompt);
        } catch (Exception e) {
            log.error("OpenAI作文批改异常", e);
            return "{\"score\":0,\"level\":\"无法评分\",\"errors\":[],\"suggestions\":[\"AI暂时无法批改\"],\"overallComment\":\"请稍后重试\"}";
        }
    }

    @Override
    public String evaluatePronunciation(String word, String audioTranscript) {
        try {
            String prompt = String.format("请评测英语口语发音（JSON格式输出score、pronunciationDetails、suggestions、encouragement）：\n目标单词：%s\n识别文本：%s", word, audioTranscript);
            return callOpenAi("你是一位英语口语评测专家，擅长分析发音准确度。请输出JSON格式。", prompt);
        } catch (Exception e) {
            log.error("OpenAI口语评测异常", e);
            return "{\"score\":0,\"level\":\"无法评测\",\"suggestions\":[\"AI暂时无法评测\"],\"encouragement\":\"请稍后重试\"}";
        }
    }
}