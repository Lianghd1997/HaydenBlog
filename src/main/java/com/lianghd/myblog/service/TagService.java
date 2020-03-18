package com.lianghd.myblog.service;

import com.lianghd.myblog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> ListTag(Pageable pageable);

    List<Tag> listTag();

    List<Tag> listTags(String ids); //1,2,3

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);

    // 首页Tag的Top展示
    List<Tag> listTagTop(Integer size);
}
