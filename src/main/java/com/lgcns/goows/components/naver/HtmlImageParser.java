package com.lgcns.goows.components.naver;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HtmlImageParser {
    public static String extractFirstImageUrl(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0") // User-Agent 설정
                    .timeout(5000)           // 타임아웃 설정
                    .get();
            Element img = doc.selectFirst("#img1");
            String src;
            if(img!=null){
                src = img.attr("src");
                if(src.isBlank()) {
                    src = img.attr("data-src");
                }
                return src;
            }else{
                return "";
            }
        } catch (Exception e) {
            System.out.println("이미지 파싱 중 오류: " + e.getMessage());
        }
        return "";
    }

    public static String extractHtmlBodyAndImgSrc(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            // 1. Lazy-loading 대응: id="img1" 또는 data-src 속성 가진 img 태그 찾기
            Element img = doc.selectFirst("img#img1[data-src]");

            if (img != null) {
                String dataSrc = img.attr("data-src");
                if (dataSrc != null && !dataSrc.isEmpty()) {
                    return dataSrc;
                }
            }

            // 2. 대체 전략: og:image 메타 태그 추출
            Element ogImage = doc.selectFirst("meta[property=og:image]");
            if (ogImage != null) {
                return ogImage.attr("content");
            }

            // 3. 대체 전략: 모든 img 태그 중 data-src 가진 첫 번째 이미지
            for (Element candidateImg : doc.select("img")) {
                String candidate = candidateImg.attr("data-src");
                if (candidate != null && !candidate.isEmpty()) {
                    return candidate;
                }
            }

        } catch (Exception e) {
            System.out.println("❌ 이미지 추출 중 오류: " + e.getMessage());
        }
        return "";
    }
}