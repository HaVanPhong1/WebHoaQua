package com.example.demo.controller;

import com.example.demo.entity.UserAccount;
import com.example.demo.memento.AdminAccountManagementService;
import com.example.demo.repository.UserAccountRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-memento")
public class TestMementoController {

    private final AdminAccountManagementService adminService;
    private final UserAccountRepository userRepo;

    public TestMementoController(AdminAccountManagementService adminService, UserAccountRepository userRepo) {
        this.adminService = adminService;
        this.userRepo = userRepo;
    }

    @GetMapping("/update")
    public String testUpdate() {
        // Tìm tài khoản Admin có sẵn đầu tiên trong DB (ID = 1)
        UserAccount account = userRepo.findById(1L).orElse(null);
        if (account == null) {
            return "Không tìm thấy tài khoản có ID = 1 trong Database để chạy thử nghiệm!";
        }

        String oldName = account.getFullName();

        // GỌI HÀM: Đảm bảo truyền đủ 5 tham số khớp với AdminAccountManagementService
        adminService.updateAccount(1L, "Tên Này Vừa Bị Sửa Nhầm", account.getEmail(), account.getRole(), account.isActive());

        return "<h3>BƯỚC 1: SỬA DỮ LIỆU THÀNH CÔNG</h3>" +
                "<p>Tên cũ trong DB: <b>" + oldName + "</b></p>" +
                "<p>Tên mới vừa cập nhật: <b>Tên Này Vừa Bị Sửa Nhầm</b></p>" +
                "<p><i>=> Hãy truy cập link <a href='/test-memento/undo'>/test-memento/undo</a> để kiểm tra tính năng hoàn tác (Undo).</i></p>";
    }

    @GetMapping("/undo")
    public String testUndo() {
        try {
            // GỌI HÀM: Khôi phục lại trạng thái cũ của ID = 1
            UserAccount restoredAccount = adminService.undo(1L);

            return "<h3>BƯỚC 2: HOÀN TÁC (UNDO) THÀNH CÔNG VỚI MEMENTO PATTERN!</h3>" +
                    "<p>Dữ liệu đã được quay trở về trạng thái lưu trữ cũ ban đầu.</p>" +
                    "<p>Tên hiện tại sau khi khôi phục: <b>" + restoredAccount.getFullName() + "</b></p>" +
                    "<p><i>=> Bạn có thể vào SQL Server kiểm tra bảng user_accounts, tên sẽ tự động trả về giá trị ban đầu!</i></p>";
        } catch (Exception e) {
            return "<h3>LỖI HOÀN TÁC:</h3>" + e.getMessage();
        }
    }
}