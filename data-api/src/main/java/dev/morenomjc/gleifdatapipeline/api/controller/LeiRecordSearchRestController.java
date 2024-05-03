package dev.morenomjc.gleifdatapipeline.api.controller;

import dev.morenomjc.gleifdatapipeline.api.model.LeiResource;
import dev.morenomjc.gleifdatapipeline.api.service.LeiRetrievalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class LeiRecordSearchRestController {

  private final LeiRetrievalService leiRetrievalService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<LeiResource> searchByName(@RequestParam("name") String name) {
    return leiRetrievalService.findByName(name);
  }
}
