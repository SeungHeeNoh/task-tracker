package com.hohohehe.checktracker.service;

import com.hohohehe.checktracker.domain.CheckList;

import java.util.List;

public interface CheckListService {

    List<CheckList> searchCheckList();

    CheckList saveCheckList(CheckList checkList);
}