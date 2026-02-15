package com.hohohehe.checktracker.contoller.v1;

import com.hohohehe.checktracker.domain.CheckList;
import com.hohohehe.checktracker.dto.v1.request.CheckListRequest;
import com.hohohehe.checktracker.dto.v1.response.CheckListResponse;
import com.hohohehe.checktracker.dto.v1.response.Response;
import com.hohohehe.checktracker.service.CheckListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/checklists")
public class CheckListController {

    private final CheckListService checkListService;

    @GetMapping
    public ResponseEntity<List<CheckListResponse>> getAllCheckLists() {
        List<CheckListResponse> results = checkListService.searchCheckList()
                .stream()
                .map(CheckListResponse::from)
                .toList();

        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<Response> addCheckList(@RequestBody CheckListRequest request) {
        checkListService.saveCheckList(CheckList.ofCreateRequest(request));

        return ResponseEntity.ok(Response.of("SC"));
    }
}
