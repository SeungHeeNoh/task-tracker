package com.hohohehe.checktracker.dto.v1;

import com.hohohehe.checktracker.domain.CheckList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckListDTO extends CheckList {
    private String createUserName;
    private String modifyUserName;
}
