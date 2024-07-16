package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Integer>,PagingAndSortingRepository<User,Integer> {

    @Query("SELECT U FROM User U where U.email = :email")
    public User getUserByEmail(@Param("email") String email);

    public Long countById(Integer id);

    /*
    /Can we Use this ? thinking.
    @Query("Upadte User U SET U.enabled= :enabled where  U.id= :id")
     */
    @Query("UPDATE User U SET U.enabled= ?2 where  U.id= ?1 ")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

    /*
    Version :1 Query
        @Query("SELECT U from User U where U.firstName LIKE %?1% OR U.lastName LIKE %?1% " +
            "OR  U.email LIKE %?1%")


     */
    @Query("SELECT U from User U where CONCAT(U.id,' ',U.email,' ',U.firstName,' ',U.lastName) LIKE %?1%")
    public Page<User> findAll(String keyword, Pageable pageable);





    //public  User getUserById(int id);

}
