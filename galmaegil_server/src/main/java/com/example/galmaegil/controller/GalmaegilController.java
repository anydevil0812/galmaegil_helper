package com.example.galmaegil.controller;

import com.example.galmaegil.service.GalmaegilService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@CrossOrigin({"*"})
@RestController
public class GalmaegilController {

    @Autowired
    GalmaegilService galmaegilService;

    @GetMapping("/getTourCenterInfo")
    public Mono<String> fetchTourCenterData(@RequestParam int pageNo, @RequestParam String type) {
       return galmaegilService.getTourCenterData(pageNo, type);
    }

    @GetMapping("/getCourseList")
    public Mono<String> fetchCourseData() {
        return galmaegilService.getCourseList();
    }

    @GetMapping("/getAttractionListByPage")
    public Mono<String> fetchAttractionListByPage(@RequestParam int pageSize, @RequestParam int currentPage) {
        return galmaegilService.getAttractionListByPage(pageSize, currentPage);
    }

    @GetMapping("/getAttractionDetail")
    public ResponseEntity<JsonNode> fetchAttractionDetail(@RequestParam int ucSeq, @RequestParam int rPage, @RequestParam int aPage,@RequestParam int pageSize) {
        return galmaegilService.getAttractionDetail(ucSeq, rPage, aPage, pageSize);
    }


}
