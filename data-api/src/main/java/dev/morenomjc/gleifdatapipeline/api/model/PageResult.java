package dev.morenomjc.gleifdatapipeline.api.model;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageResult<T> {

  private final List<T> content;
  private final long totalElements;
  private final int pageNumber;
  private final int pageSize;
  private final int totalPages;

  public PageResult(Page<T> page) {
    this.content = page.getContent();
    this.totalElements = page.getTotalElements();
    this.pageNumber = page.getNumber();
    this.pageSize = page.getSize();
    this.totalPages = page.getTotalPages();
  }
}
