package com.hohohehe.checktracker.service;

import com.hohohehe.checktracker.domain.CheckList;
import com.hohohehe.checktracker.repository.CheckListRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CheckListServiceImpl implements CheckListService {

    private final CheckListRepository checkListRepository;

    @Override
    public List<CheckList> searchCheckList() {
        return checkListRepository.findAll();
    }

    @Override
    public CheckList saveCheckList(CheckList checkList) {
        return checkListRepository.save(checkList);
    }
}
