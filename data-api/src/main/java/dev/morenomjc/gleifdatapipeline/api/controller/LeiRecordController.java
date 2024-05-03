package dev.morenomjc.gleifdatapipeline.api.controller;

import dev.morenomjc.gleifdatapipeline.api.model.LeiResource;
import dev.morenomjc.gleifdatapipeline.api.service.LeiRetrievalService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lei-records")
@RequiredArgsConstructor
public class LeiRecordController {

  private final LeiRetrievalService leiRetrievalService;

  @GetMapping
  public String getLeis(Model model) {
    return "lei-records";
  }

  @GetMapping(path = "/{lei}")
  public String getLei(@PathVariable("lei") String lei, Model model) {
    model.addAttribute("lei", lei);

    Optional<LeiResource> leiResource = leiRetrievalService.findByLei(lei);
    if (leiResource.isPresent()) {
      model.addAttribute("data", leiResource.get());
      return "lei-record";
    } else {
      return "no-lei";
    }
  }
}
