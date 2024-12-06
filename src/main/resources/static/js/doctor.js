// Hàm mở modal chỉnh sửa bác sĩ
function openEditDoctorModal(doctorId) {
    $.ajax({
        type: "GET",
        url: `/admin/doctor/edit/${doctorId}`,
        success: function(doctor) {
            $('#editDoctorId').val(doctor.doctorId);
            $('#editDoctorName').val(doctor.fullName);
            $('#editDoctorEmail').val(doctor.email);
            $('#editDoctorPhoneNumber').val(doctor.phoneNumber);
            $('#editDoctorGender').val(doctor.gender);
            $('#editDoctorDateOfBirth').val(doctor.dateOfBirth);
            $('#editDoctorSpecialization').val(doctor.specialization);
            $('#editDoctorExperienceYears').val(doctor.experienceYears);
            $('#editDoctorRoomId').val(doctor.roomId);
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
    $('#addDoctorForm').submit(function (e) {
        e.preventDefault();  // Ngừng reload trang khi submit form
        var formData = $(this).serialize();  // Lấy dữ liệu từ form

        $.ajax({
            url: '/admin/doctor/save',
            method: 'POST',
            data: formData,
            success: function(response) {
                // Kiểm tra nếu response là mảng bác sĩ
                if (Array.isArray(response)) {
                    // Hiển thị thông báo thành công
                    $('#successMessage').fadeIn().delay(2000).fadeOut();
                    $('#addDoctorModal').modal('hide');
                    updateDoctorTable(response);  // Cập nhật lại bảng bác sĩ
                } else {
                    $('#errorMessage').fadeIn().delay(2000).fadeOut();
                }
            },
            error: function() {
                $('#errorMessage').fadeIn().delay(2000).fadeOut();
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
            var searchType = 'name';  // Hoặc 'email', 'specialization', tùy vào yêu cầu tìm kiếm (có thể thay đổi qua giao diện người dùng)

            // Kiểm tra nếu từ khóa tìm kiếm không rỗng
            if (searchKeyword.trim() === "") {
                alert("Vui lòng nhập từ khóa tìm kiếm!");
                return;
            }

            // Gửi yêu cầu AJAX
            $.ajax({
                type: "GET",
                url: `/admin/doctor/search?query=${encodeURIComponent(searchKeyword)}&type=${encodeURIComponent(searchType)}`,  // Thêm tham số query và type
                success: function(response) {
                    // Kiểm tra nếu response là mảng (dữ liệu bác sĩ)
                    if (Array.isArray(response) && response.length > 0) {
                        updateDoctorTable(response);  // Cập nhật lại bảng bác sĩ với kết quả tìm kiếm

                        // Hiển thị thông báo thành công
                        $('#searchSuccessMessage').show().fadeOut(3000);
                    } else {
                        // Nếu không có bác sĩ nào tìm thấy
                        console.error("Không tìm thấy bác sĩ nào!");
                        $('#errorMessageSearch')
                            .show()
                            .text("Không tìm thấy bác sĩ nào với từ khóa tìm kiếm!")
                            .fadeOut(3000);
                    }
                },
                error: function() {
                    $('#errorMessageSearch')
                        .show()
                        .text("Đã xảy ra lỗi khi tìm kiếm bác sĩ!")
                        .fadeOut(3000);
                }
            });
        });
    });

    // phân trang
    $(document).ready(function() {
        let currentPage = 1;  // Trang hiện tại
        let pageSize = 10;    // Kích thước mỗi trang

        function fetchDoctors(page = 0) {
            $.ajax({
                type: "GET",
                url: `/admin/doctor?page=${page}&size=${pageSize}`,
                success: function(response) {
                    //updateDoctorTable(response.content);  // response.content chứa dữ liệu bác sĩ
                    setupPagination(response.totalPages, page);  // Cập nhật phân trang
                },
                error: function() {
                    alert("Không thể tải danh sách bác sĩ!");
                }
            });
        }

        function setupPagination(totalPages, currentPage) {
            // Cập nhật phân trang: hiển thị số trang và xử lý sự kiện chuyển trang
            let paginationHtml = '';
            for (let i = 0; i < totalPages; i++) {
                paginationHtml += `<li class="page-item ${i === currentPage ? 'active' : ''}">
                                       <a class="page-link" href="#" onclick="fetchDoctors(${i})">${i + 1}</a>
                                     </li>`;
            }
            $('#pagination').html(paginationHtml);  // Cập nhật phân trang
        }

        // Gọi hàm fetchDoctors để tải dữ liệu khi trang được tải
        fetchDoctors(currentPage);
    });

    document.getElementById("searchButton").addEventListener("click", function () {
        document.getElementById("searchForm").submit();
    });


});

// Hàm cập nhật lại bảng bác sĩ
function updateDoctorTable(doctors) {
    var tableBody = $('#doctorTable tbody');
    tableBody.empty(); // Xóa bảng hiện tại

    doctors.forEach(function (doctor) {
        if (!doctor.isDeleted) { // Chỉ hiển thị bác sĩ chưa bị xóa
            var row = `<tr data-doctor-id="${doctor.doctorId}">
                         <td>${doctor.doctorId}</td>
                         <td>${doctor.fullName}</td>
                         <td>${doctor.email}</td>
                         <td>${doctor.phoneNumber}</td>
                         <td>${doctor.gender}</td>
                         <td>${doctor.dateOfBirth}</td>
                         <td>${doctor.specialization}</td>
                         <td>${doctor.experienceYears}</td>
                         <td>${doctor.roomId}</td>
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
        }
    });
}



$('#deleteDoctorModal').on('hidden.bs.modal', function () {
    $('.modal-backdrop').remove();  // Loại bỏ backdrop thừa
});