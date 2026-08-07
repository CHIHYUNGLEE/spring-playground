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

import com.chihyunglee.springplayground.service.BoardApiService;

@RestController
@RequestMapping("/api/posts")
public class BoardApiController {

	private final BoardApiService boardApiService;


    public BoardApiController(BoardApiService boardApiService){

        this.boardApiService = boardApiService;

    }


    @GetMapping
    public List<Board> list(){

        return boardApiService.findAll();

    }

    @PostMapping
    public Board create(@RequestBody Board board){

        return boardApiService.save(board);

    }

    @PutMapping("/{id}")
    public Board update(
            @PathVariable Long id,
            @RequestBody Board board
    ){

        Board existing = boardApiService.findById(id);


        existing.setTitle(board.getTitle());


        return boardApiService.save(existing);

    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){

    	boardApiService.deleteById(id);

    }
    
    @GetMapping("/{id}")
    public Board detail(@PathVariable Long id){

        return boardApiService.findById(id);

    }
}