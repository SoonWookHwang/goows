package com.lgcns.goows.components.naver;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

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
                return src;
            }else{
                return "";
            }
        } catch (Exception e) {
            System.out.println("이미지 파싱 중 오류: " + e.getMessage());
        }
        return "";
    }

    public static Map<String,Object> extractHtmlBodyAndImgSrc(String url) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0") // User-Agent 설정
                    .timeout(5000)           // 타임아웃 설정
                    .get();
            Element body = doc.selectFirst("body");
            assert body != null;
            resultMap.put("htmlBody", body.text());
            String src;
            if(url.contains("n.news.naver.com")) {
                Element img = doc.selectFirst("#img1");
                if (img != null) {
                    src = img.attr("src");
                } else {
                    src = "";
                }
            }else{
                src = "";
            }
            resultMap.put("imgSrc", src);
        } catch (Exception e) {
            System.out.println("이미지 파싱 중 오류: " + e.getMessage());
        }
        return resultMap;
    }
}