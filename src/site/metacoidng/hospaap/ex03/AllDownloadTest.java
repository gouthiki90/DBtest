package site.metacoidng.hospaap.ex03;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

// 목적 : 5653개 다운 받기
// 전략 : 다운로드 1번 받아서 totalCount를 확인하기
// totalCount만큼 다운로드 - 파싱 - 검증(item 사이즈)
public class AllDownloadTest {
    // 1. URL 주소 만들기 - totalCount 확인용
    public static void main(String[] args) {
        StringBuffer totalCountCheckUrl = new StringBuffer();

        totalCountCheckUrl.append("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService");
        totalCountCheckUrl.append("?serviceKey="); // 서비스키
        totalCountCheckUrl
                .append("wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D");
        totalCountCheckUrl.append("&pageNo="); // 몇 번째 페이지
        totalCountCheckUrl.append("1");
        totalCountCheckUrl.append("&numOfRows="); // 한 페이지당 몇 개씩
        totalCountCheckUrl.append("2"); // totalCount만 체크할 것이기 때문에 2개만 받아도 된다.
        // 1개만 받으면 List가 아니라 오브젝트로 받기 때문에 2개로 함
        totalCountCheckUrl.append("&_type="); // 데이터 포맷
        totalCountCheckUrl.append("json");

        // 2. 다운로드 받기 - totalCount확인용
        try {
            // URL safer가 적용되어 있다. 이미 safer가 되어있어서 더 반영하지 않는다.
            URL urlTemp = new URL(totalCountCheckUrl.toString());
            HttpURLConnection connTemp = (HttpURLConnection) urlTemp.openConnection();
            // 리턴은 url open Connecttion만 하니까 부모 타입으로 다운캐스팅 한다. http인지 https로 쓸지 모르기 때문에.

            BufferedReader readerTemp = new BufferedReader(new InputStreamReader(connTemp.getInputStream(), "utf-8"));
            // 가변길이의 데이터를 받기 위한 버퍼

            StringBuffer sbTotalCountCheck = new StringBuffer(); // 통신 결과 모아두기
            while (true) {
                String input = readerTemp.readLine();
                if (input == null) { // 데이터가 다 다운받아질 때까지
                    break;
                }
                sbTotalCountCheck.append(input); // 데이터가 null이 되기 전에 읽기
            }

            // 3. 검증 - totalCount 체크
            System.out.println(sbTotalCountCheck.toString());

            // 4. 파싱하기
            Gson gsonTemp = new Gson();
            ResponseDto totalCountCheckDto = gsonTemp.fromJson(sbTotalCountCheck.toString(),
                    ResponseDto.class);

            // 5. totalCount 담기
            int totalCount = totalCountCheckDto.getResponse().getBody().getTotalCount();
            System.out.println("totalCount : " + totalCount);

            // 6. 다운로드 다 받기
            StringBuffer sbUrl = new StringBuffer();

            sbUrl.append("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService");
            sbUrl.append("?serviceKey="); // 서비스키
            sbUrl
                    .append("wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D");
            sbUrl.append("&pageNo="); // 몇 번째 페이지
            sbUrl.append("1");
            sbUrl.append("&numOfRows="); // 한 페이지당 몇 개씩
            sbUrl.append(totalCount); // totalCount만 체크할 것이기 때문에 2개만 받아도 된다.
            // 1개만 받으면 List가 아니라 오브젝트로 받기 때문에 2개로 함
            sbUrl.append("&_type="); // 데이터 포맷
            sbUrl.append("json");

            // 2. 다운로드 받기
            // URL safer가 적용되어 있다. 이미 safer가 되어있어서 더 반영하지 않는다.
            URL url = new URL(sbUrl.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 리턴은 url open Connecttion만 하니까 부모 타입으로 다운캐스팅 한다. http인지 https로 쓸지 모르기 때문에.

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            // 가변길이의 데이터를 받기 위한 버퍼

            StringBuffer sb = new StringBuffer(); // 통신 결과 모아두기
            while (true) {
                String input = reader.readLine();
                if (input == null) { // 데이터가 다 다운받아질 때까지
                    break;
                }
                sb.append(input); // 데이터가 다 다운받아진 후에 넣기, null 제외해서
            }

            Gson gson = new Gson();
            ResponseDto responseDto = gson.fromJson(sb.toString(),
                    ResponseDto.class);

            // 7. 사이즈 검증
            System.out.println("아이템 사이즈 : " + responseDto.getResponse().getBody().getItems().getItem().size());
            System.out.println("totalCount : " + totalCount);
            if (responseDto.getResponse().getBody().getItems().getItem().size() == totalCount) {
                System.out.println("성공");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
