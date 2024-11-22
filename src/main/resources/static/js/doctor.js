// Hàm mở modal chỉnh sửa bác sĩ
function openEditDoctorModal(doctorId) {
    $.ajax({
        type: "GET",
        url: `/admin/doctor/edit/${doctorId}`,
        success: function(doctor) {
            $('#editDoctorId').val(doctor.doctorId);
            $('#editDoctorName').val(doctor.fullName);
            $('#editDoctorEmail').val(doctor.email);
            $('#editDoctorSpecialty').val(doctor.specialization);
            $('#editDoctorExperience').val(doctor.experienceYears);
            $('#editDoctorModal').modal('show');
        },
        error: function() {
            alert("Không thể tải thông tin bác sĩ!");
        }
    });
}

// Khi mở modal
function setDeleteDoctorId(doctorId) {
    document.getElementById('deleteDoctorId').value = doctorId;
    const deleteModal = new bootstrap.Modal(document.getElementById('deleteDoctorModal'));
    deleteModal.show();
}

// Đảm bảo đóng modal trước khi mở lại (nếu modal đã được mở)
function closeModal() {
    const deleteModal = new bootstrap.Modal(document.getElementById('deleteDoctorModal'));
    deleteModal.hide();
}





$(document).ready(function() {
    $('#doctorTable').DataTable({
        dom: 'rt',
        paging: false,
        info: false,
        searching: false
    });

    // Gửi form thêm bác sĩ
    $('#addDoctorForm').on('submit', function(e) {
        e.preventDefault();
        var formData = $(this).serialize();

        $.ajax({
            type: "POST",
            url: "/admin/doctor/save",
            data: formData,
            success: function(response) {
                // Kiểm tra nếu response là mảng
                if (Array.isArray(response)) {
                    $('#successMessage')
                        .show()
                        .text("Thêm bác sĩ thành công!")
                        .fadeIn(500)
                        .delay(3000)
                        .fadeOut(500);
                    $('#addDoctorModal').modal('hide');
                    updateDoctorTable(response);  // Cập nhật lại danh sách bác sĩ
                } else {
                    console.error("Dữ liệu không phải mảng:", response);
                    $('#errorMessage')
                        .show()
                        .text("Đã xảy ra lỗi khi thêm bác sĩ!")
                        .fadeOut(3000);
                }
            },
            error: function(error) {
                $('#errorMessage')
                    .show()
                    .text("Đã xảy ra lỗi khi thêm bác sĩ!")
                    .fadeOut(3000);
            }
        });
    });


    // Gửi form cập nhật bác sĩ
    $('#editDoctorForm').on('submit', function(e) {
        e.preventDefault();
        var formData = $(this).serialize();

        $.ajax({
            type: "POST",
            url: "/admin/doctor/update",
            data: formData,
            success: function(response) {
                // Kiểm tra nếu response là mảng
                if (Array.isArray(response)) {
                    $('#editSuccessMessage')
                        .text("Cập nhật bác sĩ thành công!")
                        .fadeIn(500)
                        .delay(3000)
                        .fadeOut(500);
                    $('#editDoctorModal').modal('hide');
                    updateDoctorTable(response);  // Cập nhật lại danh sách bác sĩ
                } else {
                    console.error("Dữ liệu không phải mảng:", response);
                    $('#errorMessage')
                        .text("Đã xảy ra lỗi khi cập nhật bác sĩ!")
                        .fadeIn(500)
                        .delay(3000)
                        .fadeOut(500);
                }
            },
            error: function(error) {
                $('#errorMessage')
                    .text("Đã xảy ra lỗi khi cập nhật bác sĩ!")
                    .fadeIn(500)
                    .delay(3000)
                    .fadeOut(500);
            }
        });
    });


    // Xác nhận xóa bác sĩ
    $(document).on('click', '#confirmDeleteDoctor', function(e) {
        var doctorId = $('#deleteDoctorId').val();
        e.preventDefault();
        $.ajax({
            type: "DELETE",
            url: `/admin/doctor/delete/${doctorId}`,
            success: function(response) {
                if (Array.isArray(response)) {
                    $('#deleteSuccessMessage')
                        .text("Xóa bác sĩ thành công!")
                        .fadeIn(500)
                        .delay(3000)
                        .fadeOut(500);
                    $('#deleteDoctorModal').modal('hide');
                    updateDoctorTable(response);  // Cập nhật lại danh sách bác sĩ
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
                    .text("Đã xảy ra lỗi khi xóa bác sĩ!")
                    .fadeOut(3000);
            }
        });
    });

    $(document).ready(function() {
        // Gửi yêu cầu tìm kiếm bác sĩ khi nhấn nút tìm kiếm
        $('#searchButton').on('click', function(e) {
            e.preventDefault();  // Ngừng hành động mặc định của form

            var searchKeyword = $('#searchInput').val();  // Lấy từ khóa tìm kiếm từ input

            $.ajax({
                type: "GET",
                url: `/admin/doctor/search?username=${searchKeyword}`,  // Gửi yêu cầu tìm kiếm đến server
                success: function(response) {
                    // Kiểm tra nếu response là mảng
                    if (Array.isArray(response)) {
                        updateDoctorTable(response);  // Cập nhật lại bảng bác sĩ với kết quả tìm kiếm

                        // Hiển thị thông báo thành công
                        $('#searchSuccessMessage').show().fadeOut(3000);
                    } else {
                        console.error("Dữ liệu không phải mảng:", response);
                        $('#errorMessage')
                            .show()
                            .text("Không tìm thấy bác sĩ nào với từ khóa tìm kiếm!")
                            .fadeOut(3000);
                    }
                },
                error: function() {
                    $('#errorMessage')
                        .show()
                        .text("Đã xảy ra lỗi khi tìm kiếm bác sĩ!")
                        .fadeOut(3000);
                }
            });
        });
    });
});

// Hàm cập nhật lại bảng bác sĩ
function updateDoctorTable(doctors) {
    var tableBody = $('#doctorTable tbody');
    tableBody.empty(); // Xóa bảng hiện tại

    // Kiểm tra nếu doctors là một mảng hợp lệ
    if (Array.isArray(doctors)) {
        doctors.forEach(function(doctor) {
            var row = `<tr>
                        <td>${doctor.doctorId}</td>
                        <td>${doctor.fullName}</td>
                        <td>${doctor.email}</td>
                        <td>${doctor.specialization}</td>
                        <td>${doctor.experienceYears}</td>
                        <td>
                            <a href="#" class="btn btn-warning btn-sm" onclick="openEditDoctorModal(${doctor.doctorId})">
                                <i class="fas fa-edit"></i> Sửa
                            </a>
                            <a href="#" class="btn btn-danger btn-sm" onclick="setDeleteDoctorId(${doctor.doctorId})">
                                <i class="fas fa-trash"></i> Xóa
                            </a>
                        </td>
                    </tr>`;
            tableBody.append(row);
        });
    } else {
        console.error("Dữ liệu bác sĩ không hợp lệ hoặc không phải mảng:", doctors);
    }
}


$('#deleteDoctorModal').on('hidden.bs.modal', function () {
    $('.modal-backdrop').remove();  // Loại bỏ backdrop thừa
});