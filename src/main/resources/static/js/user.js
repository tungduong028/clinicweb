$(document).ready(function() {
    // Khởi tạo DataTable
    $('#userTable').DataTable({
        dom: 'rt',
        paging: false,
        info: false,
        searching: false
    });

    // Phân trang
    let currentPage = 0;  // Trang hiện tại
    let pageSize = 10;    // Kích thước mỗi trang

    function fetchUsers(page = 0) {
        $.ajax({
            type: "GET",
            url: `/admin/user?page=${page}&size=${pageSize}`,
            success: function(response) {
                setupPagination(response.totalPages, page);  // Cập nhật phân trang
            },
            error: function() {
                alert("Không thể tải danh sách người dùng!");
            }
        });
    }

    function setupPagination(totalPages, currentPage) {
        let paginationHtml = '';
        for (let i = 0; i < totalPages; i++) {
            paginationHtml += `<li class="page-item ${i === currentPage ? 'active' : ''}">
                                   <a class="page-link" href="#" onclick="fetchUsers(${i})">${i + 1}</a>
                                 </li>`;
        }
        $('#pagination').html(paginationHtml);  // Cập nhật phân trang
    }

    // Gọi hàm fetchUsers để tải dữ liệu khi trang được tải
    fetchUsers(currentPage);

    // Xử lý việc hiển thị form tùy theo vai trò
    document.getElementById('role').addEventListener('change', function() {
        var role = this.value;
        if (role === 'ROLE_DOCTOR') {
            // Hiển thị form bác sĩ và ẩn form bệnh nhân
            document.getElementById('doctorForm').style.display = 'block';
            document.getElementById('patientForm').style.display = 'none';
        } else if (role === 'ROLE_PATIENT') {
            // Hiển thị form bệnh nhân và ẩn form bác sĩ
            document.getElementById('patientForm').style.display = 'block';
            document.getElementById('doctorForm').style.display = 'none';
        }
    });

    // Gửi form thêm người dùng
    $('#addUserForm').submit(function (e) {
        e.preventDefault(); // Ngừng reload trang khi submit form
        var formData = $(this).serialize(); // Lấy dữ liệu từ form

        $.ajax({
            url: '/admin/user/save',
            method: 'POST',
            data: formData,
            success: function (response) {
                // Kiểm tra nếu response là mảng người dùng
                if (Array.isArray(response)) {
                    // Hiển thị thông báo thành công
                    $('#successMessage').fadeIn().delay(2000).fadeOut();
                    $('#addUserModal').modal('hide');
                    updateUserTable(response); // Cập nhật lại bảng người dùng
                } else {
                    $('#errorMessage').fadeIn().delay(2000).fadeOut();
                }
            },
            error: function () {
                $('#errorMessage').fadeIn().delay(2000).fadeOut();
            }
        });
    });


    // Khi modal đóng, reset form và ẩn các form con
    $('#addUserModal').on('hidden.bs.modal', function() {
        document.getElementById('addUserform').reset();
        document.getElementById('doctorForm').style.display = 'none';
        document.getElementById('patientForm').style.display = 'none';
    });

    // Xử lý việc hiển thị form tùy theo vai trò
    document.getElementById('role').addEventListener('change', function() {
        var role = this.value;
        if (role === 'ROLE_DOCTOR') {
            // Hiển thị form bác sĩ và ẩn form bệnh nhân
            document.getElementById('doctorForm').style.display = 'block';
            document.getElementById('patientForm').style.display = 'none';

            // Disable các trường trong form bệnh nhân khi không sử dụng
            document.querySelectorAll('#patientForm input, #patientForm select, #patientForm textarea').forEach(function(element) {
                element.disabled = true;
            });

            // Enable các trường trong form bác sĩ
            document.querySelectorAll('#doctorForm input, #doctorForm select, #doctorForm textarea').forEach(function(element) {
                element.disabled = false;
            });
        } else if (role === 'ROLE_PATIENT') {
            // Hiển thị form bệnh nhân và ẩn form bác sĩ
            document.getElementById('patientForm').style.display = 'block';
            document.getElementById('doctorForm').style.display = 'none';

            // Disable các trường trong form bác sĩ khi không sử dụng
            document.querySelectorAll('#doctorForm input, #doctorForm select, #doctorForm textarea').forEach(function(element) {
                element.disabled = true;
            });

            // Enable các trường trong form bệnh nhân
            document.querySelectorAll('#patientForm input, #patientForm select, #patientForm textarea').forEach(function(element) {
                element.disabled = false;
            });
        }
    });

    $(document).ready(function() {
        // Gửi yêu cầu tìm kiếm bác sĩ khi nhấn nút tìm kiếm
        $('#searchButton').on('click', function(e) {
            e.preventDefault();  // Ngừng hành động mặc định của form

            var searchKeyword = $('#searchInput').val();  // Lấy từ khóa tìm kiếm từ input
            var searchType = 'name';  // Hoặc 'email', 'specialization', tùy vào yêu cầu tìm kiếm (có thể thay đổi qua giao diện người dùng)

            // Kiểm tra nếu từ khóa tìm kiếm không rỗng
            if (searchKeyword.trim() === "") {
                alert("Vui lòng nhập từ khóa tìm kiếm!");
                return;
            }

            // Gửi yêu cầu AJAX
            $.ajax({
                type: "GET",
                url: `/admin/user/search?query=${encodeURIComponent(searchKeyword)}&type=${encodeURIComponent(searchType)}`,  // Thêm tham số query và type
                success: function(response) {
                    // Kiểm tra nếu response là mảng (dữ liệu bác sĩ)
                    if (Array.isArray(response) && response.length > 0) {
                       updateUserTable(response);  // Cập nhật lại bảng bác sĩ với kết quả tìm kiếm

                        // Hiển thị thông báo thành công
                        $('#searchSuccessMessage').show().fadeOut(3000);
                    } else {
                        // Nếu không có bác sĩ nào tìm thấy
                        console.error("Không tìm thấy người dùng nào!");
                        $('#errorMessageSearch')
                            .show()
                            .text("Không tìm thấy người dùng nào với từ khóa tìm kiếm!")
                            .fadeOut(3000);
                    }
                },
                error: function() {
                    $('#errorMessageSearch')
                        .show()
                        .text("Đã xảy ra lỗi khi tìm kiếm người dùng!")
                        .fadeOut(3000);
                }
            });
        });
    });

    document.getElementById("searchButton").addEventListener("click", function () {
            document.getElementById("searchForm").submit();
    });

    // Xác nhận xóa bác sĩ
    $(document).on('click', '#confirmDeleteUser', function(e) {
        var userId = $('#deleteUserId').val();
        e.preventDefault();
        $.ajax({
            type: "DELETE",
            url: `/admin/user/delete/${userId}`,
            success: function(response) {
                if (Array.isArray(response)) {
                    $('#deleteSuccessMessage')
                        .text("Xóa người dùng thành công!")
                        .fadeIn(500)
                        .delay(3000)
                        .fadeOut(500);
                    $('#deleteUserModal').modal('hide');
                    updateUserTable(response);  // Cập nhật lại danh sách bác sĩ
                } else {
                    console.error("Dữ liệu không phải mảng:", response);
                    $('#errorMessage')
                        .text("Đã xảy ra lỗi khi xóa bác sĩ!")
                        .fadeIn(500)
                        .delay(3000)
                        .fadeOut(500);
                }
            },
            error: function(error) {
                $('#errorMessage')
                    .show()
                    .text("Đã xảy ra lỗi khi xóa người dùng!")
                    .fadeOut(3000);
            }
        });
    });
});

