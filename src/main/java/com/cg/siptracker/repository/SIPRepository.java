package com.cg.siptracker.repository;

import com.cg.siptracker.model.SIP;
import com.cg.siptracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SIPRepository extends JpaRepository<SIP,Long> {
    List<SIP> findByUser(User user);
}
