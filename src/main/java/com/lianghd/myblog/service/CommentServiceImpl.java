package com.lianghd.myblog.service;

import com.lianghd.myblog.dao.CommentRepository;
import com.lianghd.myblog.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"createTime");

        // 修改findByBlogId 只取顶级节点（parentComment == null）
        List<Comment> commentList = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);

        return eachComment(commentList);

//        return commentRepository.findByBlogId(blogId,sort);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1){
            comment.setParentComment(commentRepository.getOne(parentCommentId));
        } else{
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    //--------父子类评论结构转化------------
    // 循环每个顶级的评论节点
    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView = new ArrayList<>();
        for(Comment comment : comments){
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }   // 复制所有顶级节点到 commentsView

        // 合并评论的各层子节点到第一级子节点集合
        combineChildren(commentsView);
        return commentsView;
    }

    // root根节点 blog不为空的对象集合
    private void combineChildren(List<Comment> comments){
        for (Comment comment : comments){
            List<Comment> replys1 = comment.getReplyComment();
            for (Comment reply1 : replys1){
                // 循环迭代 找出各自的子代 存放在tempReplys中
                recursively(reply1);
            }
            // 修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComment(tempReplys);
            // 清楚临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    // 存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    // 递归迭代 由外及里
    private void recursively(Comment comment){
        tempReplys.add(comment);    // 顶部节点添加到临时存放集合
        if (comment.getReplyComment().size() > 0){
            List<Comment> replys = comment.getReplyComment();
            for (Comment reply : replys){
                tempReplys.add(reply);
                if (reply.getReplyComment().size() > 0){
                    recursively(reply);
                }
            }
        }
    }
}
