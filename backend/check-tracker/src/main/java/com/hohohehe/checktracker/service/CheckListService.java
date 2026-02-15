package com.hohohehe.checktracker.service;

import com.hohohehe.checktracker.domain.CheckList;
import com.hohohehe.checktracker.dto.v1.CheckListDTO;

import java.util.List;

public interface CheckListService {

    List<CheckListDTO> searchCheckList();

    CheckList saveCheckList(CheckList checkList);
}