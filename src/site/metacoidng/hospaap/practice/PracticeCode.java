package site.metacoidng.hospaap.practice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class PracticeCode {

    public static PracticeDto Download() {

        PracticeDto responseDto = null; // 리턴을 위한 객체 초기화

        try {
            // 1. URL 주소 만들기
            StringBuffer sbUrl = new StringBuffer();
            sbUrl.append("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService"); // endpoint
            sbUrl.append("?serviceKey="); // 서비스키
            sbUrl.append(
                    "wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D");
            sbUrl.append("&pageNo="); // 몇 번째 페이지인지
            sbUrl.append("1");
            sbUrl.append("&numOfRows="); // 한 페이지당 얼마인지
            sbUrl.append(getTotalCount());
            sbUrl.append("&_type=");
            sbUrl.append("json");

            // 2. 다운로드 받기

            URL url = new URL(sbUrl.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // httpURLConnection이 아닌 openConnection을 리턴하니까 부모 타입으로 다운 캐스팅 하기

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // 버퍼 연결

            StringBuffer sbDownload = new StringBuffer();
            // 다운 받은 데이터를 스트링 버퍼에 넣기

            while (true) {
                String inputData = reader.readLine();
                // 데이터 읽기
                if (inputData == null) {
                    break; // null이면 break하기
                }
                sbDownload.append(inputData); // 읽은 데이터를 버퍼에 넣기
            }

            // 3. 파싱하기
            Gson gson = new Gson();
            responseDto = gson.fromJson(sbDownload.toString(), PracticeDto.class);
            // JSON을 받고 자바 오브젝트로 파싱하기

            // 4. 검증
            int itemSize = responseDto.getResponse().getBody().getItems().getItem().size();
            System.out.println("아이템 사이즈" + itemSize);
            System.out.println(responseDto.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseDto;
    }

    public static int getTotalCount() {
        int totalCount = 0;
        try {
            // 1. URL 주소 만들기
            StringBuffer sbUrl = new StringBuffer();
            sbUrl.append("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService"); // endpoint
            sbUrl.append("?serviceKey="); // 서비스키
            sbUrl.append(
                    "wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D");
            sbUrl.append("&pageNo="); // 몇 번째 페이지인지
            sbUrl.append("1");
            sbUrl.append("&numOfRows="); // 한 페이지당 얼마인지
            sbUrl.append("10");
            sbUrl.append("&_type=");
            sbUrl.append("json");

            // 2. 다운로드 받기

            URL url = new URL(sbUrl.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // httpURLConnection이 아닌 openConnection을 리턴하니까 부모 타입으로 다운 캐스팅 하기

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // 버퍼 연결

            StringBuffer sbDownload = new StringBuffer();
            // 다운 받은 데이터를 스트링 버퍼에 넣기

            while (true) {
                String inputData = reader.readLine();
                // 데이터 읽기
                if (inputData == null) {
                    break; // null이면 break하기
                }
                sbDownload.append(inputData); // 읽은 데이터를 버퍼에 넣기
            }

            // 3. 파싱하기
            Gson gson = new Gson();
            PracticeDto responseDto = gson.fromJson(sbDownload.toString(), PracticeDto.class);
            // JSON을 받고 자바 오브젝트로 파싱하기

            // 4. totalCount 담기
            totalCount = responseDto.getResponse().getBody().getTotalCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCount;
    }

    public static List<PracticeHospital> migration() {
        PracticeDto responseDto = Download(); // 파싱된 JAVA 오브젝트 메서드 호출
        List<Item> list = responseDto.getResponse().getBody().getItems().getItem();
        // 리스트에 파싱된 자바 오브젝트 담기

        List<PracticeHospital> hospitals = new ArrayList<>();

        for (Item item : list) {
            PracticeHospital hs = new PracticeHospital();
            hs.copy(item);
            hospitals.add(hs);
        }
        return hospitals;
    }

    public static void main(String[] args) {

    }
}
