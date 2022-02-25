package site.metacoidng.hospaap.ex01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// 목적 : 다운로드
public class DownloadTest {
    // 1. URL 주소 만들기
    public static void main(String[] args) {
        StringBuffer sbUrl = new StringBuffer();

        sbUrl.append("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService");
        sbUrl.append("?serviceKey="); // 서비스키
        sbUrl.append("wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D");
        sbUrl.append("&pageNo="); // 몇 번째 페이지
        sbUrl.append("1");
        sbUrl.append("&numOfRows="); // 한 페이지당 몇 개씩
        sbUrl.append("10");
        sbUrl.append("&_type="); // 데이터 포맷
        sbUrl.append("json");

        // 2. 다운로드 받기
        try {
            // URL safer가 적용되어 있다. 이미 safer가 되어있어서 더 반영하지 않는다.
            URL url = new URL(sbUrl.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 리턴은 url open Connecttion만 하니까 부모 타입으로 다운캐스팅 한다. http인지 https로 쓸지 모르기 때문에.

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            // 가변길이의 데이터를 받기 위한 버퍼

            StringBuffer sbDownload = new StringBuffer(); // 통신 결과 모아두기
            while (true) {
                String input = reader.readLine();
                if (input == null) { // 데이터가 다 다운받아질 때까지
                    break;
                }
                sbDownload.append(input); // 데이터가 null이 되기 전에 읽기
            }
            // 3. 검증
            System.out.println(sbDownload.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
