package com.chihyunglee.springplayground.board;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class BoardApiController {


    private final BoardApiRepository boardRepository;


    public BoardApiController(BoardApiRepository boardRepository){

        this.boardRepository = boardRepository;

    }


    @GetMapping
    public List<Board> list(){

        return boardRepository.findAll();

    }

    @PostMapping
    public Board create(@RequestBody Board board){

        return boardRepository.save(board);

    }

    @PutMapping("/{id}")
    public Board update(
            @PathVariable Long id,
            @RequestBody Board board
    ){

        Board existing = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));


        existing.setTitle(board.getTitle());


        return boardRepository.save(existing);

    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){

        boardRepository.deleteById(id);

    }
    
    @GetMapping("/{id}")
    public Board detail(@PathVariable Long id){

        return boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

    }
}