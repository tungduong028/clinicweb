package com.example.clinicweb.service;

import com.example.clinicweb.dto.DoctorDTO;
import com.example.clinicweb.dto.PatientDTO;
import com.example.clinicweb.dto.UsersDTO;
import com.example.clinicweb.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
//    // Thêm người dùng mới
//    Users addUser(UsersDTO usersDTO);
//
//    UsersDTO mapToDTO(Users user);
//
//    // Xóa người dùng theo ID
//    void deleteUserById(Long userId);
//
//    // Tìm kiếm người dùng theo username (có phân trang)
//    Page<UsersDTO> searchUsersByUsername(String username, int pageNo, int pageSize);
//
//    // Lấy tất cả người dùng (có phân trang)
//    Page<UsersDTO> getAllUsers(int pageNo, int pageSize);
//    UsersDTO saveUser(UsersDTO userDTO);
    void deleteUser(Long userId);
    Page<UsersDTO> getAllUsers(int page, int size);
//    Page<UsersDTO> searchUsers(String query, int page, int size);
    Page<UsersDTO> findAllActiveUsers(Pageable pageable);
    List<UsersDTO> findAllUsers();
    Optional<UsersDTO> findById(Long userId);
    List<Users> findByRolePatient();
//    void addDoctorUser(UsersDTO userDto, DoctorDTO doctorDto);
//
//    void addPatientUser(UsersDTO userDto, PatientDTO patientDto);
    void addUser(UsersDTO userDto, DoctorDTO doctorDto, PatientDTO patientDto);

//    boolean isUsernameExists(String username);

    Page<UsersDTO> searchUsersByNameWithPagination(String name, Pageable pageable);


}