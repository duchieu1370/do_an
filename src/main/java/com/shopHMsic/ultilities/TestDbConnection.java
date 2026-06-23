package com.shopHMsic.ultilities;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestDbConnection implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public TestDbConnection(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        System.out.println("==============================================");
        System.out.println("⏳ Đang kiểm tra kết nối tới Oracle Database...");
        try {
            // Câu lệnh SQL đặc trưng của Oracle để test nhanh
            String result = jdbcTemplate.queryForObject("SELECT 'Kết nối thành công!' FROM DUAL", String.class);
            System.out.println("✅ KẾT QUẢ: " + result);
        } catch (Exception e) {
            System.err.println("❌ LỖI KẾT NỐI DATABASE:");
            e.printStackTrace();
        }
        System.out.println("==============================================");
    }
}
