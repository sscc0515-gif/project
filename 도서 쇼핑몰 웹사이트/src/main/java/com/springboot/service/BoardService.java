package com.springboot.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.domain.Board;
import com.springboot.domain.BoardFormDto;
import com.springboot.repository.BoardRepository;

import jakarta.transaction.Transactional;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public Long savePost(BoardFormDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public List<BoardFormDto> getBoardList() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardFormDto> boardDtoList = new ArrayList<>();

        for (Board board : boardList) {
            BoardFormDto boardDto = BoardFormDto.builder()
                    .id(board.getId())
                    .writerid(board.getWriterid())
                    .writer(board.getWriter())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .createdDate(board.getCreatedDate())
                    .modifiedDate(board.getModifiedDate())
                    .build();
            boardDtoList.add(boardDto);
        }

        return boardDtoList;
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public void deleteBoardById(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<Board> listAll(int pageNum, String sortField, String sortDir) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(
                pageNum - 1,
                pageSize,
                sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending()
        );
        return boardRepository.findAll(pageable);
    }
}
