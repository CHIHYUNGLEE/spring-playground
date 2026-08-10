package com.chihyunglee.springplayground.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chihyunglee.springplayground.board.Board;
import com.chihyunglee.springplayground.board.BoardApiRepository;
import com.chihyunglee.springplayground.dto.BoardApiDto;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BoardApiService {


    private final BoardApiRepository boardApiRepository;


    public List<BoardApiDto> findAll(){

        return boardApiRepository.findAll()
                .stream()
                .map(board -> {

                    BoardApiDto dto = new BoardApiDto();

                    dto.setId(board.getId());
                    dto.setTitle(board.getTitle());
                    dto.setContent(board.getContent());
                    
                    return dto;

                })
                .toList();

    }

    public BoardApiDto update(Long id, BoardApiDto dto){

        Board existing = findEntityById(id);

        existing.setTitle(dto.getTitle());
        existing.setContent(dto.getContent());

        Board saved = boardApiRepository.save(existing);

        BoardApiDto result = new BoardApiDto();

        result.setId(saved.getId());
        result.setTitle(saved.getTitle());
        result.setContent(saved.getContent());
        
        return result;

    }
    
    public BoardApiDto save(BoardApiDto dto){

        Board board = new Board();

        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        
        Board saved = boardApiRepository.save(board);


        BoardApiDto result = new BoardApiDto();

        result.setId(saved.getId());
        result.setTitle(saved.getTitle());
        result.setContent(saved.getContent());

        return result;

    }

    
    public BoardApiDto findById(Long id){

        Board board = findEntityById(id);

        BoardApiDto dto = new BoardApiDto();

        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        
        return dto;
    }

    private Board findEntityById(Long id){

        return boardApiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

    }
    
    public void deleteById(Long id){

        //존재 여부 확인 가능
        //삭제 권한 체크 가능
        //삭제 로그 남기기 가능
        Board existing = findEntityById(id);

        boardApiRepository.delete(existing);

    }
}