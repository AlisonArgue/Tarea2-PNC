package com.twitter.demo.services;

import com.twitter.demo.entities.User;
import com.twitter.demo.entities.dto.RegisterDto;
import com.twitter.demo.entities.dto.UserDto;
import com.twitter.demo.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
@NoArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String SECRET_KEY = "no-se-que-poner-xd-10-help-123456";

    public String createUser(RegisterDto userInfo) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
                .setSubject(userInfo.getEmail())
                .setIssuedAt(new Date())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        User user = new User();
        user.setToken(token);
        user.setName(userInfo.getName());
        user.setEmail(userInfo.getEmail());
        // user.setPassword(userInfo.getPassword());
        user.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userRepository.save(user);

        return token;
    }

    public void deleteUser(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()){

        } else {
            User user = optionalUser.get();
            userRepository.deleteUserById(user.getId());

        }
    }

    public void updateUser(String id, String newName, String neEmail, String newPwr) {
        UUID userId = UUID.fromString(id);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        } else {
            User user = optionalUser.get();
            user.setName(newName);
            user.setEmail(neEmail);
            user.setPassword(newPwr);
            userRepository.save(user);

        }
    }

    public User getUser(String id){
        UUID userId = UUID.fromString(id);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return optionalUser.get();
    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public UserDto getUserByEmail(String email){
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return new UserDto(optionalUser.get().getId(), optionalUser.get().getName(), optionalUser.get().getEmail());

    }


}
