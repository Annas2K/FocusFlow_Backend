# PHÂN TÍCH QUY TRÌNH NGHIỆP VỤ (BUSINESS RULES) - ENTITY: TASK

## 1. Luồng trạng thái của Task (Status Flow)
* Một Task khi khởi tạo mặc định phải có trạng thái là `TODO`.
* Luồng di chuyển trạng thái hợp lệ (Happy Path): `TODO` -> `IN_PROGRESS` -> `DONE`.
* **Ràng buộc:** Khi Task đã chuyển sang trạng thái `DONE` (Hoàn thành), hệ thống sẽ đóng băng Task này. KHÔNG CHO PHÉP cập nhật (update) bất kỳ thông tin gì thêm (kể cả đổi trạng thái ngược lại).

## 2. Ràng buộc khi tạo mới Task (Create Validation)
* Task bắt buộc phải thuộc về một Project.
* Hệ thống phải kiểm tra `projectId` truyền lên. Nếu `projectId` không tồn tại trong Database -> Bắn lỗi "Project không tồn tại" (404 Not Found / 400 Bad Request).

## 3. Ràng buộc khi giao việc (Assign Task)
* Task phải được assign cho một User cụ thể (`userId`).
* **Rule cốt lõi:** User được assign bắt buộc phải là người đang tham gia vào Project chứa Task đó. Nếu lấy một User ất ơ ở Project khác gán vào -> Bắn lỗi "User không thuộc Project này" (Forbidden/Bad Request).

## 4. Ràng buộc về mức độ ưu tiên (Priority)
* Chỉ nhận 3 giá trị: `LOW`, `MEDIUM`, `HIGH`.