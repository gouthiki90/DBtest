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
            int result = pstmt.executeUpdate();
            ResultSet rs = pstmt.executeQuery();

            List<ItemModeling> item = new ArrayList<>();
            List<Item> dtoItem = new ArrayList<>();

            while (rs.next()) {
                for (int i = 0; i < item.size(); i++) {
                    // 리스트에 담기
                    ItemModeling items = new ItemModeling(rs.getString(dtoItem.get(i).getAddr()),
                            rs.getString(dtoItem.get(i).getMgtStaDd()), rs.getString(dtoItem.get(i).getSidoCdNm()),
                            rs.getString(dtoItem.get(i).getSgguCdNm()),
                            rs.getString(dtoItem.get(i).getYadmNm()), rs.getString(dtoItem.get(i).getPcrPsblYn()),
                            rs.getString(dtoItem.get(i).getRatPsblYn()), rs.getString(dtoItem.get(i).getRecuClCd()),
                            rs.getString(dtoItem.get(i).getXPosWgs84()), rs.getString(dtoItem.get(i).getYPosWgs84()),
                            rs.getString(dtoItem.get(i).getYkihoEnc()));

                    pstmt.setString(2, items.get주소());
                    pstmt.setString(3, items.get운영시작일자());
                    pstmt.setString(4, items.get시도명());
                    pstmt.setString(5, items.get시군구명());
                    pstmt.setString(6, items.get요양기관명());
                    pstmt.setString(7, items.get구분코드());
                    pstmt.setString(8, items.getRAT가능여부());
                    pstmt.setString(9, items.get요양종별코드());
                    pstmt.setString(10, items.getX좌표());
                    pstmt.setString(11, items.getY좌표());
                    pstmt.setString(12, items.get암호화된요양기호());

                }
            }

            if (result > 0) {
                System.out.println("성공");
            } else {
                System.out.println("실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
