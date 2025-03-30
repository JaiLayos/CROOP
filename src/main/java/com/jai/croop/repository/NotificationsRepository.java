package com.jai.croop.repository;

import com.jai.croop.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {
    @Query("SELECT n FROM Notifications n WHERE n.userID = :userID AND n.userType = :userType ORDER BY n.date DESC")
    List<Notifications> findByUserIDAndUserType(@Param("userID") int userID, @Param("userType") String userType);

}
