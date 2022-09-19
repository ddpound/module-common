package com.example.modulecommon.repository;

import com.example.modulecommon.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserModelRepository extends JpaRepository<UserModel,Integer> {


    // 사실 유저네임으로 받아오지만 이메일로 구분해야함
    UserModel findByUsername(String username);



}
