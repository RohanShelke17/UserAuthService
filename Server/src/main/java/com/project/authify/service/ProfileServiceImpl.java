package com.project.authify.service;

import com.project.authify.entity.UserEntity;
import com.project.authify.io.ProfileRequest;
import com.project.authify.io.ProfileResponse;
import com.project.authify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService
{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        if (!userRepository.existsByEmail(request.getEmail()))
         {
             UserEntity newProfile = convertToUserEntity(request);
             newProfile = userRepository.save(newProfile);
             return convertToProfileResponse(newProfile);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");

    }

    @Override
    public ProfileResponse getProfile(String email) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not FOund " +email ));

        return convertToProfileResponse(existingUser);
    }

    @Override
    public void sendResetOtp(String email) {
        UserEntity existingEntity=userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found "+email));

        String opt =String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

        long exipryTime=System.currentTimeMillis() + (15 * 60 *1000);

        existingEntity.setResetOtp(opt);
        existingEntity.setResetOtpExpireAt(exipryTime);

        userRepository.save(existingEntity);

        try{
            emailService.sendResetOtpEmail(existingEntity.getEmail(), opt);
        } catch (Exception ex){
            throw new RuntimeException("Unable to send email");
        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        UserEntity existingUsre=userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found "+ email));
        if (existingUsre.getResetOtp() == null || !existingUsre.getResetOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (existingUsre.getResetOtpExpireAt() < System.currentTimeMillis()){
            throw new RuntimeException("OTP EXpired");
        }

        existingUsre.setPassword(passwordEncoder.encode(newPassword));
        existingUsre.setResetOtp(null);
        existingUsre.setResetOtpExpireAt(0L);

        userRepository.save(existingUsre);
    }

    @Override
    public void sendOtp(String email) {
        UserEntity existingUser=userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found: " +email) );
        if (existingUser.getIsAccountVerified() != null && existingUser.getIsAccountVerified()){
            return;
        }
        String opt =String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

        long exipryTime=System.currentTimeMillis() + (24 * 60 * 60 *1000);

        existingUser.setVerifyOtp(opt);
        existingUser.setVerifyOtpExpireAt(exipryTime);

        userRepository.save(existingUser);

        try {
            emailService.sendOtpEmail(existingUser.getEmail(),opt);
        } catch (Exception e){
            throw new RuntimeException("Unable to Send Email ");
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + email));
        if (existingUser.getVerifyOtp()==null || !existingUser.getVerifyOtp().equals(otp)){
            throw new RuntimeException("invalid Otp");
        }

        if (existingUser.getVerifyOtpExpireAt() < System.currentTimeMillis()){
            throw new RuntimeException("OTP expired");
        }

        existingUser.setIsAccountVerified(true);
        existingUser.setVerifyOtp(null);
        existingUser.setVerifyOtpExpireAt(0L);

        userRepository.save(existingUser);
    }




    private ProfileResponse convertToProfileResponse(UserEntity newProfile) {
        return ProfileResponse.builder()
                .name(newProfile.getName())
                .email(newProfile.getEmail())
                .userId(newProfile.getUserId())
                .isAccountVerified(newProfile.getIsAccountVerified())
                .build();
    }


    private UserEntity convertToUserEntity(ProfileRequest request) {
        return UserEntity.builder()
                .email(request.getEmail())
                .userId(UUID.randomUUID().toString())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountVerified(false)
                .resetOtpExpireAt(0L)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .build();
    }
}
