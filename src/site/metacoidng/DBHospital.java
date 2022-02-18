package site.metacoidng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import site.metacoidng.ResponseDto.Response.Body.Items.Item;

public class DBHospital {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection // ByteStream 소켓 연결
            ("jdbc:oracle:thin:@13.124.112.253:1521:xe", "SCOTT3", "TIGER3");

            String sql = "INSERT INTO hospitalTbl(ID, 주소, 운영시작일자, 시도명, 시군구명, 요양기관명, 구분코드, RAT가능여부, 요양종별코드, X좌표, Y좌표, 암호화된요양기호) VALUES(SEQ_HOSPITALTBL.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            List<ItemModeling> hospitalList = DownloadHospital.getResponseList();
            // 자바 오브젝트 파싱 메서드 리스트에 넣기

            System.out.println("받은 사이즈" + hospitalList.size());
            for (int i = 0; i < hospitalList.size(); i++) {
                // 리스트에 있는 데이터 쿼리에 넣기
                pstmt.setString(1, hospitalList.get(i).get주소());
                pstmt.setString(2, hospitalList.get(i).get운영시작일자());
                pstmt.setString(3, hospitalList.get(i).get시도명());
                pstmt.setString(4, hospitalList.get(i).get시군구명());
                pstmt.setString(5, hospitalList.get(i).get요양기관명());
                pstmt.setString(6, hospitalList.get(i).get구분코드());
                pstmt.setString(7, hospitalList.get(i).getRAT가능여부());
                pstmt.setString(8, hospitalList.get(i).get요양종별코드());
                pstmt.setString(9, hospitalList.get(i).getX좌표());
                pstmt.setString(10, hospitalList.get(i).getY좌표());
                pstmt.setString(11, hospitalList.get(i).get암호화된요양기호());

                int result = pstmt.executeUpdate(); // sql 가동과 동시에 커밋

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}