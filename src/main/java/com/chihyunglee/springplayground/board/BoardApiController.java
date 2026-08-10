package com.chihyunglee.springplayground.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chihyunglee.springplayground.dto.BoardApiDto;
import com.chihyunglee.springplayground.service.BoardApiService;

@RestController
@RequestMapping("/api/posts")
public class BoardApiController {

	private final BoardApiService boardApiService;


    public BoardApiController(BoardApiService boardApiService){

        this.boardApiService = boardApiService;

    }


    @GetMapping public Page<BoardApiDto> list( 
    		@RequestParam(defaultValue = "") String keyword, 
    		Pageable pageable) { 
    	return boardApiService.findPage( 
    			keyword, pageable 
    	); 
    }

    @PostMapping
    public BoardApiDto create(@RequestBody BoardApiDto dto){

        return boardApiService.save(dto);

    }

    @PutMapping("/{id}")
    public BoardApiDto update(
            @PathVariable Long id,
            @RequestBody BoardApiDto dto){

        return boardApiService.update(id, dto);

    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){

    	boardApiService.deleteById(id);

    }
    
    @GetMapping("/{id}")
    public BoardApiDto detail(@PathVariable Long id){

        return boardApiService.findById(id);

    }
}