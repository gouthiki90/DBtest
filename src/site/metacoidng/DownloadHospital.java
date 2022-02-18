package site.metacoidng;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import site.metacoidng.ResponseDto.Response.Body.Items.Item;

public class DownloadHospital {
    public static List<ItemModeling> getResponseList() {
        List<ItemModeling> hospitalList = new ArrayList();

        try {

            StringBuffer sb = new StringBuffer();
            sb.append("http://apis.data.go.kr/B551182/rprtHospService/getRprtHospService?");
            sb.append(
                    "serviceKey=wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D&");
            sb.append("pageNo=1&");
            sb.append("numOfRows=5228&");
            sb.append("_type=json");

            URL url = new URL(sb.toString());

            // 스트림 연결
            HttpURLConnection stream = (HttpURLConnection) url.openConnection();
            // 버퍼 연결
            BufferedReader br = new BufferedReader(new InputStreamReader(stream.getInputStream(), "utf-8"));

            // JSON 읽기
            String responseJson = br.readLine();
            // 자바로 파싱
            Gson gson = new Gson();
            ResponseDto dto = gson.fromJson(responseJson, ResponseDto.class);
            System.out.println(responseJson);

            List<Item> result = dto.getResponse().getBody().getItems().getItem();
            for (int i = 0; i < result.size(); i++) {
                ItemModeling items = new ItemModeling(result.get(i).getAddr(), result.get(i).getMgtStaDd(),
                        result.get(i).getSidoCdNm(), result.get(i).getSgguCdNm(), result.get(i).getYadmNm(),
                        result.get(i).getPcrPsblYn(), result.get(i).getRatPsblYn(), result.get(i).getRecuClCd(),
                        result.get(i).getXPosWgs84(), result.get(i).getYPosWgs84(), result.get(i).getYkihoEnc());

                hospitalList.add(items);
            }

            return hospitalList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        getResponseList();
    }

}
