package com.example.demo.Repository;

import com.example.demo.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT count(username) FROM User WHERE username=:username")
    public int isEmailPresent(@Param("username") String username);


//    @Modifying(clearAutomatically = true)
//    @Modifying
//    @Transactional
//    @Query("update User u set u.first_name =:first_name, u.last_name =:last_name, u.password =:password where u.username =:username")
//    public int updateUserInfoByUsername(@Param("first_name") String first_name, @Param("last_name") String last_name, @Param("password") String password, @Param("username") String username);

    User findByUsername(String username);

    @Modifying
    @Transactional
    @Query("update User u set u.first_name =:first_name, u.last_name =:last_name, u.password =:password, u.account_updated =:localDateTime where u.id =:id")
    public int setUserInfoById(@Param("first_name") String first_name, @Param("last_name") String last_name, @Param("password") String password, @Param("localDateTime") LocalDateTime localDateTime, @Param("id") int id);

}
