package com.wzoto.infrastructure.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 语音评测服务 - 预留讯飞/百度语音端口
 * 开发阶段使用Mock实现，对音频进行评测打分
 */
@Slf4j
@Component
public class SpeechService {

    /**
     * 语音转写（ASR）
     *
     * @param audioUrl 音频URL
     * @return 转写后的文本
     */
    public String speechToText(String audioUrl) {
        log.info("语音转写请求, audioUrl={}", audioUrl);
        // Mock实现
        return "hello world this is a test";
    }

    /**
     * 语音评测打分
     *
     * @param referenceText 参考文本
     * @param audioUrl      音频URL
     * @return 评分（0-100）
     */
    public int evaluatePronunciation(String referenceText, String audioUrl) {
        log.info("语音评测请求, referenceText={}, audioUrl={}", referenceText, audioUrl);
        // Mock实现：返回模拟分数
        return 85;
    }
}
