package dev.morenomjc.gleifdatapipeline.api.controller;

import dev.morenomjc.gleifdatapipeline.api.model.LeiResource;
import dev.morenomjc.gleifdatapipeline.api.model.PageResult;
import dev.morenomjc.gleifdatapipeline.api.service.LeiRetrievalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lei-records")
@RequiredArgsConstructor
public class LeiRecordRestController {

  private final LeiRetrievalService leiRetrievalService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PageResult<LeiResource> getLeis(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size) {
    Sort sort = Sort.by(Order.by("lei"));
    page = page < 0 ? 0 : page;
    return new PageResult<>(leiRetrievalService.retrievePage(PageRequest.of(page, size, sort)));
  }

  @GetMapping(path = "/{lei}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<LeiResource> getLei(@PathVariable("lei") String lei) {
    return ResponseEntity.of(leiRetrievalService.findByLei(lei));
  }
}
