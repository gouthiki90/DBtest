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

            String sql = "INSERT INTO hospitalTbl(ID, 주소, 운영시작일자, 시도명, 시군구명, 요양기관명) VALUES(?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            int result = pstmt.executeUpdate();
            ResultSet rs = pstmt.executeQuery();

            List<ItemModeling> item = new ArrayList<>();
            List<Item> dtoItem = new ArrayList<>();

            for (int i = 0; i < item.size(); i++) {
                pstmt.setInt(1, rs.getInt(i)); // ID
                pstmt.setString(2, dtoItem.get(i).getAddr()); // 주소
                pstmt.setString(3, dtoItem.get(i).getMgtStaDd()); // 운영시작일자
                pstmt.setString(4, dtoItem.get(i).getSidoCdNm()); // 시도명
                pstmt.setString(5, dtoItem.get(i).getSgguCdNm()); // 시군구명
                pstmt.setString(6, dtoItem.get(i).getYadmNm()); // 요양기관명
            }

            // while (rs.next()) {
            // for (int i = 0; i < item.size(); i++) {
            // // 리스트에 담기
            // ItemModeling items = new ItemModeling(rs.getInt(i),
            // rs.getString(dtoItem.get(i).getAddr()),
            // rs.getString(dtoItem.get(i).getMgtStaDd()),
            // rs.getString(dtoItem.get(i).getSidoCdNm()),
            // rs.getString(dtoItem.get(i).getSgguCdNm()),
            // rs.getString(dtoItem.get(i).getYadmNm()));

            // }
            // }

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
