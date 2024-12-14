package com.example.clinicweb.config;

import com.example.clinicweb.model.CustomOAuth2User;
import com.example.clinicweb.model.Patient;
import com.example.clinicweb.model.Role;
import com.example.clinicweb.model.Users;
import com.example.clinicweb.repository.PatientRepository;
import com.example.clinicweb.repository.RoleRepository;
import com.example.clinicweb.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private PatientRepository patientRepository;
    public CustomOAuth2UserService(UsersRepository usersRepository, RoleRepository roleRepository) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // Sử dụng DefaultOAuth2UserService để tải thông tin người dùng từ provider OAuth2
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // Gọi phương thức processOAuth2User để xử lý và lưu người dùng vào cơ sở dữ liệu
        return processOAuth2User(oAuth2User);
    }


    public OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        Optional<Users> optionalUser = usersRepository.findByUsername(email);

        Users user;

        if (optionalUser.isEmpty()) {


            // 1. Lấy vai trò ROLE_PATIENT từ RoleRepository
            Role patientRole = roleRepository.findByroleName("ROLE_PATIENT")
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            // 2. Tạo đối tượng Users và thiết lập thông tin người dùng
            user = new Users();
            user.setUsername(email);
            user.setPasswordHash("");  // Để trống mật khẩu vì OAuth2 không cần mật khẩu
            user.setRole(patientRole);

            Users savedUser = usersRepository.save(user);  // Lưu người dùng mới vào DB


            // 3. Tạo đối tượng Patient liên kết với người dùng đã tạo và lưu vào bảng patients
            String fullName = oAuth2User.getAttribute("name");
            Patient patient = new Patient();
            patient.setUser(savedUser);
            patient.setEmail(email);
            patient.setFullName(fullName);
            patientRepository.save(patient); // Lưu Patient vào DB

        } else {
            user = optionalUser.get();

        }

        return new CustomOAuth2User(
                oAuth2User,
                user.getUsername(),
                user.getPassword(),
                List.of(() -> user.getRole().getRoleName())
        );
    }
}