package com.shopHMsic.config;

import com.shopHMsic.entities.Area;
import com.shopHMsic.repository.AreaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AreaRepository areaRepository;

    public DataInitializer(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (areaRepository.count() == 0) {
            seedAreas();
        }
    }

    private void seedAreas() {
        // Map of Province Name to its Ward Names list
        Map<String, List<String>> divisions = new HashMap<>();

        divisions.put("Hà Nội", Arrays.asList(
            "Dịch Vọng", "Láng Thượng", "Hàng Bạc", "Mỹ Đình", "Trúc Bạch", "Giảng Võ", "Thành Công"
        ));

        divisions.put("TP. Hồ Chí Minh", Arrays.asList(
            "Bến Nghé", "Tân Định", "Thảo Điền", "Phú Mỹ Hưng", "Bến Thành", "Đa Kao", "Phường 15"
        ));

        divisions.put("Đà Nẵng", Arrays.asList(
            "Thạch Thang", "Hòa Cường Bắc", "Phước Mỹ", "An Hải Tây", "Khuê Trung"
        ));

        divisions.put("Hải Phòng", Arrays.asList(
            "Cát Dài", "Lạch Tray", "Minh Khai", "Đông Khê", "Gia Viên"
        ));

        divisions.put("Cần Thơ", Arrays.asList(
            "Tân An", "An Khánh", "Cái Khế", "An Bình", "An Cư"
        ));

        divisions.put("Bình Dương", Arrays.asList(
            "Phú Cường", "Hiệp Thành", "Chánh Nghĩa", "Phú Lợi", "Phú Thọ"
        ));

        divisions.put("Đồng Nai", Arrays.asList(
            "Quyết Thắng", "Thống Nhất", "Trung Dũng", "Quang Vinh", "Bửu Long"
        ));

        divisions.put("Khánh Hòa", Arrays.asList(
            "Lộc Thọ", "Xương Huân", "Vạn Thạnh", "Phương Sài", "Vĩnh Nguyên"
        ));

        divisions.put("Lâm Đồng", Arrays.asList(
            "Phường 1", "Phường 2", "Phường 3", "Phường 4", "Phường 5"
        ));

        divisions.put("Quảng Ninh", Arrays.asList(
            "Bạch Đằng", "Hồng Gai", "Hồng Hà", "Yết Kiêu", "Bải Cháy"
        ));

        List<String> list34Provinces = Arrays.asList(
            "Hà Nội", "TP. Hồ Chí Minh", "Đà Nẵng", "Hải Phòng", "Cần Thơ",
            "Bình Dương", "Đồng Nai", "Khánh Hòa", "Lâm Đồng", "Quảng Ninh",
            "Nghệ An", "Thanh Hóa", "Thừa Thiên Huế", "Bà Rịa - Vũng Tàu", "Bắc Giang",
            "Bắc Ninh", "Bến Tre", "Bình Định", "Bình Thuận", "Cà Mau",
            "Đắk Lắk", "Đồng Tháp", "Gia Lai", "Hà Giang", "Hải Dương",
            "Hưng Yên", "Kiên Giang", "Long An", "Nam Định", "Ninh Bình",
            "Phú Thọ", "Quảng Nam", "Thái Bình", "Vĩnh Phúc"
        );

        for (String provName : list34Provinces) {
            // Save province first
            Area province = new Area();
            province.setName(provName);
            province.setParentId(0);
            province.setCreatedDate(new java.util.Date());
            province = areaRepository.save(province);

            // Update pathId for province
            province.setPathId("|" + province.getId() + "|");
            province = areaRepository.save(province);

            List<String> wards = divisions.get(provName);
            if (wards == null) {
                // Generic wards for other provinces
                wards = Arrays.asList("Phường 1", "Phường 2", "Thị trấn");
            }

            for (String wardName : wards) {
                Area ward = new Area();
                ward.setName(wardName);
                ward.setParentId(province.getId());
                ward.setCreatedDate(new java.util.Date());
                ward = areaRepository.save(ward);

                // Update pathId for ward
                ward.setPathId("|" + province.getId() + "|" + ward.getId() + "|");
                areaRepository.save(ward);
            }
        }
    }
}
