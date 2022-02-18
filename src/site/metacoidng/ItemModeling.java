package site.metacoidng;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ItemModeling {

    private int ID;
    private String 주소;
    private String 운영시작일자;
    private String 시도명;
    private String 시군구명;
    private String 요양기관명;

}
