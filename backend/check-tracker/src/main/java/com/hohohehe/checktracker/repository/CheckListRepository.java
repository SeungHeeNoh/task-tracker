package com.hohohehe.checktracker.repository;

import com.hohohehe.checktracker.domain.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckListRepository extends JpaRepository<CheckList, Long> {

    List<CheckList> findAll();
}