// Khi mở modal
function setDeleteUserId(userId) {
    document.getElementById('deleteUserId').value = userId;
    const deleteModal = new bootstrap.Modal(document.getElementById('deleteUserModal'));
    deleteModal.show();
}

// Đảm bảo đóng modal trước khi mở lại (nếu modal đã được mở)
function closeModal() {
    const deleteModal = new bootstrap.Modal(document.getElementById('deleteUserModal'));
    deleteModal.hide();
}

// Hàm cập nhật lại bảng bác sĩ
function updateUserTable(users) {
    var tableBody = $('#userTable tbody');
    tableBody.empty(); // Xóa bảng hiện tại

    users.forEach(function (user) {
        if (!user.isDeleted) { // Chỉ hiển thị bác sĩ chưa bị xóa
            var row = `<tr data-user-id="${user.userId}">
                         <td>${user.userId}</td>
                         <td>${user.username}</td>
                         <td>${user.roleName}</td>

                         <td>
                             <a href="#" class="btn btn-danger btn-sm" onclick="setDeleteUserId(${user.userId})">
                                 <i class="fas fa-trash"></i> Xóa
                             </a>
                         </td>
                     </tr>`;
            tableBody.append(row);
        }
    });
}

$('#deleteUserModal').on('hidden.bs.modal', function () {
    $('.modal-backdrop').remove();  // Loại bỏ backdrop thừa
});
