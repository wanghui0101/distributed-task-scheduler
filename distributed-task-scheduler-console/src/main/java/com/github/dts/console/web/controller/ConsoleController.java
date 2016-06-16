package com.github.dts.console.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.dts.console.Response;
import com.github.dts.console.service.ConsoleService;
import com.github.dts.core.ScheduledTaskDefinition;

@RequestMapping
@Controller
public class ConsoleController {

	@Autowired
	private ConsoleService consoleService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String main() {
		return "index";
	}
	
	@RequestMapping(value = "/leader", method = RequestMethod.GET)
	@ResponseBody
	public String leader() {
		return consoleService.leader();
	}
	
	@RequestMapping(value = "/all", method = RequestMethod.POST)
	@ResponseBody
	public List<ScheduledTaskDefinition> findAll() {
		return consoleService.findAll();
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() {
		return "add";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Response add(ScheduledTaskDefinition task) {
		consoleService.add(task);
		return Response.success();
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable String id, Model model) {
		model.addAttribute("id", id);
		return "update";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ScheduledTaskDefinition findOne(@PathVariable String id) {
		return consoleService.findOne(id);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Response update(ScheduledTaskDefinition task) {
		consoleService.update(task);
		return Response.success();
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Response delete(@PathVariable String id) {
		consoleService.delete(id);
		return Response.success();
	}
	
	@RequestMapping(value = "/run/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Response run(@PathVariable String id) {
		consoleService.run(id);
		return Response.success();
	}
	
	@RequestMapping(value = "/stop/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Response stop(@PathVariable String id) {
		consoleService.stop(id);
		return Response.success();
	}
}