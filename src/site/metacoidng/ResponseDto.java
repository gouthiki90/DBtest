package site.metacoidng;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseDto {
    private Response response;

    @AllArgsConstructor
    @Data
    class Response {
        private Header header;
        private Body body;

        @AllArgsConstructor
        @Data
        class Header {
            private String resultCode;
            private String resultMsg;

        }

        @AllArgsConstructor
        @Data
        class Body {
            private Items items;
            private int numOfRows;
            private int pageNo;
            private int totalCount;

            @AllArgsConstructor
            @Data
            class Items {
                private List<Item> item;

                @AllArgsConstructor
                @Data
                class Item {
                    private String addr; // 주소
                    private String mgtStaDd; // 운영시작일자
                    private String pcrPsblYn; // 구분코드
                    private String ratPsblYn; // RAT 가능 여부
                    private int recuClCd; // 요양종별코드
                    private String sgguCdNm; // 시군구명
                    private String sidoCdNm; // 시도명
                    private String XPosWgs84; // 좌표
                    private String YPosWgs84; // 좌표
                    private String yadmNm; // 요양기관명
                    private String ykihoEnc; // 암호화된 요양기호
                }
            }
        }
    }
}
