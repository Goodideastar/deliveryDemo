package com.warehouse.deliverydemo.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RegionCodeUtil {
    
    private static final Map<String, String> CODE_TO_NAME = new HashMap<>();
    
    static {
        // 省份
        CODE_TO_NAME.put("110000", "北京市");
        CODE_TO_NAME.put("120000", "天津市");
        CODE_TO_NAME.put("130000", "河北省");
        CODE_TO_NAME.put("140000", "山西省");
        CODE_TO_NAME.put("150000", "内蒙古自治区");
        CODE_TO_NAME.put("210000", "辽宁省");
        CODE_TO_NAME.put("220000", "吉林省");
        CODE_TO_NAME.put("230000", "黑龙江省");
        CODE_TO_NAME.put("310000", "上海市");
        CODE_TO_NAME.put("320000", "江苏省");
        CODE_TO_NAME.put("330000", "浙江省");
        CODE_TO_NAME.put("340000", "安徽省");
        CODE_TO_NAME.put("350000", "福建省");
        CODE_TO_NAME.put("360000", "江西省");
        CODE_TO_NAME.put("370000", "山东省");
        CODE_TO_NAME.put("410000", "河南省");
        CODE_TO_NAME.put("420000", "湖北省");
        CODE_TO_NAME.put("430000", "湖南省");
        CODE_TO_NAME.put("440000", "广东省");
        CODE_TO_NAME.put("450000", "广西壮族自治区");
        CODE_TO_NAME.put("460000", "海南省");
        CODE_TO_NAME.put("500000", "重庆市");
        CODE_TO_NAME.put("510000", "四川省");
        CODE_TO_NAME.put("520000", "贵州省");
        CODE_TO_NAME.put("530000", "云南省");
        CODE_TO_NAME.put("540000", "西藏自治区");
        CODE_TO_NAME.put("610000", "陕西省");
        CODE_TO_NAME.put("620000", "甘肃省");
        CODE_TO_NAME.put("630000", "青海省");
        CODE_TO_NAME.put("640000", "宁夏回族自治区");
        CODE_TO_NAME.put("650000", "新疆维吾尔自治区");
        CODE_TO_NAME.put("710000", "台湾省");
        CODE_TO_NAME.put("810000", "香港特别行政区");
        CODE_TO_NAME.put("820000", "澳门特别行政区");
        
        // 主要城市（示例，实际应该包含所有城市）
        // 广东省
        CODE_TO_NAME.put("440100", "广州市");
        CODE_TO_NAME.put("440300", "深圳市");
        CODE_TO_NAME.put("440400", "珠海市");
        CODE_TO_NAME.put("440500", "汕头市");
        CODE_TO_NAME.put("440600", "佛山市");
        CODE_TO_NAME.put("440700", "江门市");
        CODE_TO_NAME.put("440800", "湛江市");
        CODE_TO_NAME.put("440900", "茂名市");
        CODE_TO_NAME.put("441200", "肇庆市");
        CODE_TO_NAME.put("441300", "惠州市");
        CODE_TO_NAME.put("441400", "梅州市");
        CODE_TO_NAME.put("441500", "汕尾市");
        CODE_TO_NAME.put("441600", "河源市");
        CODE_TO_NAME.put("441700", "阳江市");
        CODE_TO_NAME.put("441800", "清远市");
        CODE_TO_NAME.put("441900", "东莞市");
        CODE_TO_NAME.put("442000", "中山市");
        CODE_TO_NAME.put("445100", "潮州市");
        CODE_TO_NAME.put("445200", "揭阳市");
        CODE_TO_NAME.put("445300", "云浮市");
        
        // 北京市
        CODE_TO_NAME.put("110100", "北京市");
        
        // 上海市
        CODE_TO_NAME.put("310100", "上海市");
        
        // 主要区县（示例）
        // 湛江市
        CODE_TO_NAME.put("440802", "赤坎区");
        CODE_TO_NAME.put("440803", "霞山区");
        CODE_TO_NAME.put("440804", "坡头区");
        CODE_TO_NAME.put("440811", "麻章区");
        CODE_TO_NAME.put("440823", "遂溪县");
        CODE_TO_NAME.put("440825", "徐闻县");
        CODE_TO_NAME.put("440881", "廉江市");
        CODE_TO_NAME.put("440882", "雷州市");
        CODE_TO_NAME.put("440883", "吴川市");
    }
    
    public static String getNameByCode(String code) {
        if (code == null || code.isEmpty()) {
            return code;
        }
        return CODE_TO_NAME.getOrDefault(code, code);
    }
    
    public static String formatAddress(String province, String city, String district, String detail) {
        StringBuilder sb = new StringBuilder();
        sb.append(getNameByCode(province));
        sb.append(getNameByCode(city));
        sb.append(getNameByCode(district));
        sb.append(detail != null ? detail : "");
        return sb.toString();
    }
}
