package com.hohohehe.checktracker.service;

import com.hohohehe.checktracker.domain.CheckList;
import com.hohohehe.checktracker.dto.v1.CheckListDTO;
import com.hohohehe.checktracker.mapper.CheckListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CheckListServiceImpl implements CheckListService {

    private final CheckListMapper checkListMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CheckListDTO> searchCheckList() {
        return checkListMapper.findAll();
    }

    @Override
    public CheckList saveCheckList(CheckList checkList) {
        return checkListMapper.save(checkList);
    }
}
