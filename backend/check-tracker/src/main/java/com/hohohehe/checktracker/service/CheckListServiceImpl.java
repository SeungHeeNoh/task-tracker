package com.hohohehe.checktracker.service;

import com.hohohehe.checktracker.domain.CheckList;
import com.hohohehe.checktracker.repository.CheckListRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CheckListServiceImpl implements CheckListService {

    private final CheckListRepository checkListRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CheckList> searchCheckList() {
        return checkListRepository.findAll();
    }

    @Override
    public CheckList saveCheckList(CheckList checkList) {
        return checkListRepository.save(checkList);
    }
}
