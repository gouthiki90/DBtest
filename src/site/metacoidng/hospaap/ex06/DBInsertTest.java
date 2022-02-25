package site.metacoidng.hospaap.ex06;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

// 목적 :  5653개 다운로드 받기
// 전략 : (1) 다운로드 1번 -> totalCount 확인
//           (2) totalCount 만큼 다운로드 - 파싱 - 검증(item 사이즈)       
public class DBInsertTest {

    // 몇개 다운로드 받을지 먼저 totalCount 확인하기
    public static int getTotalCount() {

        int totalCount = 0;

        try {

            // 1. URL 주소 만들기 - totalCount 확인용
            StringBuffer sb = new StringBuffer();

            sb.append("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService");
            sb.append("?serviceKey="); // 서비스키
            sb.append("wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g==");
            sb.append("&pageNo=?"); // 몇번째 페이지 인지
            sb.append("1");
            sb.append("&numOfRows=");
            sb.append("2"); // totalCount 체크만 할 것이기 때문에 2개만 받아도 된다. (왜 2개냐면 1개만 받으면 List가 아니라 Object로 받더라)
            sb.append("&_type=");
            sb.append("json"); // 데이터 포맷은 JSON

            // 2. 다운로드 받기 - totalCount 확인용

            URL url = new URL(sb.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));

            StringBuffer sbDownload = new StringBuffer(); // 통신결과 모아두기
            while (true) {
                String input = br.readLine();
                if (input == null) {
                    break;
                }
                sbDownload.append(input);
            }

            // 3. 검증 - totalCountCheck
            // System.out.println(sb.toString());

            // 4. 파싱
            Gson gson = new Gson();
            ResponseDto responseDto = gson.fromJson(sbDownload.toString(), ResponseDto.class);

            // 5. totalCount 담기
            totalCount = responseDto.getResponse().getBody().getTotalCount();
            System.out.println("totalCount : " + totalCount);
            return totalCount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCount;
    }

    // KISS하게 만드는 것이 좋다.
    // totalCount 기반으로 전체 데이터 다운로드 해서 자바 오브젝트로 파싱하기
    public static ResponseDto download() {

        ResponseDto responseDto = null;

        try {
            // 6. 전체 데이터 받기
            // (1) URL 주소 만들기
            int totalCount = getTotalCount();

            if (totalCount == 0) {
                System.out.println("totalCount를 제대로 받지 못하였습니다.");
                return null;
            }

            StringBuffer sb = new StringBuffer();

            sb.append("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService");
            sb.append("?serviceKey="); // 서비스키
            sb.append("wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g==");
            sb.append("&pageNo=?"); // 몇번째 페이지 인지
            sb.append("1");
            sb.append("&numOfRows=");
            sb.append(totalCount); // totalCount 체크만 할 것이기 때문에 2개만 받아도 된다. (왜 2개냐면 1개만 받으면 List가 아니라 Object로 받더라)
            sb.append("&_type=");
            sb.append("json"); // 데이터 포맷은 JSON

            // (2) 다운로드 받기
            URL url = new URL(sb.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));

            StringBuffer sbDownload = new StringBuffer(); // 통신결과 모아두기
            while (true) {
                String input = br.readLine();
                if (input == null) {
                    break;
                }
                sbDownload.append(input);
            }

            // (3) 파싱
            Gson gson = new Gson();
            responseDto = gson.fromJson(sbDownload.toString(), ResponseDto.class);

            // 7. 사이즈 검증
            System.out.println("아이템 사이즈 : " + responseDto.getResponse().getBody().getItems().getItem().size());
            System.out.println("totalCount : " + totalCount);
            if (responseDto.getResponse().getBody().getItems().getItem().size() == totalCount) {
                System.out.println("성공~~~~~~~~~~~~~~~~");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseDto;
    }

    public static List<Hospital> migration() {
        ResponseDto responseDto = download();
        List<Item> list = responseDto.getResponse().getBody().getItems().getItem();

        List<Hospital> hospitals = new ArrayList<>(); // hospital을 list로 옮기기

        for (Item item : list) { // 리스트의 크기만큼 add하기
            Hospital hs = new Hospital(); // 값을 추가하기
            hs.copy(item); // 다른 타입의 item을 받아서 hos에 추가하기
            hospitals.add(hs);
        }
        // 검증
        System.out.println(hospitals.get(5681).getYadmNm());

        return hospitals;
    }

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection // ByteStream 소켓 연결
            ("jdbc:oracle:thin:@13.124.112.253:1521:xe", "SCOTT3", "TIGER3");

            String sql = "INSERT INTO hospitalTbl(ID, 주소, 운영시작일자, 시도명, 시군구명, 요양기관명, 구분코드, RAT가능여부, 요양종별코드, X좌표, Y좌표, 암호화된요양기호) VALUES(SEQ_HOSPITALTBL.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            List<Hospital> hospitals = migration();

            PreparedStatement pstmt = conn.prepareStatement(sql);// pstmt 접근을 위해서 위에 만듦
            // 쿼리 만들기, 버퍼를 단다.
            // pstmt하나만 만들어서 sql을 초기화 해주어야됨
            for (Hospital hospital : hospitals) {
                pstmt.setString(1, hospital.getYadmNm()); // 쿼리 만들어 넣기
                pstmt.setString(2, hospital.getPcrPsblYn());
                pstmt.setString(3, hospital.getAddr());

                // 한 방에 Insert문을 전송해서 한 번에 commit = 배치 프로그램
                // pstmt.executeUpdate(); DB에 날아가서 커밋, 데이터 들어올 때마다 insert하게됨
                pstmt.addBatch(); // 쿼리를 하나씩 저장해둔다.
                pstmt.clearParameters(); // 완성된 쿼리를 원복시켜준다. pstmt를 하나만 쓰기 때문에.
                // pstmt를 초기화
            }
            pstmt.executeLargeUpdate(); // 한 번에 커밋
            pstmt.close(); // 통신의 부하를 줄여준다.

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}