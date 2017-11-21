package com.example.controller;

import com.example.model.Ballot;
import com.example.service.BallotService;
import java.io.IOException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BallotController {
	
	@Autowired
	private BallotService ballotService;

	
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public ModelAndView registration(){
		ModelAndView modelAndView = new ModelAndView();
		Ballot ballot = new Ballot();
		modelAndView.addObject("ballot", ballot);
		modelAndView.setViewName("registration");
		return modelAndView;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid Ballot ballot, BindingResult bindingResult) throws IOException {
		ModelAndView modelAndView = new ModelAndView();
                ballot = ballotService.doBallot(ballot);
                modelAndView.addObject("ballot", ballot);
                if(ballot.getMonth() == null){
                    modelAndView.addObject("successMessage", String.format("%s cannot be paired", ballot.getEmail()));
		
                }else{
                    modelAndView.addObject("successMessage", String.format("%s is paired to %s", ballot.getEmail(), ballot.getMonth()));
                }
                modelAndView.setViewName("registration");
			
		
		return modelAndView;
	}
	
	

}
