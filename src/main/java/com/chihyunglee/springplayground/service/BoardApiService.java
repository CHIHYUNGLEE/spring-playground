package com.chihyunglee.springplayground.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chihyunglee.springplayground.board.Board;
import com.chihyunglee.springplayground.board.BoardApiRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BoardApiService {


    private final BoardApiRepository boardApiRepository;


    public List<Board> findAll(){

        return boardApiRepository.findAll();

    }

    public Board save(Board board){

        return boardApiRepository.save(board);

    }

    public Board findById(Long id){

        return boardApiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

    }


    public void deleteById(Long id){

        boardApiRepository.deleteById(id);

    }
}