package com.wzoto.infrastructure.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * OCR图片识别服务 - 预留百度OCR/腾讯OCR端口
 * 开发阶段使用Mock实现，从图片中提取文字
 */
@Slf4j
@Component
public class OcrService {

    /**
     * 识别图片中的文字
     *
     * @param imageUrl 图片URL
     * @return 识别出的文字内容
     */
    public String recognizeText(String imageUrl) {
        log.info("OCR识别请求, imageUrl={}", imageUrl);
        // Mock实现：返回模拟识别文字
        // 生产环境对接百度OCR/腾讯OCR API
        return "这是一道数学题：小明有5个苹果，给了小红2个，还剩几个？";
    }

    /**
     * 识别图片中的数学公式
     *
     * @param imageUrl 图片URL
     * @return 识别出的公式文本
     */
    public String recognizeMathFormula(String imageUrl) {
        log.info("OCR数学公式识别请求, imageUrl={}", imageUrl);
        return "2x + 3 = 7, 求x = ?";
    }
}
