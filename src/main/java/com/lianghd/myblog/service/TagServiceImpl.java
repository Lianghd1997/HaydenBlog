package com.lianghd.myblog.service;

import com.lianghd.myblog.NotFoundException;
import com.lianghd.myblog.dao.TagRepository;
import com.lianghd.myblog.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> ListTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    // string 转 iterable
    private List<Long> convertTolist(String ids){
        List<Long> list = new ArrayList<>();
        if (!StringUtils.isEmpty(ids)){
            String[] id_array = ids.split(",");
            for (int i = 0; i < id_array.length; i++){
                list.add(new Long(id_array[i]));
            }
        }
        return list;
    }

    @Override
    public List<Tag> listTags(String ids) { //1,2,3
        return tagRepository.findAllById(convertTolist(ids));
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.getOne(id);
        if (t == null){
            throw new NotFoundException("不存在该标签");
        }
        // tag重新赋值到 t
        BeanUtils.copyProperties(tag,t);
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);

        return tagRepository.findTop(pageable);
    }
}
